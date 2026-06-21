package com.dreamysips.service_pedidos.model;

import java.time.LocalDateTime;
import java.util.List;

import com.dreamysips.service_pedidos.DTO.ClienteDto;
import com.dreamysips.service_pedidos.DTO.DireccionEnvioDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table (name = "pedidos")
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

    @Transient
    private ClienteDto datosCliente;
    @Transient
    private DireccionEnvioDto datosDireccion;

    @OneToMany (mappedBy = "pedido", cascade = CascadeType.ALL)
    private List <DetallePedido> detalles;

    // OneToMany = un pedido puede tener muchos productos.
    // pero estos pertenecen a un solo pedido.
}
