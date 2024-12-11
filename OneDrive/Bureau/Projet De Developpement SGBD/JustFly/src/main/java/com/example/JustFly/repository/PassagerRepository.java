/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.JustFly.repository;

import com.example.JustFly.entity.Passager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassagerRepository extends JpaRepository<Passager, Long> {
    
    // Trouver un passager par son email
    Optional<Passager> findByEmail(String email);

    // Trouver un passager par son numéro de passeport
    Optional<Passager> findByNumeroPasseport(String numeroPasseport);

    // Supprimer un passager par son email
    void deleteByEmail(String email);

    // Vérifier si un passager existe par son email
    boolean existsByEmail(String email);

    // Vérifier si un passager existe par son numéro de passeport
    boolean existsByNumeroPasseport(String numeroPasseport);
}
