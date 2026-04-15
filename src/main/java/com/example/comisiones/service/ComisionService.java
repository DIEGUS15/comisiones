package com.example.comisiones.service;

import com.example.comisiones.dto.ComisionCalculadaDTO;
import com.example.comisiones.dto.CorridaResultadoDTO;
import com.example.comisiones.exception.RecursoNoEncontradoException;
import com.example.comisiones.model.Agente;
import com.example.comisiones.model.ComisionCalculada;
import com.example.comisiones.model.Liquidacion;
import com.example.comisiones.repository.AgenteRepository;
import com.example.comisiones.repository.ComisionCalculadaRepository;
import com.example.comisiones.repository.LiquidacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComisionService {

    private final AgenteRepository agenteRepository;
    private final LiquidacionRepository liquidacionRepository;
    private final ComisionCalculadaRepository comisionCalculadaRepository;

    public CorridaResultadoDTO correrComisiones() {
        Map<String, Agente> agentesMap = agenteRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Agente::getCodigoAgente, agente -> agente));

        List<Liquidacion> liquidaciones = liquidacionRepository.findAll();

        List<ComisionCalculada> resultados = new ArrayList<>();
        int sinAgente = 0;
        int conComprobante = 0;

        for (Liquidacion liquidacion : liquidaciones) {
            Agente agente = agentesMap.get(liquidacion.getIdAgente());

            if (agente == null) {
                log.warn("Agente no encontrado para idAgente={} en liquidacion={}",
                        liquidacion.getIdAgente(), liquidacion.getIdLiquidacion());
                sinAgente++;
                continue;
            }

            double montoIva = liquidacion.getMonto() * agente.getIva();
            double montoImpuestoCedular = liquidacion.getMonto() * agente.getImpuestoCedular();
            double total = liquidacion.getMonto() + montoIva - montoImpuestoCedular;

            ComisionCalculada comision = new ComisionCalculada();

            // Si ya existe un registro para esta liquidación, reusar su id para sobreescribirlo
            comisionCalculadaRepository.findByIdLiquidacion(liquidacion.getIdLiquidacion())
                    .ifPresent(existente -> comision.setId(existente.getId()));

            // Datos de la liquidación
            comision.setIdLiquidacion(liquidacion.getIdLiquidacion());
            comision.setPolizaId(liquidacion.getPolizaId());
            comision.setIdAgente(liquidacion.getIdAgente());
            comision.setNombreAgente(liquidacion.getNombreAgente());
            comision.setMonto(liquidacion.getMonto());
            comision.setMoneda(liquidacion.getMoneda());

            // Datos del agente
            comision.setNombreAgenteOficial(agente.getNombre());
            comision.setIdOficina(agente.getIdOficina());
            comision.setIvaRate(agente.getIva());
            comision.setImpuestoCedularRate(agente.getImpuestoCedular());

            // Valores calculados
            comision.setMontoIva(montoIva);
            comision.setMontoImpuestoCedular(montoImpuestoCedular);
            comision.setTotal(total);

            // Comprobante si tiene facturación automática
            if (Boolean.TRUE.equals(agente.getFacturacionAutomatica())) {
                comision.setComprobante(UUID.randomUUID().toString());
                conComprobante++;
            }

            comision.setFechaCalculo(LocalDateTime.now());
            resultados.add(comision);
        }

        comisionCalculadaRepository.saveAll(resultados);

        log.info("Corrida completada: {} procesadas, {} con comprobante, {} sin agente",
                resultados.size(), conComprobante, sinAgente);

        return new CorridaResultadoDTO(resultados.size(), conComprobante, sinAgente);
    }

    public List<ComisionCalculadaDTO> obtenerTodas() {
        return comisionCalculadaRepository.findAll()
                .stream()
                .map(ComisionCalculadaDTO::from)
                .toList();
    }

    public ComisionCalculadaDTO obtenerPorId(String id) {
        ComisionCalculada comision = comisionCalculadaRepository.findById(id).orElse(null);
        if (comision == null) {
            throw new RecursoNoEncontradoException("Comision no encontrada con id: " + id);
        }
        return ComisionCalculadaDTO.from(comision);
    }
}
