package com.sistema.taller.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.taller.demo.model.Categorias;

public interface CategoriaRepository extends JpaRepository<Categorias, Integer> {

    @Query(value = "SELECT * FROM VW_STOCK_CATEGORIA", nativeQuery = true)
    List<Map<String, Object>> findStockCategoria();
}
