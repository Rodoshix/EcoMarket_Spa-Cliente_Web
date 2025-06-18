package com.ecomarket.clienteweb.controller;

import com.ecomarket.clienteweb.model.Carrito;
import com.ecomarket.clienteweb.services.CarritoService;
import com.ecomarket.clienteweb.assembler.CarritoModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/carrito")
public class CarritoControllerV2 {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoModelAssembler assembler;

    // GET carrito de un usuario con HATEOAS
    @GetMapping("/{email}")
    public CollectionModel<EntityModel<Carrito>> obtenerCarrito(@PathVariable String email) {
        List<Carrito> lista = carritoService.obtenerCarrito(email);
        List<EntityModel<Carrito>> carritoModels = lista.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(carritoModels,
                linkTo(methodOn(CarritoControllerV2.class).obtenerCarrito(email)).withSelfRel());
    }

    // POST agregar producto (HATEOAS response)
    @PostMapping
    public EntityModel<Carrito> agregarProducto(
            @RequestBody Carrito item,
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        Carrito agregado = carritoService.agregarProducto(item, jwt);
        return assembler.toModel(agregado);
    }

    // DELETE eliminar producto
    @DeleteMapping("/{email}/producto/{idProducto}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable String email,
            @PathVariable Long idProducto,
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        carritoService.eliminarProducto(email, idProducto, jwt);
        return ResponseEntity.noContent().build();
    }

    // DELETE vaciar carrito
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> vaciarCarrito(
            @PathVariable String email,
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        carritoService.vaciarCarrito(email, jwt);
        return ResponseEntity.noContent().build();
    }
}