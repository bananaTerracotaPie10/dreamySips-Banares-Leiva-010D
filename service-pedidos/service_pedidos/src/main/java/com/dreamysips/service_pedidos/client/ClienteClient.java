package com.dreamysips.service_pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dreamysips.service_pedidos.dto.ClienteDto;
import com.dreamysips.service_pedidos.dto.DireccionEnvioDto;

@FeignClient(name = "service-cliente", url = "${services.cliente.url}")
public interface ClienteClient {

    @GetMapping("/api/v1/clientes/{run}")
    ClienteDto obtenerCliente(@PathVariable Long run);

    @GetMapping("/api/v1/direcciones/{idDireccion}")
    DireccionEnvioDto obtenerDireccion(@PathVariable Long idDireccion);
}