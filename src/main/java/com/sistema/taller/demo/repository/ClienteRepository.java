package com.sistema.taller.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.taller.demo.model.Clientes;

public interface ClienteRepository extends JpaRepository<Clientes, Integer> {

}
