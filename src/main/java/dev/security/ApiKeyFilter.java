package dev.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class ApiKeyFilter extends OncePerRequestFilter {

    private final String apiKeyHeader = "api_key";
    private final String API_KEY = "myKey";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       try{
           Optional<String> apiKeyOpt = Optional.of(request.getHeader(apiKeyHeader));
           final var apiKey = apiKeyOpt.orElseThrow(() -> new BadCredentialsException("Missing API Key"));
           if(apiKey.equals(API_KEY )) {
               filterChain.doFilter(request, response);
           }else{
               throw new BadCredentialsException("Invalid API Key");
           }
       }catch (BadCredentialsException exception){
           throw new BadCredentialsException("Invalid API Key");
       }
    }
}
