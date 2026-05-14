package com.dreamysips.service_catalogo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamysips.service_catalogo.model.Producto;
import com.dreamysips.service_catalogo.repository.ProductoRepository;


@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos(){
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long idProducto){
        return productoRepository.findById(idProducto);
    }

    public Producto guardar(Producto producto){
        return productoRepository.save(producto);
    }

    public void eliminar (Long idProducto){
        productoRepository.deleteById(idProducto);
    }
}
