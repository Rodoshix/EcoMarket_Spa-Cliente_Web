package com.ecomarket.clienteweb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Email
    @Size(max = 100)
    @Schema(description = "Correo del usuario que marcó como favorito", example = "cliente@correo.com")
    private String emailUsuario;

    @NotNull
    @Schema(description = "ID del producto favorito", example = "205")
    private Long idProducto;

    @NotBlank
    @Size(max = 150)
    @Schema(description = "Nombre del producto", example = "Té Verde Orgánico")
    private String nombreProducto;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Categoría del producto", example = "Bebidas")
    private String categoria;

    @Min(1)
    @Schema(description = "Precio del producto", example = "3490")
    private double precio;
}