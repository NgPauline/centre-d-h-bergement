/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.service;

import com.example.JustFly.entity.Vol;
import com.example.JustFly.repository.VolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolService {

    private final VolRepository volRepository;

    @Autowired
    public VolService(VolRepository volRepository) {
        this.volRepository = volRepository;
    }

    // Récupérer tous les vols
    public List<Vol> getAllVols() {
        return volRepository.findAll();
    }

    // Récupérer un vol par son identifiant
    public Optional<Vol> getVolById(Long id) {
        return volRepository.findById(id);
    }

    // Ajouter un nouveau vol
    public Vol addVol(Vol vol) {
        return volRepository.save(vol);
    }

    // Mettre à jour un vol existant
    public Vol updateVol(Long id, Vol vol) {
        if (volRepository.existsById(id)) {
            vol.setId(id);
            return volRepository.save(vol);
        }
        return null;
    }

    // Supprimer un vol
    public boolean deleteVol(Long id) {
        if (volRepository.existsById(id)) {
            volRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
