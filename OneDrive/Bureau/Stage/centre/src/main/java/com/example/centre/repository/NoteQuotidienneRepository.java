/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.NoteQuotidienne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoteQuotidienneRepository extends JpaRepository<NoteQuotidienne, Long> {
    List<NoteQuotidienne> findByResidentId(Long residentId);
    List<NoteQuotidienne> findByAuteurId(Long auteurId);
    List<NoteQuotidienne> findByImportantTrue();
    
    @Query("SELECT n FROM NoteQuotidienne n WHERE n.dateHeure BETWEEN :start AND :end")
    List<NoteQuotidienne> findByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT n FROM NoteQuotidienne n WHERE n.resident.id = :residentId AND n.dateHeure >= :date")
    List<NoteQuotidienne> findRecentNotesByResident(@Param("residentId") Long residentId, @Param("date") LocalDateTime date);
    
    @Query("SELECT n FROM NoteQuotidienne n WHERE n.typeNote = :typeNote")
    List<NoteQuotidienne> findByTypeNote(@Param("typeNote") String typeNote);
    
    // AJOUT : Méthode pour compter les notes d'aujourd'hui
    @Query("SELECT COUNT(n) FROM NoteQuotidienne n WHERE DATE(n.dateHeure) = CURRENT_DATE")
    long countNotesToday();
}