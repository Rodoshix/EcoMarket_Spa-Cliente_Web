package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.HistorialNavegacion;
import com.ecomarket.clienteweb.services.HistorialNavegacionService;
import com.ecomarket.clienteweb.assembler.HistorialNavegacionModelAssembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistorialNavegacionControllerV2.class)
@ActiveProfiles("test")
class HistorialNavegacionControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistorialNavegacionService historialService;

    @MockBean
    private HistorialNavegacionModelAssembler assembler;

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

        when(assembler.toModel(any(HistorialNavegacion.class)))
                .thenReturn(EntityModel.of(historial));

        mockMvc.perform(get("/api/v2/historial/usuario@correo.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.historialNavegacionList[0].nombreProducto")
                        .value("Producto Navegado"));
    }

    @Test
    void testRegistrarNavegacion() throws Exception {
        when(historialService.registrarNavegacion(any(HistorialNavegacion.class), anyString()))
                .thenReturn(historial);

        when(assembler.toModel(any(HistorialNavegacion.class)))
                .thenReturn(EntityModel.of(historial));

        mockMvc.perform(post("/api/v2/historial")
                .header("Authorization", "Bearer fake.jwt.token")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(historial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Producto Navegado"));
    }
}