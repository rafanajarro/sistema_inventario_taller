package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.taller.demo.model.DetalleServicio;

public interface DetalleServicioRepository extends JpaRepository<DetalleServicio, Integer> {

    // Devuelve entidades completas filtradas por ID_SERVICIO
    @Query("SELECT d FROM DetalleServicio d WHERE d.idServicio.id = :idServicio")
    List<DetalleServicio> findByIdServicio(@Param("idServicio") Integer idServicio);
}