package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.HistorialNavegacion;
import com.ecomarket.clienteweb.services.HistorialNavegacionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistorialNavegacionController.class)
@ActiveProfiles("test")
class HistorialNavegacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistorialNavegacionService historialService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HistorialNavegacion historial;

    @BeforeEach
    void setUp() {
        historial = new HistorialNavegacion(
            1L,
            "usuario@correo.com",
            300L,
            "Producto Navegado",
            "Bebidas",
            LocalDateTime.now()
        );

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testObtenerHistorial() throws Exception {
        when(historialService.obtenerHistorial("usuario@correo.com"))
                .thenReturn(List.of(historial));

        mockMvc.perform(get("/api/historial/usuario@correo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreProducto").value("Producto Navegado"));
    }

    @Test
    void testRegistrarNavegacion() throws Exception {
        when(historialService.registrarNavegacion(any(HistorialNavegacion.class), anyString()))
                .thenReturn(historial);

        mockMvc.perform(post("/api/historial")
                .header("Authorization", "Bearer fake.jwt.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(historial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Producto Navegado"));
    }
}