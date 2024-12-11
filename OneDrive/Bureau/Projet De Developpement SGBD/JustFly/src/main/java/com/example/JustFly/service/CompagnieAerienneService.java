/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.service;

import com.example.JustFly.entity.CompagnieAerienne;
import com.example.JustFly.repository.CompagnieAerienneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompagnieAerienneService {

    private final CompagnieAerienneRepository compagnieAerienneRepository;

    @Autowired
    public CompagnieAerienneService(CompagnieAerienneRepository compagnieAerienneRepository) {
        this.compagnieAerienneRepository = compagnieAerienneRepository;
    }

    // Récupérer toutes les compagnies aériennes
    public List<CompagnieAerienne> getAllCompagnies() {
        return compagnieAerienneRepository.findAll();
    }

    // Récupérer une compagnie aérienne par son identifiant
    public Optional<CompagnieAerienne> getCompagnieById(Long id) {
        return compagnieAerienneRepository.findById(id);
    }

    // Ajouter une nouvelle compagnie aérienne
    public CompagnieAerienne addCompagnieAerienne(CompagnieAerienne compagnieAerienne) {
        return compagnieAerienneRepository.save(compagnieAerienne);
    }

    // Mettre à jour une compagnie aérienne existante
    public CompagnieAerienne updateCompagnieAerienne(Long id, CompagnieAerienne compagnieAerienne) {
        if (compagnieAerienneRepository.existsById(id)) {
            compagnieAerienne.setId(id);
            return compagnieAerienneRepository.save(compagnieAerienne);
        }
        return null;
    }

    // Supprimer une compagnie aérienne
    public boolean deleteCompagnieAerienne(Long id) {
        if (compagnieAerienneRepository.existsById(id)) {
            compagnieAerienneRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
