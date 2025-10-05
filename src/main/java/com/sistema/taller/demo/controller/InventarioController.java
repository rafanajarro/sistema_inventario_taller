package com.sistema.taller.demo.controller;

import com.sistema.taller.demo.model.Inventario;
import com.sistema.taller.demo.model.MovimientoInventario;
import com.sistema.taller.demo.model.Usuario;
import com.sistema.taller.demo.service.InventarioService;
import com.sistema.taller.demo.service.MovimientoInventarioService;
import com.sistema.taller.demo.service.ProductoService;
import com.sistema.taller.demo.service.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    // 📌 Listar inventario
    @GetMapping
    public String listarInventario(Model model) {
        List<Inventario> inventarios = inventarioService.obtenerTodo();
        model.addAttribute("inventario", inventarios);
        return "inventario/listado_inventario";
    }

    // 📌 Mostrar formulario de nuevo inventario
    @GetMapping("/nuevo")
    public String nuevoInventario(Model model) {
        model.addAttribute("inventario", new Inventario());
        model.addAttribute("productos", productoService.obtenerProductoSinInventario());
        return "inventario/crear_editar_inventario";
    }

    // 📌 Guardar inventario (nuevo o existente)
    @PostMapping("/guardar")
    public String guardarInventario(@ModelAttribute Inventario inventario,
            RedirectAttributes redirectAttrs) {
        try {
            // VALIDAR QUE NO EXISTA UN INVENTARIO YA REGISTRADO
            Inventario inv = inventarioService.buscarPorIdProducto(inventario.getProducto().getIdProducto());
            if (inv != null) {
                redirectAttrs.addFlashAttribute("mensaje",
                        "Ya existe un inventario para el producto: " + inventario.getProducto().getNombreProducto());
                redirectAttrs.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/inventario";
            }

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
        model.addAttribute("productos", productoService.obtenerProductoSinInventario());
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
