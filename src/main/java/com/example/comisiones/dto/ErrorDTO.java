package com.example.comisiones.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDTO {

    private int codigo;
    private String mensaje;
    private LocalDateTime timestamp;

    public ErrorDTO(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now();
    }
}
