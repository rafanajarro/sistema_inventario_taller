package com.sistema.taller.demo.controller;

import com.sistema.taller.demo.model.Inventario;
import com.sistema.taller.demo.service.InventarioService;
import com.sistema.taller.demo.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoService productoService;

    // 📌 Listar inventario
    @GetMapping
    public String listarInventario(Model model) {
        model.addAttribute("inventario", inventarioService.obtenerTodo());
        return "inventario/listado_inventario"; // nombre correcto del template
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
    public String guardarInventario(@ModelAttribute Inventario inventario) {
        inventarioService.guardar(inventario);
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
    public String actualizarInventario(@PathVariable Integer id, @ModelAttribute Inventario inventario) {
        inventario.setIdInventario(id);
        inventarioService.guardar(inventario);
        return "redirect:/inventario";
    }

    // 📌 Eliminar inventario
    @GetMapping("/eliminar/{id}")
    public String eliminarInventario(@PathVariable Integer id) {
        inventarioService.eliminar(id);;
        return "redirect:/inventario";
    }
}
