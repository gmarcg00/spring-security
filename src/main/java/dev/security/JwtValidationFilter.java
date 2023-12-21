package dev.security;

import dev.service.JwtService;
import dev.service.JwtUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final JwtUserDetailService jwtUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final var authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);

            try{
                username = jwtService.getUsernameFromToken(jwt);
            }catch (IllegalArgumentException exception){
                throw new RuntimeException("Unable to get JWT Token");
            }catch (ExpiredJwtException exception){
                throw new RuntimeException("JWT Token has expired");
            }
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            var userDetails = jwtUserDetailService.loadUserByUsername(username);

            if(jwtService.validateToken(jwt, userDetails)){
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(userDetails);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else{
                throw new RuntimeException("Invalid JWT Token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
