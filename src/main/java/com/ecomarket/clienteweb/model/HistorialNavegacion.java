package com.ecomarket.clienteweb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Email
    @Size(max = 100)
    @Schema(description = "Correo del usuario que navegó", example = "cliente@correo.com")
    private String emailUsuario;

    @NotNull
    @Schema(description = "ID del producto visualizado", example = "302")
    private Long idProducto;

    @NotBlank
    @Size(max = 150)
    @Schema(description = "Nombre del producto", example = "Pan Integral Artesanal")
    private String nombreProducto;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Categoría del producto", example = "Panadería")
    private String categoria;

    @NotNull
    @Schema(description = "Fecha y hora en que el producto fue visualizado", example = "2025-06-17T18:45:00")
    private LocalDateTime fechaHora;
}