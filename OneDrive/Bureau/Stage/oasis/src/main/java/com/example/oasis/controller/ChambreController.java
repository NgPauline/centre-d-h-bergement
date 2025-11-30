/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.oasis.controller;

import com.example.oasis.entity.*;
import com.example.oasis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/chambres")
public class ChambreController {

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private EquipementRepository equipementRepository;

    @GetMapping
    public String listeChambres(Model model,
                              @RequestParam(required = false) Integer etage,
                              @RequestParam(required = false) TypeChambre type) {
        
        List<Chambre> chambres;
        
        if (etage != null) {
            chambres = chambreRepository.findByEtage(etage);
        } else if (type != null) {
            chambres = chambreRepository.findByType(type);
        } else {
            chambres = chambreRepository.findAll();
        }

        model.addAttribute("chambres", chambres);
        model.addAttribute("typesChambre", TypeChambre.values());
        return "chambres/liste";
    }

    @GetMapping("/{id}")
    public String detailChambre(@PathVariable Long id, Model model) {
        Optional<Chambre> chambre = chambreRepository.findById(id);
        if (chambre.isPresent()) {
            model.addAttribute("chambre", chambre.get());
            model.addAttribute("residents", residentRepository.findByChambreId(id));
            return "chambres/detail";
        }
        return "redirect:/chambres";
    }

    @GetMapping("/nouvelle")
    public String formulaireNouvelleChambre(Model model) {
        model.addAttribute("chambre", new Chambre());
        model.addAttribute("typesChambre", TypeChambre.values());
        model.addAttribute("equipements", equipementRepository.findAll());
        return "chambres/formulaire";
    }

    @PostMapping("/creer")
    public String creerChambre(@ModelAttribute Chambre chambre,
                             @RequestParam(required = false) List<Long> equipementIds) {
        
        if (equipementIds != null) {
            List<Equipement> equipements = equipementRepository.findAllById(equipementIds);
            chambre.setEquipements(equipements);
        }
        
        chambreRepository.save(chambre);
        return "redirect:/chambres";
    }

    @GetMapping("/{id}/modifier")
    public String formulaireModifierChambre(@PathVariable Long id, Model model) {
        Optional<Chambre> chambre = chambreRepository.findById(id);
        if (chambre.isPresent()) {
            model.addAttribute("chambre", chambre.get());
            model.addAttribute("typesChambre", TypeChambre.values());
            model.addAttribute("equipements", equipementRepository.findAll());
            return "chambres/formulaire";
        }
        return "redirect:/chambres";
    }

    @PostMapping("/{id}/modifier")
    public String modifierChambre(@PathVariable Long id, @ModelAttribute Chambre chambre) {
        chambre.setId(id);
        chambreRepository.save(chambre);
        return "redirect:/chambres/" + id;
    }

    @GetMapping("/{id}/supprimer")
    public String supprimerChambre(@PathVariable Long id) {
        chambreRepository.deleteById(id);
        return "redirect:/chambres";
    }

    @GetMapping("/disponibles")
    public String chambresDisponibles(Model model) {
        List<Chambre> chambresDisponibles = chambreRepository.findChambresDisponibles();
        model.addAttribute("chambres", chambresDisponibles);
        return "chambres/disponibles";
    }

    @GetMapping("/occupation")
    public String occupationChambres(Model model) {
        List<Object[]> occupation = chambreRepository.findOccupationChambres();
        model.addAttribute("occupation", occupation);
        return "chambres/occupation";
    }
}