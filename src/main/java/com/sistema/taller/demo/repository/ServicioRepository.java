package com.sistema.taller.demo.repository;

import com.sistema.taller.demo.model.Servicio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    @Query(value = "SELECT * FROM SERVICIOS WHERE ESTADO_PAGO IN(:estado1)", nativeQuery = true)
    List<Servicio> encontrarServiciosActivos(@Param("estado1") String estado1);
}
