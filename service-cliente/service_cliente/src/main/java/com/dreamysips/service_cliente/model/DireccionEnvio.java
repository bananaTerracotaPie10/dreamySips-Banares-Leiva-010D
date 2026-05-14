package com.dreamysips.service_cliente.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direccion_envio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionEnvio 
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long idDireccion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "run_cliente")
    @JsonIgnore 
    private Cliente cliente;

    private String ciudad;
    private String comuna;
    private String calle;
    private String numero;
    private String referencia;

    //ManyToOne = varias direcciones pueden pertenecer a un mismo cliente
    //Join COlumn = Se crea una columna en la tabla direccion_envio llamada run_cliente
    // es una FK (foreign key) que refiere a la pk de la tabla cliente (run)
    

}
