package com.appsdeveloperblog.photoapp.api.users.security;

import com.appsdeveloperblog.photoapp.api.users.data.UserEntity;
import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import com.appsdeveloperblog.photoapp.api.users.shared.RoleDto;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.LoginRequestModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UsersService usersService;
    private final Environment environment;

    public AuthenticationFilter(UsersService usersService, Environment environment, AuthenticationManager authenticationManager) {
        this.usersService = usersService;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);

            UserDto userRoles = usersService.getUserDetailsByEmail(creds.getEmail());

            List<String> roles = new ArrayList<>();

            if (userRoles != null) {
                roles = userRoles.getRoles().stream()
                        .map(RoleDto::getRole).collect(Collectors.toList());
            }

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            AuthorityUtils.createAuthorityList(String.valueOf(roles)))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = usersService.getUserDetailsByEmail(username);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth",userDetails.getRoles().stream().map(RoleDto::getRole).collect(Collectors.joining(",")));
        Date issueDate = new Date();
        Date validity = new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time")));

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issueDate)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(token);
        tokenResponse.setTokenExpiration(validity);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(tokenResponse));
    }
}
