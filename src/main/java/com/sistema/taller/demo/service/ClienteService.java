package com.sistema.taller.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.taller.demo.model.Clientes;
import com.sistema.taller.demo.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Map<String, Object>> obtenerCuentasPendientesClientes() {
        return clienteRepository.findCuentasPendientesClientes();
    }

    public List<Clientes> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public Clientes obtenerPorId(Integer id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public Clientes guardar(Clientes usuario) {
        return clienteRepository.save(usuario);
    }

    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }
}
