package com.dreamysips.service_pago.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamysips.service_pago.model.TransaccionPago;
import com.dreamysips.service_pago.service.TransaccionPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/transacciones")
@Tag(name = "Transacciones", description = "Operaciones de transacciones")
@CrossOrigin (origins = "*")
public class TransaccionPagoController{

    @Autowired
    private TransaccionPagoService transaccionPagoService;
    

    @Operation(summary = "Crear transacción")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transacción creada"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PostMapping("/{idPago}")
    public TransaccionPago crear(@Parameter(description = "ID del pago")
            @PathVariable Long idPago,
            @RequestBody TransaccionPago transaccion) 
    {
        return transaccionPagoService.guardar(
                idPago,
                transaccion);
    }

    @Operation(summary = "Obtener todas las transacciones")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public List<TransaccionPago> listar() {
        return transaccionPagoService.listarTodos();
    }

    @Operation(summary = "Buscar transacción por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transacción encontrada"),
        @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransaccionPago> buscarPorId(@Parameter(description = "ID de la transacción")
            @PathVariable Long id) 
    {
        return transaccionPagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar transacciones por estado")
    @ApiResponse(responseCode = "200", description = "Transacciones encontradas")
    @GetMapping("/estado/{estado}")
    public List<TransaccionPago> buscarPorEstado(@Parameter(description = "Estado de la transacción")
            @PathVariable String estado) 
    {
        return transaccionPagoService.buscarPorEstadoTransaccion(estado);
    }

    @Operation(summary = "Eliminar transacción")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Transacción eliminada"),
        @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la transacción")
            @PathVariable Long id) 
    {
        transaccionPagoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
