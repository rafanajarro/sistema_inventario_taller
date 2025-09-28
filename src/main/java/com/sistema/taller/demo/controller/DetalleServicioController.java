package com.sistema.taller.demo.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sistema.taller.demo.model.DetalleServicio;
import com.sistema.taller.demo.service.DetalleServicioService;

@Controller
public class DetalleServicioController {

    @Autowired
    private DetalleServicioService detalleServicioService;

    @GetMapping("/topProductos")
    public ResponseEntity<?> obtenerStockCategoria() {
        List<Map<String, Object>> resumen = detalleServicioService.obtenerTopProductos();
        return ResponseEntity.ok(resumen);
    }

    // Mostrar categorías
    @GetMapping("/detalleServicio")
    public String listarDetalleServicio(Model model) {
        List<DetalleServicio> detalleServicios = detalleServicioService.obtenerTodos();
        model.addAttribute("detalleServicios", detalleServicios);
        return "detalleServicio/listar_detalleServicio";
    }

    // Formulario nueva categoría
    @GetMapping("/detalleServicio/nueva")
    public String mostrarFormularioDetalleServicio(Model model) {
        model.addAttribute("detalleServicios", new DetalleServicio());
        return "detalleServicio/crear_editar_detalleServicio";
    }

    // Guardar categoría
    @PostMapping("/detalleServicio/crear")
    public String guardarDetalleServicio(@ModelAttribute DetalleServicio detalleServicio,
            RedirectAttributes redirectAttributes) {
        try {
            detalleServicioService.guardar(detalleServicio);
            redirectAttributes.addFlashAttribute("mensaje", "El detalle se guardó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Ocurrió un error al guardar el detalle.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/detalleServicio";
    }

    // Formulario editar categoría
    @GetMapping("/detalleServicio/editar/{id}")
    public String editarDetalleServicioForm(@PathVariable Integer id, Model model) {
        DetalleServicio detalleServicio = detalleServicioService.obtenerPorId(id);
        if (detalleServicio == null) {
            return "redirect:/detalleServicio";
        }
        model.addAttribute("detalleServicio", detalleServicio);
        return "detalleServicio/crear_editar_detalleServicio";
    }

    // Editar categoría
    @PostMapping("/detalleServicio/editar/{id}")
    public String editarDetalleServicio(@PathVariable Integer id, @ModelAttribute DetalleServicio detalleServicio,
            RedirectAttributes redirectAttributes) {
        try {
            DetalleServicio detalleServicioExistente = detalleServicioService.obtenerPorId(id);
            if (detalleServicioExistente == null) {
                return "redirect:/detalleServicio";
            }

            detalleServicioExistente.setIdServicio(detalleServicio.getIdServicio());
            detalleServicioExistente.setIdProducto(detalleServicio.getIdProducto());
            detalleServicioExistente.setCantidadUsada(detalleServicio.getCantidadUsada());
            detalleServicioExistente.setPrecioUnitario(detalleServicio.getPrecioUnitario());
            detalleServicioExistente.setSubtotal(detalleServicio.getSubtotal());

            detalleServicioService.guardar(detalleServicioExistente);
            redirectAttributes.addFlashAttribute("mensaje", "El detalle se actualizó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al editar el detalle.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/detalleServicio";
    }

    // Eliminar categoría
    @GetMapping("/detalleServicio/eliminar/{id}")
    public String eliminarDetalleServicio(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            detalleServicioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "El detalle se eliminó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al eliminar el detalle.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/detalleServicio";
    }
}
