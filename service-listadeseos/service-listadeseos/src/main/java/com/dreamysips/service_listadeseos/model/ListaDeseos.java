package com.dreamysips.service_listadeseos.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lista_deseos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaDeseos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;

    private Long productoId;

    private Double precioRegistrado;

    private Boolean notificarDescuento;

    private LocalDateTime fechaAgregado;
}