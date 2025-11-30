/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.centre.repository;

import com.example.centre.entity.Utilisateur;
import com.example.centre.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByUsername(String username);
    Boolean existsByUsername(String username);
    List<Utilisateur> findByRole(Role role);
    List<Utilisateur> findByActifTrue();
    
    @Query("SELECT u FROM Utilisateur u WHERE u.personnel.id = :personnelId")
    Optional<Utilisateur> findByPersonnelId(@Param("personnelId") Long personnelId);
    
    @Query("SELECT COUNT(u) FROM Utilisateur u WHERE u.role = :role")
    Long countByRole(@Param("role") Role role);
}
