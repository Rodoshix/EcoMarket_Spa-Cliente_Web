package com.ecomarket.clienteweb.assembler;

import com.ecomarket.clienteweb.controller.FavoritoControllerV2;
import com.ecomarket.clienteweb.model.Favorito;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class FavoritoModelAssembler implements RepresentationModelAssembler<Favorito, EntityModel<Favorito>> {

    @Override
    public EntityModel<Favorito> toModel(Favorito favorito) {
        return EntityModel.of(favorito,
                linkTo(methodOn(FavoritoControllerV2.class).obtenerFavoritos(favorito.getEmailUsuario())).withRel("favoritos-del-usuario"),
                linkTo(methodOn(FavoritoControllerV2.class).eliminarFavorito(
                        favorito.getEmailUsuario(),
                        favorito.getIdProducto(),
                        "")).withRel("eliminar-favorito"));
    }
}