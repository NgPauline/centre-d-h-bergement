/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Don;
import com.example.centre.entity.TypeDon;
import com.example.centre.entity.TypeDonateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonRepository extends JpaRepository<Don, Long> {
    List<Don> findByTypeDonateur(TypeDonateur typeDonateur);
    List<Don> findByTypeDon(TypeDon typeDon);
    List<Don> findByRecuFiscalDemandeTrue();
    List<Don> findByRecuFiscalEnvoyeFalse();
    
    @Query("SELECT d FROM Don d WHERE d.dateDon BETWEEN :start AND :end")
    List<Don> findByDateDonBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT COUNT(d) FROM Don d WHERE MONTH(d.dateDon) = MONTH(CURRENT_DATE) AND YEAR(d.dateDon) = YEAR(CURRENT_DATE)")
    long countDonsThisMonth();
    
    @Query("SELECT SUM(d.montant) FROM Don d WHERE d.dateDon BETWEEN :start AND :end")
    Double findTotalDonsPeriode(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT d FROM Don d WHERE d.nomDonateur LIKE %:nom% OR d.organisationDonateur LIKE %:nom%")
    List<Don> findByDonateurContaining(@Param("nom") String nom);
    
    @Query("SELECT d FROM Don d WHERE d.campagne.id = :campagneId")
    List<Don> findByCampagneId(@Param("campagneId") Long campagneId);
}
