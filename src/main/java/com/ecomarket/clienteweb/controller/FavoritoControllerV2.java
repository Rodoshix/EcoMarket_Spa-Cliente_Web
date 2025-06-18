package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Favorito;
import com.ecomarket.clienteweb.services.FavoritoService;
import com.ecomarket.clienteweb.assembler.FavoritoModelAssembler;

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
@RequestMapping("/api/v2/favoritos")
@Tag(name = "Favoritos V2", description = "Gestión de favoritos con soporte HATEOAS")
public class FavoritoControllerV2 {

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private FavoritoModelAssembler assembler;

    @Operation(summary = "Obtener productos favoritos del usuario (con HATEOAS)")
    @ApiResponse(responseCode = "200", description = "Lista de favoritos obtenida correctamente")
    @GetMapping("/{email}")
    public CollectionModel<EntityModel<Favorito>> obtenerFavoritos(
            @Parameter(description = "Correo del usuario") @PathVariable String email) {
        List<Favorito> lista = favoritoService.obtenerFavoritos(email);
        List<EntityModel<Favorito>> favoritoModels = lista.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(favoritoModels,
                linkTo(methodOn(FavoritoControllerV2.class).obtenerFavoritos(email)).withSelfRel());
    }

    @Operation(summary = "Agregar un producto a favoritos (con HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto agregado a favoritos"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @PostMapping
    public EntityModel<Favorito> agregarFavorito(
            @RequestBody Favorito favorito,
            @Parameter(description = "Token JWT con prefijo Bearer") @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        Favorito agregado = favoritoService.agregarFavorito(favorito, jwt);
        return assembler.toModel(agregado);
    }

    @Operation(summary = "Eliminar un producto de favoritos")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado de favoritos"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @DeleteMapping("/{email}/producto/{idProducto}")
    public ResponseEntity<Void> eliminarFavorito(
            @Parameter(description = "Correo del usuario") @PathVariable String email,
            @Parameter(description = "ID del producto a eliminar") @PathVariable Long idProducto,
            @Parameter(description = "Token JWT con prefijo Bearer") @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        favoritoService.eliminarFavorito(email, idProducto, jwt);
        return ResponseEntity.noContent().build();
    }
}