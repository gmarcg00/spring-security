package dev.service;

import dev.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailService implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username).map(customer -> {
            final var roles = customer.getRoles()
                    .stream()
                    .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                    .toList();
            return new User(customer.getEmail(), customer.getPassword(), roles);
        }).orElseThrow(()  -> new UsernameNotFoundException("User not found"));
    }
}
