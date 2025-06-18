package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Favorito;
import com.ecomarket.clienteweb.services.FavoritoService;
import com.ecomarket.clienteweb.assembler.FavoritoModelAssembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritoControllerV2.class)
@ActiveProfiles("test")
class FavoritoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoritoService favoritoService;

    @MockBean
    private FavoritoModelAssembler assembler;

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

        when(assembler.toModel(any(Favorito.class)))
                .thenReturn(EntityModel.of(favorito));

        mockMvc.perform(get("/api/v2/favoritos/usuario@correo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.favoritoList[0].nombreProducto").value("Producto Favorito"));
    }

    @Test
    void testAgregarFavorito() throws Exception {
        when(favoritoService.agregarFavorito(any(Favorito.class), anyString()))
                .thenReturn(favorito);

        when(assembler.toModel(any(Favorito.class)))
                .thenReturn(EntityModel.of(favorito));

        mockMvc.perform(post("/api/v2/favoritos")
                .contentType("application/json")
                .header("Authorization", "Bearer fake.jwt.token")
                .content(objectMapper.writeValueAsString(favorito)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Producto Favorito"));
    }

    @Test
    void testEliminarFavorito() throws Exception {
        mockMvc.perform(delete("/api/v2/favoritos/usuario@correo.com/producto/200")
                .header("Authorization", "Bearer fake.jwt.token"))
                .andExpect(status().isNoContent());
    }
}