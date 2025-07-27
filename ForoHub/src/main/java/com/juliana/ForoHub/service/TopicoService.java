package com.juliana.ForoHub.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.juliana.ForoHub.dto.TopicoRequest;
import com.juliana.ForoHub.dto.TopicoResponse;
import com.juliana.ForoHub.dto.TopicoUpdateRequest;
import com.juliana.ForoHub.exception.TopicoException;
import com.juliana.ForoHub.model.Topico;
import com.juliana.ForoHub.model.Usuario;
import com.juliana.ForoHub.repository.TopicoRepository;
import com.juliana.ForoHub.repository.UsuarioRepository;

@Service
public class TopicoService {
    
    @Autowired
    private TopicoRepository topicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public TopicoResponse crearTopico(TopicoRequest request) {
        // Verificar si el tópico ya existe
        if (topicoRepository.existsByTituloAndMensaje(request.getTitulo(), request.getMensaje())) {
            throw new TopicoException("Ya existe un tópico con el mismo título y mensaje");
        }
        
        // Buscar el autor
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario autor = usuarioRepository.findByCorreoElectronico(authentication.getName())
                .orElseThrow(() -> new TopicoException("Usuario no encontrado"));
        
        // Crear el nuevo tópico
        Topico topico = new Topico();
        topico.setTitulo(request.getTitulo());
        topico.setMensaje(request.getMensaje());
        topico.setAutor(autor);
        topico.setFechaCreacion(LocalDateTime.now());
        topico.setStatus("NO_RESPONDIDO");
        
        // Guardar en la base de datos
        Topico topicoGuardado = topicoRepository.save(topico);
        
        // Crear y retornar la respuesta
        return new TopicoResponse(
                topicoGuardado.getId(),
                topicoGuardado.getTitulo(),
                topicoGuardado.getMensaje(),
                topicoGuardado.getFechaCreacion(),
                topicoGuardado.getStatus(),
                topicoGuardado.getAutor().getNombre()
        );
    }

    public TopicoResponse obtenerTopico(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new TopicoException("El tópico con ID " + id + " no existe"));

        return new TopicoResponse(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor().getNombre()
        );
    }
    
    public List<TopicoResponse> obtenerTodosLosTopicos() {
        List<Topico> topicos = topicoRepository.findAll();
        return topicos.stream()
                .map(topico -> new TopicoResponse(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getMensaje(),
                        topico.getFechaCreacion(),
                        topico.getStatus(),
                        topico.getAutor().getNombre()
                ))
                .collect(Collectors.toList());
    }
    
    public TopicoResponse actualizarTopico(Long id, TopicoUpdateRequest request) {
        // Obtener el tópico existente
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new TopicoException("El tópico con ID " + id + " no existe"));
        
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = usuarioRepository.findByCorreoElectronico(authentication.getName())
                .orElseThrow(() -> new TopicoException("Usuario no encontrado"));
        
        // Verificar que el usuario sea el autor del tópico
        if (!topico.getAutor().getId().equals(usuarioActual.getId())) {
            throw new TopicoException("No tienes permisos para editar este tópico");
        }
        
        // Variables para almacenar los valores que se van a actualizar
        String nuevoTitulo = request.getTitulo();
        String nuevoMensaje = request.getMensaje();

        if (nuevoTitulo == null) {
            nuevoTitulo = topico.getTitulo();
        }

        if (nuevoMensaje == null) {
            nuevoMensaje = topico.getMensaje();
        }

        // Verificar si los nuevos valores ya existen en otro tópico (solo si algo cambió)
        if (!topico.getTitulo().equals(nuevoTitulo) || !topico.getMensaje().equals(nuevoMensaje)) {
            if (topicoRepository.existsByTituloAndMensaje(nuevoTitulo, nuevoMensaje)) {
                throw new TopicoException("Ya existe un tópico con el mismo título y mensaje");
            }
        }

        if (request.getTitulo() != null) {
            topico.setTitulo(nuevoTitulo);
        }
        if (request.getMensaje() != null) {
            topico.setMensaje(nuevoMensaje);
        }
        
        // Guardar los cambios
        Topico topicoActualizado = topicoRepository.save(topico);
        
        return new TopicoResponse(
                topicoActualizado.getId(),
                topicoActualizado.getTitulo(),
                topicoActualizado.getMensaje(),
                topicoActualizado.getFechaCreacion(),
                topicoActualizado.getStatus(),
                topicoActualizado.getAutor().getNombre()
        );
    }
    
    public void eliminarTopico(Long id) {
        // Obtener el tópico existente
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new TopicoException("El tópico con ID " + id + " no existe"));
        
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = usuarioRepository.findByCorreoElectronico(authentication.getName())
                .orElseThrow(() -> new TopicoException("Usuario no encontrado"));
        
        // Verificar que el usuario sea el autor del tópico
        if (!topico.getAutor().getId().equals(usuarioActual.getId())) {
            throw new TopicoException("No tienes permisos para eliminar este tópico");
        }
        
        // Eliminar el tópico
        topicoRepository.delete(topico);
    }
} 