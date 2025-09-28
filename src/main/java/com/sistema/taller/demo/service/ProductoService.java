package com.sistema.taller.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.taller.demo.model.Producto;
import com.sistema.taller.demo.repository.ProductoRepository;

@Service
public class ProductoService {
@Autowired
private ProductoRepository productoRepository;
 public List<Producto> obtenerTodo(){
    return productoRepository.findAll();
 }
 public Producto obtenertPorProducto(Integer idInteger){
    return productoRepository.findById(idInteger).orElse(null);

 }
 public Producto guardar(Producto producto){
    return productoRepository.save(producto);
 } 
 public void eliminar(Integer integer){
     productoRepository.deleteById(integer);
 }

}
