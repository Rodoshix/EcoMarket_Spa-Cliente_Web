package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Favorito;
import com.ecomarket.clienteweb.services.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    // Obtener lista de favoritos por email (si deseas puedes protegerlo también)
    @GetMapping("/{email}")
    public ResponseEntity<List<Favorito>> obtenerFavoritos(@PathVariable String email) {
        return ResponseEntity.ok(favoritoService.obtenerFavoritos(email));
    }

    // Agregar un producto a favoritos (requiere token)
    @PostMapping
    public ResponseEntity<Favorito> agregarFavorito(
            @RequestBody Favorito favorito,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.replace("Bearer ", "").trim();
        return ResponseEntity.ok(favoritoService.agregarFavorito(favorito, jwt));
    }

    // Eliminar favorito (requiere token)
    @DeleteMapping("/{email}/producto/{idProducto}")
    public ResponseEntity<Void> eliminarFavorito(
            @PathVariable String email,
            @PathVariable Long idProducto,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.replace("Bearer ", "").trim();
        favoritoService.eliminarFavorito(email, idProducto, jwt);
        return ResponseEntity.noContent().build();
    }
}