package com.ecomarket.clienteweb.integration;

import com.ecomarket.clienteweb.model.Carrito;
import com.ecomarket.clienteweb.model.Favorito;
import com.ecomarket.clienteweb.model.HistorialNavegacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClienteWebIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private final String token = "Bearer dummy-token";

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testHistorialNavegacion() throws Exception {
        HistorialNavegacion historial = new HistorialNavegacion(null, "usuario@correo.com", 10L, "Producto X", "Electro", LocalDateTime.now());

        mockMvc.perform(post("/api/historial")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(historial)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/historial/usuario@correo.com"))
                .andExpect(status().isOk());
    }

    @Test
    void testFavorito() throws Exception {
        Favorito favorito = new Favorito(
            null,
            "usuario@correo.com",
            200L,
            "Producto Y",
            "Hogar",
            4990.0
        );

        mockMvc.perform(post("/api/favoritos")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(favorito)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/favoritos/usuario@correo.com"))
                .andExpect(status().isOk());
    }

    @Test
    void testCarrito() throws Exception {
        Carrito carrito = new Carrito(null, "usuario@correo.com", 30L, "Producto Z", 2, 19990.0);

        mockMvc.perform(post("/api/carrito")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(carrito)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/carrito/usuario@correo.com"))
                .andExpect(status().isOk());
    }
}