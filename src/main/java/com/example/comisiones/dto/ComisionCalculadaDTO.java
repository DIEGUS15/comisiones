package com.example.comisiones.dto;

import com.example.comisiones.model.ComisionCalculada;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComisionCalculadaDTO {

    private String id;
    private String idLiquidacion;
    private String polizaId;
    private String idAgente;
    private String nombreAgente;
    private String idOficina;
    private Double monto;
    private String moneda;
    private Double tasaIva;
    private Double tasaCedular;
    private Double montoIva;
    private Double montoCedular;
    private Double total;
    private String comprobante;
    private LocalDateTime fechaCalculo;

    public static ComisionCalculadaDTO from(ComisionCalculada modelo) {
        ComisionCalculadaDTO dto = new ComisionCalculadaDTO();
        dto.setId(modelo.getId());
        dto.setIdLiquidacion(modelo.getIdLiquidacion());
        dto.setPolizaId(modelo.getPolizaId());
        dto.setIdAgente(modelo.getIdAgente());
        dto.setNombreAgente(modelo.getNombreAgenteOficial());
        dto.setIdOficina(modelo.getIdOficina());
        dto.setMonto(modelo.getMonto());
        dto.setMoneda(modelo.getMoneda());
        dto.setTasaIva(modelo.getIvaRate());
        dto.setTasaCedular(modelo.getImpuestoCedularRate());
        dto.setMontoIva(modelo.getMontoIva());
        dto.setMontoCedular(modelo.getMontoImpuestoCedular());
        dto.setTotal(modelo.getTotal());
        dto.setComprobante(modelo.getComprobante());
        dto.setFechaCalculo(modelo.getFechaCalculo());
        return dto;
    }
}
