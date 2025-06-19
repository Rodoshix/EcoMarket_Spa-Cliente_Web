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
@Table(name = "carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un producto agregado al carrito de un usuario")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autogenerado del ítem en el carrito", example = "1")
    private Long id;

    @NotBlank
    @Email
    @Size(max = 100)
    @Schema(description = "Correo del usuario que agregó el producto", example = "cliente@correo.com")
    private String emailUsuario;

    @NotNull
    @Schema(description = "ID del producto agregado", example = "101")
    private Long idProducto;

    @NotBlank
    @Size(max = 150)
    @Schema(description = "Nombre del producto", example = "Aceite de Oliva Extra Virgen")
    private String nombreProducto;

    @Min(1)
    @Schema(description = "Cantidad de unidades del producto", example = "2")
    private int cantidad;

    @Min(1)
    @Schema(description = "Precio unitario del producto", example = "5990")
    private double precioUnitario;
}