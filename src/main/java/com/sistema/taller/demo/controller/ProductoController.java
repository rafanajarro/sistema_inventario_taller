package com.sistema.taller.demo.controller;

import com.sistema.taller.demo.model.Producto;
import com.sistema.taller.demo.service.ProductoService;
import com.sistema.taller.demo.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    // 📌 Listar todos los productos
    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.obtenerTodo());
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
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.guardar(producto);
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
    public String actualizarProducto(@PathVariable Integer id, @ModelAttribute Producto producto) {
        producto.setIdMovimiento(id);
        productoService.guardar(producto);
        return "redirect:/productos";
    }

    // 📌 Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id) {
        productoService.eliminar(id);
        return "redirect:/productos";
    }
}
