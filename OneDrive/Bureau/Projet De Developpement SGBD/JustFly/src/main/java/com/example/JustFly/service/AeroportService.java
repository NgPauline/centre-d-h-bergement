/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.service;

import com.example.JustFly.entity.Aeroport;
import com.example.JustFly.repository.AeroportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AeroportService {

    private final AeroportRepository aeroportRepository;

    @Autowired
    public AeroportService(AeroportRepository aeroportRepository) {
        this.aeroportRepository = aeroportRepository;
    }

    // Méthode pour récupérer tous les aéroports
    public List<Aeroport> getAllAeroports() {
        return aeroportRepository.findAll();
    }

    // Méthode pour récupérer un aéroport par son identifiant
    public Optional<Aeroport> getAeroportById(Long id) {
        return aeroportRepository.findById(id);
    }

    // Méthode pour ajouter un nouvel aéroport
    public Aeroport addAeroport(Aeroport aeroport) {
        return aeroportRepository.save(aeroport);
    }

    // Méthode pour supprimer un aéroport par son identifiant
    public boolean deleteAeroport(Long id) {
        if (aeroportRepository.existsById(id)) {
            aeroportRepository.deleteById(id);
            return true;
        }
        return false; // Si l'aéroport n'existe pas
    }
}
