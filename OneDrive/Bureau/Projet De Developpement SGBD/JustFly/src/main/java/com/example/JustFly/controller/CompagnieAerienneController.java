/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.JustFly.controller;

import com.example.JustFly.entity.CompagnieAerienne;
import com.example.JustFly.service.CompagnieAerienneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/compagnies")
public class CompagnieAerienneController {

    private final CompagnieAerienneService compagnieAerienneService;

    // Constructeur injectant le service
    @Autowired
    public CompagnieAerienneController(CompagnieAerienneService compagnieAerienneService) {
        this.compagnieAerienneService = compagnieAerienneService;
    }

    // Créer une nouvelle compagnie aérienne
    @PostMapping
    public CompagnieAerienne saveCompagnieAerienne(@RequestBody CompagnieAerienne compagnieAerienne) {
        return compagnieAerienneService.addCompagnieAerienne(compagnieAerienne);
    }

    // Récupérer toutes les compagnies aériennes
    @GetMapping
    public List<CompagnieAerienne> getAllCompagnies() {
        return compagnieAerienneService.getAllCompagnies();
    }

    // Récupérer une compagnie aérienne par son ID
    @GetMapping("/{id}")
    public Optional<CompagnieAerienne> getCompagnieById(@PathVariable Long id) {
        return compagnieAerienneService.getCompagnieById(id);
    }

    // Supprimer une compagnie aérienne
    @DeleteMapping("/{id}")
    public void deleteCompagnieAerienne(@PathVariable Long id) {
        compagnieAerienneService.deleteCompagnieAerienne(id);
    }
}
