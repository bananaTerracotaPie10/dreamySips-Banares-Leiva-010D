package com.dreamysips.service_resenas.model;

import java.time.LocalDateTime;

import com.dreamysips.service_resenas.DTO.ClienteDto;
import com.dreamysips.service_resenas.DTO.PedidoDto;
import com.dreamysips.service_resenas.DTO.ProductoDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "resenas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resena 
{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idResena;
    private Long idProducto; // fk logica de producto
    private Long runCliente; // fk logica de cliente
    private Long idPedido; // fk logica de pedido
    private int puntuacion;
    private String comentario;
    private LocalDateTime fecha;

    @Transient
    private ProductoDto datosProducto;

    @Transient
    private ClienteDto datosCliente;
    
    @Transient
    private PedidoDto datosPedido;
    

}

