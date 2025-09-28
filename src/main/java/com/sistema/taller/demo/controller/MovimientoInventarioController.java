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
import com.sistema.taller.demo.model.MovimientoInventario;
import com.sistema.taller.demo.model.Producto;
import com.sistema.taller.demo.service.MovimientoInventarioService;

@Controller
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    @GetMapping("/movimientosMensuales")
    public ResponseEntity<?> obtenerMovimientosMensuales() {
        List<Map<String, Object>> resumen = movimientoInventarioService.obtenerMovimientosMensuales();
        return ResponseEntity.ok(resumen);
    }

    // Mostrar
    @GetMapping("/movimientoInventario")
    public String listarMovimientosInventario(Model model) {
        List<MovimientoInventario> movimientosInventario = movimientoInventarioService.obtenerTodos();
        model.addAttribute("movimientoInventario", movimientosInventario);
        return "movimientosInventario/listar_movimientosInventario";
    }

    // Formulario nueva
    @GetMapping("/movimientoInventario/nueva")
    public String mostrarFormularioMovimientosInventario(Model model) {
        model.addAttribute("movimientoInventario", new MovimientoInventario());
        return "movimientosInventario/crear_editar_movimientosInventario";
    }

    // Guardar
    @PostMapping("/movimientoInventario/crear")
    public String guardarMovimientosInventario(@ModelAttribute MovimientoInventario movimientoInventario,
            RedirectAttributes redirectAttributes) {
        try {
            movimientoInventario.setIdProducto(new Producto()); // PENDIENTE
            movimientoInventarioService.guardar(movimientoInventario);
            redirectAttributes.addFlashAttribute("mensaje", "El movimiento se guardó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Ocurrió un error al guardar el movimiento." + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/movimientoInventario";
    }

    // Formulario editar
    @GetMapping("/movimientoInventario/editar/{id}")
    public String editarMovimientosInventarioForm(@PathVariable Integer id, Model model) {
        MovimientoInventario movimientoInventario = movimientoInventarioService.obtenerPorId(id);
        if (movimientoInventario == null) {
            return "redirect:/movimientoInventario";
        }
        model.addAttribute("movimientoInventario", movimientoInventario);
        return "movimientosInventario/crear_editar_movimientosInventario";
    }

    // Editar
    @PostMapping("/movimientoInventario/editar/{id}")
    public String editarMovimientosInventario(@PathVariable Integer id,
            @ModelAttribute MovimientoInventario movimientoInventario,
            RedirectAttributes redirectAttributes) {
        try {
            MovimientoInventario movimientoExistente = movimientoInventarioService.obtenerPorId(id);
            if (movimientoExistente == null) {
                return "redirect:/movimientoInventario";
            }

            movimientoExistente.setTipoMovimiento(movimientoInventario.getTipoMovimiento());
            movimientoExistente.setCantidad(movimientoInventario.getCantidad());
            movimientoExistente.setIdUsuario(movimientoInventario.getIdUsuario());
            movimientoExistente.setIdProducto(movimientoInventario.getIdProducto());

            movimientoInventarioService.guardar(movimientoExistente);
            redirectAttributes.addFlashAttribute("mensaje", "El movimiento se actualizó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al editar el movimiento.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/movimientoInventario";
    }

    // Eliminar
    @GetMapping("/movimientoInventario/eliminar/{id}")
    public String eliminarMovimientosInventario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            movimientoInventarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "El movimiento se eliminó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al eliminar el movimiento.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/movimientoInventario";
    }
}
