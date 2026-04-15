package com.example.comisiones.dto;

import lombok.Data;

@Data
public class CorridaResultadoDTO {

    private int procesadas;
    private int conComprobante;
    private int sinAgente;

    public CorridaResultadoDTO(int procesadas, int conComprobante, int sinAgente) {
        this.procesadas = procesadas;
        this.conComprobante = conComprobante;
        this.sinAgente = sinAgente;
    }
}
