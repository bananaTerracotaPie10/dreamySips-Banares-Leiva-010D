package com.dreamysips.service_resenas.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto 
{

    private Long idProducto;
    private Long idCategoria;
    private String descripcion;
    private int valor;
    private int stock;
    private String estado;

}
