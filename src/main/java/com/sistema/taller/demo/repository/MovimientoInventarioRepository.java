package com.sistema.taller.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.taller.demo.model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {

}
