package com.sistema.taller.demo.controller;

import com.sistema.taller.demo.model.Producto;
import com.sistema.taller.demo.service.ProductoService;
import com.sistema.taller.demo.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    // 📌 Listar todos los productos
    @GetMapping
    public String listarProductos(Model model,
                                  @RequestParam(value = "mensaje", required = false) String mensaje,
                                  @RequestParam(value = "tipoMensaje", required = false) String tipoMensaje) {
        model.addAttribute("productos", productoService.obtenerTodo());
        if (mensaje != null && !mensaje.isEmpty()) {
            model.addAttribute("mensaje", mensaje);
            model.addAttribute("tipoMensaje", tipoMensaje);
        }
        return "producto/listado_productos";
    }

    // 📌 Mostrar formulario para crear un nuevo producto
    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.obtenerTodos());
        return "producto/crear_editar_producto";
    }

    // 📌 Guardar un nuevo producto
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        productoService.guardar(producto);
        redirectAttributes.addAttribute("mensaje", "Producto creado correctamente");
        redirectAttributes.addAttribute("tipoMensaje", "success");
        return "redirect:/productos";
    }

    // 📌 Mostrar formulario para editar un producto existente
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto producto = productoService.obtenertPorProducto(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.obtenerTodos());
        return "producto/crear_editar_producto";
    }

    // 📌 Actualizar producto
    @PostMapping("/editar/{id}")
    public String actualizarProducto(@PathVariable Integer id, @ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        producto.setIdMovimiento(id);
        productoService.guardar(producto);
        redirectAttributes.addAttribute("mensaje", "Producto actualizado correctamente");
        redirectAttributes.addAttribute("tipoMensaje", "success");
        return "redirect:/productos";
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
