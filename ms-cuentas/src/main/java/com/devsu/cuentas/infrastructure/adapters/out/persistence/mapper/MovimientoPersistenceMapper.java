package com.devsu.cuentas.infrastructure.adapters.out.persistence.mapper;

import com.devsu.cuentas.domain.entity.Movimiento;
import com.devsu.cuentas.infrastructure.adapters.out.persistence.entity.MovimientoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CuentaPersistenceMapper.class})
public interface MovimientoPersistenceMapper {
    MovimientoEntity toEntity(Movimiento domain);
    
    @Mapping(target = "cuenta", expression = "java(null)") // Avoid recursion if needed, or map it carefully
    Movimiento toDomain(MovimientoEntity entity);

    default Movimiento toDomainWithCuenta(MovimientoEntity entity, CuentaPersistenceMapper cuentaMapper) {
        Movimiento domain = toDomain(entity);
        if (entity.getCuenta() != null) {
            domain.setCuenta(cuentaMapper.toDomain(entity.getCuenta()));
        }
        return domain;
    }
}
