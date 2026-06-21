package com.dreamysips.service_despacho.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "despachos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pedidoId;

    private Long clienteId;

    private String direccionEntrega;

    private LocalDate fechaDespacho;

    private LocalDate fechaEstimada;

    private LocalDate fechaEntrega;

    private String estado;
    //ESTADOS POSIBLES: PREPARANDO, EN_TRANSITO, ENTREGADO, CANCELADO

    private String codigoSeguimiento;
}