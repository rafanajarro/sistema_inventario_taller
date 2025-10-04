package com.sistema.taller.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import com.sistema.taller.demo.model.CuentasPendientes;
import com.sistema.taller.demo.model.DetalleServicio;
import com.sistema.taller.demo.model.Pagos;
import com.sistema.taller.demo.model.Servicio;
import com.sistema.taller.demo.model.Usuario;
import com.sistema.taller.demo.service.CuentasPendientesService;
import com.sistema.taller.demo.service.DetalleServicioService;
import com.sistema.taller.demo.service.PagoService;
import com.sistema.taller.demo.service.ServicioService;
import com.sistema.taller.demo.service.UsuarioService;

@Controller
public class PagoController {

    @Autowired
    private PagoService pagoService;
    @Autowired
    private ServicioService servicioService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DetalleServicioService detalleServicioService;
    @Autowired
    private CuentasPendientesService cuentasPendientesService;

    // Mostrar
    @GetMapping("/pagos")
    public String listarPagos(Model model) {
        List<Pagos> pagos = pagoService.obtenerTodos();
        model.addAttribute("pagos", pagos);
        return "pagos/listar_pagos";
    }

    // Formulario nuevo
    @GetMapping("/pagos/nuevo")
    public String mostrarFormularioPago(Model model) {
        List<Servicio> serviciosActivos = servicioService.obtenerServiciosActivos();
        model.addAttribute("pagos", new Pagos());
        model.addAttribute("serviciosActivos", serviciosActivos);
        return "pagos/crear_editar_pagos";
    }

    // Guardar categoría
    @PostMapping("/pagos/crear")
    public String guardarPago(@ModelAttribute Pagos pagos, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "Anónimo";
            Usuario usuario = usuarioService.obtenerPorUsername(username);
            pagos.setUsuario(usuario);
            pagoService.guardar(pagos);

            // ACTUALIZAR SERVICIO
            Servicio servicio = servicioService.buscarPorId(pagos.getServicio().getIdServicio());
            double precioBase = (servicio != null && servicio.getPrecioBase() != null)
                    ? servicio.getPrecioBase()
                    : 0.00;
            List<DetalleServicio> detalleServicio = detalleServicioService
                    .obtenerTodosporServicioID(pagos.getServicio().getIdServicio());
            double sumaSubtotal = detalleServicio.stream()
                    .mapToDouble(DetalleServicio::getSubtotal)
                    .sum();

            double montoPagarTotal = sumaSubtotal + precioBase;

            if (pagos.getMontoPagado() == montoPagarTotal) {
                servicio.setEstadoPago("Pagado");
            } else {
                servicio.setEstadoPago("Parcial");

                // AGREGAR A CUENTAS PENDIENTES
                double saldoPendiente = montoPagarTotal - pagos.getMontoPagado();
                CuentasPendientes cuentasPendientes = new CuentasPendientes();
                cuentasPendientes.setIdCliente(servicio.getCliente());
                cuentasPendientes.setMontoTotal(montoPagarTotal);
                cuentasPendientes.setMontoPagado(pagos.getMontoPagado());
                cuentasPendientes.setUltimaFechaPago(LocalDateTime.now());
                cuentasPendientes.setEstado("Pendiente");
                cuentasPendientes.setIdServicio(servicio);
                cuentasPendientes.setSaldoPendiente(saldoPendiente);

                cuentasPendientesService.guardar(cuentasPendientes);
            }
            servicioService.guardar(servicio);

            redirectAttributes.addFlashAttribute("mensaje", "El pago se guardó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Ocurrió un error al guardar el pago." + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/pagos";
    }

    // Formulario editar categoría
    @GetMapping("/pagos/editar/{id}")
    public String editarPagoForm(@PathVariable Integer id, Model model) {
        Pagos pagos = pagoService.obtenerPorId(id);
        if (pagos == null) {
            return "redirect:/pagos";
        }
        List<Servicio> serviciosActivos = servicioService.obtenerServiciosActivos();
        model.addAttribute("pagos", pagos);
        model.addAttribute("serviciosActivos", serviciosActivos);

        return "pagos/crear_editar_pagos";
    }

    // Editar categoría
    @PostMapping("/pagos/editar/{id}")
    public String editarPago(@PathVariable Integer id, @ModelAttribute Pagos pagos,
            RedirectAttributes redirectAttributes) {
        try {
            Pagos pagoExistente = pagoService.obtenerPorId(id);
            if (pagoExistente == null) {
                return "redirect:/pagos";
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "Anónimo";
            Usuario usuario = usuarioService.obtenerPorUsername(username);

            pagoExistente.setServicio(pagos.getServicio());
            pagoExistente.setMontoPagado(pagos.getMontoPagado());
            pagoExistente.setUsuario(usuario);
            pagoExistente.setMetodoPago(pagos.getMetodoPago());

            pagoService.guardar(pagoExistente);
            redirectAttributes.addFlashAttribute("mensaje", "El pago se actualizó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al editar el pago.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/pagos";
    }

    // Eliminar categoría
    @GetMapping("/pagos/eliminar/{id}")
    public String eliminarPago(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            pagoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "El pago se eliminó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al eliminar el pago.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/pagos";
    }
}
