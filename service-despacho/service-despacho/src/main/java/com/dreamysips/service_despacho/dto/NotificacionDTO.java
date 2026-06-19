package com.dreamysips.service_despacho.dto;

import lombok.Data;

@Data
public class NotificacionDTO {

    private Long clienteId;
    private String tipo;
    private String asunto;
    private String mensaje;
}
