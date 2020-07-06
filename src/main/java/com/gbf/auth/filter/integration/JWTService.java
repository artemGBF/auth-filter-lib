package com.gbf.auth.filter.integration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.util.ArrayList;

@Service
public class JWTService {
    private PublicKey publicKey;

    @PostConstruct
    public void sendRequest() throws IOException, ClassNotFoundException {
        String validateUrl = "http://localhost:8081/getkey";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        PublicKeyDto publicKeyDto = restTemplate.getForObject(validateUrl, PublicKeyDto.class);

        ByteArrayInputStream bi = new ByteArrayInputStream(publicKeyDto.getBytes());
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        System.out.println("obj = " + obj);

        publicKey = (PublicKey) obj;

        oi.close();
        bi.close();

    }

    public AuthorisationData validate(String jwt) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwt);
        String subject = claimsJws.getBody().getSubject();
        ArrayList<String> authorities = claimsJws.getBody().get("authorities", ArrayList.class);
        return new AuthorisationData(subject, authorities);
    }
}
