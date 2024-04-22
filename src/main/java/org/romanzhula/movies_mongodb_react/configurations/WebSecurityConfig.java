package org.romanzhula.movies_mongodb_react.configurations;

import lombok.RequiredArgsConstructor;
import org.romanzhula.movies_mongodb_react.configurations.security.implementations.UserDetailServiceImpl;
import org.romanzhula.movies_mongodb_react.configurations.security.jwt.components.AuthEntryPointJwt;
import org.romanzhula.movies_mongodb_react.configurations.security.jwt.filters.AuthJwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Value("${app.webSecMyKey}")
    private String myKey;

    private final UserDetailServiceImpl userDetailService;

    private final AuthEntryPointJwt authEntryPointJwtUnauthorizedHandler;

    @Bean
    public AuthJwtFilter authJwtFilter() {
        return new AuthJwtFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();

        dao.setUserDetailsService(userDetailService);
        dao.setPasswordEncoder(passwordEncoder());

        return dao;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity,
            RememberMeServices rememberMeServices
    ) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPointJwtUnauthorizedHandler)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/movies/**").permitAll()
                        .requestMatchers("/api/v1/reviews/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**", "/swagger-resources", "/v3/api-docs/**", "/proxy/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //відсутність зберігання стану сесії на сервері
                )
                .rememberMe((remember) -> remember
                        .rememberMeServices(rememberMeServices)
                )
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(authJwtFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        return httpSecurity.build();
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices.RememberMeTokenAlgorithm encodingAlgorithm =
                TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256;

        TokenBasedRememberMeServices rememberMe =
                new TokenBasedRememberMeServices(myKey, userDetailsService, encodingAlgorithm);

        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256);

        return rememberMe;
    }

}
