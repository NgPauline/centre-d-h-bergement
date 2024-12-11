/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.JustFly.repository;


import com.example.JustFly.entity.Vol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface VolRepository extends JpaRepository<Vol, Long> {
    
    // Trouver tous les vols par un aéroport de départ
    List<Vol> findByAeroportDepart(String aeroportDepart);

    // Trouver tous les vols par un aéroport d'arrivée
    List<Vol> findByAeroportArrivee(String aeroportArrivee);

    // Trouver un vol par son numéro de vol
    Optional<Vol> findByNumVol(String numVol);

    // Trouver tous les vols d'une compagnie spécifique
    List<Vol> findByCompagnieId(Long compagnieId);
}

