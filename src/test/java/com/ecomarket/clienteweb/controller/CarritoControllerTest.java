package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Carrito;
import com.ecomarket.clienteweb.services.CarritoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CarritoController.class)
@ActiveProfiles("test")
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarritoService carritoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Carrito ejemplo;

    @BeforeEach
    void setup() {
        ejemplo = new Carrito(1L, "usuario@correo.com", 10L, "Producto X", 2, 4990);
    }

    @Test
    void testObtenerCarrito() throws Exception {
        when(carritoService.obtenerCarrito("usuario@correo.com"))
                .thenReturn(List.of(ejemplo));

        mockMvc.perform(get("/api/carrito/usuario@correo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreProducto").value("Producto X"));
    }

    @Test
    void testAgregarProducto() throws Exception {
        when(carritoService.agregarProducto(any(Carrito.class), anyString()))
                .thenReturn(ejemplo);

        mockMvc.perform(post("/api/carrito")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer fake.jwt.token")
                .content(objectMapper.writeValueAsString(ejemplo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Producto X"));
    }

    @Test
    void testEliminarProducto() throws Exception {
        doNothing().when(carritoService).eliminarProducto(anyString(), anyLong(), anyString());

        mockMvc.perform(delete("/api/carrito/usuario@correo.com/producto/10")
                .header("Authorization", "Bearer fake.jwt.token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testVaciarCarrito() throws Exception {
        doNothing().when(carritoService).vaciarCarrito(anyString(), anyString());

        mockMvc.perform(delete("/api/carrito/usuario@correo.com")
                .header("Authorization", "Bearer fake.jwt.token"))
                .andExpect(status().isNoContent());
    }
}