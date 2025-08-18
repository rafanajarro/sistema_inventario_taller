package com.sistema.taller.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.taller.demo.model.RolUsuario;

public interface UsuarioRolRepository extends JpaRepository<RolUsuario, Integer> {

    @Query(value = "SELECT * FROM ROL_USUARIO WHERE ESTADO = :estado", nativeQuery = true)
    List<RolUsuario> encontrarRolesActivos(@Param("estado") String estado);
}
