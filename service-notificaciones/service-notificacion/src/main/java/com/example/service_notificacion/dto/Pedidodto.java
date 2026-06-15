package com.example.service_notificacion.dto;

import lombok.Data;

@Data
public class Pedidodto {
    private Long id;
    private String estado;
    private Double total;
}