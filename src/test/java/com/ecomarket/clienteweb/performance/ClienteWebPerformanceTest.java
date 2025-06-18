package com.ecomarket.clienteweb.performance;

import com.ecomarket.clienteweb.model.HistorialNavegacion;
import com.ecomarket.clienteweb.model.Favorito;
import com.ecomarket.clienteweb.model.Carrito;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClienteWebPerformanceTest {

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
    void testCargaMasiva() throws Exception {
        for (int i = 0; i < 30; i++) {
            HistorialNavegacion historial = new HistorialNavegacion(null, "usuario@correo.com", 100L + i, "Prod" + i, "Cat", LocalDateTime.now());
            Favorito favorito = new Favorito(null, "usuario@correo.com", 200L, "Producto Y", "Hogar", 4990.0);
            Carrito carrito = new Carrito(null, "usuario@correo.com", 300L + i, "Item" + i, 1, 9990.0);

            mockMvc.perform(post("/api/historial")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(historial)))
                    .andExpect(status().isOk());

            mockMvc.perform(post("/api/favoritos")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(favorito)))
                    .andExpect(status().isOk());

            mockMvc.perform(post("/api/carrito")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(carrito)))
                    .andExpect(status().isOk());
        }
    }
}