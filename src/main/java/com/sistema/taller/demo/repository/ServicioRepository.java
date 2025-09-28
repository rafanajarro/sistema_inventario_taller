package com.sistema.taller.demo.repository;

import com.sistema.taller.demo.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
}
