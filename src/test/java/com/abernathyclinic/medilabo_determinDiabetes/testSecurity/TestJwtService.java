//package com.abernathyclinic.medilabo_determinDiabetes.testSecurity;
//
//import com.abernathyclinic.medilabo_determinDiabetes.security.JwtService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//import io.jsonwebtoken.security.Keys;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//public class TestJwtService {
//
//    @Autowired
//    private JwtService jwtService;
//
//    private final String secret = "hste39h@01maLqP09182naheyd517809";
//
//    @Test
//    void testParse_ValidToken() {
//
//        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//
//        String token = Jwts.builder()
//                .setSubject("testUser")
//                .claim("roles", List.of("ADMIN", "USER"))
//                .setIssuedAt(new Date())
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        Jws<Claims> jws = jwtService.parse(token);
//
//        assertEquals("testUser", jws.getBody().getSubject());
//        assertEquals(List.of("ADMIN", "USER"), jws.getBody().get("roles"));
//    }
//
//    @Test
//    void testParse_InvalidToken() {
//        String invalidToken = "this.is.not.a.jwt";
//        assertThrows(JwtException.class, () -> jwtService.parse(invalidToken));
//    }
//}
