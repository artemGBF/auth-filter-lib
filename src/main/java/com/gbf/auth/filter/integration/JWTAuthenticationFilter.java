package com.gbf.auth.filter.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api")) {
            System.out.println("JWTAuthenticationFilter.doFilterInternal");
            Cookie cookie = WebUtils.getCookie(request, "COOKIE-BEARER");
            if (cookie == null) {
                throw new RuntimeException("No cookie");
            }
            String value = cookie.getValue();
            AuthorisationData authorisationData = service.validate(value);

            List<SimpleGrantedAuthority> grantedAuthorities = authorisationData.getAuthorities().stream()
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            Authentication auth = new UsernamePasswordAuthenticationToken(authorisationData.getLogin(), null, grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
