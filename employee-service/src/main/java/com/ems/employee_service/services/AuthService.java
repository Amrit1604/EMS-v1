package com.ems.employee_service.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Import @Lazy
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ems.employee_service.config.JwtUtil;
import com.ems.employee_service.dto.LoginDto;
import com.ems.employee_service.dto.RegisterDto;
import com.ems.employee_service.models.User;
import com.ems.employee_service.repositories.UserRepository;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy // Add this annotation to break the cycle
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy // Add this annotation to break the cycle
    private AuthenticationManager authenticationManager; // Add this field

    @Autowired
    private JwtUtil jwtUtil;

    public User register(RegisterDto registerDto) {
        // Check if user already exists
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        User user = new User();
        user.setFullName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail());
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setStatus("ACTIVE");
        // By default, a new user is an employee
        user.setRoles(Set.of("ROLE_EMPLOYEE"));

        return userRepository.save(user);
    }

    public String login(LoginDto loginDto) { // Corrected the method signature
        // This line validates the user's credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        // If credentials are valid, generate a token
        final UserDetails userDetails = loadUserByUsername(loginDto.getEmail());
        return jwtUtil.generateToken(userDetails);
    }

    // This method is required by the UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Converts our User and roles into a format Spring Security understands
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }
}