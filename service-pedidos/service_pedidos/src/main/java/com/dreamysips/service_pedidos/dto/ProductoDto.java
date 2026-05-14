package com.dreamysips.service_pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto 
{

    private Long idProducto;
    private String descripcion;
    private int valor;
    private int stock;
    private boolean estado;

}
