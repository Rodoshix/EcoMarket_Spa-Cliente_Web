package com.ecomarket.clienteweb.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private int cantidadEnStock;
    private double precioUnitario;
    private String proveedor;
}