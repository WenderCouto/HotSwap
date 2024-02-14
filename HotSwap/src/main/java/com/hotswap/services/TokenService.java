package com.hotswap.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hotswap.repository.UserKeysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Random;

@Service
public class TokenService {

    @Autowired
    UserKeysRepository userKey;

    private static final Random RANDOM = new SecureRandom();
    private static final int SECRET_LENGTH = 256;

    public String generateSecret(){
        byte[] tokenSecret = new byte[SECRET_LENGTH];
        RANDOM.nextBytes(tokenSecret);
        String temp = Base64.getEncoder().encodeToString(tokenSecret);
        userKey.setUserTokenSecret(temp);
        return temp;
    }

    public String generateToken(String registnumber){
        try{
            String secret = generateSecret();
            Algorithm algorithm = Algorithm.HMAC256(secret);
           String token = JWT.create()
                   .withIssuer("auth-api")
                   .withSubject(registnumber)
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

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(userKey.findUserTokenSecret());
            return JWT.require(algorithm)
                    .withIssuer("api-auth")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch(JWTVerificationException exception){
            return "";
        }

    }
}