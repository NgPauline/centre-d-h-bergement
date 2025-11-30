/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.ContactUrgence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactUrgenceRepository extends JpaRepository<ContactUrgence, Long> {
    List<ContactUrgence> findByResidentId(Long residentId);
    List<ContactUrgence> findByContactPrincipalTrue();
    
    @Query("SELECT c FROM ContactUrgence c WHERE c.resident.id = :residentId AND c.contactPrincipal = true")
    Optional<ContactUrgence> findContactPrincipalByResidentId(@Param("residentId") Long residentId);
    
    @Query("SELECT c FROM ContactUrgence c WHERE c.telephone LIKE %:telephone%")
    List<ContactUrgence> findByTelephoneContaining(@Param("telephone") String telephone);
}
