package com.ecomarket.clienteweb.assembler;

import com.ecomarket.clienteweb.controller.HistorialNavegacionControllerV2;
import com.ecomarket.clienteweb.model.HistorialNavegacion;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class HistorialNavegacionModelAssembler implements RepresentationModelAssembler<HistorialNavegacion, EntityModel<HistorialNavegacion>> {

    @Override
    public EntityModel<HistorialNavegacion> toModel(HistorialNavegacion historial) {
        return EntityModel.of(historial,
                linkTo(methodOn(HistorialNavegacionControllerV2.class).obtenerHistorial(historial.getEmailUsuario())).withRel("historial-del-usuario"));
    }
}