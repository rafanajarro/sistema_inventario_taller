package com.sistema.taller.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.taller.demo.model.RolUsuario;
import com.sistema.taller.demo.repository.UsuarioRepository;
import com.sistema.taller.demo.repository.UsuarioRolRepository;

@Service
public class RolUsuarioService {
    @Autowired
   private UsuarioRolRepository usuarioRolRepository;
    public List<RolUsuario> obtenerTodos(){
        return usuarioRolRepository.findAll();
    }
    public RolUsuario obtenerPorId(Integer id){
        return usuarioRolRepository.findById(id).orElse(null);
    }
    public void guardar (RolUsuario rolUsuario){
        usuarioRolRepository.save(rolUsuario);
    }
    public void eliminar(Integer id){
        usuarioRolRepository.deleteById(id);
    }


    
}
