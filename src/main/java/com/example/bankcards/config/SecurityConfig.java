package com.example.bankcards.config;

import com.example.bankcards.security.PersonDetailsService;
import com.example.bankcards.service.JwtService;
import com.example.bankcards.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    public static final String SWAGGER_UI_RESOURCES = "/swagger-ui/**";
    public static final String SWAGGER_API_DOCS = "/v3/api-docs/**";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/**";
    public static final String SWAGGER_CONFIG = "/configuration/**";
    public static final String WEBJARS = "/webjars/**";
    private static final String API_AUTH = "/api/auth/**";
    private static final String LOGIN_PAGE = "/login";
    private static final String LOGOUT_PAGE = "/logout";
    private static final String ADMIN_PAGE = "/admin";
    private static final String TRANSFER_PAGE = "/transfer";
    private static final String USER_PAGE = "/user";

    private final PersonDetailsService personDetails;
    private final JwtService jwtService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetails, JwtService jwtService) {
        this.personDetails = personDetails;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                LOGIN_PAGE,
                                API_AUTH,
                                SWAGGER_UI,
                                SWAGGER_UI_HTML,
                                SWAGGER_UI_RESOURCES,
                                SWAGGER_API_DOCS,
                                SWAGGER_RESOURCES,
                                SWAGGER_CONFIG,
                                WEBJARS,
                                "/"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, API_AUTH).permitAll()
                        .requestMatchers(ADMIN_PAGE + "/**").hasRole("ADMIN")
                        .requestMatchers(USER_PAGE + "/**").hasRole("USER")
                        .requestMatchers(TRANSFER_PAGE + "/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin -> formLogin
                        .defaultSuccessUrl("/users/info", true)
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe.key("AbcdEfghIjkl..."))
                .logout(logout -> logout
                        .logoutUrl(LOGOUT_PAGE)
                        .logoutSuccessUrl(LOGIN_PAGE)
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(personDetails);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, personDetails);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}