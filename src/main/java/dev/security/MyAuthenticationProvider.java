package dev.security;

import dev.model.Customer;
import dev.model.Rol;
import dev.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        final Optional<Customer> customerRepository1 = customerRepository.findByEmail(username);
        final Customer customer = customerRepository1.orElseThrow(() -> new BadCredentialsException("Invalid Credentials"));

        final String customerPassword = customer.getPassword();

        final List<Rol> roles = customer.getRoles();
        final var authorities = roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName())).toList();
        return new UsernamePasswordAuthenticationToken(username, password, authorities);

/*
        if(passwordEncoder.matches(password, customerPassword)){
            final List<Rol> roles = customer.getRoles();
            final var authorities = roles.stream()
                    .map(rol -> new SimpleGrantedAuthority(rol.getName())).toList();
            return new UsernamePasswordAuthenticationToken(username, password, authorities);
        }else{
            throw new BadCredentialsException("Invalid Credentials");
        }
 */
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
