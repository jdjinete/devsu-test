package com.devsu.cuentas.application.port.in;

import com.devsu.cuentas.application.dto.ReporteDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface ReporteUseCase {
    List<ReporteDTO> generarReporte(Long clienteId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
