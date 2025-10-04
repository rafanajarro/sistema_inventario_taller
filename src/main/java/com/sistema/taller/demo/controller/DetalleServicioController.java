package com.sistema.taller.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.sistema.taller.demo.model.DetalleServicio;
import com.sistema.taller.demo.model.Inventario;
import com.sistema.taller.demo.model.MovimientoInventario;
import com.sistema.taller.demo.model.Producto;
import com.sistema.taller.demo.model.Servicio;
import com.sistema.taller.demo.model.Usuario;
import com.sistema.taller.demo.repository.InventarioRepository;
import com.sistema.taller.demo.repository.ServicioRepository;
import com.sistema.taller.demo.service.DetalleServicioService;
import com.sistema.taller.demo.service.MovimientoInventarioService;
import com.sistema.taller.demo.service.ProductoService;
import com.sistema.taller.demo.service.ServicioService;
import com.sistema.taller.demo.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DetalleServicioController {
    private final ProductoService productoService;
    private final ServicioService servicioService;
    private final ServicioRepository servicioRepository;
    private final InventarioRepository inventarioRepository;

    @Autowired
    private DetalleServicioService detalleServicioService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    @Autowired
    public DetalleServicioController(
            ServicioRepository servicioRepository,
            ServicioService servicioService,
            ProductoService productoService,
            InventarioRepository inventarioRepository) {
        this.servicioRepository = servicioRepository;
        this.servicioService = servicioService;
        this.productoService = productoService;
        this.inventarioRepository = inventarioRepository;
    }

    @GetMapping("/topProductos")
    public ResponseEntity<?> obtenerStockCategoria() {
        List<Map<String, Object>> resumen = detalleServicioService.obtenerTopProductos();
        return ResponseEntity.ok(resumen);
    }

    // Listar
    @GetMapping("/detalleServicio")
    public String listarDetalleServicio(Model model) {
        List<DetalleServicio> detalleServicios = detalleServicioService.obtenerTodos();
        model.addAttribute("detalleServicios", detalleServicios);
        return "detalleServicio/listar_detalleServicio";
    }

    // Nuevo detalle
    @GetMapping("/detalleServicio/nueva")
    public String nuevoDetalle(Model model) {
        model.addAttribute("detalleServicio", new DetalleServicio());
        model.addAttribute("servicios", servicioService.listar());
        model.addAttribute("productos", productoService.obtenerTodo());
        return "detalleServicio/crear_editar_detalleServicio";
    }

    @PostMapping("/detalleServicio/crear")
    public String guardarDetalleServicio(
            @RequestParam("idServicio") Integer idServicio,
            @RequestParam("idProducto") Integer idProducto,
            @RequestParam("cantidadUsada") Integer cantidadUsada,
            RedirectAttributes redirectAttributes) {

        try {
            // Buscar inventario del producto
            Inventario inventario = inventarioRepository.findByProductoIdProducto(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto sin inventario"));

            if (inventario.getStockActual() < cantidadUsada) {
                redirectAttributes.addFlashAttribute("mensaje", "Stock insuficiente para este producto.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/servicios";
            }

            // Buscar entidades completas
            Servicio servicio = servicioService.buscarPorId(idServicio);
            Producto producto = productoService.obtenertPorProducto(idProducto);

            // Calcular subtotal
            BigDecimal subtotal = inventario.getPrecioVenta().multiply(BigDecimal.valueOf(cantidadUsada));

            // Crear detalle
            DetalleServicio detalle = new DetalleServicio();
            detalle.setIdServicio(servicio);
            detalle.setIdProducto(producto);
            detalle.setCantidadUsada(cantidadUsada);
            detalle.setSubtotal(subtotal.doubleValue());

            detalleServicioService.guardar(detalle);

            // Actualizar inventario
            inventario.setStockActual(inventario.getStockActual() - cantidadUsada);
            inventarioRepository.save(inventario);

            // MOVIMIENTO DE INVENTARIO
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "Anónimo";
            Usuario usuario = usuarioService.obtenerPorUsername(username);

            MovimientoInventario movimientoInventario = new MovimientoInventario();
            movimientoInventario.setTipoMovimiento("SALIDA");
            movimientoInventario.setCantidad(cantidadUsada);
            movimientoInventario.setFechaMovimiento(LocalDateTime.now());
            movimientoInventario.setIdUsuario(usuario);
            movimientoInventario.setIdProducto(producto);
            movimientoInventarioService.guardar(movimientoInventario);

            redirectAttributes.addFlashAttribute("mensaje", "Detalle guardado y stock actualizado.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar detalle: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/servicios";
    }

    // Editar formulario
    @GetMapping("/detalleServicio/editar/{id}")
    public String editarDetalleServicioForm(@PathVariable Integer id, Model model) {
        DetalleServicio detalleServicio = detalleServicioService.obtenerPorId(id);
        if (detalleServicio == null) {
            return "redirect:/detalleServicio";
        }
        model.addAttribute("detalleServicio", detalleServicio);
        return "detalleServicio/crear_editar_detalleServicio";
    }

    // Editar detalle
    @PostMapping("/detalleServicio/editar/{id}")
    public String editarDetalleServicio(@PathVariable Integer id,
            @ModelAttribute DetalleServicio detalleServicio,
            RedirectAttributes redirectAttributes) {
        try {
            DetalleServicio detalleServicioExistente = detalleServicioService.obtenerPorId(id);
            if (detalleServicioExistente == null) {
                return "redirect:/detalleServicio";
            }

            detalleServicioExistente.setIdServicio(detalleServicio.getIdServicio());
            detalleServicioExistente.setIdProducto(detalleServicio.getIdProducto());
            detalleServicioExistente.setCantidadUsada(detalleServicio.getCantidadUsada());
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

    @GetMapping("/detalleServicio/listar/{id}")
    public String listarDetalleServicioid(@PathVariable("id") Integer id, Model model) {
        List<DetalleServicio> detalleServicios = detalleServicioService.obtenerTodosporServicioID(id);
        model.addAttribute("detalleServicios", detalleServicios);
        return "detalleServicio/listar_detalleServicio";
    }

    // Eliminar
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
