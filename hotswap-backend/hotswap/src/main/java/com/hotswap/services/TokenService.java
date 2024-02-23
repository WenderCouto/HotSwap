package com.hotswap.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TokenService {

    private static final Random RANDOM = new SecureRandom();
    private static final int SECRET_LENGTH = 256;
    private Map<Integer, String> userSecrets = new HashMap<>();

    public String generateSecret(int registnumber){
        byte[] tokenSecret = new byte[SECRET_LENGTH];
        RANDOM.nextBytes(tokenSecret);
        String secret = Base64.getEncoder().encodeToString(tokenSecret);
        userSecrets.put(registnumber, secret); // KEY and VALUE
        return secret;
    }

    public String generateToken(int registnumber){
        try{
            String secret = userSecrets.get(registnumber);
            if(secret == null || secret.isEmpty()){
                secret = generateSecret(registnumber);
            }
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(String.valueOf(registnumber))
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(6).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token, int registnumber){
        try{
            String secret = userSecrets.get(registnumber);
            if(secret == null || secret.isEmpty()){
                secret = generateSecret(registnumber);
            }
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch(JWTVerificationException exception){
            return "Exceção de verificação JWT";
        }
    }
}