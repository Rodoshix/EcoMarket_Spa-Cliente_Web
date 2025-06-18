package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Favorito;
import com.ecomarket.clienteweb.services.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@Tag(name = "Favoritos", description = "Gestión de productos favoritos del usuario")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @Operation(summary = "Obtener lista de productos favoritos de un usuario")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping("/{email}")
    public ResponseEntity<List<Favorito>> obtenerFavoritos(
            @Parameter(description = "Correo del usuario") @PathVariable String email) {
        return ResponseEntity.ok(favoritoService.obtenerFavoritos(email));
    }

    @Operation(summary = "Agregar un producto a favoritos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto agregado correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @PostMapping
    public ResponseEntity<Favorito> agregarFavorito(
            @RequestBody Favorito favorito,
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        return ResponseEntity.ok(favoritoService.agregarFavorito(favorito, jwt));
    }

    @Operation(summary = "Eliminar un producto de favoritos")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @DeleteMapping("/{email}/producto/{idProducto}")
    public ResponseEntity<Void> eliminarFavorito(
            @PathVariable String email,
            @PathVariable Long idProducto,
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        favoritoService.eliminarFavorito(email, idProducto, jwt);
        return ResponseEntity.noContent().build();
    }
}