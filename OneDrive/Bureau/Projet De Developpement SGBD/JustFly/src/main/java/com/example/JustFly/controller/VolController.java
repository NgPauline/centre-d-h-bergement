/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.controller;

import com.example.JustFly.entity.Vol;
import com.example.JustFly.service.VolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vols")
public class VolController {

    private final VolService volService;

    @Autowired
    public VolController(VolService volService) {
        this.volService = volService;
    }

    @PostMapping
    public Vol saveVol(@RequestBody Vol vol) {
        return volService.addVol(vol);
    }

    @GetMapping
    public List<Vol> getAllVols() {
        return volService.getAllVols();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vol> getVolById(@PathVariable Long id) {
        Optional<Vol> vol = volService.getVolById(id);  // Utilisation de la méthode correcte
        return vol.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());  // Retourne 404 si vol non trouvé
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVol(@PathVariable Long id) {
        if (volService.deleteVol(id)) {
            return ResponseEntity.noContent().build();  // Retourne 204 si la suppression réussit
        }
        return ResponseEntity.notFound().build();  // Retourne 404 si le vol n'existe pas
    }
}
