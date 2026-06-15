package com.dreamysips.service_listadeseos.dto;
import lombok.Data;

@Data
public class Productodto {
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
}