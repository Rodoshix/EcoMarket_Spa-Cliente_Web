package com.ecomarket.clienteweb.assembler;

import com.ecomarket.clienteweb.controller.CarritoControllerV2;
import com.ecomarket.clienteweb.model.Carrito;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CarritoModelAssembler implements RepresentationModelAssembler<Carrito, EntityModel<Carrito>> {

    @Override
    public EntityModel<Carrito> toModel(Carrito carrito) {
        return EntityModel.of(carrito,
                linkTo(methodOn(CarritoControllerV2.class).obtenerCarrito(carrito.getEmailUsuario())).withRel("carrito-del-usuario"),
                linkTo(methodOn(CarritoControllerV2.class).vaciarCarrito(carrito.getEmailUsuario(), "")).withRel("vaciar-carrito"),
                linkTo(methodOn(CarritoControllerV2.class).eliminarProducto(
                        carrito.getEmailUsuario(),
                        carrito.getIdProducto(),
                        "")).withRel("eliminar-producto"));
    }
}