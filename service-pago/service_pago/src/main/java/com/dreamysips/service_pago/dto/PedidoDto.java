package com.dreamysips.service_pago.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto 
{

    private Long idPedido;
    private String runCliente;
    private String estado;
    private int total;

}
