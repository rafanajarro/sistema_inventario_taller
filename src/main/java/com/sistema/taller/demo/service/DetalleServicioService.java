package com.sistema.taller.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.taller.demo.model.DetalleServicio;
import com.sistema.taller.demo.repository.DetalleServicioRepository;

@Service
public class DetalleServicioService {

    @Autowired
    private DetalleServicioRepository detalleServicioRepository;

    public List<Map<String, Object>> obtenerTopProductos() {
        return detalleServicioRepository.findTopProductos();
    }

    public List<DetalleServicio> obtenerTodos() {
        return detalleServicioRepository.findAll();
    }

    public DetalleServicio obtenerPorId(Integer id) {
        return detalleServicioRepository.findById(id).orElse(null);
    }

    public DetalleServicio guardar(DetalleServicio detalleServicio) {
        return detalleServicioRepository.save(detalleServicio);
    }

    public void eliminar(Integer id) {
        detalleServicioRepository.deleteById(id);
    }
}
