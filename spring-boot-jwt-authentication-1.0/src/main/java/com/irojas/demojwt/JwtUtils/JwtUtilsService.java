package com.irojas.demojwt.JwtUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtilsService {
    //Aqui recien utilizamos JWToken

    private static final String SECRET_KEY="586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, claims -> claims.getSubject());  //esto ES UN METODO GENERICO QUE DEVUELVE [ T ]
        //pero que T adquiere un String para devolver debido a la funcion que yo programe, claims -> claims.getSubject() donde getSubject() devuelve un String
        //claims (argumento o parametro que recibe funtion interfase), -> claims.getSubject() es lo que va a devolver
    }

    //Estoy diciendo que al mismo tiempo que reciva un T por parametro, voy a devolver un T que podria ser un Object cualquiera
    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);  //retorna como sifuera un stream o lista de Claims
        return claimsResolver.apply(claims); //Por cada CLaim, osea, [ claims -> claims.getSubject() ]  ejecutare la funcion getSubject
    }
    //ESTE METODO IGUAL SIRVE
    /*public String getClaim(String token, Function<Claims, String> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }*/


    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts     //Lo que hacemos aqui es usar el algoritmo de ecriptacion para generar el token
            .builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private Key getKey() {
       byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
       return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
    
}
