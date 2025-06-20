package com.ecomarket.clienteweb.services;

import com.ecomarket.clienteweb.dto.ProductoDTO;
import com.ecomarket.clienteweb.dto.UsuarioDTO;
import com.ecomarket.clienteweb.model.HistorialNavegacion;
import com.ecomarket.clienteweb.repository.HistorialNavegacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional 
public class HistorialNavegacionService {

    @Value("${auth.mock.enabled:false}")
    private boolean mockAuth;

    @Value("${inventario.mock.enabled:false}")
    private boolean mockInventario;

    @Autowired
    private HistorialNavegacionRepository historialRepo;

    @Autowired
    private RestTemplate restTemplate;

    // Obtener historial por usuario
    public List<HistorialNavegacion> obtenerHistorial(String emailUsuario) {
        return historialRepo.findByEmailUsuario(emailUsuario);
    }

    // Registrar nueva navegación (requiere validación)
    public HistorialNavegacion registrarNavegacion(HistorialNavegacion item, String jwtToken) {
        verificarUsuario(item.getEmailUsuario(), jwtToken);

        if (mockInventario) {
            // Simulación de datos para test
            item.setNombreProducto("Producto Mock");
            item.setCategoria("Categoría Mock");
        } else {
        // Obtener producto desde inventario-service
        String urlProducto = "http://localhost:8082/api/productos/" + item.getIdProducto();
        ProductoDTO producto = restTemplate.getForObject(urlProducto, ProductoDTO.class);

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        item.setNombreProducto(producto.getNombre());
        item.setCategoria(producto.getCategoria());
        }
        // Si no viene fecha, la generamos
        if (item.getFechaHora() == null) {
            item.setFechaHora(LocalDateTime.now());
        }

        return historialRepo.save(item);
    }

    // Validación con reenvío del token JWT a usuarios-auth
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
