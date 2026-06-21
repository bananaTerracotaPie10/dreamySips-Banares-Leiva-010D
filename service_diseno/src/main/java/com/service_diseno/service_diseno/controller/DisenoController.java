package com.service_diseno.service_diseno.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.service_diseno.service_diseno.dto.DisenoDTO;
import com.service_diseno.service_diseno.model.Diseno;
import com.service_diseno.service_diseno.service.DisenoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/disenos")
@Tag(name = "Diseños", description = "Operaciones relacionadas con los diseños personalizados")
public class DisenoController {

    @Autowired
    private DisenoService disenoService;

    @Operation(summary = "Listar todos los diseños")
    @ApiResponse(responseCode = "200", description = "Lista de diseños obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Diseno>> listar() {
        return ResponseEntity.ok(disenoService.listarTodos());
    }

    @Operation(summary = "Obtener diseño por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Diseño encontrado"),
        @ApiResponse(responseCode = "404", description = "Diseño no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Diseno> obtener(
            @Parameter(description = "ID del diseño")
            @PathVariable Long id) {

        return disenoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear diseño personalizado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Diseño creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Diseno> crear(
            @Valid @RequestBody DisenoDTO dto) {

        Diseno diseno = new Diseno();

        diseno.setTamano(dto.getTamano());
        diseno.setAColor(dto.getAColor());
        diseno.setPrecio(dto.getPrecio());
        diseno.setFoto(dto.getFoto());
        diseno.setDibujo(dto.getDibujo());
        diseno.setIdDetalle(dto.getIdDetalle());

        return ResponseEntity.ok(disenoService.guardar(diseno));
    }

    @Operation(summary = "Eliminar diseño")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Diseño eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Diseño no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del diseño")
            @PathVariable Long id) {

        disenoService.eliminar(id);
        return ResponseEntity.ok("Diseño eliminado");
    }

    @Operation(summary = "Listar diseños a color")
    @ApiResponse(responseCode = "200", description = "Diseños encontrados")
    @GetMapping("/color")
    public ResponseEntity<List<Diseno>> obtenerDisenosColor() {

        return ResponseEntity.ok(
                disenoService.listarDisenosColor());
    }

    @Operation(summary = "Buscar diseños por detalle")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Diseños encontrados"),
        @ApiResponse(responseCode = "404", description = "No existen diseños para ese detalle")
    })
    @GetMapping("/detalle/{idDetalle}")
    public ResponseEntity<List<Diseno>> buscarPorDetalle(
            @Parameter(description = "ID del detalle del pedido")
            @PathVariable Long idDetalle) {

        return ResponseEntity.ok(
                disenoService.buscarPorDetalle(idDetalle));
    }
}