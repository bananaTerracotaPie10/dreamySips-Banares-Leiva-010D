package com.example.service_notificacion.dto;

import lombok.Data;

@Data
public class Clientedto {

    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
}