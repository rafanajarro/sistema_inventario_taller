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

import com.sistema.taller.demo.model.Clientes;
import com.sistema.taller.demo.service.ClienteService;

@Controller
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/cuentasPendientesClientes")
    public ResponseEntity<?> obtenerCuentasPendientesClientes() {
        List<Map<String, Object>> resumen = clienteService.obtenerCuentasPendientesClientes();
        return ResponseEntity.ok(resumen);
    }

    // Mostrar clientes
    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        List<Clientes> clientes = clienteService.obtenerTodos();
        model.addAttribute("clientes", clientes);
        return "clientes/listar_clientes";
    }

    // Formulario nuevo cliente
    @GetMapping("/clientes/nuevo")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("clientes", new Clientes());
        return "clientes/crear_editar_clientes";
    }

    // Guardar cliente
    @PostMapping("/clientes/crear")
    public String guardarCliente(@ModelAttribute Clientes cliente, RedirectAttributes redirectAttributes) {
        try {
            clienteService.guardar(cliente);
            redirectAttributes.addFlashAttribute("mensaje", "El cliente se guardó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Ocurrió un error al guardar el cliente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/clientes";
    }

    // Formulario editar cliente
    @GetMapping("/clientes/editar/{id}")
    public String editarClienteForm(@PathVariable Integer id, Model model) {
        Clientes cliente = clienteService.obtenerPorId(id);
        if (cliente == null) {
            return "redirect:/cliente";
        }
        model.addAttribute("clientes", cliente);
        System.out.println("Estado de la cliente: " + cliente);
        return "clientes/crear_editar_clientes";
    }

    // Editar cliente
    @PostMapping("/clientes/editar/{id}")
    public String editarCliente(@PathVariable Integer id, @ModelAttribute Clientes cliente,
            RedirectAttributes redirectAttributes) {
        try {
            Clientes clienteExistente = clienteService.obtenerPorId(id);
            if (clienteExistente == null) {
                return "redirect:/cliente";
            }

            clienteExistente.setNombres(cliente.getNombres());
            clienteExistente.setApellidos(cliente.getApellidos());
            clienteExistente.setTelefono(cliente.getTelefono());
            clienteExistente.setDireccion(cliente.getDireccion());
            clienteExistente.setCorreo(cliente.getCorreo());
            clienteExistente.setEstado(cliente.getEstado());
            clienteExistente.setDui(cliente.getDui());

            clienteService.guardar(clienteExistente);
            redirectAttributes.addFlashAttribute("mensaje", "El cliente se actualizó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al editar el cliente." + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/clientes";
    }

    // Eliminar cliente
    @GetMapping("/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "El cliente se eliminó correctamente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Ocurrió un error al eliminar el cliente.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/clientes";
    }
}
