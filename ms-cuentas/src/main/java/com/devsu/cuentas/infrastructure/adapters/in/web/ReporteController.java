package com.devsu.cuentas.infrastructure.adapters.in.web;

import com.devsu.cuentas.application.dto.ReporteDTO;
import com.devsu.cuentas.application.port.in.ReporteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteUseCase reporteUseCase;

    @GetMapping
    public List<ReporteDTO> generar(
            @RequestParam("cliente") Long clienteId,
            @RequestParam("fecha") String rangoFechas) {
        
        // El formato suele ser "fechaInicio,fechaFin" según el requerimiento.
        // Simularemos el parseo de un rango simple o usaremos parámetros separados si es más limpio.
        // Pero el requerimiento dice: /reportes?fecha={rango_fechas}&cliente={cliente_id}
        
        String[] partes = rangoFechas.split(",");
        LocalDateTime inicio = java.time.LocalDate.parse(partes[0].trim()).atStartOfDay();
        LocalDateTime fin = java.time.LocalDate.parse(partes[1].trim()).atTime(java.time.LocalTime.MAX);
        
        return reporteUseCase.generarReporte(clienteId, inicio, fin);
    }
}
