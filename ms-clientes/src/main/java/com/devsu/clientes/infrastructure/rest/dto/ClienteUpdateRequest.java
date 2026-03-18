package com.devsu.clientes.infrastructure.rest.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ClienteUpdateRequest {
    
    private String nombre;
    
    @Pattern(regexp = "[MFO]", message = "El género debe ser M, F o O")
    private String genero;
    
    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edad;
    
    private String direccion;
    
    private String telefono;
}
