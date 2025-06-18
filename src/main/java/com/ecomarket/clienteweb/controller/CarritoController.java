package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Carrito;
import com.ecomarket.clienteweb.services.CarritoService;
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
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Operaciones sobre el carrito de compras")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Obtener los productos del carrito de un usuario")
    @ApiResponse(responseCode = "200", description = "Lista de productos del carrito obtenida correctamente")
    @GetMapping("/{email}")
    public ResponseEntity<List<Carrito>> obtenerCarrito(
            @Parameter(description = "Correo del usuario") @PathVariable String email) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(email));
    }

    @Operation(summary = "Agregar un producto al carrito")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto agregado correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @PostMapping
    public ResponseEntity<Carrito> agregarProducto(
            @RequestBody Carrito item,
            @Parameter(description = "Token JWT con prefijo Bearer") @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        return ResponseEntity.ok(carritoService.agregarProducto(item, jwt));
    }

    @Operation(summary = "Eliminar un producto específico del carrito")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @DeleteMapping("/{email}/producto/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable String email,
            @PathVariable Long idProducto,
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        carritoService.eliminarProducto(email, idProducto, jwt);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vaciar todo el carrito de un usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Carrito vaciado correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> vaciarCarrito(
            @PathVariable String email,
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        carritoService.vaciarCarrito(email, jwt);
        return ResponseEntity.noContent().build();
    }
}