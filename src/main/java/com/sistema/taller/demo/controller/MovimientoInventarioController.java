package com.sistema.taller.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sistema.taller.demo.model.MovimientoInventario;
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
}
