package com.service_diseno.service_diseno.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diseno")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diseno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diseno")
    private Long idDiseno;

    private String tamano;

    @Column(name = "a_color")
    private Boolean aColor;

    private int precio;

    private String foto;

    private String dibujo;

    @Column(name = "id_detalle")
    private Long idDetalle;

}