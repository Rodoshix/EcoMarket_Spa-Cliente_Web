package com.ecomarket.clienteweb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_navegacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que almacena el historial de navegación de productos por parte del usuario")
public class HistorialNavegacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autogenerado del registro de historial", example = "1")
    private Long id;

    @Schema(description = "Correo del usuario que navegó", example = "cliente@correo.com")
    private String emailUsuario;

    @Schema(description = "ID del producto visualizado", example = "302")
    private Long idProducto;

    @Schema(description = "Nombre del producto", example = "Pan Integral Artesanal")
    private String nombreProducto;

    @Schema(description = "Categoría del producto", example = "Panadería")
    private String categoria;

    @Schema(description = "Fecha y hora en que el producto fue visualizado", example = "2025-06-17T18:45:00")
    private LocalDateTime fechaHora;
}