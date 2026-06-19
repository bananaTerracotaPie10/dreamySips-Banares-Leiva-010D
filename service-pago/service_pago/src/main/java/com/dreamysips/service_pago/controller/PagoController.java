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

import com.dreamysips.service_pago.model.Pago;
import com.dreamysips.service_pago.model.TransaccionPago;
import com.dreamysips.service_pago.service.PagoService;
import com.dreamysips.service_pago.service.TransaccionPagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Operaciones de pagos")
@CrossOrigin (origins = "*")
public class PagoController 
{

    @Autowired
    private PagoService pagoService;

    @Autowired
    private TransaccionPagoService transaccionPagoService;

    @Operation(summary = "Crear un pago", description = "Registra un pago en db_pagos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public Pago crear(@RequestBody Pago pago) {
        return pagoService.guardar(pago);
    }

    @Operation(summary = "Eliminar un pago", description = "Borra un pago en db_pagos")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pago eliminado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del pago")
            @PathVariable Long id) {

        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener todos los pagos")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida")
    @GetMapping
    public List<Pago> listar() {
        return pagoService.listarTodos();
    }

    @Operation(summary = "Buscar pago por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(
            @Parameter(description = "ID del pago")
            @PathVariable Long id) {

        return pagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar pagos por estado")
    @ApiResponse(responseCode = "200", description = "Pagos encontrados")
    @GetMapping("/estado/{estado}")
    public List<Pago> buscarPorEstado(
            @Parameter(description = "Estado del pago")
            @PathVariable String estado) {

        return pagoService.buscarPorEstado(estado);
    }

    @Operation(summary = "Obtener pago completo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}/completo")
    public Pago obtenerCompleto(
            @Parameter(description = "ID del pago")
            @PathVariable Long id) {

        return pagoService.obtenerPagoConDatosExternos(id);
    }

    @Operation(summary = "Agregar transacción a un pago")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transacción agregada"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PostMapping("/{idPago}/transacciones")
    public ResponseEntity<TransaccionPago> agregarTransaccion(

            @Parameter(description = "ID del pago")
            @PathVariable Long idPago,

            @RequestBody TransaccionPago transaccion) {

        return ResponseEntity.ok(
                transaccionPagoService.guardarConPago(idPago, transaccion));
    }

    @Operation(summary = "Vincular transacción existente a un pago")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transacción vinculada"),
        @ApiResponse(responseCode = "404", description = "Pago o transacción no encontrados")
    })
    @PostMapping("/{idPago}/transacciones/{idTransaccion}")
    public ResponseEntity<TransaccionPago> vinculaConId(

            @Parameter(description = "ID del pago")
            @PathVariable Long idPago,

            @Parameter(description = "ID de la transacción")
            @PathVariable Long idTransaccion) {

        return ResponseEntity.ok(
                transaccionPagoService.vincularConIdTransaccion(
                        idPago,
                        idTransaccion));
    }

}
