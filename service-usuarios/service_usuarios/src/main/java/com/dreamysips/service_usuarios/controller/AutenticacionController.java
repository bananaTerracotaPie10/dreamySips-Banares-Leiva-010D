package com.dreamysips.service_usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamysips.service_usuarios.dto.AuthRequest;
import com.dreamysips.service_usuarios.model.Usuario;
import com.dreamysips.service_usuarios.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping ("/api/v1/auth")
@Tag (name = "Autenticacion", description = "Endpoints para registro y login de usuario")
public class AutenticacionController 
{

    @Autowired
    private AuthService authService;
    @Operation (summary = "Registrar un nuevo usuario", description = "Guarda el usuario con la contraseña encriptada")
    @PostMapping ("/registrar")
    public ResponseEntity <String> registrar (@RequestBody Usuario usuario)
    {
        return ResponseEntity.ok(authService.registrar(usuario));
    }
    @Operation (summary = "Iniciar sesion", description = "Retorna un token JWT si las credenciales son validas")
    @PostMapping ("/login")
    public ResponseEntity <String> login (@RequestBody AuthRequest request)
    {

        try
        {

            String token = authService.login(request.getNombreUsuario(), request.getPassword());
            return ResponseEntity.ok(token);
            
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }


}
