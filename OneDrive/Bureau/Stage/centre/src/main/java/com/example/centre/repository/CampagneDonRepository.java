/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.CampagneDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CampagneDonRepository extends JpaRepository<CampagneDon, Long> {
    List<CampagneDon> findByActiveTrue();
    
    @Query("SELECT c FROM CampagneDon c WHERE c.dateDebut <= :date AND c.dateFin >= :date")
    List<CampagneDon> findCampagnesEnCours(@Param("date") LocalDate date);
    
    @Query("SELECT c FROM CampagneDon c WHERE c.dateFin < :date")
    List<CampagneDon> findCampagnesTerminees(@Param("date") LocalDate date);
    
    @Query("SELECT c FROM CampagneDon c WHERE c.dateDebut > :date")
    List<CampagneDon> findCampagnesAVenir(@Param("date") LocalDate date);
    
    @Query("SELECT c FROM CampagneDon c WHERE c.montantCollecte >= c.objectifMontant")
    List<CampagneDon> findCampagnesObjectifAtteint();
    
    @Query("SELECT c FROM CampagneDon c WHERE LOWER(c.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CampagneDon> searchByKeyword(@Param("keyword") String keyword);
}
