/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Personnel;
import com.example.centre.entity.Poste;
import com.example.centre.entity.TypeContrat;
import com.example.centre.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/personnel")
public class PersonnelController {

    @Autowired
    private PersonnelRepository personnelRepository;

    @GetMapping
    public String index() {
        return "redirect:/personnel/liste";
    }

    // Liste du personnel avec recherche
    @GetMapping("/liste")
    public String listePersonnel(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String searchType,
            Model model) {
        
        List<Personnel> personnelList;
        
        if (search != null && !search.trim().isEmpty()) {
            // Recherche activée
            if ("matricule".equals(searchType)) {
                personnelList = personnelRepository.findByMatriculeContainingIgnoreCase(search);
            } else if ("prenom".equals(searchType)) {
                personnelList = personnelRepository.findByPrenomContainingIgnoreCase(search);
            } else {
                // Recherche par nom par défaut
                personnelList = personnelRepository.findByNomContainingIgnoreCase(search);
            }
            model.addAttribute("search", search);
            model.addAttribute("searchType", searchType);
        } else {
            // Aucune recherche, afficher tout le personnel
            personnelList = personnelRepository.findAll();
        }
        
        model.addAttribute("personnelList", personnelList);
        model.addAttribute("pageTitle", "Liste du Personnel");
        return "personnel/liste";
    }

    // Formulaire nouveau personnel
    @GetMapping("/nouveau")
    public String nouveauPersonnel(Model model) {
        model.addAttribute("personnel", new Personnel());
        model.addAttribute("postes", Poste.values());
        model.addAttribute("typesContrat", TypeContrat.values());
        model.addAttribute("pageTitle", "Nouveau Personnel");
        return "personnel/formulaire";
    }

    // Créer un nouveau personnel
    @PostMapping("/nouveau")
    public String creerPersonnel(@ModelAttribute Personnel personnel, RedirectAttributes redirectAttributes) {
        try {
            personnelRepository.save(personnel);
            redirectAttributes.addFlashAttribute("success", "Membre du personnel créé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création: " + e.getMessage());
        }
        return "redirect:/personnel/liste";
    }

    // Formulaire modification
    @GetMapping("/{id}/modifier")
    public String modifierPersonnel(@PathVariable Long id, Model model) {
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personnel introuvable"));
        
        model.addAttribute("personnel", personnel);
        model.addAttribute("postes", Poste.values());
        model.addAttribute("typesContrat", TypeContrat.values());
        model.addAttribute("pageTitle", "Modifier Personnel");
        return "personnel/formulaire";
    }

    // Mettre à jour le personnel
    @PostMapping("/{id}/modifier")
    public String updatePersonnel(@PathVariable Long id, @ModelAttribute Personnel personnel, RedirectAttributes redirectAttributes) {
        try {
            personnel.setId(id);
            personnelRepository.save(personnel);
            redirectAttributes.addFlashAttribute("success", "Personnel modifié avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
        }
        return "redirect:/personnel/liste";
    }

    // Supprimer le personnel
    @PostMapping("/{id}/supprimer")
    public String supprimerPersonnel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            personnelRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Personnel supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/personnel/liste";
    }

    // Détails du personnel
    @GetMapping("/{id}")
    public String detailsPersonnel(@PathVariable Long id, Model model) {
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personnel introuvable"));
        
        model.addAttribute("personnel", personnel);
        model.addAttribute("pageTitle", "Détails Personnel");
        return "personnel/details";
    }
}