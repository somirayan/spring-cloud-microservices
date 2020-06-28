package com.appsdeveloperblog.photoapp.api.gateway.security;

import com.appsdeveloperblog.photoapp.api.gateway.exception.CustomException;
import io.jsonwebtoken.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final Environment env;

    public AuthorizationFilter(AuthenticationManager authenticationManager, Environment env) {
        super(authenticationManager);
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(env.getProperty("authorization.token.header.name"));

        if (authorizationHeader == null || !authorizationHeader.startsWith(env.getProperty("authorization.token.header.prefix"))) {
            chain.doFilter(request, response);
            return;
        }

        validateToken(authorizationHeader);

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(authorizationHeader);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private boolean validateToken(String authorizationHeader) throws JwtException, IllegalArgumentException, IOException {
        String token = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"), "");
        Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(token);
        return true;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String authorizationHeader) {

        if (authorizationHeader == null) {
            return null;
        }

        String token = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"), "");
        String userId = Jwts.parser()
                .setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();


        if (userId == null) {
            return null;
        }


        return new UsernamePasswordAuthenticationToken(userId, null, getAuthorities(token));
    }

    private List<SimpleGrantedAuthority> getAuthorities(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(token)
                .getBody();

        return Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }


}
