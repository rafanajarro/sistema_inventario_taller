package com.sistema.taller.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sistema.taller.demo.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
}
