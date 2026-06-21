package com.dreamysips.service_despacho.dto;

import lombok.Data;

@Data
public class PedidoDTO {

    private Long id;
    private Long clienteId;
    private String estado;
    private Double total;
}
