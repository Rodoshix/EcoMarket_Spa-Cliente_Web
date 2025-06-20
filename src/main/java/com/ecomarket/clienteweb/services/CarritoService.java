package com.ecomarket.clienteweb.services;

import com.ecomarket.clienteweb.dto.ProductoDTO;
import com.ecomarket.clienteweb.dto.UsuarioDTO;
import com.ecomarket.clienteweb.model.Carrito;
import com.ecomarket.clienteweb.repository.CarritoRepository;
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
public class CarritoService {

    @Value("${auth.mock.enabled:false}")
    private boolean mockAuth;

    @Value("${inventario.mock.enabled:false}")
    private boolean mockInventario;
    
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Obtener el carrito del usuario
    public List<Carrito> obtenerCarrito(String emailUsuario) {
        return carritoRepository.findByEmailUsuario(emailUsuario);
    }

    // Agregar un producto al carrito (verifica usuario y producto)
    public Carrito agregarProducto(Carrito item, String jwtToken) {
        // Verificar usuario con token
        verificarUsuario(item.getEmailUsuario(), jwtToken);
    if (!mockInventario) {
        // Obtener datos del producto desde inventario-service
        String urlProducto = "http://localhost:8082/api/productos/" + item.getIdProducto();
        ProductoDTO producto = restTemplate.getForObject(urlProducto, ProductoDTO.class);

        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        item.setNombreProducto(producto.getNombre());
        item.setPrecioUnitario(producto.getPrecioUnitario());
    } else {
        // Datos simulados para entorno de test
        item.setNombreProducto("Producto Simulado");
        item.setPrecioUnitario(9990.0);
    }
        return carritoRepository.save(item);
    }

    public void eliminarProducto(String emailUsuario, Long idProducto, String jwtToken) {
        verificarUsuario(emailUsuario, jwtToken);
        carritoRepository.deleteByEmailUsuarioAndIdProducto(emailUsuario, idProducto);
    }

    public void vaciarCarrito(String emailUsuario, String jwtToken) {
        verificarUsuario(emailUsuario, jwtToken);
        carritoRepository.deleteByEmailUsuario(emailUsuario);
    }

    // Método privado para validar usuario usando JWT
    private void verificarUsuario(String emailUsuario, String jwtToken) {
        if (mockAuth) return; // ← Evita la llamada real en test

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
