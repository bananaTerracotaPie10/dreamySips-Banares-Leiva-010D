package com.example.api_gateway.filter;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>
{

    @Value("${jwt.secret}")
    private String secreto;

    public AuthenticationFilter()
    {
        super(Config.class);
    }

    public static class Config
    {
        // Configuracion adicional si es necesario
    }

    @Override
    public GatewayFilter apply (Config config)
    {
        return (exchange, chain) ->
        {
            // cabecera
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            // valida que exista y el formato
            if (authHeader == null || !authHeader.startsWith("Bearer "))
            {
                return onError(exchange,  "Token faltante o formato incorrecto", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring( 7);
            try
            {
            // sintaxis para version 0.10.x y 0.11.x
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token);
            }
            catch (Exception e)
            {
                return onError(exchange,  "Token invalido o expirado", HttpStatus.UNAUTHORIZED);    
            }
            return chain.filter(exchange);
        };
    }

    private Mono <Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus)
    {

        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();

    }


}
