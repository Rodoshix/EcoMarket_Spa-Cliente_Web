package com.ecomarket.clienteweb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favoritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un producto marcado como favorito por un usuario")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autogenerado del favorito", example = "1")
    private Long id;

    @Schema(description = "Correo del usuario que marcó como favorito", example = "cliente@correo.com")
    private String emailUsuario;

    @Schema(description = "ID del producto favorito", example = "205")
    private Long idProducto;

    @Schema(description = "Nombre del producto", example = "Té Verde Orgánico")
    private String nombreProducto;

    @Schema(description = "Categoría del producto", example = "Bebidas")
    private String categoria;

    @Schema(description = "Precio del producto", example = "3490")
    private double precio;
}