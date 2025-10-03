package com.sistema.taller.demo.controller;

import com.sistema.taller.demo.model.Servicio;
import com.sistema.taller.demo.service.ClienteService;
import com.sistema.taller.demo.service.ProductoService;
import com.sistema.taller.demo.service.ServicioService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private ClienteService clienteService;
    @Autowired

 private ProductoService productoService;

    // Listar servicios
    @GetMapping
    public String listarServicios(Model model,
                                  @RequestParam(value = "mensaje", required = false) String mensaje,
                                  @RequestParam(value = "tipoMensaje", required = false) String tipoMensaje) {
        model.addAttribute("servicios", servicioService.listar());
        model.addAttribute("productos", productoService.obtenerTodo())  ;                              
        if (mensaje != null && !mensaje.isEmpty()) {
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("tipoMensaje", tipoMensaje);
        }

        return "servicio/listar_servicios";
    }

    // Nuevo servicio
    @GetMapping("/nuevo")
    public String nuevoServicio(Model model) {
        Servicio servicio = new Servicio();
        servicio.setFechaServicio(LocalDate.now());
        model.addAttribute("servicio", servicio);
        model.addAttribute("clientes", clienteService.obtenerTodos());
        model.addAttribute("fechaServicioValue", servicio.getFechaServicio().format(DateTimeFormatter.ISO_LOCAL_DATE));
        return "servicio/crear_editar_servicio";
    }

    // Guardar
    @PostMapping
    public String guardarServicio(@ModelAttribute Servicio servicio, RedirectAttributes redirectAttributes) {
        servicioService.guardar(servicio);
        redirectAttributes.addAttribute("mensaje", "Servicio creado correctamente");
        redirectAttributes.addAttribute("tipoMensaje", "success");
        return "redirect:/servicios";
    }

    // Editar
    @GetMapping("/editar/{id}")
    public String editarServicio(@PathVariable Integer id, Model model) {
        Servicio servicio = servicioService.buscarPorId(id);
        model.addAttribute("servicio", servicio);
        model.addAttribute("clientes", clienteService.obtenerTodos());

        if (servicio.getFechaServicio() != null) {
            model.addAttribute("fechaServicioValue", servicio.getFechaServicio().format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            model.addAttribute("fechaServicioValue", "");
        }

        return "servicio/crear_editar_servicio";
    }

    // Actualizar
    @PostMapping("/editar/{id}")
    public String actualizarServicio(@PathVariable Integer id, @ModelAttribute Servicio servicio, RedirectAttributes redirectAttributes) {
        servicio.setIdServicio(id);
        servicioService.guardar(servicio);
        redirectAttributes.addAttribute("mensaje", "Servicio actualizado correctamente");
        redirectAttributes.addAttribute("tipoMensaje", "success");
        return "redirect:/servicios";
    }

   @GetMapping("/eliminar/{id}")
public String eliminarServicio(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
    try {
        servicioService.eliminar(id);
        redirectAttributes.addAttribute("mensaje", "Servicio eliminado correctamente");
        redirectAttributes.addAttribute("tipoMensaje", "success");
    } catch (DataIntegrityViolationException ex) {
        // Aquí capturamos el error por FK
        redirectAttributes.addAttribute("mensaje", "No se puede eliminar el servicio: tiene cuentas pendientes asociadas");
        redirectAttributes.addAttribute("tipoMensaje", "danger");
    }
    return "redirect:/servicios";
}
}
