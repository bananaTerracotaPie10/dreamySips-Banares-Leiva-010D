package com.dreamysips.service_pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDto 
{

    private Long run;
    private String primerNombre;
    private String segundoNombre;
    private String apPaterno;
    private String apMaterno;
    private String correo;
    private String telefono;

}
