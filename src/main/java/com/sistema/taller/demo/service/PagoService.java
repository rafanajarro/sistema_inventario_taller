package com.sistema.taller.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.taller.demo.model.Pagos;
import com.sistema.taller.demo.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pagos> obtenerTodos() {
        return pagoRepository.findAll();
    }

    public Pagos obtenerPorId(Integer id) {
        return pagoRepository.findById(id).orElse(null);
    }

    public Pagos guardar(Pagos usuario) {
        return pagoRepository.save(usuario);
    }

    public void eliminar(Integer id) {
        pagoRepository.deleteById(id);
    }
}
