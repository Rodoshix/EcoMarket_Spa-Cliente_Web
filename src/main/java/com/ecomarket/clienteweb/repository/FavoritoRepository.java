package com.ecomarket.clienteweb.repository;

import com.ecomarket.clienteweb.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    // Buscar todos los favoritos de un usuario por email
    List<Favorito> findByEmailUsuario(String emailUsuario);

    // Eliminar favorito específico
    void deleteByEmailUsuarioAndIdProducto(String emailUsuario, Long idProducto);
}