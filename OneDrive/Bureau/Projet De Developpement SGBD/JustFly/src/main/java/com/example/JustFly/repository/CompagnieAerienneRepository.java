/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.JustFly.repository;

import com.example.JustFly.entity.CompagnieAerienne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompagnieAerienneRepository extends JpaRepository<CompagnieAerienne, Long> {
    
    // Trouver une compagnie par son nom
    Optional<CompagnieAerienne> findByNomCompagnie(String nomCompagnie);

    // Vérifier si une compagnie existe par son nom
    boolean existsByNomCompagnie(String nomCompagnie);
}

