package com.example.comisiones.repository;

import com.example.comisiones.model.Liquidacion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LiquidacionRepository extends MongoRepository<Liquidacion, String> {
}
