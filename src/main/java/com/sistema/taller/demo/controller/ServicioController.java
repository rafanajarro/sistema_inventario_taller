package com.sistema.taller.demo.controller;

import com.sistema.taller.demo.model.Servicio;
import com.sistema.taller.demo.service.ClienteService;
import com.sistema.taller.demo.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private ClienteService clienteService;

    // Listar servicios
    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioService.listar());
        return "servicio/listar_servicios";
    }

    // Nuevo servicio
    @GetMapping("/nuevo")
    public String nuevoServicio(Model model) {
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "servicio/crear_editar_servicio";
    }

    // Guardar
    @PostMapping
    public String guardarServicio(@ModelAttribute Servicio servicio) {
        servicioService.guardar(servicio);
        return "redirect:/servicios";
    }

    // Editar
    @GetMapping("/editar/{id}")
    public String editarServicio(@PathVariable Integer id, Model model) {
        Servicio servicio = servicioService.buscarPorId(id);
        model.addAttribute("servicio", servicio);
        model.addAttribute("clientes", clienteService.obtenerTodos());
        return "servicio/crear_editar_servicio";
    }

    // Actualizar
    @PostMapping("/editar/{id}")
    public String actualizarServicio(@PathVariable Integer id, @ModelAttribute Servicio servicio) {
        servicio.setIdServicio(id);
        servicioService.guardar(servicio);
        return "redirect:/servicios";
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Integer id) {
        servicioService.eliminar(id);
        return "redirect:/servicios";
    }
}
