package com.abernathyclinic.medilabo_determinRisk.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.List;

@Configuration
public class SpringSecurityConfig {
    private final JwtService jwtService = new JwtService();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/history/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/history/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtCookieAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }));
        return http.build();
    }


    @Bean
    public Filter jwtCookieAuthFilter() {
        return new Filter() {

            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletRequest request = (HttpServletRequest) req;
                String token = readJwtFromCookie(request);
                if (token != null) {
                    try {
                        var jws = jwtService.parse(token);
                        String username = jws.getBody().getSubject();
                        @SuppressWarnings("unchecked")
                        List<String> roles = (List<String>) jws.getBody().get("roles");
                        var authorities = roles.stream()
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                                .toList();
                        var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } catch (JwtException ignored) {
                    }
                }
                chain.doFilter(req, res);
            }
        };
    }

    private String readJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie c : request.getCookies()) {
            if ("AUTH_TOKEN".equals(c.getName())) return c.getValue();
        }
        return null;
    }
}