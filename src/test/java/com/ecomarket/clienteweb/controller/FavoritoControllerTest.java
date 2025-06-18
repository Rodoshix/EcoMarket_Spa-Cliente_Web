package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Favorito;
import com.ecomarket.clienteweb.services.FavoritoService;

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

@WebMvcTest(FavoritoController.class)
@ActiveProfiles("test")
class FavoritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoritoService favoritoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Favorito favorito;

    @BeforeEach
    void setUp() {
        favorito = new Favorito(1L, "usuario@correo.com", 200L, "Producto Favorito", "Snacks", 2990);
    }

    @Test
    void testObtenerFavoritos() throws Exception {
        when(favoritoService.obtenerFavoritos("usuario@correo.com"))
                .thenReturn(List.of(favorito));

        mockMvc.perform(get("/api/favoritos/usuario@correo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreProducto").value("Producto Favorito"));
    }

    @Test
    void testAgregarFavorito() throws Exception {
        when(favoritoService.agregarFavorito(any(Favorito.class), anyString()))
                .thenReturn(favorito);

        mockMvc.perform(post("/api/favoritos")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer fake.jwt.token")
                .content(objectMapper.writeValueAsString(favorito)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Producto Favorito"));
    }

    @Test
    void testEliminarFavorito() throws Exception {
        doNothing().when(favoritoService).eliminarFavorito(anyString(), anyLong(), anyString());

        mockMvc.perform(delete("/api/favoritos/usuario@correo.com/producto/200")
                .header("Authorization", "Bearer fake.jwt.token"))
                .andExpect(status().isNoContent());
    }
}