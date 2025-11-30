/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Pathologie;
import com.example.centre.entity.DossierMedical;
import com.example.centre.repository.PathologieRepository;
import com.example.centre.repository.DossierMedicalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/infirmier/pathologies")
public class PathologieController {

    private final PathologieRepository pathologieRepository;
    private final DossierMedicalRepository dossierMedicalRepository;

    public PathologieController(PathologieRepository pathologieRepository, 
                              DossierMedicalRepository dossierMedicalRepository) {
        this.pathologieRepository = pathologieRepository;
        this.dossierMedicalRepository = dossierMedicalRepository;
    }

    /**
     * Affiche le formulaire d'ajout d'une pathologie
     */
    @GetMapping("/ajouter/{dossierId}")
    public String ajouterPathologieForm(@PathVariable Long dossierId, Model model, RedirectAttributes redirectAttributes) {
        Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
        
        if (dossierOpt.isPresent()) {
            model.addAttribute("dossier", dossierOpt.get());
            model.addAttribute("pathologie", new Pathologie());
            model.addAttribute("pageTitle", "Ajouter une pathologie");
            model.addAttribute("aujourdhui", LocalDate.now());
            return "infirmier/pathologie-formulaire";
        } else {
            redirectAttributes.addFlashAttribute("error", "Dossier médical non trouvé");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Traite l'ajout d'une nouvelle pathologie
     */
    @PostMapping("/ajouter/{dossierId}")
    public String ajouterPathologie(@PathVariable Long dossierId,
                                  @ModelAttribute Pathologie pathologie,
                                  RedirectAttributes redirectAttributes) {
        try {
            Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
            
            if (dossierOpt.isPresent()) {
                DossierMedical dossier = dossierOpt.get();
                
                pathologie.setDossierMedical(dossier);
                pathologieRepository.save(pathologie);
                
                // Mettre à jour la date de modification du dossier
                dossier.mettreAJour();
                dossierMedicalRepository.save(dossier);
                
                redirectAttributes.addFlashAttribute("success", "Pathologie ajoutée avec succès");
                return "redirect:/infirmier/dossiers-medicaux/" + dossier.getResident().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Dossier médical non trouvé");
                return "redirect:/infirmier/dossiers-medicaux";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de la pathologie: " + e.getMessage());
            return "redirect:/infirmier/pathologies/ajouter/" + dossierId;
        }
    }

    /**
     * Affiche les détails d'une pathologie
     */
    @GetMapping("/{id}")
    public String detailsPathologie(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Pathologie> pathologieOpt = pathologieRepository.findById(id);
        
        if (pathologieOpt.isPresent()) {
            Pathologie pathologie = pathologieOpt.get();
            model.addAttribute("pathologie", pathologie);
            model.addAttribute("pageTitle", "Détails pathologie - " + pathologie.getNom());
            return "infirmier/pathologie-details";
        } else {
            redirectAttributes.addFlashAttribute("error", "Pathologie non trouvée");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Affiche le formulaire de modification d'une pathologie
     */
    @GetMapping("/{id}/modifier")
    public String modifierPathologieForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Pathologie> pathologieOpt = pathologieRepository.findById(id);
        
        if (pathologieOpt.isPresent()) {
            Pathologie pathologie = pathologieOpt.get();
            model.addAttribute("pathologie", pathologie);
            model.addAttribute("pageTitle", "Modifier pathologie - " + pathologie.getNom());
            model.addAttribute("aujourdhui", LocalDate.now());
            return "infirmier/pathologie-formulaire";
        } else {
            redirectAttributes.addFlashAttribute("error", "Pathologie non trouvée");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Traite la modification d'une pathologie
     */
    @PostMapping("/{id}/modifier")
    public String modifierPathologie(@PathVariable Long id,
                                   @ModelAttribute Pathologie pathologieDetails,
                                   RedirectAttributes redirectAttributes) {
        try {
            Optional<Pathologie> pathologieOpt = pathologieRepository.findById(id);
            
            if (pathologieOpt.isPresent()) {
                Pathologie pathologie = pathologieOpt.get();
                
                // Mise à jour des champs
                pathologie.setNom(pathologieDetails.getNom());
                pathologie.setDescription(pathologieDetails.getDescription());
                pathologie.setDateDiagnostic(pathologieDetails.getDateDiagnostic());
                pathologie.setChronique(pathologieDetails.getChronique());
                pathologie.setTraitement(pathologieDetails.getTraitement());
                pathologie.setSymptomes(pathologieDetails.getSymptomes());
                
                pathologieRepository.save(pathologie);
                
                // Mettre à jour la date de modification du dossier
                DossierMedical dossier = pathologie.getDossierMedical();
                dossier.mettreAJour();
                dossierMedicalRepository.save(dossier);
                
                redirectAttributes.addFlashAttribute("success", "Pathologie modifiée avec succès");
                return "redirect:/infirmier/dossiers-medicaux/" + dossier.getResident().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Pathologie non trouvée");
                return "redirect:/infirmier/dossiers-medicaux";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
            return "redirect:/infirmier/pathologies/" + id + "/modifier";
        }
    }

    /**
     * Supprime une pathologie
     */
    @PostMapping("/{id}/supprimer")
    public String supprimerPathologie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Pathologie> pathologieOpt = pathologieRepository.findById(id);
            
            if (pathologieOpt.isPresent()) {
                Pathologie pathologie = pathologieOpt.get();
                DossierMedical dossier = pathologie.getDossierMedical();
                Long residentId = dossier.getResident().getId();
                
                pathologieRepository.delete(pathologie);
                
                // Mettre à jour la date de modification du dossier
                dossier.mettreAJour();
                dossierMedicalRepository.save(dossier);
                
                redirectAttributes.addFlashAttribute("success", "Pathologie supprimée avec succès");
                return "redirect:/infirmier/dossiers-medicaux/" + residentId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Pathologie non trouvée");
                return "redirect:/infirmier/dossiers-medicaux";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Liste toutes les pathologies d'un dossier médical
     */
    @GetMapping("/dossier/{dossierId}")
    public String listePathologiesDossier(@PathVariable Long dossierId, Model model, RedirectAttributes redirectAttributes) {
        Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
        
        if (dossierOpt.isPresent()) {
            DossierMedical dossier = dossierOpt.get();
            List<Pathologie> pathologies = pathologieRepository.findByDossierMedicalId(dossierId);
            
            model.addAttribute("dossier", dossier);
            model.addAttribute("pathologies", pathologies);
            model.addAttribute("pageTitle", "Pathologies - " + dossier.getResident().getNomComplet());
            return "infirmier/pathologies-liste";
        } else {
            redirectAttributes.addFlashAttribute("error", "Dossier médical non trouvé");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Liste les pathologies chroniques
     */
    @GetMapping("/chroniques")
    public String listePathologiesChroniques(Model model) {
        List<Pathologie> pathologies = pathologieRepository.findByChroniqueTrue();
        model.addAttribute("pathologies", pathologies);
        model.addAttribute("pageTitle", "Pathologies Chroniques");
        return "infirmier/pathologies-chroniques";
    }
}