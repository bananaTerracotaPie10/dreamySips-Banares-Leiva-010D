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

    @Operation (summary= "Crea un pago", description= "Registra un pago en db_pagos")
    @PostMapping
    public Pago crear (@RequestBody Pago pago)
    {
        return pagoService.guardar(pago);
    }

    @Operation (summary= "Elimina un pago", description= "Borra un pago en db_pagos")
    @DeleteMapping("/{id}")
    public ResponseEntity <Void> eliminar (@PathVariable Long id)
    {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation (summary= "Obtener todos los pagos", description= "Retorna una lista con todos los pagos registrados")
    @GetMapping
    public List<Pago> listar()
    {
        return pagoService.listarTodos();
    }


    @Operation (summary= "Buscar por id", description= "busca un pago por su id")
    @GetMapping("/{id}]")
    public ResponseEntity <Pago> buscarPorId(@PathVariable Long id)
    {
        return pagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation (summary= "Buscar por estado", description= "busca un pago por su estado")
    @GetMapping("/estado/{estado}")
    public List<Pago> buscarPorEstado(@PathVariable String estado) {
        return pagoService.buscarPorEstado(estado);
    }

    @Operation (summary= "Obtener pago completo", description= "Retorna datos de pago + transacciones asociadas")
    @GetMapping("/{id}/completo")
    public Pago obtenerCompleto (@PathVariable Long id)
    {
        return pagoService.obtenerPagoConDatosExternos(id);
    }

    @Operation (summary= "Agregar transaccion a pago", description= "Permite agregar una transaccion a un pago existente")
    @PostMapping ("/{idPago}/transacciones")
    public ResponseEntity<TransaccionPago> agregarTransaccion(@PathVariable Long idPago, @RequestBody TransaccionPago transaccion)
    {
        return ResponseEntity.ok(transaccionPagoService.guardarConPago(idPago, transaccion));
    }

    @Operation (summary= "Vincular con id", description= "Vincula transaccion existente a pedido con su id ")
    @PostMapping ("/{idPago}/transacciones/{idTransaccion}")
    public ResponseEntity <TransaccionPago> vinculaConId (@PathVariable Long idPago, @PathVariable Long idTransaccion)
    {
        return ResponseEntity.ok(transaccionPagoService.vincularConIdTransaccion(idPago, idTransaccion));
    }



    

}
