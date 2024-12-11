/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.JustFly.repository;


import com.example.JustFly.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Trouver toutes les réservations d'un passager
    List<Reservation> findByPassagerId(Long passagerId);

    // Trouver toutes les réservations pour un vol spécifique
    List<Reservation> findByVolId(Long volId);

    // Trouver une réservation par passager et vol
    Optional<Reservation> findByPassagerIdAndVolId(Long passagerId, Long volId);

    // Supprimer les réservations d'un passager spécifique
    void deleteByPassagerId(Long passagerId);
}

