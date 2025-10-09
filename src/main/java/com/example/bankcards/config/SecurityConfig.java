package com.example.bankcards.config;

import com.example.bankcards.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String API_DOCS = "/v3/api-docs/**";
    private static final String REGISTRATION_PAGE = "/registration";
    private static final String LOGIN_PAGE = "/login";
    private static final String LOGOUT_PAGE = "/logout";
    private static final String ADMIN_PAGE = "/admin";
    private static final String TRANSFER_PAGE = "/transfer";
    private static final String USER_PAGE = "/user";

    private final PersonDetailsService personDetails;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetails) {
        this.personDetails = personDetails;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                        auth -> auth.requestMatchers(LOGIN_PAGE,
                                        REGISTRATION_PAGE,
                                        SWAGGER_UI,
                                        API_DOCS,
                                        "/").permitAll()
                                .requestMatchers(HttpMethod.POST, REGISTRATION_PAGE).permitAll()
                                .requestMatchers(ADMIN_PAGE + "/**").hasRole("ADMIN")
                                .requestMatchers(TRANSFER_PAGE + "/**", USER_PAGE + "/**").hasRole("USER")
                                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .defaultSuccessUrl("/users/info", true)
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe.key("AbcdEfghIjkl..."))
                .logout(logout -> logout
                        .logoutUrl(LOGOUT_PAGE)
                        .logoutSuccessUrl(LOGIN_PAGE))
                .csrf(csrf -> csrf.ignoringRequestMatchers(REGISTRATION_PAGE, SWAGGER_UI, API_DOCS))
                .authenticationProvider(authenticationProvider());

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
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
