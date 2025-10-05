package com.sistema.taller.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.taller.demo.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    Optional<Inventario> findByProductoIdProducto(Integer idProducto);

    @Query(value = "SELECT * FROM INVENTARIO WHERE ID_PRODUCTO = :idProducto", nativeQuery = true)
    Inventario findInventarioByIdProducto(@Param("idProducto") Integer idProducto);
}
