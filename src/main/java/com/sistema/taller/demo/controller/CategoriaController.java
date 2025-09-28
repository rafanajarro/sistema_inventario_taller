package com.sistema.taller.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistema.taller.demo.model.Categorias;
import com.sistema.taller.demo.service.CategoriaService;

@Controller
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    // Mostrar categorías
    @GetMapping("/categoria")
    public String listarCategorias(Model model) {
        List<Categorias> categorias = categoriaService.obtenerTodos();
        model.addAttribute("categorias", categorias);
        return "categorias/listar_categorias";
    }

    // Formulario nueva categoría
    @GetMapping("/categorias/nueva")
    public String mostrarFormularioCategoria(Model model) {
        model.addAttribute("categorias", new Categorias());
        return "categorias/crear_editar_categorias";
    }

    // Guardar categoría
    @PostMapping("/categorias/crear")
    public String guardarCategoria(@ModelAttribute Categorias categoria, RedirectAttributes redirectAttributes) {
        try {            
            categoriaService.guardar(categoria);
            redirectAttributes.addFlashAttribute("mensaje", "La categoría se guardó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Ocurrió un error al guardar la categoría.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/categoria";
    }

    // Formulario editar categoría
    @GetMapping("/categorias/editar/{id}")
    public String editarCategoriaForm(@PathVariable Integer id, Model model) {
        Categorias categoria = categoriaService.obtenerPorId(id);
        if (categoria == null) {
            return "redirect:/categoria";
        }
        model.addAttribute("categorias", categoria);
        System.out.println("Estado de la categoria: " + categoria.getEstado());
        return "categorias/crear_editar_categorias";
    }

    // Editar categoría
    @PostMapping("/categorias/editar/{id}")
    public String editarCategoria(@PathVariable Integer id, @ModelAttribute Categorias categoria,
            RedirectAttributes redirectAttributes) {
        try {
            Categorias categoriaExistente = categoriaService.obtenerPorId(id);
            if (categoriaExistente == null) {
                return "redirect:/categoria";
            }

            categoriaExistente.setNombre(categoria.getNombre());
            categoriaExistente.setDescripcion(categoria.getDescripcion());
            categoriaExistente.setEstado(categoria.getEstado());

            categoriaService.guardar(categoriaExistente);
            redirectAttributes.addFlashAttribute("mensaje", "La categoría se actualizó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al editar la categoría.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/categoria";
    }

    // Eliminar categoría
    @GetMapping("/categorias/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "La categoría se eliminó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al eliminar la categoría.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/categoria";
    }
}
