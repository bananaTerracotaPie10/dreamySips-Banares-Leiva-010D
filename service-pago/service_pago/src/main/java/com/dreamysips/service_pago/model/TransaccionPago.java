package com.dreamysips.service_pago.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table (name = "transacciones_pago")
@AllArgsConstructor
@NoArgsConstructor
@Schema (description= "Modelo que representa las transacciones efectuadas")
public class TransaccionPago 
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema (description= "id unico autoincremental", example="1")
    private Long idTransaccion;


    @Schema (description= "id unico autoincremental", example="cobro")
    private String tipoTransaccion;
    // cobro, reembolso, ajuste


    @Schema (description= "Monto de la transaccion", example="10000")
    private int montoTransaccion;

    @Schema (description= "Estado de la transaccion", example="aprobado")
    private String estadoTransaccion;
    // estado: pendiente, aprobado, rechazado

    @Schema (description= "Fecha de la transaccion", example="2026-06-18T18:16:00")
    private LocalDateTime fechaTransaccion;


    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "id_pago")
    @JsonIgnore
    private Pago pagos;
    

}
