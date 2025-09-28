package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.DetalleServicio;

public interface DetalleServicioRepository extends JpaRepository<DetalleServicio, Integer> {
    @Query(value = "SELECT TOP 10\r\n" + //
            "P.NOMBRE_PRODUCTO AS Producto,\r\n" + //
            "C.NOMBRE AS Categoria,\r\n" + //
            "SUM(DS.CANTIDAD_USADA) AS TotalUtilizado,\r\n" + //
            "SUM(DS.SUBTOTAL) AS TotalVendido,\r\n" + //
            "COUNT(DISTINCT DS.ID_SERVICIO) AS VecesUtilizado\r\n" + //
            "FROM DETALLE_SERVICIO DS\r\n" + //
            "INNER JOIN PRODUCTOS P ON DS.ID_PRODUCTO = P.ID_PRODUCTO\r\n" + //
            "INNER JOIN CATEGORIAS C ON P.ID_CATEGORIA = C.ID_CATEGORIA\r\n" + //
            "INNER JOIN SERVICIOS S ON DS.ID_SERVICIO = S.ID_SERVICIO\r\n" + //
            "WHERE S.FECHA_SERVICIO >= DATEADD(MONTH, -3, GETDATE())\r\n" + //
            "GROUP BY P.ID_PRODUCTO, P.NOMBRE_PRODUCTO, C.NOMBRE\r\n" + //
            "ORDER BY TotalUtilizado DESC;", nativeQuery = true)
    List<Map<String, Object>> findTopProductos();
}
