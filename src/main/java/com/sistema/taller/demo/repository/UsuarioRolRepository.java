package com.sistema.taller.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.taller.demo.model.RolUsuario;

public interface UsuarioRolRepository extends JpaRepository<RolUsuario,Integer> {

}
