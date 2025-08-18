package com.sistema.taller.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.taller.demo.model.Categorias;

public interface CategoriaRepository extends JpaRepository<Categorias, Integer> {

}
