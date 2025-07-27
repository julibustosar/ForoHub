package com.juliana.ForoHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicoUpdateRequest {
    
    private String titulo;
    
    private String mensaje;
} 