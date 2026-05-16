package com.dreamysips.service_resenas.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto 
{

    private Long idPedido;
    private Long runCliente;
    private Long idDireccion;
    private LocalDateTime fechaPedido;
    private String estado;
    private int valorTotal;

}
