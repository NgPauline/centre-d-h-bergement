/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JustFly.controller;

import com.example.JustFly.entity.Passager;
import com.example.JustFly.service.PassagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/passagers")
public class PassagerController {

    private final PassagerService passagerService;

    @Autowired
    public PassagerController(PassagerService passagerService) {
        this.passagerService = passagerService;
    }

    @PostMapping
    public Passager savePassager(@RequestBody Passager passager) {
        return passagerService.addPassager(passager);
    }

    @GetMapping
    public List<Passager> getAllPassagers() {
        return passagerService.getAllPassagers();
    }

    @GetMapping("/{id}")
    public Optional<Passager> getPassagerById(@PathVariable Long id) {
        return passagerService.getPassagerById(id); // Méthode modifiée pour récupérer par id
    }

    @DeleteMapping("/{id}")
    public void deletePassager(@PathVariable Long id) {
        passagerService.deletePassager(id);
    }
}
