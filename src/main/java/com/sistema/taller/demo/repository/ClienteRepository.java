package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.Clientes;

public interface ClienteRepository extends JpaRepository<Clientes, Integer> {
    @Query(value = "SELECT * FROM VW_CUENTAS_PENDIENTES_CLIENTES", nativeQuery = true)
    List<Map<String, Object>> findCuentasPendientesClientes();
}
