package com.dreamysips.service_cliente.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "cliente")
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Cliente 
{

    @Id
    private Long run;

    private String primerNombre;
    private String segundoNombre;
    private String apPaterno;
    private String apMaterno;
    private String correo;
    private String telefono;

    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<DireccionEnvio> direccionEnvio;

    //OneToMany = un cliente puede tenet varias direcciones,
    //pero esas direcciones pertenecen a un solo cliente
}
