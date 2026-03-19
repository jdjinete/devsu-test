package com.devsu.cuentas.infrastructure.adapters.out.persistence.mapper;

import com.devsu.cuentas.domain.entity.Cuenta;
import com.devsu.cuentas.infrastructure.adapters.out.persistence.entity.CuentaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CuentaPersistenceMapper {
    CuentaEntity toEntity(Cuenta domain);
    Cuenta toDomain(CuentaEntity entity);
}
