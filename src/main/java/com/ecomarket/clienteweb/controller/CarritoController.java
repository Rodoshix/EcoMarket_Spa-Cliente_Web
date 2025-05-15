package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Carrito;
import com.ecomarket.clienteweb.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // Obtener carrito por email (público o puedes proteger si lo deseas)
    @GetMapping("/{email}")
    public ResponseEntity<List<Carrito>> obtenerCarrito(@PathVariable String email) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(email));
    }

    // Agregar producto al carrito (con token)
    @PostMapping
    public ResponseEntity<Carrito> agregarProducto(
            @RequestBody Carrito item,
            @RequestHeader("Authorization") String token
    ) {
        // Limpia el "Bearer " y deja solo el token
        String jwt = token.replace("Bearer ", "").trim();
        return ResponseEntity.ok(carritoService.agregarProducto(item, jwt));
    }

    // Eliminar producto del carrito
    @DeleteMapping("/{email}/producto/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable String email,
            @PathVariable Long idProducto,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.replace("Bearer ", "").trim();
        carritoService.eliminarProducto(email, idProducto, jwt);
        return ResponseEntity.noContent().build();
    }

    // Vaciar todo el carrito
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> vaciarCarrito(
            @PathVariable String email,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.replace("Bearer ", "").trim();
        carritoService.vaciarCarrito(email, jwt);
        return ResponseEntity.noContent().build();
    }
}