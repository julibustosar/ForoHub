package com.juliana.ForoHub.controller;

import java.util.List;

import com.juliana.ForoHub.dto.TopicoRequest;
import com.juliana.ForoHub.dto.TopicoResponse;
import com.juliana.ForoHub.dto.TopicoUpdateRequest;
import com.juliana.ForoHub.exception.TopicoException;
import com.juliana.ForoHub.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topicos")
public class TopicoController {
    
    @Autowired
    private TopicoService topicoService;
    
    @PostMapping
    public ResponseEntity<TopicoResponse> crearTopico(@Valid @RequestBody TopicoRequest request) {
        TopicoResponse response = topicoService.crearTopico(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponse> obtenerTopico(@PathVariable Long id) {
        TopicoResponse response = topicoService.obtenerTopico(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TopicoResponse>> obtenerTopicos() {
        List<TopicoResponse> topicos = topicoService.obtenerTodosLosTopicos();
        return ResponseEntity.ok(topicos);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TopicoResponse> actualizarTopico(
            @PathVariable Long id, 
            @Valid @RequestBody TopicoUpdateRequest request) {
        TopicoResponse response = topicoService.actualizarTopico(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTopico(@PathVariable Long id) {
        topicoService.eliminarTopico(id);
        return ResponseEntity.ok("Tópico eliminado exitosamente");
    }
} 