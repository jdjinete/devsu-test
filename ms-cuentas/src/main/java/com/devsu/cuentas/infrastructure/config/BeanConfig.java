package com.devsu.cuentas.infrastructure.config;

import com.devsu.cuentas.application.port.in.CuentaUseCase;
import com.devsu.cuentas.application.port.in.MovimientoUseCase;
import com.devsu.cuentas.application.port.in.ReporteUseCase;
import com.devsu.cuentas.application.service.CuentaService;
import com.devsu.cuentas.application.service.MovimientoService;
import com.devsu.cuentas.application.service.ReporteService;
import com.devsu.cuentas.domain.port.CuentaRepositoryPort;
import com.devsu.cuentas.domain.port.MovimientoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CuentaUseCase cuentaUseCase(CuentaRepositoryPort cuentaRepository) {
        return new CuentaService(cuentaRepository);
    }

    @Bean
    public MovimientoUseCase movimientoUseCase(MovimientoRepositoryPort movimientoRepository, CuentaRepositoryPort cuentaRepository) {
        return new MovimientoService(movimientoRepository, cuentaRepository);
    }

    @Bean
    public ReporteUseCase reporteUseCase(MovimientoRepositoryPort movimientoRepository) {
        return new ReporteService(movimientoRepository);
    }
}
