package com.ecomarket.clienteweb.repository;

import com.ecomarket.clienteweb.model.HistorialNavegacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialNavegacionRepository extends JpaRepository<HistorialNavegacion, Long> {

    List<HistorialNavegacion> findByEmailUsuario(String emailUsuario);
}