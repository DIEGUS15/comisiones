package com.example.comisiones.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "liquidaciones")
public class Liquidacion {

    @Id
    private String id;

    private String idLiquidacion;

    private String polizaId;

    private String idAgente;

    private String nombreAgente;

    private Double monto;

    private String moneda;
}
