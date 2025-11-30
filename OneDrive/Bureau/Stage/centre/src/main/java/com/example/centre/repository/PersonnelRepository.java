/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Personnel;
import com.example.centre.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
    Optional<Personnel> findByMatricule(String matricule);
    List<Personnel> findByNomContainingIgnoreCase(String nom);
    List<Personnel> findByPrenomContainingIgnoreCase(String prenom);
    
    // ✅ AJOUTEZ CETTE MÉTHODE POUR LA RECHERCHE PAR MATRICULE
    List<Personnel> findByMatriculeContainingIgnoreCase(String matricule);
    
    Optional<Personnel> findByNomAndPrenom(String nom, String prenom);
    
    Boolean existsByMatricule(String matricule);
    Boolean existsByEmail(String email);
    
    @Query("SELECT p FROM Personnel p WHERE p.utilisateur.role = :role")
    List<Personnel> findByRole(@Param("role") Role role);
    
    @Query("SELECT p FROM Personnel p WHERE p.dateEmbauche >= :date")
    List<Personnel> findEmbauchesApres(@Param("date") java.time.LocalDate date);
}