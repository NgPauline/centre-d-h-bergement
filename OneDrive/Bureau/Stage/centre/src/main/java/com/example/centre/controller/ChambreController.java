/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Chambre;
import com.example.centre.entity.TypeChambre;
import com.example.centre.repository.ChambreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/chambres")
public class ChambreController {

    @Autowired
    private ChambreRepository chambreRepository;

    // === PAGES WEB ===
    
    @GetMapping
    public String index() {
        return "redirect:/chambres/liste";
    }

    @GetMapping("/liste")
    public String listeChambres(Model model) {
        List<Chambre> chambres = chambreRepository.findAll();
        model.addAttribute("chambres", chambres);
        model.addAttribute("typesChambre", TypeChambre.values());
        model.addAttribute("pageTitle", "Liste des Chambres");
        return "chambres/liste";
    }
    
    @GetMapping("/details/{id}")
    public String detailsChambre(@PathVariable Long id, Model model) {
        Chambre chambre = chambreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Chambre non trouvée avec l'id: " + id));
        model.addAttribute("chambre", chambre);
        return "chambres/details";
    }

    @GetMapping("/nouvelle")
    public String nouvelleChambre(Model model) {
        model.addAttribute("chambre", new Chambre());
        model.addAttribute("typesChambre", TypeChambre.values());
        model.addAttribute("pageTitle", "Nouvelle Chambre");
        return "chambres/formulaire";
    }

    @PostMapping("/nouvelle")
    public String creerChambre(@ModelAttribute Chambre chambre, RedirectAttributes redirectAttributes) {
        try {
            // Vérifier si le numéro existe déjà
            if (chambreRepository.findByNumero(chambre.getNumero()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Une chambre avec ce numéro existe déjà");
                return "redirect:/chambres/nouvelle";
            }
            
            chambreRepository.save(chambre);
            redirectAttributes.addFlashAttribute("success", "Chambre créée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création: " + e.getMessage());
        }
        return "redirect:/chambres/liste";
    }

    @GetMapping("/{id}/modifier")
    public String modifierChambre(@PathVariable Long id, Model model) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre introuvable"));
        
        model.addAttribute("chambre", chambre);
        model.addAttribute("typesChambre", TypeChambre.values());
        model.addAttribute("pageTitle", "Modifier Chambre");
        return "chambres/formulaire";
    }

    @PostMapping("/{id}/modifier")
    public String updateChambre(@PathVariable Long id, @ModelAttribute Chambre chambre, RedirectAttributes redirectAttributes) {
        try {
            chambre.setId(id);
            chambreRepository.save(chambre);
            redirectAttributes.addFlashAttribute("success", "Chambre modifiée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification: " + e.getMessage());
        }
        return "redirect:/chambres/liste";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimerChambre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            chambreRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Chambre supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/chambres/liste";
    }

    @GetMapping("/disponibles")
    public String chambresDisponibles(Model model) {
        List<Chambre> chambresDisponibles = chambreRepository.findChambresDisponibles();
        model.addAttribute("chambres", chambresDisponibles);
        model.addAttribute("pageTitle", "Chambres Disponibles");
        return "chambres/liste";
    }
}