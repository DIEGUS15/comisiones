package com.example.comisiones.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "comisiones_calculadas")
public class ComisionCalculada {

    @Id
    private String id;

    // Datos de la liquidación
    private String idLiquidacion;
    private String polizaId;
    private String idAgente;
    private String nombreAgente;
    private Double monto;
    private String moneda;

    // Datos del agente
    private String nombreAgenteOficial;
    private String idOficina;
    private Double ivaRate;
    private Double impuestoCedularRate;

    // Valores calculados
    private Double montoIva;
    private Double montoImpuestoCedular;
    private Double total;

    // UUID generado si el agente tiene facturación automática
    private String comprobante;

    private LocalDateTime fechaCalculo;
}
