package com.juliana.ForoHub.service;

import com.juliana.ForoHub.dto.AuthRequest;
import com.juliana.ForoHub.dto.AuthResponse;
import com.juliana.ForoHub.model.Usuario;
import com.juliana.ForoHub.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreoElectronico(),
                        request.getContrasena()
                )
        );
        var user = usuarioRepository.findByCorreoElectronico(request.getCorreoElectronico())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, "Autenticación exitosa");
    }

    public AuthResponse register(AuthRequest request) {
        var user = new Usuario();
        user.setCorreoElectronico(request.getCorreoElectronico());
        user.setContrasena(passwordEncoder.encode(request.getContrasena()));
        user.setNombre("Usuario " + request.getCorreoElectronico());
        user.setPerfiles(new ArrayList<>());
        
        usuarioRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, "Usuario registrado exitosamente");
    }
} 