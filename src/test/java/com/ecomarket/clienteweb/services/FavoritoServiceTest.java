package com.ecomarket.clienteweb.services;

import com.ecomarket.clienteweb.model.Favorito;
import com.ecomarket.clienteweb.repository.FavoritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoritoServiceTest {

    @InjectMocks
    private FavoritoService favoritoService;

    @Mock
    private FavoritoRepository favoritoRepository;

    @Mock
    private org.springframework.web.client.RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(favoritoService, "mockAuth", true);
        ReflectionTestUtils.setField(favoritoService, "mockInventario", true);
    }

    @Test
    void testObtenerFavoritos() {
        String email = "cliente@correo.com";
        Favorito f1 = new Favorito(1L, email, 10L, "Prod1", "Hogar", 4990.0);
        Favorito f2 = new Favorito(2L, email, 11L, "Prod2", "Tecnología", 9990.0);

        when(favoritoRepository.findByEmailUsuario(email)).thenReturn(Arrays.asList(f1, f2));

        List<Favorito> resultado = favoritoService.obtenerFavoritos(email);

        assertEquals(2, resultado.size());
        verify(favoritoRepository, times(1)).findByEmailUsuario(email);
    }

    @Test
    void testAgregarFavoritoMockeado() {
        Favorito favorito = new Favorito(null, "cliente@correo.com", 999L, null, null, 0.0);

        when(favoritoRepository.save(any(Favorito.class))).thenAnswer(invoc -> {
            Favorito f = invoc.getArgument(0);
            f.setId(1L);
            return f;
        });

        Favorito resultado = favoritoService.agregarFavorito(favorito, "Bearer test-token");

        assertNotNull(resultado.getId());
        assertEquals("Producto Simulado", resultado.getNombreProducto());
        assertEquals("Categoría Test", resultado.getCategoria());
        assertEquals(9990.0, resultado.getPrecio());
    }

    @Test
    void testEliminarFavorito() {
        favoritoService.eliminarFavorito("cliente@correo.com", 100L, "Bearer token");
        verify(favoritoRepository).deleteByEmailUsuarioAndIdProducto("cliente@correo.com", 100L);
    }
}