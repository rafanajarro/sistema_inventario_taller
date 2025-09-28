package com.sistema.taller.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.taller.demo.model.MovimientoInventario;
import com.sistema.taller.demo.repository.MovimientoInventarioRepository;

@Service
public class MovimientoInventarioService {
    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    public List<Map<String, Object>> obtenerMovimientosMensuales() {
        return movimientoInventarioRepository.findMovimientosMensuales();
    }

    public List<MovimientoInventario> obtenerTodos() {
        return movimientoInventarioRepository.findAll();
    }

    public MovimientoInventario obtenerPorId(Integer id) {
        return movimientoInventarioRepository.findById(id).orElse(null);
    }

    public void guardar(MovimientoInventario movimientoInventario) {
        movimientoInventarioRepository.save(movimientoInventario);
    }

    public void eliminar(Integer id) {
        movimientoInventarioRepository.deleteById(id);
    }
}
