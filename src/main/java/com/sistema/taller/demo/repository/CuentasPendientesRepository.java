package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.CuentasPendientes;

public interface CuentasPendientesRepository extends JpaRepository<CuentasPendientes, Integer> {

    @Query(value = "SELECT * FROM VW_ALERTAS_PAGOS_PENDIENTES", nativeQuery = true)
    List<Map<String, Object>> findAlertasPagosPendientes();
}
