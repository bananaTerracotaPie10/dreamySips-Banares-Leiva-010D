package com.example.service_notificacion.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;

    private Long pedidoId;

    private String tipo; // EMAIL, SMS, PUSH

    private String asunto;

    private String mensaje;

    private LocalDateTime fechaEnvio;

    private String estado; 
    // ENVIADO, ENTREGADO, LEIDO, ERROR

    private Boolean leido;
}