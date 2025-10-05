package com.sistema.taller.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query(value = "SELECT * FROM VW_PRODUCTOS_SIN_INVENTARIO", nativeQuery =  true)
    List<Producto> findProductoSinInventario();

    @Query(value = "SELECT * FROM VW_PRODUCTOS_CON_INVENTARIO", nativeQuery =  true)
    List<Producto> findProductoConInventario();
}
