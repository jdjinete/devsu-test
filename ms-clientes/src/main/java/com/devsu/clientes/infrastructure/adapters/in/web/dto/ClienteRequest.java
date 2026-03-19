package com.devsu.clientes.infrastructure.adapters.in.web.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Cuerpo de la petición para la creación de un nuevo cliente")
public class ClienteRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo de la persona", example = "Juan Perez")
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    @Pattern(regexp = "[MFO]", message = "El género debe ser M, F o O")
    @Schema(description = "Género de la persona", example = "M")
    private String genero;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Schema(description = "Edad en años", example = "30")
    private Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    @Schema(description = "Identificación única", example = "1720000000")
    private String identificacion;

    @NotBlank(message = "La dirección es obligatoria")
    @Schema(description = "Dirección de residencia", example = "Av. Principal y Calle Secundaria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Schema(description = "Número telefónico de contacto", example = "0987654321")
    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Schema(description = "Contraseña de acceso del cliente", example = "12345678")
    private String contrasena;
}
