package com.sistema.taller.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sistema.taller.demo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
