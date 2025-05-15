package com.ecomarket.clienteweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Indica que esta clase será una tabla en la BD
@Table(name = "favoritos")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emailUsuario;
    private Long idProducto;
    private String nombreProducto;
    private String categoria;
    private double precio;
}