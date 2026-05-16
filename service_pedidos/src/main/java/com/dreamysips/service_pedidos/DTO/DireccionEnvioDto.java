package com.dreamysips.service_pedidos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionEnvioDto 
{

    private Long idDireccion;
    private Long runCliente;
    private String ciudad;
    private String comuna;
    private String calle;
    private String numero;
    private String referencia;

}
