/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.NoteQuotidienne;
import com.example.centre.repository.NoteQuotidienneRepository;
import com.example.centre.repository.ResidentRepository;
import com.example.centre.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class NoteQuotidienneController {

    @Autowired
    private NoteQuotidienneRepository noteQuotidienneRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    // === ACCÈS MULTI-ROLES ===
    @PostMapping("/notes-quotidiennes")
    public ResponseEntity<?> createNote(@RequestBody Map<String, Object> noteData) {
        try {
            Long residentId = Long.valueOf(noteData.get("residentId").toString());
            Long auteurId = Long.valueOf(noteData.get("auteurId").toString());
            
            // Vérifier les existences
            if (!residentRepository.existsById(residentId)) {
                return ResponseEntity.badRequest().body("Résident non trouvé");
            }
            if (!personnelRepository.existsById(auteurId)) {
                return ResponseEntity.badRequest().body("Auteur non trouvé");
            }
            
            NoteQuotidienne note = new NoteQuotidienne();
            note.setResident(residentRepository.findById(residentId).get());
            note.setAuteur(personnelRepository.findById(auteurId).get());
            note.setContenu((String) noteData.get("contenu"));
            note.setTypeNote((String) noteData.get("typeNote"));
            note.setHumeur(com.example.centre.entity.Humeur.valueOf((String) noteData.get("humeur")));
            
            if (noteData.get("important") != null) {
                note.setImportant(Boolean.valueOf(noteData.get("important").toString()));
            }
            
            NoteQuotidienne savedNote = noteQuotidienneRepository.save(note);
            return ResponseEntity.ok(savedNote);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la création de la note: " + e.getMessage());
        }
    }

    @GetMapping("/notes-quotidiennes")
    public List<NoteQuotidienne> getAllNotes() {
        return noteQuotidienneRepository.findAll();
    }

    @GetMapping("/notes-quotidiennes/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable Long id) {
        Optional<NoteQuotidienne> note = noteQuotidienneRepository.findById(id);
        return note.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/notes-quotidiennes/{id}")
    public ResponseEntity<?> updateNote(@PathVariable Long id, @RequestBody NoteQuotidienne noteDetails) {
        Optional<NoteQuotidienne> noteOpt = noteQuotidienneRepository.findById(id);
        if (noteOpt.isPresent()) {
            NoteQuotidienne note = noteOpt.get();
            note.setContenu(noteDetails.getContenu());
            note.setHumeur(noteDetails.getHumeur());
            note.setImportant(noteDetails.getImportant());
            note.setTypeNote(noteDetails.getTypeNote());
            note.setActions(noteDetails.getActions());
            
            NoteQuotidienne updatedNote = noteQuotidienneRepository.save(note);
            return ResponseEntity.ok(updatedNote);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/notes-quotidiennes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        if (noteQuotidienneRepository.existsById(id)) {
            noteQuotidienneRepository.deleteById(id);
            return ResponseEntity.ok("Note supprimée avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    // === ROUTES SPÉCIFIQUES PAR RÔLE ===
    
    // Infirmier - Notes médicales
    @GetMapping("/infirmier/notes-quotidiennes/medicales")
    public List<NoteQuotidienne> getNotesMedicales() {
        return noteQuotidienneRepository.findByTypeNote("Médicale");
    }

    @GetMapping("/infirmier/notes-quotidiennes/resident/{residentId}")
    public List<NoteQuotidienne> getNotesMedicalesParResident(@PathVariable Long residentId) {
        return noteQuotidienneRepository.findByResidentId(residentId);
    }

    // Éducateur - Notes éducatives
    @GetMapping("/educateur/notes-quotidiennes/educatives")
    public List<NoteQuotidienne> getNotesEducatives() {
        return noteQuotidienneRepository.findByTypeNote("Éducative");
    }

    // Aide-soignant - Notes récentes
    @GetMapping("/aide-soignant/notes-quotidiennes/recentes")
    public List<NoteQuotidienne> getNotesRecentess() {
        return noteQuotidienneRepository.findByDateBetween(
            LocalDateTime.now().minusDays(1), 
            LocalDateTime.now()
        );
    }

    // Directeur - Toutes les notes importantes
    @GetMapping("/directeur/notes-quotidiennes/importantes")
    public List<NoteQuotidienne> getNotesImportantes() {
        return noteQuotidienneRepository.findByImportantTrue();
    }

    @GetMapping("/notes-quotidiennes/resident/{residentId}")
    public List<NoteQuotidienne> getNotesByResident(@PathVariable Long residentId) {
        return noteQuotidienneRepository.findByResidentId(residentId);
    }

    @GetMapping("/notes-quotidiennes/auteur/{auteurId}")
    public List<NoteQuotidienne> getNotesByAuteur(@PathVariable Long auteurId) {
        return noteQuotidienneRepository.findByAuteurId(auteurId);
    }

    @PutMapping("/notes-quotidiennes/{id}/marquer-important")
    public ResponseEntity<?> marquerNoteImportante(@PathVariable Long id) {
        Optional<NoteQuotidienne> noteOpt = noteQuotidienneRepository.findById(id);
        if (noteOpt.isPresent()) {
            NoteQuotidienne note = noteOpt.get();
            note.marquerImportant();
            noteQuotidienneRepository.save(note);
            return ResponseEntity.ok("Note marquée comme importante");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/notes-quotidiennes/recentes/resident/{residentId}")
    public List<NoteQuotidienne> getNotesRecentessParResident(@PathVariable Long residentId) {
        return noteQuotidienneRepository.findRecentNotesByResident(
            residentId, 
            LocalDateTime.now().minusDays(7)
        );
    }
}
