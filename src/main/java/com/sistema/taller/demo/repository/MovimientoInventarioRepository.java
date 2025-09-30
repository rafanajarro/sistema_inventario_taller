package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {

    @Query(value = "SELECT * FROM VW_MOVIMIENTOS_MENSUALES", nativeQuery = true)
    List<Map<String, Object>> findMovimientosMensuales();
}
