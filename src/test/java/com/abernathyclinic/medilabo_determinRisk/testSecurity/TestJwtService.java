package com.abernathyclinic.medilabo_determinRisk.testSecurity;

import com.abernathyclinic.medilabo_determinRisk.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestJwtService {

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testParse_ValidToken() {

        String token = Jwts.builder()
                .setSubject("testUser")
                .claim("roles", List.of("ADMIN", "USER"))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,
                        "hste39h@01maLqP09182naheyd517809".getBytes())
                .compact();

        Jws<Claims> jws = jwtService.parse(token);

        assertEquals("testUser", jws.getBody().getSubject());
        assertEquals(List.of("ADMIN", "USER"), jws.getBody().get("roles"));
    }

    @Test
    void testParse_InvalidToken() {
        String invalidToken = "this.is.not.a.jwt";

        assertThrows(JwtException.class, () -> jwtService.parse(invalidToken));
    }
}