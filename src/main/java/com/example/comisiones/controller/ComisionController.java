package com.example.comisiones.controller;

import com.example.comisiones.dto.ComisionCalculadaDTO;
import com.example.comisiones.dto.CorridaResultadoDTO;
import com.example.comisiones.service.ComisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comisiones")
@RequiredArgsConstructor
public class ComisionController {

    private final ComisionService comisionService;

    @PostMapping("/correr")
    public ResponseEntity<CorridaResultadoDTO> correrComisiones() {
        CorridaResultadoDTO resultado = comisionService.correrComisiones();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping
    public ResponseEntity<List<ComisionCalculadaDTO>> obtenerTodas() {
        return ResponseEntity.ok(comisionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComisionCalculadaDTO> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(comisionService.obtenerPorId(id));
    }
}
