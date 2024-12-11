/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.service;

import com.example.JustFly.entity.Passager;
import com.example.JustFly.repository.PassagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassagerService {

    private final PassagerRepository passagerRepository;

    @Autowired
    public PassagerService(PassagerRepository passagerRepository) {
        this.passagerRepository = passagerRepository;
    }

    public Passager addPassager(Passager passager) {
        return passagerRepository.save(passager);
    }

    public List<Passager> getAllPassagers() {
        return passagerRepository.findAll();
    }

    // Recherche par id
    public Optional<Passager> getPassagerById(Long id) {
        return passagerRepository.findById(id);
    }

    public Optional<Passager> getPassagerByEmail(String email) {
        return passagerRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return passagerRepository.existsByEmail(email);
    }

    public void deletePassagerByEmail(String email) {
        passagerRepository.deleteByEmail(email);
    }

    // Méthode pour supprimer par id (optionnelle, pour cohérence avec le contrôleur)
    public void deletePassager(Long id) {
        passagerRepository.deleteById(id);
    }
}
