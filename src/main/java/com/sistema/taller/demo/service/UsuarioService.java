package com.sistema.taller.demo.service;

import com.sistema.taller.demo.model.Usuario;
import com.sistema.taller.demo.model.UsuarioActividad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.taller.demo.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodo() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorID(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioActividad> obtenerUltimaActividadUsuarioActividads(String usuarioMod) {
        return usuarioRepository.findActividadReciente(usuarioMod);
    }

    public Usuario obtenerPorUsername(String username) {
        return usuarioRepository.encontrarPorUsername(username);
    }
}
