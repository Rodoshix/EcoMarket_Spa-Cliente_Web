package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.HistorialNavegacion;
import com.ecomarket.clienteweb.services.HistorialNavegacionService;
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
@RequestMapping("/api/historial")
@Tag(name = "Historial de Navegación", description = "Registro de navegación del usuario por productos")
public class HistorialNavegacionController {

    @Autowired
    private HistorialNavegacionService historialService;

    @Operation(summary = "Obtener el historial de navegación de un usuario")
    @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente")
    @GetMapping("/{email}")
    public ResponseEntity<List<HistorialNavegacion>> obtenerHistorial(
            @Parameter(description = "Correo del usuario") @PathVariable String email) {
        return ResponseEntity.ok(historialService.obtenerHistorial(email));
    }

    @Operation(summary = "Registrar visualización de un producto por un usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Navegación registrada correctamente"),
        @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    })
    @PostMapping
    public ResponseEntity<HistorialNavegacion> registrarNavegacion(
            @RequestBody HistorialNavegacion item,
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        return ResponseEntity.ok(historialService.registrarNavegacion(item, jwt));
    }
}