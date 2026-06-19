package com.dreamysips.service_pago.model;


import java.time.LocalDateTime;
import java.util.List;

import com.dreamysips.service_pago.dto.PedidoDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table (name = "pagos")
@AllArgsConstructor
@NoArgsConstructor
@Schema (description = "Modelo que representa los pagos efectuados")
public class Pago 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema (description= "id unico autoincremental", example="1")
    private Long idPago;

    @Schema (description= "id unico autoincremental", example="1")
    private Long idPedido;

    @Schema (description= "metodo de pago", example="debito")
    private String metodo;

    @Schema (description= "Monto pagado", example="5.000")
    private int monto;

    @Schema (description= "Estado del pago", example="aprobado")
    private String estado; 
    // estado general de pago (pendiente, aprobado y rechazado)

    @Schema (description= "Fecha en que se concreto el pago", example="2026-06-18T18:16:00")
    private LocalDateTime fechaPago;
    //cuando se realizo el pago

    @Transient
    private PedidoDto datosPedido;
    
    @OneToMany (mappedBy = "pagos", cascade = CascadeType.ALL)
    private List <TransaccionPago> detalles;

}
