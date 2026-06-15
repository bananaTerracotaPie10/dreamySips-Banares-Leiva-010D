package com.example.service_notificacion.dto;

import lombok.Data;

@Data
public class Notificaciondto {

    private Long clienteId;
    private Long pedidoId;

    private String tipo;
    private String asunto;
    private String mensaje;
}