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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComisionServiceTest {

    @Mock
    private AgenteRepository agenteRepository;

    @Mock
    private LiquidacionRepository liquidacionRepository;

    @Mock
    private ComisionCalculadaRepository comisionCalculadaRepository;

    @InjectMocks
    private ComisionService comisionService;

    private Agente agente;
    private Liquidacion liquidacion;

    @BeforeEach
    void setUp() {
        agente = new Agente();
        agente.setId("1");
        agente.setNombre("Carlos Pérez");
        agente.setCodigoAgente("AGT001");
        agente.setIdOficina("OFI01");
        agente.setIva(0.16);
        agente.setImpuestoCedular(0.10);
        agente.setFacturacionAutomatica(true);

        liquidacion = new Liquidacion();
        liquidacion.setId("1");
        liquidacion.setIdLiquidacion("LIQ001");
        liquidacion.setPolizaId("POL-001");
        liquidacion.setIdAgente("AGT001");
        liquidacion.setNombreAgente("Carlos Pérez");
        liquidacion.setMonto(5000.0);
        liquidacion.setMoneda("MXN");
    }

    // -------------------------------------------------------
    // correrComisiones
    // -------------------------------------------------------

    @Test
    void correrComisiones_agenteyLiquidacionValidos_procesaCorrectamente() {
        when(agenteRepository.findAll()).thenReturn(List.of(agente));
        when(liquidacionRepository.findAll()).thenReturn(List.of(liquidacion));
        when(comisionCalculadaRepository.findByIdLiquidacion("LIQ001")).thenReturn(Optional.empty());
        when(comisionCalculadaRepository.saveAll(any())).thenReturn(List.of());

        CorridaResultadoDTO resultado = comisionService.correrComisiones();

        assertEquals(1, resultado.getProcesadas());
        assertEquals(1, resultado.getConComprobante());
        assertEquals(0, resultado.getSinAgente());
    }

    @Test
    void correrComisiones_agenteNoExiste_incrementaSinAgente() {
        liquidacion.setIdAgente("AGT999");

        when(agenteRepository.findAll()).thenReturn(List.of(agente));
        when(liquidacionRepository.findAll()).thenReturn(List.of(liquidacion));
        when(comisionCalculadaRepository.saveAll(any())).thenReturn(List.of());

        CorridaResultadoDTO resultado = comisionService.correrComisiones();

        assertEquals(0, resultado.getProcesadas());
        assertEquals(0, resultado.getConComprobante());
        assertEquals(1, resultado.getSinAgente());
    }

    @Test
    void correrComisiones_sinFacturacionAutomatica_noGeneraComprobante() {
        agente.setFacturacionAutomatica(false);

        when(agenteRepository.findAll()).thenReturn(List.of(agente));
        when(liquidacionRepository.findAll()).thenReturn(List.of(liquidacion));
        when(comisionCalculadaRepository.findByIdLiquidacion("LIQ001")).thenReturn(Optional.empty());
        when(comisionCalculadaRepository.saveAll(any())).thenReturn(List.of());

        CorridaResultadoDTO resultado = comisionService.correrComisiones();

        assertEquals(1, resultado.getProcesadas());
        assertEquals(0, resultado.getConComprobante());
    }

    @Test
    void correrComisiones_liquidacionDuplicada_sobreescribeRegistro() {
        ComisionCalculada existente = new ComisionCalculada();
        existente.setId("id-existente");

        when(agenteRepository.findAll()).thenReturn(List.of(agente));
        when(liquidacionRepository.findAll()).thenReturn(List.of(liquidacion));
        when(comisionCalculadaRepository.findByIdLiquidacion("LIQ001")).thenReturn(Optional.of(existente));
        when(comisionCalculadaRepository.saveAll(any())).thenReturn(List.of());

        CorridaResultadoDTO resultado = comisionService.correrComisiones();

        assertEquals(1, resultado.getProcesadas());
    }

    // -------------------------------------------------------
    // obtenerTodas
    // -------------------------------------------------------

    @Test
    void obtenerTodas_retornaListaDeDTO() {
        ComisionCalculada comision = buildComisionCalculada();
        when(comisionCalculadaRepository.findAll()).thenReturn(List.of(comision));

        List<ComisionCalculadaDTO> resultado = comisionService.obtenerTodas();

        assertEquals(1, resultado.size());
        assertEquals("LIQ001", resultado.get(0).getIdLiquidacion());
    }

    @Test
    void obtenerTodas_sinResultados_retornaListaVacia() {
        when(comisionCalculadaRepository.findAll()).thenReturn(List.of());

        List<ComisionCalculadaDTO> resultado = comisionService.obtenerTodas();

        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------
    // obtenerPorId
    // -------------------------------------------------------

    @Test
    void obtenerPorId_idExistente_retornaDTO() {
        ComisionCalculada comision = buildComisionCalculada();
        when(comisionCalculadaRepository.findById("1")).thenReturn(Optional.of(comision));

        ComisionCalculadaDTO resultado = comisionService.obtenerPorId("1");

        assertNotNull(resultado);
        assertEquals("LIQ001", resultado.getIdLiquidacion());
        assertEquals(5000.0, resultado.getMonto());
    }

    @Test
    void obtenerPorId_idNoExistente_lanzaExcepcion() {
        when(comisionCalculadaRepository.findById("abc")).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(
                RecursoNoEncontradoException.class,
                () -> comisionService.obtenerPorId("abc")
        );

        assertTrue(ex.getMessage().contains("abc"));
    }

    // -------------------------------------------------------
    // Helper
    // -------------------------------------------------------

    private ComisionCalculada buildComisionCalculada() {
        ComisionCalculada c = new ComisionCalculada();
        c.setId("1");
        c.setIdLiquidacion("LIQ001");
        c.setPolizaId("POL-001");
        c.setIdAgente("AGT001");
        c.setNombreAgente("Carlos Pérez");
        c.setNombreAgenteOficial("Carlos Pérez");
        c.setIdOficina("OFI01");
        c.setMonto(5000.0);
        c.setMoneda("MXN");
        c.setIvaRate(0.16);
        c.setImpuestoCedularRate(0.10);
        c.setMontoIva(800.0);
        c.setMontoImpuestoCedular(500.0);
        c.setTotal(5300.0);
        c.setComprobante("uuid-123");
        return c;
    }
}
