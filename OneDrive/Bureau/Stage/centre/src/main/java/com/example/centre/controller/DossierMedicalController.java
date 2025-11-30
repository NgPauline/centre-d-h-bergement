/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.*;
import com.example.centre.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/infirmier")
public class DossierMedicalController {
    
    private final MedicamentRepository medicamentRepository;
    private final DossierMedicalRepository dossierMedicalRepository;
    private final ResidentRepository residentRepository;
    private final AllergieRepository allergieRepository;
    private final PathologieRepository pathologieRepository;
    
    public DossierMedicalController(MedicamentRepository medicamentRepository,
                                  DossierMedicalRepository dossierMedicalRepository,
                                  ResidentRepository residentRepository,
                                  AllergieRepository allergieRepository,
                                  PathologieRepository pathologieRepository) {
        this.medicamentRepository = medicamentRepository;
        this.dossierMedicalRepository = dossierMedicalRepository;
        this.residentRepository = residentRepository;
        this.allergieRepository = allergieRepository;
        this.pathologieRepository = pathologieRepository;
    }

    // ==================== DOSSIERS MÉDICAUX ====================

    /**
     * Affiche la liste des résidents avec statut de leur dossier médical
     */
    @GetMapping("/dossiers-medicaux")
    public String dossiersMedicaux(Model model) {
        List<Resident> residents = residentRepository.findAll();
        
        // Préparer les données avec informations de dossier
        List<ResidentInfo> residentsWithDossierInfo = residents.stream()
            .map(resident -> {
                boolean hasDossier = dossierMedicalRepository.existsByResidentId(resident.getId());
                return new ResidentInfo(resident, hasDossier);
            })
            .toList();
        
        model.addAttribute("pageTitle", "Dossiers Médicaux");
        model.addAttribute("residents", residentsWithDossierInfo);
        return "infirmier/dossiers-medicaux";
    }

    /**
     * Sélectionne un résident pour voir son dossier
     */
    @GetMapping("/dossiers-medicaux/selectionner")
    public String selectionnerResident(@RequestParam Long residentId, RedirectAttributes redirectAttributes) {
        return "redirect:/infirmier/dossiers-medicaux/" + residentId;
    }

    /**
     * Affiche le formulaire de création d'un dossier médical
     */
    @GetMapping("/dossiers-medicaux/creer/{residentId}")
    public String creerDossierMedicalForm(@PathVariable Long residentId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Resident> residentOpt = residentRepository.findById(residentId);
        
        if (residentOpt.isPresent()) {
            if (dossierMedicalRepository.existsByResidentId(residentId)) {
                redirectAttributes.addFlashAttribute("error", "Un dossier médical existe déjà pour ce résident");
                return "redirect:/infirmier/dossiers-medicaux";
            }
            
            model.addAttribute("pageTitle", "Créer Dossier Médical");
            model.addAttribute("resident", residentOpt.get());
            model.addAttribute("dossier", new DossierMedical());
            return "infirmier/dossier-medical-formulaire";
        } else {
            redirectAttributes.addFlashAttribute("error", "Résident non trouvé");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Traite la création d'un nouveau dossier médical
     */
    @PostMapping("/dossiers-medicaux/creer")
    public String creerDossierMedical(@RequestParam Long residentId,
                                    @ModelAttribute DossierMedical dossier,
                                    RedirectAttributes redirectAttributes) {
        try {
            Optional<Resident> residentOpt = residentRepository.findById(residentId);
            
            if (residentOpt.isPresent()) {
                if (dossierMedicalRepository.existsByResidentId(residentId)) {
                    redirectAttributes.addFlashAttribute("error", "Un dossier médical existe déjà pour ce résident");
                    return "redirect:/infirmier/dossiers-medicaux";
                }

                dossier.setResident(residentOpt.get());
                dossierMedicalRepository.save(dossier);
                
                redirectAttributes.addFlashAttribute("success", "Dossier médical créé avec succès");
                return "redirect:/infirmier/dossiers-medicaux/" + residentId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Résident non trouvé");
                return "redirect:/infirmier/dossiers-medicaux";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création du dossier: " + e.getMessage());
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Affiche les détails d'un dossier médical
     */
    @GetMapping("/dossiers-medicaux/{residentId}")
    public String voirDossierMedical(@PathVariable Long residentId, Model model, RedirectAttributes redirectAttributes) {
        Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findByResidentId(residentId);
        Optional<Resident> residentOpt = residentRepository.findById(residentId);

        if (residentOpt.isPresent()) {
            Resident resident = residentOpt.get();
            model.addAttribute("resident", resident);
            model.addAttribute("pageTitle", "Dossier Médical - " + resident.getNomComplet());
            
            if (dossierOpt.isPresent()) {
                model.addAttribute("dossier", dossierOpt.get());
            } else {
                model.addAttribute("dossier", null);
            }
            return "infirmier/dossier-medical-details";
        } else {
            redirectAttributes.addFlashAttribute("error", "Résident non trouvé");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Affiche le formulaire de modification d'un dossier médical
     */
    @GetMapping("/dossiers-medicaux/{dossierId}/modifier")
    public String modifierDossierMedicalForm(@PathVariable Long dossierId, Model model, RedirectAttributes redirectAttributes) {
        Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
        
        if (dossierOpt.isPresent()) {
            DossierMedical dossier = dossierOpt.get();
            model.addAttribute("pageTitle", "Modifier Dossier Médical");
            model.addAttribute("dossier", dossier);
            model.addAttribute("resident", dossier.getResident());
            return "infirmier/dossier-medical-formulaire";
        } else {
            redirectAttributes.addFlashAttribute("error", "Dossier médical non trouvé");
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Traite la modification d'un dossier médical
     */
    @PostMapping("/dossiers-medicaux/{dossierId}/modifier")
    public String modifierDossierMedical(@PathVariable Long dossierId,
                                       @ModelAttribute DossierMedical dossierDetails,
                                       RedirectAttributes redirectAttributes) {
        try {
            Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
            
            if (dossierOpt.isPresent()) {
                DossierMedical dossier = dossierOpt.get();
                
                dossier.setGroupeSanguin(dossierDetails.getGroupeSanguin());
                dossier.setPoids(dossierDetails.getPoids());
                dossier.setTaille(dossierDetails.getTaille());
                dossier.setAntecedents(dossierDetails.getAntecedents());
                dossier.setTraitementsEnCours(dossierDetails.getTraitementsEnCours());
                dossier.mettreAJour();

                dossierMedicalRepository.save(dossier);
                redirectAttributes.addFlashAttribute("success", "Dossier médical mis à jour avec succès");
                return "redirect:/infirmier/dossiers-medicaux/" + dossier.getResident().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Dossier médical non trouvé");
                return "redirect:/infirmier/dossiers-medicaux";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    // ==================== ALLERGIES ====================

    /**
     * Affiche le formulaire d'ajout d'une allergie
     */
    @GetMapping("/dossiers-medicaux/{dossierId}/allergie/ajouter")
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
     * Traite l'ajout d'une allergie
     */
    @PostMapping("/dossiers-medicaux/{dossierId}/allergies")
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
                    return "redirect:/infirmier/dossiers-medicaux/" + dossier.getResident().getId();
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
            Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
            if (dossierOpt.isPresent()) {
                return "redirect:/infirmier/dossiers-medicaux/" + dossierOpt.get().getResident().getId();
            }
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Supprime une allergie
     */
    @PostMapping("/allergies/{allergieId}/supprimer")
    public String supprimerAllergie(@PathVariable Long allergieId, RedirectAttributes redirectAttributes) {
        try {
            Optional<Allergie> allergieOpt = allergieRepository.findById(allergieId);
            
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

    // ==================== PATHOLOGIES ====================

    /**
     * Affiche le formulaire d'ajout d'une pathologie
     */
    @GetMapping("/dossiers-medicaux/{dossierId}/pathologie/ajouter")
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
     * Traite l'ajout d'une pathologie
     */
    @PostMapping("/dossiers-medicaux/{dossierId}/pathologies")
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
            Optional<DossierMedical> dossierOpt = dossierMedicalRepository.findById(dossierId);
            if (dossierOpt.isPresent()) {
                return "redirect:/infirmier/dossiers-medicaux/" + dossierOpt.get().getResident().getId();
            }
            return "redirect:/infirmier/dossiers-medicaux";
        }
    }

    /**
     * Supprime une pathologie
     */
    @PostMapping("/pathologies/{pathologieId}/supprimer")
    public String supprimerPathologie(@PathVariable Long pathologieId, RedirectAttributes redirectAttributes) {
        try {
            Optional<Pathologie> pathologieOpt = pathologieRepository.findById(pathologieId);
            
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

    // ==================== MÉDICAMENTS ====================

    @GetMapping("/medicaments")
    public String medicaments(Model model) {
        List<Medicament> medicaments = medicamentRepository.findAll();
        model.addAttribute("pageTitle", "Gestion des Médicaments");
        model.addAttribute("medicaments", medicaments);
        return "infirmier/medicaments";
    }

    @GetMapping("/medicaments/nouveau")
    public String nouveauMedicament(Model model) {
        model.addAttribute("pageTitle", "Nouveau Médicament");
        model.addAttribute("medicament", new Medicament());
        model.addAttribute("mode", "creation");
        return "infirmier/medicament-formulaire";
    }

    @PostMapping("/medicaments/enregistrer")
    public String enregistrerMedicament(@ModelAttribute Medicament medicament, 
                                      RedirectAttributes redirectAttributes) {
        try {
            if (medicament.getNom() == null || medicament.getNom().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le nom du médicament est obligatoire");
                return "redirect:/infirmier/medicaments/nouveau";
            }
            medicamentRepository.save(medicament);
            redirectAttributes.addFlashAttribute("success", "Médicament créé avec succès!");
            return "redirect:/infirmier/medicaments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création du médicament: " + e.getMessage());
            return "redirect:/infirmier/medicaments/nouveau";
        }
    }

    @GetMapping("/medicaments/details/{id}")
    public String detailsMedicament(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Medicament> medicamentOpt = medicamentRepository.findById(id);
        if (medicamentOpt.isPresent()) {
            Medicament medicament = medicamentOpt.get();
            model.addAttribute("medicament", medicament);
            model.addAttribute("pageTitle", "Détails - " + medicament.getNom());
            return "infirmier/medicament-details";
        } else {
            redirectAttributes.addFlashAttribute("error", "Médicament non trouvé");
            return "redirect:/infirmier/medicaments";
        }
    }

    @GetMapping("/medicaments/modifier/{id}")
    public String modifierMedicament(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Medicament> medicamentOpt = medicamentRepository.findById(id);
        if (medicamentOpt.isPresent()) {
            Medicament medicament = medicamentOpt.get();
            model.addAttribute("medicament", medicament);
            model.addAttribute("pageTitle", "Modifier - " + medicament.getNom());
            model.addAttribute("mode", "modification");
            return "infirmier/medicament-formulaire";
        } else {
            redirectAttributes.addFlashAttribute("error", "Médicament non trouvé");
            return "redirect:/infirmier/medicaments";
        }
    }

    @PostMapping("/medicaments/modifier/{id}")
    public String mettreAJourMedicament(@PathVariable Long id, 
                                      @ModelAttribute Medicament medicamentDetails,
                                      RedirectAttributes redirectAttributes) {
        try {
            Optional<Medicament> medicamentOpt = medicamentRepository.findById(id);
            if (medicamentOpt.isPresent()) {
                Medicament medicament = medicamentOpt.get();
                medicament.setNom(medicamentDetails.getNom());
                medicament.setPrincipeActif(medicamentDetails.getPrincipeActif());
                medicament.setForme(medicamentDetails.getForme());
                medicament.setDosage(medicamentDetails.getDosage());
                medicament.setUnite(medicamentDetails.getUnite());
                medicament.setLaboratoire(medicamentDetails.getLaboratoire());
                medicament.setCodeATC(medicamentDetails.getCodeATC());
                medicamentRepository.save(medicament);
                redirectAttributes.addFlashAttribute("success", "Médicament modifié avec succès!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Médicament non trouvé");
            }
            return "redirect:/infirmier/medicaments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
            return "redirect:/infirmier/medicaments/modifier/" + id;
        }
    }

    @PostMapping("/medicaments/supprimer/{id}")
    public String supprimerMedicament(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return supprimerMedicamentAction(id, redirectAttributes);
    }

    @PostMapping("/medicaments/{id}/supprimer")
    public String supprimerMedicamentAlternate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return supprimerMedicamentAction(id, redirectAttributes);
    }

    private String supprimerMedicamentAction(Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Medicament> medicamentOpt = medicamentRepository.findById(id);
            if (medicamentOpt.isPresent()) {
                Medicament medicament = medicamentOpt.get();
                if (medicament.getPrescriptions() != null && !medicament.getPrescriptions().isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", 
                        "Impossible de supprimer ce médicament car il est associé à des prescriptions");
                } else {
                    medicamentRepository.deleteById(id);
                    redirectAttributes.addFlashAttribute("success", "Médicament supprimé avec succès!");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Médicament non trouvé");
            }
            return "redirect:/infirmier/medicaments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
            return "redirect:/infirmier/medicaments";
        }
    }

    @GetMapping("/medicaments/recherche")
    public String rechercherMedicaments(@RequestParam String keyword, Model model) {
        List<Medicament> medicaments = medicamentRepository.searchByKeyword(keyword);
        model.addAttribute("pageTitle", "Résultats de recherche");
        model.addAttribute("medicaments", medicaments);
        model.addAttribute("keyword", keyword);
        model.addAttribute("resultCount", medicaments.size());
        return "infirmier/medicaments";
    }

    // ==================== AUTRES PAGES ====================

    @GetMapping("/consultations")
    public String consultations(Model model) {
        model.addAttribute("pageTitle", "Consultations");
        return "infirmier/consultations";
    }

    @GetMapping("/prescriptions")
    public String prescriptions(Model model) {
        model.addAttribute("pageTitle", "Prescriptions");
        return "infirmier/prescriptions";
    }

    @GetMapping("/planning-visite")
    public String planningVisite(Model model) {
        model.addAttribute("pageTitle", "Planning des Visites");
        return "infirmier/planning-visite";
    }

    @GetMapping("/urgences")
    public String urgences(Model model) {
        model.addAttribute("pageTitle", "Urgences");
        return "infirmier/urgences";
    }

    // ==================== CLASSES INTERNES ====================

    /**
     * Classe pour transporter les informations résident + statut dossier
     */
    public static class ResidentInfo {
        private Resident resident;
        private boolean hasDossier;
        
        public ResidentInfo(Resident resident, boolean hasDossier) {
            this.resident = resident;
            this.hasDossier = hasDossier;
        }
        
        public Resident getResident() { return resident; }
        public boolean isHasDossier() { return hasDossier; }
    }
}