package com.ecomarket.clienteweb.services;

import com.ecomarket.clienteweb.dto.ProductoDTO;
import com.ecomarket.clienteweb.dto.UsuarioDTO;
import com.ecomarket.clienteweb.model.Favorito;
import com.ecomarket.clienteweb.repository.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional 
public class FavoritoService {

    @Value("${auth.mock.enabled:false}")
    private boolean mockAuth;

    @Value("${inventario.mock.enabled:false}")
    private boolean mockInventario;

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Obtener favoritos por email
    public List<Favorito> obtenerFavoritos(String emailUsuario) {
        return favoritoRepository.findByEmailUsuario(emailUsuario);
    }

    // Agregar un producto a favoritos (requiere validación de usuario y producto)
    public Favorito agregarFavorito(Favorito favorito, String jwtToken) {
        verificarUsuario(favorito.getEmailUsuario(), jwtToken);
    
    if (!mockInventario) {
        // Consultar producto desde inventario-service
        String urlProducto = "http://localhost:8082/api/productos/" + favorito.getIdProducto();
        ProductoDTO producto = restTemplate.getForObject(urlProducto, ProductoDTO.class);

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        favorito.setNombreProducto(producto.getNombre());
        favorito.setCategoria(producto.getCategoria());
        favorito.setPrecio(producto.getPrecioUnitario());
    } else {
        // Datos simulados en entorno de pruebas
        favorito.setNombreProducto("Producto Simulado");
        favorito.setCategoria("Categoría Test");
        favorito.setPrecio(9990.0);
    }
        return favoritoRepository.save(favorito);
    }

    // Eliminar favorito
    public void eliminarFavorito(String emailUsuario, Long idProducto, String jwtToken) {
        verificarUsuario(emailUsuario, jwtToken);
        favoritoRepository.deleteByEmailUsuarioAndIdProducto(emailUsuario, idProducto);
    }

    // Validar usuario autenticado vía JWT (token se reenvía a usuarios-auth-service)
    private void verificarUsuario(String emailUsuario, String jwtToken) {
        if (mockAuth) return; // ← Evita la llamada real en entorno de test

        String url = "http://localhost:8081/api/usuarios/buscar?email=" + emailUsuario;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UsuarioDTO> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            UsuarioDTO.class
        );

        UsuarioDTO usuario = response.getBody();

        if (usuario == null) {
            throw new RuntimeException("Usuario no válido");
        }
    }
}
