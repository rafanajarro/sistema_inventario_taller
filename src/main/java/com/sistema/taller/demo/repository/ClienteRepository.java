package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.Clientes;

public interface ClienteRepository extends JpaRepository<Clientes, Integer> {
    @Query(value = "SELECT \r\n" + //
            "CL.NOMBRES + ' ' + CL.APELLIDOS AS Cliente,\r\n" + //
            "COUNT(CP.ID_CUENTA) AS CuentasPendientes,\r\n" + //
            "SUM(CP.SALDO_PENDIENTE) AS SaldoTotalPendiente,\r\n" + //
            "MAX(CP.ULTIMA_FECHA_PAGO) AS UltimoPago\r\n" + //
            "FROM CLIENTES CL\r\n" + //
            "LEFT JOIN CUENTAS_PENDIENTES CP ON CL.ID_CLIENTE = CP.ID_CLIENTE\r\n" + //
            "WHERE CP.ESTADO IN('Pendiente', 'Parcial') OR CP.ESTADO IS NULL\r\n" + //
            "GROUP BY CL.ID_CLIENTE, CL.NOMBRES, CL.APELLIDOS\r\n" + //
            "HAVING SUM(CP.SALDO_PENDIENTE) > 0\r\n" + //
            "ORDER BY SaldoTotalPendiente DESC;", nativeQuery = true)
    List<Map<String, Object>> findCuentasPendientesClientes();
}
