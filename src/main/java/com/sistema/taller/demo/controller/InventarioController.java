package com.sistema.taller.demo.controller;

import com.sistema.taller.demo.model.Inventario;
import com.sistema.taller.demo.service.InventarioService;
import com.sistema.taller.demo.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoService productoService;

    // 📌 Listar inventario
    @GetMapping
    public String listarInventario(Model model,
                                   @ModelAttribute("mensaje") String mensaje,
                                   @ModelAttribute("tipoMensaje") String tipoMensaje) {
        model.addAttribute("inventario", inventarioService.obtenerTodo());
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("tipoMensaje", tipoMensaje);
        return "inventario/listado_inventario";
    }

    // 📌 Mostrar formulario de nuevo inventario
    @GetMapping("/nuevo")
    public String nuevoInventario(Model model) {
        model.addAttribute("inventario", new Inventario());
        model.addAttribute("productos", productoService.obtenerTodo());
        return "inventario/crear_editar_inventario";
    }

    // 📌 Guardar inventario (nuevo o existente)
    @PostMapping("/guardar")
    public String guardarInventario(@ModelAttribute Inventario inventario,
                                    RedirectAttributes redirectAttrs) {
        try {
            inventarioService.guardar(inventario);
            redirectAttrs.addFlashAttribute("mensaje", "Inventario guardado correctamente.");
            redirectAttrs.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensaje", "Error al guardar inventario.");
            redirectAttrs.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/inventario";
    }

    // 📌 Mostrar formulario para editar inventario
    @GetMapping("/editar/{id}")
    public String editarInventario(@PathVariable Integer id, Model model) {
        Inventario inventario = inventarioService.obtenerPorID(id);
        model.addAttribute("inventario", inventario);
        model.addAttribute("productos", productoService.obtenerTodo());
        return "inventario/crear_editar_inventario";
    }

    // 📌 Actualizar inventario
    @PostMapping("/editar/{id}")
    public String actualizarInventario(@PathVariable Integer id,
                                       @ModelAttribute Inventario inventario,
                                       RedirectAttributes redirectAttrs) {
        try {
            inventario.setIdInventario(id);
            inventarioService.guardar(inventario);
            redirectAttrs.addFlashAttribute("mensaje", "Inventario actualizado correctamente.");
            redirectAttrs.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensaje", "Error al actualizar inventario.");
            redirectAttrs.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/inventario";
    }

    // 📌 Eliminar inventario
    @GetMapping("/eliminar/{id}")
    public String eliminarInventario(@PathVariable Integer id,
                                     RedirectAttributes redirectAttrs) {
        try {
            inventarioService.eliminar(id);
            redirectAttrs.addFlashAttribute("mensaje", "Inventario eliminado correctamente.");
            redirectAttrs.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensaje", "Error al eliminar inventario.");
            redirectAttrs.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/inventario";
    }
}
