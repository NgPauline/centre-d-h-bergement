/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Utilisateur;
import com.example.centre.entity.Role;
import com.example.centre.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByUsername(username);
        
        if (utilisateurOpt.isPresent() && passwordEncoder.matches(password, utilisateurOpt.get().getPassword())) {
            Utilisateur utilisateur = utilisateurOpt.get();
            if (!utilisateur.getActif()) {
                return ResponseEntity.badRequest().body("Compte désactivé");
            }

            // Mettre à jour la dernière connexion
            utilisateur.setDerniereConnexion(LocalDateTime.now());
            utilisateurRepository.save(utilisateur);

            Map<String, Object> response = new HashMap<>();
            response.put("id", utilisateur.getId());
            response.put("username", utilisateur.getUsername());
            response.put("role", utilisateur.getRole());
            response.put("email", utilisateur.getEmail());
            
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.badRequest().body("Identifiants invalides");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");
        String email = userData.get("email");
        String roleStr = userData.get("role");

        if (utilisateurRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Nom d'utilisateur déjà utilisé");
        }

        try {
            Role role = Role.valueOf(roleStr);
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setUsername(username);
            utilisateur.setPassword(passwordEncoder.encode(password));
            utilisateur.setEmail(email);
            utilisateur.setRole(role);
            utilisateur.setActif(true);

            utilisateurRepository.save(utilisateur);
            return ResponseEntity.ok("Utilisateur créé avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Rôle invalide");
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        boolean exists = utilisateurRepository.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}
