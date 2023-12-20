package dev.controller;


import dev.request.model.JwtRequest;
import dev.response.model.JwtResponse;
import dev.service.JwtService;
import dev.service.JwtUserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUserDetailService jwtUserDetailService;

    private final JwtService jwtService;
    @PostMapping("/authenticate")
    public ResponseEntity<?> postToken (@RequestBody JwtRequest jwtRequest){
        authenticate(jwtRequest);
        final var userDetails = jwtUserDetailService.loadUserByUsername(jwtRequest.getUsername());
        final var token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(JwtRequest jwtRequest) {
       try{
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
       }catch (BadCredentialsException | DisabledException exception ){
           throw new RuntimeException(exception.getMessage());
       }
    }
}
