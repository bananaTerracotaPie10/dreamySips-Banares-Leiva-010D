package com.dreamysips.service_pedidos.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "pedidos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido 
{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idPedido;
    
    private Long runCliente;
    private Long idDireccionEnvio;

    private LocalDateTime fechaPedido;
    private String estado;
    private int valorTotal;


}
