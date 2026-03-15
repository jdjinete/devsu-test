package com.devsu.clientes.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "persona_id")
@Getter
@Setter
@NoArgsConstructor
public class ClienteEntity extends PersonaEntity {

    @Column(unique = true, nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false, length = 20)
    private String estadoCliente;

    private Long ultimoAcceso;
}
