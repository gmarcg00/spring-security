package dev.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtValidationFilter jwtValidationFilter) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        var requestHandler = new CsrfTokenRequestAttributeHandler();
        try {
             http.authorizeHttpRequests(auth ->
                            //auth.requestMatchers("/loans/**","/balance/**","/accounts", "/cards")
                            auth.requestMatchers("/loans","/cards").hasRole("USER")
                                    //.requestMatchers("/account","/balance").hasRole("ADMIN")
                                    .anyRequest().permitAll())
                    .formLogin(Customizer.withDefaults())
                    .httpBasic(Customizer.withDefaults());

             http.addFilterAfter(jwtValidationFilter, BasicAuthenticationFilter.class);

             http.cors(cors -> corsConfigurationSource());
             http.csrf(csrf -> csrf.csrfTokenRequestHandler(requestHandler)
                     .ignoringRequestMatchers("/welcome","/about_us","/authenticate")
                     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
             .addFilterAfter( new CsrfCookieFilter(), BasicAuthenticationFilter.class);
             return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /*
    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        UserDetails admin = User.withUsername("admin")
                .password("security")
                .authorities("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password("security")
                .authorities("USER")
                .build();

        return new InMemoryUserDetailsManager(admin,user);
    }


    @Bean
    UserDetailsService userDetailsService(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration corsConfiguration = new CorsConfiguration();
      //corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
      corsConfiguration.setAllowedOrigins(List.of("*"));
      //corsConfiguration.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
      corsConfiguration.setAllowedMethods(List.of("*"));
      //corsConfiguration.setAllowedHeaders(List.of("Authorization","Content-Type"));
      corsConfiguration.setAllowedHeaders(List.of("*"));

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", corsConfiguration);

      return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
