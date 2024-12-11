/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.JustFly.repository;

import com.example.JustFly.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    
    // Trouver tous les paiements associés à une réservation
    List<Paiement> findByReservationId(Long reservationId);

    // Trouver un paiement par son ID de réservation
    Optional<Paiement> findByReservationIdAndMontant(Long reservationId, Double montant);

    // Supprimer un paiement par ID de réservation
    void deleteByReservationId(Long reservationId);
}
