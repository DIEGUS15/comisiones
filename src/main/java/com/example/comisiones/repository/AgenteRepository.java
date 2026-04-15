package com.example.comisiones.repository;

import com.example.comisiones.model.Agente;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AgenteRepository extends MongoRepository<Agente, String> {

    Optional<Agente> findByCodigoAgente(String codigoAgente);
}
