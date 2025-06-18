package com.ecomarket.clienteweb.services;

import com.ecomarket.clienteweb.model.HistorialNavegacion;
import com.ecomarket.clienteweb.repository.HistorialNavegacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistorialNavegacionServiceTest {

    @InjectMocks
    private HistorialNavegacionService historialService;

    @Mock
    private HistorialNavegacionRepository historialRepo;

    @Mock
    private org.springframework.web.client.RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(historialService, "mockAuth", true);
        ReflectionTestUtils.setField(historialService, "mockInventario", true);
    }

    @Test
    void testObtenerHistorial() {
        String email = "usuario@correo.com";
        HistorialNavegacion h1 = new HistorialNavegacion(1L, email, 10L, "Prod1", "Cat1", LocalDateTime.now());
        HistorialNavegacion h2 = new HistorialNavegacion(2L, email, 11L, "Prod2", "Cat2", LocalDateTime.now());

        when(historialRepo.findByEmailUsuario(email)).thenReturn(Arrays.asList(h1, h2));

        List<HistorialNavegacion> resultado = historialService.obtenerHistorial(email);

        assertEquals(2, resultado.size());
        verify(historialRepo, times(1)).findByEmailUsuario(email);
    }

    @Test
    void testRegistrarNavegacionMockeado() {
        HistorialNavegacion item = new HistorialNavegacion(null, "usuario@correo.com", 55L, null, null, null);

        when(historialRepo.save(any(HistorialNavegacion.class))).thenAnswer(invocation -> {
            HistorialNavegacion h = invocation.getArgument(0);
            h.setId(1L);
            return h;
        });

        HistorialNavegacion resultado = historialService.registrarNavegacion(item, "Bearer dummy");

        assertNotNull(resultado.getId());
        assertEquals("Producto Mock", resultado.getNombreProducto());
        assertEquals("Categoría Mock", resultado.getCategoria());
        assertNotNull(resultado.getFechaHora());
    }
}