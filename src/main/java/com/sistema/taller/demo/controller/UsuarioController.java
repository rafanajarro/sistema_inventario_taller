package com.sistema.taller.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sistema.taller.demo.model.RolUsuario;
import com.sistema.taller.demo.model.Usuario;
import com.sistema.taller.demo.service.RolUsuarioService;
import com.sistema.taller.demo.service.UsuarioService;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolUsuarioService rolUsuarioService;

    // 📌 Listar usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodo();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/listado_usuarios";
    }

    // 📌 Mostrar formulario para crear nuevo usuario
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        Usuario usuario = new Usuario();
        List<RolUsuario> roles = rolUsuarioService.obtenerTodos();

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", roles);

        return "usuarios/crear_editar_usuario";
    }

 // 📌 Guardar nuevo usuario
@PostMapping("/guardar")
public String guardarUsuario(@ModelAttribute Usuario usuario) {
    if (usuario.getIdRol() != null && usuario.getIdRol().getIdRol() != null) {
        RolUsuario rol = rolUsuarioService.obtenerPorId(usuario.getIdRol().getIdRol());
        usuario.setIdRol(rol);
    }
    usuarioService.guardar(usuario);
    return "redirect:/usuarios";
}



    // 📌 Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer idUsuario, Model model) {
        Usuario usuario = usuarioService.obtenerPorID(idUsuario);
        List<RolUsuario> roles = rolUsuarioService.obtenerTodos();

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", roles);

        return "usuarios/crear_editar_usuario";
    }

    /* 📌 Actualizar usuario
    @PostMapping("/actualizar")
    public String actualizarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.actualizar(usuario);
        return "redirect:/usuarios";
    }*/

    // 📌 Eliminar usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer idUsuario) {
        usuarioService.eliminar(idUsuario);
        return "redirect:/usuarios";
    }
}
