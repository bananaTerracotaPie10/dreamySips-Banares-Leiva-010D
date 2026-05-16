package com.dreamysips.service_cliente.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dreamysips.service_cliente.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>
{

    //encuentra por run

    Cliente findByRun (Long run);

    @Query ("SELECT c FROM Cliente c" )
    List<Cliente>findAllClientes();

    //encuentra por  nombre o apellido

    List<Cliente> findByPrimerNombreIgnoreCase(String primerNombre);

    List<Cliente> findByApPaternoIgnoreCaseOrApMaternoIgnoreCase(String apPaterno, String apMaterno);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.primerNombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Cliente> buscarPorNombreParcial(@Param("nombre") String nombre);

    //encuentra por correo

    Optional<Cliente> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    //encuentra por comuna o ciudad
    //(retornan clientes que vivan en esa comuna o ciudad)

    @Query("SELECT c FROM Cliente c JOIN c.direccionEnvio d WHERE LOWER(d.comuna) = LOWER(:comuna)")
    List<Cliente> findByComuna(@Param("comuna") String comuna);

    @Query("SELECT c FROM Cliente c JOIN c.direccionEnvio d WHERE LOWER(d.ciudad) = LOWER(:ciudad)")
    List<Cliente> findByCiudad(@Param("ciudad") String ciudad);

    // ve si hay clientes con o sin direccion
    @Query("SELECT c FROM Cliente c WHERE c.direccionEnvio IS NOT EMPTY")
    List<Cliente> findClientesConDireccion();

    @Query("SELECT c FROM Cliente c WHERE c.direccionEnvio IS EMPTY")
    List<Cliente> findClientesSinDireccion();

}
