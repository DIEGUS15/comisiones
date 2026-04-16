package com.example.comisiones.exception;

import com.example.comisiones.dto.ErrorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRecursoNoEncontrado_devuelve404ConMensaje() {
        RecursoNoEncontradoException ex = new RecursoNoEncontradoException("Comision no encontrada con id: abc");

        ResponseEntity<ErrorDTO> response = handler.handleRecursoNoEncontrado(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getCodigo());
        assertEquals("Comision no encontrada con id: abc", response.getBody().getMensaje());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleErrorGeneral_devuelve500ConMensajeGenerico() {
        Exception ex = new RuntimeException("algo falló");

        ResponseEntity<ErrorDTO> response = handler.handleErrorGeneral(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCodigo());
        assertEquals("Error interno del servidor", response.getBody().getMensaje());
        assertNotNull(response.getBody().getTimestamp());
    }
}
