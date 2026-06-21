package com.dreamysips.service_cliente.controller;

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

import com.dreamysips.service_cliente.model.Cliente;
import com.dreamysips.service_cliente.model.DireccionEnvio;
import com.dreamysips.service_cliente.service.ClienteService;
import com.dreamysips.service_cliente.service.DireccionEnvioService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping ("/api/v1/clientes")
@Tag(name = "Clientes", description = "Operaciones de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DireccionEnvioService direccionEnvioService;

    @Operation(summary = "Listar todos los clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida")
    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarTodos();
    }
    
    @Operation(summary = "Buscar cliente por RUN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{run}")
    public ResponseEntity<Cliente> obtener(
            @Parameter(description = "RUN del cliente")
            @PathVariable Long run) {

        return clienteService.buscarPorRun(run)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //CRUD

    @Operation(summary = "Crear cliente")
    @ApiResponse(responseCode = "200", description = "Cliente creado correctamente")
    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.guardar(cliente));
    }

    @Operation(summary = "Eliminar cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente eliminado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{run}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "RUN del cliente")
            @PathVariable Long run) {

        clienteService.eliminar(run);
        return ResponseEntity.noContent().build();
    }

    // agrega direccion

    @Operation(summary = "Agregar dirección a un cliente")
    @PostMapping("/{run}/direcciones")
    public ResponseEntity<DireccionEnvio> agregarDireccion(
            @Parameter(description = "RUN del cliente")
            @PathVariable Long run,
            @RequestBody DireccionEnvio direccion) {

        return ResponseEntity.ok(
                direccionEnvioService.guardarConCliente(run, direccion));
    }

    // agrega direccion con id de direccion

    @Operation(summary = "Vincular dirección existente a un cliente")
    @PostMapping("/{run}/direcciones/{idDireccion}")
    public ResponseEntity<DireccionEnvio> vincularDireccion(
            @Parameter(description = "RUN del cliente")
            @PathVariable Long run,

            @Parameter(description = "ID de la dirección")
            @PathVariable Long idDireccion) {

        return ResponseEntity.ok(
                direccionEnvioService.vincularConCliente(run, idDireccion));
    }

    // query por nombre y apellido

    @Operation(summary = "Buscar clientes por nombre")
    @GetMapping("/buscar/nombre/{nombre}")
    public List<Cliente> buscarPorNombre(
            @Parameter(description = "Nombre del cliente")
            @PathVariable String nombre) {

        return clienteService.buscarPorNombre(nombre);
    }

    @Operation(summary = "Buscar clientes por apellido")
    @GetMapping("/buscar/apellido/{apellido}")
    public List<Cliente> buscarPorApellido(
            @Parameter(description = "Apellido del cliente")
            @PathVariable String apellido) {

        return clienteService.buscarPorApellido(apellido);
    }

    @Operation(summary = "Buscar clientes por nombre parcial")
    @GetMapping("/buscar/nombre-parcial/{nombre}")
    public List<Cliente> buscarPorNombreParcial(
            @Parameter(description = "Texto parcial del nombre")
            @PathVariable String nombre) 
    {
        return clienteService.buscarPorNombreParcial(nombre);
    }

    //query por correo

    @Operation(summary = "Buscar cliente por correo")
    @GetMapping("/buscar/correo/{correo}")
    public ResponseEntity<Cliente> buscarPorCorreo(
            @Parameter(description = "Correo electrónico")
            @PathVariable String correo) {

        return clienteService.buscarPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Verificar existencia de correo")
    @GetMapping("/existe/correo/{correo}")
    public ResponseEntity<Boolean> existeCorreo(
            @Parameter(description = "Correo electrónico")
            @PathVariable String correo) {

        return ResponseEntity.ok(
                clienteService.existeCorreo(correo));
    }

    // comuna o ciudad 
    @Operation(summary = "Buscar clientes por comuna")
    @GetMapping("/buscar/comuna/{comuna}")
    public List<Cliente> buscarPorComuna(
            @Parameter(description = "Comuna")
            @PathVariable String comuna) {

        return clienteService.buscarPorComuna(comuna);
    }

    @Operation(summary = "Buscar clientes por ciudad")
    @GetMapping("/buscar/ciudad/{ciudad}")
    public List<Cliente> buscarPorCiudad(
            @Parameter(description = "Ciudad")
            @PathVariable String ciudad) {

        return clienteService.buscarPorCiudad(ciudad);
    }

    // Con o sin dirección 
    @Operation(summary = "Listar clientes con dirección")
    @GetMapping("/con-direccion")
    public List<Cliente> listarConDireccion() {
        return clienteService.listarConDireccion();
    }

    @Operation(summary = "Listar clientes sin dirección")
    @GetMapping("/sin-direccion")
    public List<Cliente> listarSinDireccion() {
        return clienteService.listarSinDireccion();
    }


}
