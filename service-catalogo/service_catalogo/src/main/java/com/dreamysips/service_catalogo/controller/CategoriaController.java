package com.dreamysips.service_catalogo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dreamysips.service_catalogo.model.Categoria;
import com.dreamysips.service_catalogo.model.Producto;
import com.dreamysips.service_catalogo.service.CategoriaService;
import com.dreamysips.service_catalogo.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/categoria")
@Tag(name = "Categorias", description = "Operaciones relacionadas con las categorías")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Listar todas las categorías")
    @ApiResponse(responseCode = "200", description = "Categorías obtenidas correctamente")
    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listarTodas();
    }

    @Operation(summary = "Buscar categoría por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtener(
            @Parameter(description = "ID de la categoría")
            @PathVariable Long id) {

        return categoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Categoria> guardar(
            @RequestBody Categoria categoria) {

        return ResponseEntity.ok(
                categoriaService.guardar(categoria));
    }

    @Operation(summary = "Eliminar una categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la categoría")
            @PathVariable Long id) {

        categoriaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Crear un producto asociado a una categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto creado y asociado correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PostMapping("/{idCategoria}/producto")
    public ResponseEntity<Producto> crearConCategoria(

            @Parameter(description = "ID de la categoría")
            @PathVariable Long idCategoria,

            @RequestBody Producto producto) {

        return ResponseEntity.ok(
                productoService.guardarConCategoria(
                        idCategoria,
                        producto));
    }

    @Operation(summary = "Vincular un producto existente a una categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto vinculado correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoría o producto no encontrado")
    })
    @PostMapping("/{idCategoria}/vincular/{idProducto}")
    public ResponseEntity<Producto> vincularConProducto(

            @Parameter(description = "ID de la categoría")
            @PathVariable Long idCategoria,

            @Parameter(description = "ID del producto")
            @PathVariable Long idProducto) {

        return ResponseEntity.ok(
                productoService.vincularConProducto(
                        idCategoria,
                        idProducto));
    }
}