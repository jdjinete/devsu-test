package com.devsu.clientes.infrastructure.rest.dto;

import lombok.Data;

@Data
public class ClienteResponse {
    private Long clienteId;
    private String nombre;
    private String identificacion;
    private String direccion;
    private String telefono;
    private String estadoCliente;
}
