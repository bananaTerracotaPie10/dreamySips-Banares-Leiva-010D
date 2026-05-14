package com.dreamysips.service_pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dreamysips.service_pedidos.dto.ProductoDto;

@FeignClient(name = "service-catalogo", url = "${services.catalogo.url}")
public interface ProductoClient {

    @GetMapping("/api/v1/producto/{id}")
    ProductoDto obtenerProducto(@PathVariable Long id);
}