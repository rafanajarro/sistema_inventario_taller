package com.sistema.taller.demo.controller;

import com.sistema.taller.demo.model.RolUsuario;
import com.sistema.taller.demo.service.RolUsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RolUsuarioController {

    @Autowired
    private RolUsuarioService rolUsuarioService;

    // Listar roles
    @GetMapping("/roles")
    public String listarRoles(Model model) {
        List<RolUsuario> roles = rolUsuarioService.obtenerTodos();
        model.addAttribute("roles", roles);
        return "roles/listar_roles"; // Vista: templates/roles/listar_roles.html
    }

    // Formulario crear nuevo rol
    @GetMapping("/roles/nuevo")
    public String mostrarFormularioNuevoRol(Model model) {
        model.addAttribute("rol", new RolUsuario());
        return "roles/crear_editar_rol"; // Vista: templates/roles/crear_editar_rol.html
    }

    // Guardar rol
    @PostMapping("/roles/crear")
    public String guardarRol(@ModelAttribute RolUsuario rol, RedirectAttributes redirectAttributes) {
        try {
            rolUsuarioService.guardar(rol);
            redirectAttributes.addFlashAttribute("mensaje", "El rol se guardó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al guardar el rol.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/roles";
    }

    // Formulario editar rol
    @GetMapping("/roles/editar/{id}")
    public String mostrarFormularioEditarRol(@PathVariable Integer id, Model model) {
        RolUsuario rol = rolUsuarioService.obtenerPorId(id);
        if (rol == null) {
            return "redirect:/roles";
        }
        model.addAttribute("rol", rol);
        return "roles/crear_editar_rol";
    }

    // Editar rol
    @PostMapping("/roles/editar/{id}")
    public String editarRol(@PathVariable Integer id, @ModelAttribute RolUsuario rol,
                            RedirectAttributes redirectAttributes) {
        try {
            RolUsuario rolExistente = rolUsuarioService.obtenerPorId(id);
            if (rolExistente == null) {
                return "redirect:/roles";
            }

            rolExistente.setDescripcion(rol.getDescripcion());
            rolExistente.setEstado(rol.getEstado());

            rolUsuarioService.guardar(rolExistente);
            redirectAttributes.addFlashAttribute("mensaje", "El rol se actualizó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al editar el rol.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/roles";
    }

    // Eliminar rol
    @GetMapping("/roles/eliminar/{id}")
    public String eliminarRol(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            rolUsuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "El rol se eliminó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al eliminar el rol.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/roles";
    }
}
