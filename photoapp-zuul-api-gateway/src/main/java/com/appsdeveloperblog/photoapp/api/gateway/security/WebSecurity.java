package com.appsdeveloperblog.photoapp.api.gateway.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final Environment env;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    public WebSecurity(Environment env, RestAuthenticationEntryPoint restAuthenticationEntryPoint, RestAccessDeniedHandler restAccessDeniedHandler) {

        this.env = env;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                .antMatchers(env.getProperty("api.users.actuator.url.path")).permitAll()
                .antMatchers(env.getProperty("api.zuul.actuator.url.path")).permitAll()
                .antMatchers(env.getProperty("api.h2console.url.path")).permitAll()
                .antMatchers(HttpMethod.POST, env.getProperty("api.registration.url.path")).permitAll()
                .antMatchers(HttpMethod.POST, env.getProperty("api.login.url.path")).permitAll()
                .antMatchers(env.getProperty("api.users.url.path")).hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(restAccessDeniedHandler).authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .addFilter(new AuthorizationFilter(authenticationManager(), env));

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
