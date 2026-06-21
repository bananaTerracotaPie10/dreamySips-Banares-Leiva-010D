package com.dreamysips.service_catalogo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamysips.service_catalogo.model.Categoria;
import com.dreamysips.service_catalogo.model.Producto;
import com.dreamysips.service_catalogo.repository.ProductoRepository;

import jakarta.transaction.Transactional;


@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaService categoriaRepository;

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

    @Transactional
    public Producto guardarConCategoria (Long idCategoria, Producto producto)
    {
        Categoria categoria = categoriaRepository.buscarPorId(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada "));
        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }


    public Producto vincularConProducto(Long idCategoria, Long idProducto) 
    {
        Categoria categoria = categoriaRepository.buscarPorId(idCategoria)
            .orElseThrow(() -> new RuntimeException("Categoria no encontrada "));
        
        Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + idProducto));
        
        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }

}
