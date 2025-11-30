/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Facture;
import com.example.centre.entity.StatutFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    List<Facture> findByResidentId(Long residentId);
    List<Facture> findByStatut(StatutFacture statut);
    Optional<Facture> findByNumero(String numero);
    
    @Query("SELECT f FROM Facture f WHERE f.dateEmission BETWEEN :start AND :end")
    List<Facture> findByDateEmissionBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT f FROM Facture f WHERE f.dateEcheance < :date AND f.statut != 'PAYEE'")
    List<Facture> findFacturesEnRetard(@Param("date") LocalDate date);
    
    @Query("SELECT SUM(f.montantTotal) FROM Facture f WHERE f.statut = 'PAYEE' AND f.dateEmission BETWEEN :start AND :end")
    Double findTotalFacturesPayeesPeriode(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT f FROM Facture f WHERE f.montantDejaPaye < f.montantTotal")
    List<Facture> findFacturesPartiellementPayees();
    
    @Query("SELECT COUNT(f) FROM Facture f WHERE MONTH(f.dateEmission) = MONTH(CURRENT_DATE) AND YEAR(f.dateEmission) = YEAR(CURRENT_DATE)")
    long countFacturesThisMonth();
    
    // AJOUT : Méthode pour compter les factures en retard
    @Query("SELECT COUNT(f) FROM Facture f WHERE f.statut = 'EN_RETARD'")
    long countFacturesEnRetard();
}