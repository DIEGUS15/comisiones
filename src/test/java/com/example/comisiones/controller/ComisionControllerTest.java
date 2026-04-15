package com.example.comisiones.controller;

import com.example.comisiones.dto.ComisionCalculadaDTO;
import com.example.comisiones.dto.CorridaResultadoDTO;
import com.example.comisiones.exception.RecursoNoEncontradoException;
import com.example.comisiones.service.ComisionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComisionControllerTest {

    @Mock
    private ComisionService comisionService;

    @InjectMocks
    private ComisionController comisionController;

    @Test
    void correrComisiones_responde200ConResumen() {
        CorridaResultadoDTO resultado = new CorridaResultadoDTO(3, 2, 1);
        when(comisionService.correrComisiones()).thenReturn(resultado);

        ResponseEntity<CorridaResultadoDTO> response = comisionController.correrComisiones();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getProcesadas());
        assertEquals(2, response.getBody().getConComprobante());
        assertEquals(1, response.getBody().getSinAgente());
    }

    @Test
    void obtenerTodas_responde200ConLista() {
        ComisionCalculadaDTO dto = buildDTO();
        when(comisionService.obtenerTodas()).thenReturn(List.of(dto));

        ResponseEntity<List<ComisionCalculadaDTO>> response = comisionController.obtenerTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("LIQ001", response.getBody().get(0).getIdLiquidacion());
    }

    @Test
    void obtenerTodas_sinResultados_responde200ConListaVacia() {
        when(comisionService.obtenerTodas()).thenReturn(List.of());

        ResponseEntity<List<ComisionCalculadaDTO>> response = comisionController.obtenerTodas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void obtenerPorId_idExistente_responde200() {
        ComisionCalculadaDTO dto = buildDTO();
        when(comisionService.obtenerPorId("1")).thenReturn(dto);

        ResponseEntity<ComisionCalculadaDTO> response = comisionController.obtenerPorId("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("LIQ001", response.getBody().getIdLiquidacion());
        assertEquals(5000.0, response.getBody().getMonto());
        assertEquals(5300.0, response.getBody().getTotal());
    }

    @Test
    void obtenerPorId_idNoExistente_lanzaExcepcion() {
        when(comisionService.obtenerPorId("abc"))
                .thenThrow(new RecursoNoEncontradoException("Comision no encontrada con id: abc"));

        RecursoNoEncontradoException ex = assertThrows(
                RecursoNoEncontradoException.class,
                () -> comisionController.obtenerPorId("abc")
        );

        assertTrue(ex.getMessage().contains("abc"));
    }

    // -------------------------------------------------------
    // Helper
    // -------------------------------------------------------

    private ComisionCalculadaDTO buildDTO() {
        ComisionCalculadaDTO dto = new ComisionCalculadaDTO();
        dto.setId("1");
        dto.setIdLiquidacion("LIQ001");
        dto.setPolizaId("POL-001");
        dto.setIdAgente("AGT001");
        dto.setNombreAgente("Carlos Pérez");
        dto.setIdOficina("OFI01");
        dto.setMonto(5000.0);
        dto.setMoneda("MXN");
        dto.setTasaIva(0.16);
        dto.setTasaCedular(0.10);
        dto.setMontoIva(800.0);
        dto.setMontoCedular(500.0);
        dto.setTotal(5300.0);
        dto.setComprobante("uuid-123");
        return dto;
    }
}
