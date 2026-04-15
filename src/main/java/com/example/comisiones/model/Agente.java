package com.example.comisiones.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "agentes")
public class Agente {

    @Id
    private String id;

    private String nombre;

    private String codigoAgente;

    private String idOficina;

    private Double iva;

    private Double impuestoCedular;

    private Boolean facturacionAutomatica;
}
