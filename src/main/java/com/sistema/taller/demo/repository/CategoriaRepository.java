package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.Categorias;

public interface CategoriaRepository extends JpaRepository<Categorias, Integer> {

    @Query(value = "SELECT \r\n" + //
                "C.NOMBRE AS Categoria,\r\n" + //
                "SUM(I.STOCK_ACTUAL) AS StockTotal,\r\n" + //
                "SUM(I.STOCK_ACTUAL * I.PRECIO_VENTA) AS ValorizacionTotal,\r\n" + //
                "COUNT(P.ID_PRODUCTO) AS CantidadProductos\r\n" + //
                "FROM CATEGORIAS C\r\n" + //
                "LEFT JOIN PRODUCTOS P ON C.ID_CATEGORIA = P.ID_CATEGORIA\r\n" + //
                "LEFT JOIN INVENTARIO I ON P.ID_PRODUCTO = I.ID_PRODUCTO\r\n" + //
                "WHERE P.ESTADO = 'Disponible' OR P.ESTADO IS NULL\r\n" + //
                "GROUP BY C.ID_CATEGORIA, C.NOMBRE\r\n" + //
                "ORDER BY StockTotal DESC;", nativeQuery = true)
    List<Map<String, Object>> findStockCategoria();
}
