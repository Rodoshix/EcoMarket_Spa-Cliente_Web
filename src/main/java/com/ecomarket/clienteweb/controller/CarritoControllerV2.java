package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Carrito;
import com.ecomarket.clienteweb.services.CarritoService;
import com.ecomarket.clienteweb.assembler.CarritoModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/carrito")
@Tag(name = "Carrito V2", description = "Operaciones con soporte HATEOAS sobre el carrito de compras")
public class CarritoControllerV2 {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoModelAssembler assembler;

    @Operation(summary = "Obtener los productos del carrito de un usuario (con HATEOAS)")
    @ApiResponse(responseCode = "200", description = "Lista de productos del carrito obtenida correctamente")
    @GetMapping("/{email}")
    public CollectionModel<EntityModel<Carrito>> obtenerCarrito(
            @Parameter(description = "Correo del usuario") @PathVariable String email) {
        List<Carrito> lista = carritoService.obtenerCarrito(email);
        List<EntityModel<Carrito>> carritoModels = lista.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(carritoModels,
                linkTo(methodOn(CarritoControllerV2.class).obtenerCarrito(email)).withSelfRel());
    }

    @Operation(summary = "Agregar un producto al carrito (con HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto agregado correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @PostMapping
    public EntityModel<Carrito> agregarProducto(
            @RequestBody Carrito item,
            @Parameter(description = "Token JWT con prefijo Bearer") @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        Carrito agregado = carritoService.agregarProducto(item, jwt);
        return assembler.toModel(agregado);
    }

    @Operation(summary = "Eliminar un producto del carrito")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @DeleteMapping("/{email}/producto/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "Correo del usuario") @PathVariable String email,
            @Parameter(description = "ID del producto") @PathVariable Long idProducto,
            @Parameter(description = "Token JWT con prefijo Bearer") @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        carritoService.eliminarProducto(email, idProducto, jwt);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vaciar el carrito completo de un usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Carrito vaciado correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> vaciarCarrito(
            @Parameter(description = "Correo del usuario") @PathVariable String email,
            @Parameter(description = "Token JWT con prefijo Bearer") @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        carritoService.vaciarCarrito(email, jwt);
        return ResponseEntity.noContent().build();
    }
}