package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.DetalleServicio;

public interface DetalleServicioRepository extends JpaRepository<DetalleServicio, Integer> {
    @Query(value = "SELECT * FROM VW_TOP_PRODUCTOS", nativeQuery = true)
    List<Map<String, Object>> findTopProductos();
}
