package com.sistema.taller.demo.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sistema.taller.demo.model.CuentasPendientes;
import com.sistema.taller.demo.service.CuentasPendientesService;

@Controller
public class CuentasPendientesController {

    @Autowired
    private CuentasPendientesService cuentasPendientesService;

    @Autowired
    private static Logger logger = LoggerFactory.getLogger(CuentasPendientesController.class);

    // Mostrar
    @GetMapping("/cuentasPendientes")
    public String listarCuentasPendientes(Model model) {
        List<CuentasPendientes> cuentasPendientes = cuentasPendientesService.obtenerTodos();
        model.addAttribute("cuentasPendientes", cuentasPendientes);
        return "cuentasPendientes/listar_cuentasPendientes";
    }

    // Formulario nueva
    @GetMapping("/cuentasPendientes/nueva")
    public String mostrarFormularioCuentasPendientes(Model model) {
        model.addAttribute("cuentasPendientes", new CuentasPendientes());
        return "cuentasPendientes/crear_editar_cuentasPendientes";
    }

    // Guardar
    @PostMapping("/cuentasPendientes/crear")
    public String guardarCuentasPendientes(@ModelAttribute CuentasPendientes cuentasPendientes,
            RedirectAttributes redirectAttributes) {
        try {
            cuentasPendientesService.guardar(cuentasPendientes);
            redirectAttributes.addFlashAttribute("mensaje", "La cuenta se guardó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Ocurrió un error al guardar la cuenta.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/cuentasPendientes";
    }

    // Formulario editar
    @GetMapping("/cuentasPendientes/editar/{id}")
    public String editarCuentasPendientesForm(@PathVariable Integer id, Model model) {
        CuentasPendientes cuentasPendientes = cuentasPendientesService.obtenerPorId(id);
        if (cuentasPendientes == null) {
            return "redirect:/cuentasPendientes";
        }
        model.addAttribute("cuentasPendientes", cuentasPendientes);
        return "cuentasPendientes/crear_editar_cuentasPendientes";
    }

    // Editar
    @PostMapping("/cuentasPendientes/editar")
    public String editarCuentasPendientes(@RequestParam("idCuenta") Integer idCuenta,
            @RequestParam("abono") Double abono, RedirectAttributes redirectAttributes) {
        try {
            CuentasPendientes cuentasPendientesExistente = cuentasPendientesService.obtenerPorId(idCuenta);
            if (cuentasPendientesExistente == null) {
                return "redirect:/cuentasPendientes";
            }

            if (abono <= 0) {
                redirectAttributes.addFlashAttribute("mensaje", "El abono debe ser mayor a cero.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/cuentasPendientes";
            }

            double saldoPendienteActual = cuentasPendientesExistente.getSaldoPendiente();
            logger.info("saldoPendienteActual " + saldoPendienteActual);
            if (abono > saldoPendienteActual) {
                redirectAttributes.addFlashAttribute("mensaje",
                        "El abono no puede ser mayor al saldo pendiente. Saldo pendiente actual: $" + saldoPendienteActual);
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/cuentasPendientes";
            }

            double nuevoMontoPagado = cuentasPendientesExistente.getMontoPagado() + abono;
            double nuevoSaldoPendiente = saldoPendienteActual - abono;

            cuentasPendientesExistente.setMontoPagado(nuevoMontoPagado);
            cuentasPendientesExistente.setSaldoPendiente(nuevoSaldoPendiente);

            if (nuevoSaldoPendiente == 0) {
                cuentasPendientesExistente.setEstado("Liquidado");
            } else {
                cuentasPendientesExistente.setEstado("Parcial");
            }

            cuentasPendientesService.guardar(cuentasPendientesExistente);
            redirectAttributes.addFlashAttribute("mensaje", "La cuenta se actualizó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al editar la cuenta.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/cuentasPendientes";
    }

    // Eliminar
    @GetMapping("/cuentasPendientes/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            cuentasPendientesService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "La cuenta se eliminó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al eliminar la cuenta.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/cuentasPendientes";
    }
}
