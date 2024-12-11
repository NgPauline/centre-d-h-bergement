/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.JustFly.repository;

import com.example.JustFly.entity.Aeroport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AeroportRepository extends JpaRepository<Aeroport, Long> {
    
    // Trouver un aéroport par son nom
    Optional<Aeroport> findByNomAeroport(String nomAeroport);

    // Trouver un aéroport par sa ville
    List<Aeroport> findByVille(String ville);

    // Vérifier si un aéroport existe par son nom
    boolean existsByNomAeroport(String nomAeroport);
}

