package com.example.comisiones.repository;

import com.example.comisiones.model.ComisionCalculada;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ComisionCalculadaRepository extends MongoRepository<ComisionCalculada, String> {

    Optional<ComisionCalculada> findByIdLiquidacion(String idLiquidacion);
}
