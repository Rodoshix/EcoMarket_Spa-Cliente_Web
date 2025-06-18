package com.ecomarket.clienteweb.services;

import com.ecomarket.clienteweb.model.Carrito;
import com.ecomarket.clienteweb.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarritoServiceTest {

    @InjectMocks
    private CarritoService carritoService;

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private org.springframework.web.client.RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Activar mocks de entorno de test
        ReflectionTestUtils.setField(carritoService, "mockAuth", true);
        ReflectionTestUtils.setField(carritoService, "mockInventario", true);
    }

    @Test
    void testObtenerCarrito() {
        String email = "cliente@correo.com";
        Carrito c1 = new Carrito(1L, email, 100L, "Prod1", 2, 2990.0);
        Carrito c2 = new Carrito(2L, email, 101L, "Prod2", 1, 3990.0);

        when(carritoRepository.findByEmailUsuario(email)).thenReturn(Arrays.asList(c1, c2));

        List<Carrito> result = carritoService.obtenerCarrito(email);

        assertEquals(2, result.size());
        verify(carritoRepository, times(1)).findByEmailUsuario(email);
    }

    @Test
    void testAgregarProductoMockeado() {
        Carrito item = new Carrito(null, "cliente@correo.com", 999L, null, 1, 0.0);

        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> {
            Carrito c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        Carrito result = carritoService.agregarProducto(item, "Bearer test-token");

        assertNotNull(result.getId());
        assertEquals("Producto Simulado", result.getNombreProducto());
        assertEquals(9990.0, result.getPrecioUnitario());
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void testEliminarProducto() {
        carritoService.eliminarProducto("cliente@correo.com", 100L, "Bearer token");
        verify(carritoRepository).deleteByEmailUsuarioAndIdProducto("cliente@correo.com", 100L);
    }

    @Test
    void testVaciarCarrito() {
        carritoService.vaciarCarrito("cliente@correo.com", "Bearer token");
        verify(carritoRepository).deleteByEmailUsuario("cliente@correo.com");
    }
}