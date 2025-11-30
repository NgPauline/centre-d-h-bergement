/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.ConsultationMedicale;
import com.example.centre.entity.TypeConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationMedicaleRepository extends JpaRepository<ConsultationMedicale, Long> {
    List<ConsultationMedicale> findByResidentId(Long residentId);
    List<ConsultationMedicale> findByPersonnelId(Long personnelId);
    List<ConsultationMedicale> findByType(TypeConsultation type);
    List<ConsultationMedicale> findByUrgenceTrue();
    
    @Query("SELECT c FROM ConsultationMedicale c WHERE c.dateHeure BETWEEN :start AND :end")
    List<ConsultationMedicale> findByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT c FROM ConsultationMedicale c WHERE c.dateHeure >= :date")
    List<ConsultationMedicale> findFutureConsultations(@Param("date") LocalDateTime date);
    
    @Query("SELECT c FROM ConsultationMedicale c WHERE c.medecinExterne LIKE %:medecin%")
    List<ConsultationMedicale> findByMedecinContaining(@Param("medecin") String medecin);
    
    @Query("SELECT c FROM ConsultationMedicale c WHERE c.specialite = :specialite")
    List<ConsultationMedicale> findBySpecialite(@Param("specialite") String specialite);
}