package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {

    @Query(value = "SELECT \r\n" + //
            "FORMAT(M.FECHA_MOVIMIENTO, 'yyyy-MM') AS Mes,\r\n" + //
            "SUM(CASE WHEN M.TIPO_MOVIMIENTO LIKE '%COMPRA%' OR M.TIPO_MOVIMIENTO LIKE '%ENTRADA%' THEN M.CANTIDAD ELSE 0 END) AS Entradas,\r\n"
            + //
            "SUM(CASE WHEN M.TIPO_MOVIMIENTO LIKE '%VENTA%' OR M.TIPO_MOVIMIENTO LIKE '%SERVICIO%' OR M.TIPO_MOVIMIENTO LIKE '%SALIDA%' THEN M.CANTIDAD ELSE 0 END) AS Salidas,\r\n"
            + //
            "COUNT(*) AS TotalMovimientos\r\n" + //
            "FROM MOVIMIENTOS_INVENTARIO M\r\n" + //
            "WHERE M.FECHA_MOVIMIENTO >= DATEADD(MONTH, -6, GETDATE())\r\n" + //
            "GROUP BY FORMAT(M.FECHA_MOVIMIENTO, 'yyyy-MM')\r\n" + //
            "ORDER BY Mes;", nativeQuery = true)
    List<Map<String, Object>> findMovimientosMensuales();
}
