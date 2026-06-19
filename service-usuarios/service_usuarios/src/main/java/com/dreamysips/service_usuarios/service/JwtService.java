package com.dreamysips.service_usuarios.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtService 
{

    @Value("${jwt.secret}")
    private String secreto;
    public String generarToken (String username, List<String> roles)
    {

        long dosHorasEnMilSegundos = 1000* 60 * 60 * 2;
        return Jwts.builder()
               .setSubject(username)
               .claim("roles", roles)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + dosHorasEnMilSegundos))
               .signWith(Keys.hmacShaKeyFor(secreto.getBytes()), SignatureAlgorithm.HS256)
               .compact();

    }

}
