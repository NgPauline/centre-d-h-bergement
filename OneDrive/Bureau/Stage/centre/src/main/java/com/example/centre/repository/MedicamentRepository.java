/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    List<Medicament> findByNomContainingIgnoreCase(String nom);
    List<Medicament> findByPrincipeActifContainingIgnoreCase(String principeActif);
    Optional<Medicament> findByCodeATC(String codeATC);
    
    @Query("SELECT m FROM Medicament m WHERE m.nom LIKE %:keyword% OR m.principeActif LIKE %:keyword%")
    List<Medicament> searchByKeyword(@Param("keyword") String keyword);
}
