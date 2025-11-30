/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.controller;

import com.example.centre.entity.Role;
import com.example.centre.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ViewController {

    private final UtilisateurRepository utilisateurRepository;
    private final DashboardController dashboardController;

    public ViewController(UtilisateurRepository utilisateurRepository, DashboardController dashboardController) {
        this.utilisateurRepository = utilisateurRepository;
        this.dashboardController = dashboardController;
    }

    // Redirection vers le dashboard CORRECT
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    // Page de login
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // ✅ DASHBOARD UNIQUE AVEC REDIRECTION AUTOMATIQUE CORRIGÉE
    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }

        // Récupérer le rôle de l'utilisateur
        Role userRole = utilisateurRepository.findByUsername(auth.getName())
                .map(user -> user.getRole())
                .orElse(Role.SECRETAIRE);

        // ✅ REDIRECTION AUTOMATIQUE CORRIGÉE - ADMIN et DIRECTEUR séparés
        switch (userRole) {
            case ADMIN:
                return "redirect:/admin/dashboard";
            case DIRECTEUR:
                return "redirect:/directeur/dashboard"; // ⭐ CHANGÉ
            case INFIRMIER:
                return "redirect:/infirmier/dashboard";
            case AIDE_SOIGNANT:
                return "redirect:/aide-soignant/dashboard";
            case EDUCATEUR:
                return "redirect:/educateur/dashboard";
            case COMPTABLE:
                return "redirect:/comptable/dashboard";
            case SECRETAIRE:
                return "redirect:/secretaire/dashboard";
            default:
                return "redirect:/admin/dashboard";
        }
    }

    // ✅ DASHBOARD ADMIN (spécifique)
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Tableau de bord - Administration Système");
        model.addAttribute("dashboardType", "admin");
        // Stats spécifiques ADMIN
        Map<String, Object> stats = dashboardController.getStatsDirecteur();
        model.addAttribute("stats", stats);
        return "dashboard/admin";
    }

    // ✅ DASHBOARD DIRECTEUR (spécifique) - AJOUTÉ
    @GetMapping("/directeur/dashboard")
    public String directeurDashboard(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Tableau de bord - Direction");
        model.addAttribute("dashboardType", "directeur");
        // Stats spécifiques DIRECTEUR
        Map<String, Object> stats = dashboardController.getStatsDirecteur();
        model.addAttribute("stats", stats);
        return "dashboard/directeur";
    }

    @GetMapping("/infirmier/dashboard")
    public String infirmierDashboard(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Tableau de bord - Infirmier");
        model.addAttribute("dashboardType", "infirmier");
        Map<String, Object> stats = dashboardController.getStatsInfirmier();
        model.addAttribute("stats", stats);
        return "dashboard/infirmier";
    }

    @GetMapping("/aide-soignant/dashboard")
    public String aideSoignantDashboard(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Tableau de bord - Aide-soignant");
        model.addAttribute("dashboardType", "aide-soignant");
        Map<String, Object> stats = dashboardController.getStatsAideSoignant();
        model.addAttribute("stats", stats);
        return "dashboard/aide-soignant";
    }

    @GetMapping("/educateur/dashboard")
    public String educateurDashboard(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Tableau de bord - Éducateur");
        model.addAttribute("dashboardType", "educateur");
        Map<String, Object> stats = dashboardController.getStatsEducateur();
        model.addAttribute("stats", stats);
        return "dashboard/educateur";
    }

    @GetMapping("/comptable/dashboard")
    public String comptableDashboard(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Tableau de bord - Comptable");
        model.addAttribute("dashboardType", "comptable");
        Map<String, Object> stats = dashboardController.getStatsComptable();
        model.addAttribute("stats", stats);
        return "dashboard/comptable";
    }

    @GetMapping("/secretaire/dashboard")
    public String secretaireDashboard(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Tableau de bord - Secrétaire");
        model.addAttribute("dashboardType", "secretaire");
        Map<String, Object> stats = dashboardController.getStatsSecretaire();
        model.addAttribute("stats", stats);
        return "dashboard/secretaire";
    }

    // Autres pages...
    /* @GetMapping("/chambres")
    public String chambres(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Gestion des chambres");
        return "chambres/index";
    }

    @GetMapping("/personnel")
    public String personnel(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Gestion du personnel");
        return "personnel/index";
    }*/

    @GetMapping("/rapports")
    public String rapports(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Rapports et statistiques");
        return "rapports/index";
    }

    @GetMapping("/admin/utilisateurs")
    public String adminUtilisateurs(HttpServletRequest request, Model model) {
        setActivePage(request, model);
        model.addAttribute("pageTitle", "Gestion des utilisateurs");
        return "admin/utilisateurs";
    }

    // Méthode utilitaire
    private void setActivePage(HttpServletRequest request, Model model) {
        model.addAttribute("activePage", request.getRequestURI());
    }
}