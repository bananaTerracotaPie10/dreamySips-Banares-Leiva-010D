package com.service_diseno.service_diseno.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisenoDTO {

    @NotBlank(message = "El tamaño es obligatorio")
    private String tamano;

    @NotNull(message = "Debe indicar si es a color")
    private Boolean aColor;

    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private int precio;

    private String foto;

    private String dibujo;

    @NotNull(message = "Debe tener un detalle asociado")
    private Long idDetalle;
}
