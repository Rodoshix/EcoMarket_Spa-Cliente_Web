package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.HistorialNavegacion;
import com.ecomarket.clienteweb.services.HistorialNavegacionService;
import com.ecomarket.clienteweb.assembler.HistorialNavegacionModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
@RequestMapping("/api/v2/historial")
@Tag(name = "Historial Navegación V2", description = "Historial de productos visualizados por el usuario (HATEOAS)")
public class HistorialNavegacionControllerV2 {

    @Autowired
    private HistorialNavegacionService historialService;

    @Autowired
    private HistorialNavegacionModelAssembler assembler;

    @Operation(summary = "Obtener historial de navegación del usuario (con HATEOAS)")
    @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente")
    @GetMapping("/{email}")
    public CollectionModel<EntityModel<HistorialNavegacion>> obtenerHistorial(
            @Parameter(description = "Correo del usuario") @PathVariable String email) {
        List<HistorialNavegacion> lista = historialService.obtenerHistorial(email);
        List<EntityModel<HistorialNavegacion>> historialModels = lista.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(historialModels,
                linkTo(methodOn(HistorialNavegacionControllerV2.class).obtenerHistorial(email)).withSelfRel());
    }

    @Operation(summary = "Registrar navegación de un producto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Navegación registrada correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @PostMapping
    public EntityModel<HistorialNavegacion> registrarNavegacion(
            @RequestBody HistorialNavegacion item,
            @Parameter(description = "Token JWT con prefijo Bearer") @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        HistorialNavegacion registrado = historialService.registrarNavegacion(item, jwt);
        return assembler.toModel(registrado);
    }
}