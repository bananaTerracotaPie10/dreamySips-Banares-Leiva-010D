package com.dreamysips.service_catalogo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamysips.service_catalogo.model.Categoria;
import com.dreamysips.service_catalogo.model.Producto;
import com.dreamysips.service_catalogo.service.CategoriaService;
import com.dreamysips.service_catalogo.service.ProductoService;

@RestController
@RequestMapping("/api/v1/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtener(@PathVariable Long id) {
        return categoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Categoria> guardar(@RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.guardar(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //Crea un producto con categoria

    @PostMapping("/{idCategoria}/producto")
    public ResponseEntity<Producto> crearConCategoria(
        @PathVariable Long idCategoria,
        @RequestBody Producto producto)
    {
        return ResponseEntity.ok(productoService.guardarConCategoria(idCategoria, producto));
    }

    //vincula un producto ya existente con una categoria
    @PostMapping("/{idCategoria}/vincular/{idProducto}")
    public ResponseEntity<Producto> vincularConProducto(
        @PathVariable Long idCategoria,
        @PathVariable Long idProducto)
    {
        return ResponseEntity.ok (productoService.vincularConProducto(idCategoria, idProducto));
    }

    

}