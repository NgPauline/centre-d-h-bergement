/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Allergie;
import com.example.centre.entity.DossierMedical;
import com.example.centre.entity.NiveauGravite;
import com.example.centre.repository.AllergieRepository;
import com.example.centre.repository.DossierMedicalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/infirmier/allergies")
public class AllergieController {

    private final AllergieRepository allergieRepository;
    private final DossierMedicalRepository dossierMedicalRepository;

    public AllergieController(AllergieRepository allergieRepository, 
                            DossierMedicalRepository dossierMedicalRepository) {
        this.allergieRepository = allergieRepository;
        this.dossierMedicalRepository = dossierMedicalRepository;
    }

    /**
     * Affiche le formulaire d'ajout d'une allergie
     */
    @GetMapping("/ajouter/{dossierId}")
    public String ajouterAllergieForm(@PathVariable Long dossierId, Model model, RedirectAttributes redirectAttributes) {
        Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
        
        if (dossierOpt.isPresent()) {
            model.addAttribute("dossier", dossierOpt.get());
            model.addAttribute("allergie", new Allergie());
            model.addAttribute("niveauxGravite", NiveauGravite.values());
            model.addAttribute("pageTitle", "Ajouter une allergie");
            return "infirmier/allergie-formulaire";
        } else {
            redirectAttributes.addFlashAttribute("error", "Dossier médical non trouvé");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Traite l'ajout d'une nouvelle allergie
     */
    @PostMapping("/ajouter/{dossierId}")
    public String ajouterAllergie(@PathVariable Long dossierId,
                                @ModelAttribute Allergie allergie,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
            
            if (dossierOpt.isPresent()) {
                DossierMedical dossier = dossierOpt.get();
                
                // Vérifier si l'allergie existe déjà
                if (allergieRepository.existsByDossierMedicalIdAndSubstanceIgnoreCase(dossierId, allergie.getSubstance())) {
                    redirectAttributes.addFlashAttribute("error", "Cette allergie existe déjà pour ce résident");
                    return "redirect:/infirmier/allergies/ajouter/" + dossierId;
                }
                
                allergie.setDossierMedical(dossier);
                allergieRepository.save(allergie);
                
                // Mettre à jour la date de modification du dossier
                dossier.mettreAJour();
                dossierMedicalRepository.save(dossier);
                
                redirectAttributes.addFlashAttribute("success", "Allergie ajoutée avec succès");
                return "redirect:/infirmier/dossiers-medicaux/" + dossier.getResident().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Dossier médical non trouvé");
                return "redirect:/infirmier/dossiers-medicaux";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de l'allergie: " + e.getMessage());
            return "redirect:/infirmier/allergies/ajouter/" + dossierId;
        }
    }

    /**
     * Affiche les détails d'une allergie
     */
    @GetMapping("/{id}")
    public String detailsAllergie(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Allergie> allergieOpt = allergieRepository.findById(id);
        
        if (allergieOpt.isPresent()) {
            Allergie allergie = allergieOpt.get();
            model.addAttribute("allergie", allergie);
            model.addAttribute("pageTitle", "Détails allergie - " + allergie.getSubstance());
            return "infirmier/allergie-details";
        } else {
            redirectAttributes.addFlashAttribute("error", "Allergie non trouvée");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Affiche le formulaire de modification d'une allergie
     */
    @GetMapping("/{id}/modifier")
    public String modifierAllergieForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Allergie> allergieOpt = allergieRepository.findById(id);
        
        if (allergieOpt.isPresent()) {
            Allergie allergie = allergieOpt.get();
            model.addAttribute("allergie", allergie);
            model.addAttribute("niveauxGravite", NiveauGravite.values());
            model.addAttribute("pageTitle", "Modifier allergie - " + allergie.getSubstance());
            return "infirmier/allergie-formulaire";
        } else {
            redirectAttributes.addFlashAttribute("error", "Allergie non trouvée");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Traite la modification d'une allergie
     */
    @PostMapping("/{id}/modifier")
    public String modifierAllergie(@PathVariable Long id,
                                 @ModelAttribute Allergie allergieDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            Optional<Allergie> allergieOpt = allergieRepository.findById(id);
            
            if (allergieOpt.isPresent()) {
                Allergie allergie = allergieOpt.get();
                
                // Mise à jour des champs
                allergie.setSubstance(allergieDetails.getSubstance());
                allergie.setTypeReaction(allergieDetails.getTypeReaction());
                allergie.setGravite(allergieDetails.getGravite());
                allergie.setTraitement(allergieDetails.getTraitement());
                allergie.setObservations(allergieDetails.getObservations());
                
                allergieRepository.save(allergie);
                
                // Mettre à jour la date de modification du dossier
                DossierMedical dossier = allergie.getDossierMedical();
                dossier.mettreAJour();
                dossierMedicalRepository.save(dossier);
                
                redirectAttributes.addFlashAttribute("success", "Allergie modifiée avec succès");
                return "redirect:/infirmier/dossiers-medicaux/" + dossier.getResident().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Allergie non trouvée");
                return "redirect:/infirmier/dossiers-medicaux";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
            return "redirect:/infirmier/allergies/" + id + "/modifier";
        }
    }

    /**
     * Supprime une allergie
     */
    @PostMapping("/{id}/supprimer")
    public String supprimerAllergie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Allergie> allergieOpt = allergieRepository.findById(id);
            
            if (allergieOpt.isPresent()) {
                Allergie allergie = allergieOpt.get();
                DossierMedical dossier = allergie.getDossierMedical();
                Long residentId = dossier.getResident().getId();
                
                allergieRepository.delete(allergie);
                
                // Mettre à jour la date de modification du dossier
                dossier.mettreAJour();
                dossierMedicalRepository.save(dossier);
                
                redirectAttributes.addFlashAttribute("success", "Allergie supprimée avec succès");
                return "redirect:/infirmier/dossiers-medicaux/" + residentId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Allergie non trouvée");
                return "redirect:/infirmier/dossiers-medicaux";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Liste toutes les allergies d'un dossier médical
     */
    @GetMapping("/dossier/{dossierId}")
    public String listeAllergiesDossier(@PathVariable Long dossierId, Model model, RedirectAttributes redirectAttributes) {
        Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
        
        if (dossierOpt.isPresent()) {
            DossierMedical dossier = dossierOpt.get();
            List<Allergie> allergies = allergieRepository.findByDossierMedicalId(dossierId);
            
            model.addAttribute("dossier", dossier);
            model.addAttribute("allergies", allergies);
            model.addAttribute("pageTitle", "Allergies - " + dossier.getResident().getNomComplet());
            return "infirmier/allergies-liste";
        } else {
            redirectAttributes.addFlashAttribute("error", "Dossier médical non trouvé");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }
}