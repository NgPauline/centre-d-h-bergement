/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    List<Qualification> findByPersonnelId(Long personnelId);
    
    @Query("SELECT q FROM Qualification q WHERE q.dateExpiration IS NOT NULL AND q.dateExpiration <= :date")
    List<Qualification> findQualificationsExpirees(@Param("date") LocalDate date);
    
    @Query("SELECT q FROM Qualification q WHERE q.dateExpiration IS NOT NULL AND q.dateExpiration BETWEEN :start AND :end")
    List<Qualification> findQualificationsExpirantSoon(@Param("start") LocalDate start, @Param("end") LocalDate end);
    
    @Query("SELECT q FROM Qualification q WHERE LOWER(q.intitule) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Qualification> findByIntituleContaining(@Param("keyword") String keyword);
    
    @Query("SELECT q FROM Qualification q WHERE q.organisme = :organisme")
    List<Qualification> findByOrganisme(@Param("organisme") String organisme);
}