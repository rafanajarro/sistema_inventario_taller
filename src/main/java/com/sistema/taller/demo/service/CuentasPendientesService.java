package com.sistema.taller.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sistema.taller.demo.model.CuentasPendientes;
import com.sistema.taller.demo.repository.CuentasPendientesRepository;

@Service
public class CuentasPendientesService {

    @Autowired
    private CuentasPendientesRepository cuentasPendientesRepository;

    public List<Map<String, Object>> obtenerAlertasPagosPendientes() {
        return cuentasPendientesRepository.findAlertasPagosPendientes();
    }

    public List<CuentasPendientes> obtenerTodos() {
        return cuentasPendientesRepository.findAll();
    }

    public CuentasPendientes obtenerPorId(Integer id) {
        return cuentasPendientesRepository.findById(id).orElse(null);
    }

    public CuentasPendientes guardar(CuentasPendientes cuentasPendientes) {
        return cuentasPendientesRepository.save(cuentasPendientes);
    }

    public void eliminar(Integer id) {
        cuentasPendientesRepository.deleteById(id);
    }
}
