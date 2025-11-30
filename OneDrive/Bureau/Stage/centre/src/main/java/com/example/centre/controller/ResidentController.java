/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Resident;
import com.example.centre.entity.StatutResident;
import com.example.centre.entity.TypeHandicap;
import com.example.centre.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/residents")
public class ResidentController {

    @Autowired
    private ResidentRepository residentRepository;

    // ✅ AJOUTEZ CE MAPPING POUR LA RACINE /residents
    @GetMapping
    public String index() {
        return "redirect:/residents/liste";
    }

    // LISTE AVEC RECHERCHE
    @GetMapping("/liste")
    public String listerResidents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String searchType,
            Model model) {
        
        List<Resident> residents;
        
        if (search != null && !search.trim().isEmpty()) {
            // Recherche activée
            if ("prenom".equals(searchType)) {
                residents = residentRepository.findByPrenomContainingIgnoreCase(search);
            } else if ("numeroNational".equals(searchType)) {
                residents = residentRepository.findByNumeroNationalContainingIgnoreCase(search);
            } else {
                // Recherche par nom par défaut
                residents = residentRepository.findByNomContainingIgnoreCase(search);
            }
            model.addAttribute("search", search);
            model.addAttribute("searchType", searchType);
        } else {
            // Aucune recherche, afficher tous les résidents
            residents = residentRepository.findAll();
        }
        
        model.addAttribute("residents", residents);
        model.addAttribute("pageTitle", "Liste des Résidents");
        return "residents/liste";
    }

    // NOUVEAU FORM
    @GetMapping("/nouveau")
    public String nouveauResident(Model model) {
        model.addAttribute("resident", new Resident());
        model.addAttribute("typesHandicap", TypeHandicap.values());
        model.addAttribute("statuts", StatutResident.values());
        model.addAttribute("pageTitle", "Nouveau Résident");
        return "residents/formulaire";
    }

    // CREATE
    @PostMapping("/nouveau")
    public String creerResident(@ModelAttribute Resident resident, RedirectAttributes redirectAttributes) {
        try {
            if (resident.getStatut() == null)
                resident.setStatut(StatutResident.ACTIF);

            if (resident.getDateAdmission() == null)
                resident.setDateAdmission(LocalDate.now());

            residentRepository.save(resident);
            redirectAttributes.addFlashAttribute("success", "Résident créé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création du résident");
        }

        return "redirect:/residents/liste";
    }

    // EDIT FORM
    @GetMapping("/{id}/modifier")
    public String afficherFormulaireModification(@PathVariable Long id, Model model) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Résident introuvable"));

        model.addAttribute("resident", resident);
        model.addAttribute("typesHandicap", TypeHandicap.values());
        model.addAttribute("statuts", StatutResident.values());
        model.addAttribute("pageTitle", "Modifier Résident");
        
        return "residents/formulaire";
    }

    // UPDATE
    @PostMapping("/{id}/modifier")
    public String modifierResident(@PathVariable Long id, @ModelAttribute Resident resident, RedirectAttributes redirectAttributes) {
        try {
            resident.setId(id);
            residentRepository.save(resident);
            redirectAttributes.addFlashAttribute("success", "Résident modifié avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification du résident");
        }

        return "redirect:/residents/liste";
    }

    // DELETE
    @PostMapping("/{id}/supprimer")
    public String supprimerResident(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            residentRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Résident supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du résident");
        }
        return "redirect:/residents/liste";
    }

    // ✅ AJOUTEZ UN MAPPING POUR LES DÉTAILS
    @GetMapping("/{id}")
    public String detailsResident(@PathVariable Long id, Model model) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Résident introuvable"));
        
        model.addAttribute("resident", resident);
        model.addAttribute("pageTitle", "Détails Résident");
        return "residents/details";
    }
}