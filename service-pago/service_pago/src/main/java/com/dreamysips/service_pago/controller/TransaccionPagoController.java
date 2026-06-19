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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/transaccion")
@Tag(name = "Transacciones", description = "Operaciones de transacciones")
@CrossOrigin (origins = "*")
public class TransaccionPagoController 
{

    @Autowired
    private TransaccionPagoService transaccionPagoService;
    

    @Operation (summary= "Crear transaccion", description= "Registra una transaccion en db_pagos")
    @PostMapping("/{idPago}")
    public TransaccionPago crear(@PathVariable Long idPago, @RequestBody TransaccionPago transaccion) 
    {
        return transaccionPagoService.guardar(idPago, transaccion);
    }

    
    @Operation (summary= "Obtener todas las transacciones", description= "Retorna una lista con todas las transacciones registradas en db_pagos")
    @GetMapping
    public List <TransaccionPago> listar()
    {
        return transaccionPagoService.listarTodos();
    }

    @Operation (summary= "Buscar por id", description= "Busca una transaccion por id en db_pagos")
    @GetMapping("/{id}")
    public ResponseEntity<TransaccionPago> buscarPorId(@PathVariable Long id) 
    {
        return transaccionPagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation (summary= "Buscar por estado", description= "Busca una transaccion por estado en db_pagos")
    @GetMapping("/estado/{estado}")
    public List<TransaccionPago> buscarPorEstado(@PathVariable String estado) 
    {
        return transaccionPagoService.buscarPorEstadoTransaccion(estado);
    }


    @Operation (summary= "Eliminar transaccion", description= "Borra una transaccion en db_pagos")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) 
    {
        transaccionPagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }




}
