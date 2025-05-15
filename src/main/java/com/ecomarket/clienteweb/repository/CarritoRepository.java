package com.ecomarket.clienteweb.repository;

import com.ecomarket.clienteweb.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    List<Carrito> findByEmailUsuario(String emailUsuario);

    void deleteByEmailUsuario(String emailUsuario);

    void deleteByEmailUsuarioAndIdProducto(String emailUsuario, Long idProducto);
}