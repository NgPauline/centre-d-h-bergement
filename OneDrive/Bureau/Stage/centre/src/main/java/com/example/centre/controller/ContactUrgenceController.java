/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.ContactUrgence;
import com.example.centre.repository.ContactUrgenceRepository;
import com.example.centre.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ContactUrgenceController {

    @Autowired
    private ContactUrgenceRepository contactUrgenceRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @PostMapping("/directeur/contacts-urgence")
    public ResponseEntity<?> createContactUrgence(@RequestBody ContactUrgence contactUrgence) {
        try {
            // Vérifier si le résident existe
            if (!residentRepository.existsById(contactUrgence.getResident().getId())) {
                return ResponseEntity.badRequest().body("Résident non trouvé");
            }

            ContactUrgence savedContact = contactUrgenceRepository.save(contactUrgence);
            return ResponseEntity.ok(savedContact);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création du contact d'urgence: " + e.getMessage());
        }
    }

    @GetMapping("/contacts-urgence/resident/{residentId}")
    public List<ContactUrgence> getContactsByResident(@PathVariable Long residentId) {
        return contactUrgenceRepository.findByResidentId(residentId);
    }

    @GetMapping("/contacts-urgence/{id}")
    public ResponseEntity<?> getContactById(@PathVariable Long id) {
        Optional<ContactUrgence> contact = contactUrgenceRepository.findById(id);
        return contact.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/directeur/contacts-urgence/{id}")
    public ResponseEntity<?> updateContactUrgence(@PathVariable Long id, @RequestBody ContactUrgence contactDetails) {
        Optional<ContactUrgence> contactOpt = contactUrgenceRepository.findById(id);
        if (contactOpt.isPresent()) {
            ContactUrgence contact = contactOpt.get();
            contact.setNom(contactDetails.getNom());
            contact.setPrenom(contactDetails.getPrenom());
            contact.setTelephone(contactDetails.getTelephone());
            contact.setEmail(contactDetails.getEmail());
            contact.setLienParente(contactDetails.getLienParente());
            contact.setContactPrincipal(contactDetails.getContactPrincipal());
            
            ContactUrgence updatedContact = contactUrgenceRepository.save(contact);
            return ResponseEntity.ok(updatedContact);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/directeur/contacts-urgence/{id}")
    public ResponseEntity<?> deleteContactUrgence(@PathVariable Long id) {
        if (contactUrgenceRepository.existsById(id)) {
            contactUrgenceRepository.deleteById(id);
            return ResponseEntity.ok("Contact d'urgence supprimé avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/contacts-urgence/principal/resident/{residentId}")
    public ResponseEntity<?> getContactPrincipalByResident(@PathVariable Long residentId) {
        Optional<ContactUrgence> contact = contactUrgenceRepository.findContactPrincipalByResidentId(residentId);
        return contact.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/directeur/contacts-urgence/{id}/definir-principal")
    public ResponseEntity<?> definirContactPrincipal(@PathVariable Long id) {
        Optional<ContactUrgence> contactOpt = contactUrgenceRepository.findById(id);
        if (contactOpt.isPresent()) {
            ContactUrgence contact = contactOpt.get();
            
            // Retirer le statut principal des autres contacts du même résident
            List<ContactUrgence> autresContacts = contactUrgenceRepository.findByResidentId(contact.getResident().getId());
            for (ContactUrgence autreContact : autresContacts) {
                if (autreContact.getContactPrincipal()) {
                    autreContact.setContactPrincipal(false);
                    contactUrgenceRepository.save(autreContact);
                }
            }
            
            // Définir ce contact comme principal
            contact.setContactPrincipal(true);
            contactUrgenceRepository.save(contact);
            
            return ResponseEntity.ok("Contact défini comme principal");
        }
        return ResponseEntity.notFound().build();
    }
}
