package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.HistorialNavegacion;
import com.ecomarket.clienteweb.services.HistorialNavegacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialNavegacionController {

    @Autowired
    private HistorialNavegacionService historialService;

    // Obtener historial por email (puedes protegerlo también si deseas)
    @GetMapping("/{email}")
    public ResponseEntity<List<HistorialNavegacion>> obtenerHistorial(@PathVariable String email) {
        return ResponseEntity.ok(historialService.obtenerHistorial(email));
    }

    // Registrar navegación con token JWT
    @PostMapping
    public ResponseEntity<HistorialNavegacion> registrarNavegacion(
            @RequestBody HistorialNavegacion item,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.replace("Bearer ", "").trim();
        return ResponseEntity.ok(historialService.registrarNavegacion(item, jwt));
    }
}