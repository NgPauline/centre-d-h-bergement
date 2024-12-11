/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.controller;

import com.example.JustFly.entity.Aeroport;
import com.example.JustFly.service.AeroportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/aeroports")
public class AeroportController {

    private final AeroportService aeroportService;

    @Autowired
    public AeroportController(AeroportService aeroportService) {
        this.aeroportService = aeroportService;
    }

    @PostMapping
    public Aeroport saveAeroport(@RequestBody Aeroport aeroport) {
        return aeroportService.addAeroport(aeroport);
    }

    @GetMapping
    public List<Aeroport> getAllAeroports() {
        return aeroportService.getAllAeroports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aeroport> getAeroportById(@PathVariable Long id) {
        Optional<Aeroport> aeroport = aeroportService.getAeroportById(id);
        return aeroport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAeroport(@PathVariable Long id) {
        boolean isDeleted = aeroportService.deleteAeroport(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
