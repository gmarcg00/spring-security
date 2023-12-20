package dev.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    public final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    public final String JWT_SECRET = "4wD9w!0G7^RfTb#zYgHjKlmN9QpRs3u6";

    private Claims getClaims(String token){
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaimsFromToken(String token, Function<Claims,T> claimsResolver) {
        final var claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDateFromToken(String token){
        return getClaimsFromToken(token,Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        final var expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String getUsernameFromToken(String token){
        return getClaimsFromToken(token,Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final var usernameFromUserDetails = userDetails.getUsername();
        final var usernameFromToken = getUsernameFromToken(token);
        return usernameFromUserDetails.equals(usernameFromToken) && !isTokenExpired(token);
    }

    private String getToken(Map<String,Object> claims, String subject){
        final var key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        final Map<String,Object> claims = Collections.singletonMap("ROLES", userDetails.getAuthorities().toString());
        return getToken(claims,userDetails.getUsername());
    }
}