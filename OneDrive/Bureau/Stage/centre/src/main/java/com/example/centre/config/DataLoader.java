/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.config;

import com.example.centre.entity.*;
import com.example.centre.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ChambreRepository chambreRepository;
    
    @Autowired
    private ResidentRepository residentRepository;
    
    @Autowired
    private PersonnelRepository personnelRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private ContactUrgenceRepository contactUrgenceRepository;
    
    @Autowired
    private ContratRepository contratRepository;
    
    @Autowired
    private DossierMedicalRepository dossierMedicalRepository;
    
    @Autowired
    private DonRepository donRepository;
    
    @Autowired
    private EquipementRepository equipementRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 === DÉMARRAGE DE L'APPLICATION CENTRE ===");
        
        // VÉRIFICATION DES UTILISATEURS AVANT TOUT
        System.out.println("=== VÉRIFICATION DES UTILISATEURS EXISTANTS ===");
        checkUser("admin");
        checkUser("infirmier");
        checkUser("aidesoignant");
        checkUser("directeur");
        checkUser("secretaire");
        checkUser("comptable");
        checkUser("educateur");
        
        // Charger les données de test seulement si nécessaire
        if (utilisateurRepository.count() < 7) {
            System.out.println("📦 Chargement des données de test...");
            chargerDonneesTest();
        } else {
            System.out.println("ℹ️ Données déjà existantes, skip du chargement");
        }
        
        System.out.println("✅ === APPLICATION PRÊTE ===");
    }
    
    private void checkUser(String username) {
        Optional<Utilisateur> user = utilisateurRepository.findByUsername(username);
        if (user.isPresent()) {
            System.out.println("✅ Utilisateur '" + username + "' trouvé");
            System.out.println("   - Mot de passe hash: " + user.get().getPassword());
            System.out.println("   - Rôle: " + user.get().getRole());
            System.out.println("   - Actif: " + user.get().getActif());
            
            // Test du mot de passe
            String testPassword = getTestPassword(username);
            boolean passwordMatches = passwordEncoder.matches(testPassword, user.get().getPassword());
            System.out.println("   - Mot de passe '" + testPassword + "' valide: " + passwordMatches);
            
            if (!passwordMatches) {
                System.out.println("⚠️  RECRÉATION DE L'UTILISATEUR " + username);
                recreerUtilisateur(user.get(), testPassword);
            }
        } else {
            System.out.println("❌ Utilisateur '" + username + "' NON trouvé - sera créé");
            creerUtilisateurManquant(username);
        }
    }
    
    private String getTestPassword(String username) {
        switch (username) {
            case "admin": return "admin123";
            case "directeur": return "directeur123";
            case "secretaire": return "secretaire123";
            case "infirmier": return "infirmier123";
            case "aidesoignant": return "aide123";
            case "educateur": return "educateur123"; 
            case "comptable": return "comptable123";
            default: return "password";
        }
    }
    
    private void recreerUtilisateur(Utilisateur oldUser, String plainPassword) {
        oldUser.setPassword(passwordEncoder.encode(plainPassword));
        utilisateurRepository.save(oldUser);
        System.out.println("✅ Utilisateur " + oldUser.getUsername() + " recréé avec nouveau mot de passe");
    }
    
    private void creerUtilisateurManquant(String username) {
        switch (username) {
            case "directeur":
                creerDirecteur();
                break;
            case "secretaire":
                creerSecretaire();
                break;
            case "educateur":
                creerEducateur();
                break;
            case "comptable":
                creerComptable();
                break;
            default:
                System.out.println("⚠️  Utilisateur " + username + " non géré dans la création manquante");
        }
    }
    
    private void creerDirecteur() {
        System.out.println("➕ Création de l'utilisateur DIRECTEUR...");
        
        Personnel directeurPersonnel = personnelRepository.findByNomAndPrenom("Martin", "Pierre")
            .orElseGet(() -> {
                Personnel nouveau = new Personnel();
                nouveau.setNom("Martin");
                nouveau.setPrenom("Pierre");
                nouveau.setEmail("directeur@centre.com");
                nouveau.setTelephone("0102030408");
                nouveau.setPoste(Poste.DIRECTEUR);
                
                Adresse adresse = new Adresse();
                adresse.setRue("Rue du Centre");
                adresse.setNumero("1");
                adresse.setCodePostal("1000");
                adresse.setVille("Bruxelles");
                adresse.setPays("Belgique");
                nouveau.setAdresse(adresse);
                
                return personnelRepository.save(nouveau);
            });
        
        Utilisateur directeur = new Utilisateur();
        directeur.setUsername("directeur");
        directeur.setPassword(passwordEncoder.encode("directeur123"));
        directeur.setRole(Role.DIRECTEUR);
        directeur.setEmail("directeur@centre.com");
        directeur.setActif(true);
        directeur.setPersonnel(directeurPersonnel);
        utilisateurRepository.save(directeur);
        
        System.out.println("✅ Utilisateur DIRECTEUR créé: directeur / directeur123");
    }
    
    private void creerSecretaire() {
        System.out.println("➕ Création de l'utilisateur SECRETAIRE...");
        
        Personnel secretairePersonnel = personnelRepository.findByNomAndPrenom("Dubois", "Marie")
            .orElseGet(() -> {
                Personnel nouveau = new Personnel();
                nouveau.setNom("Dubois");
                nouveau.setPrenom("Marie");
                nouveau.setEmail("secretaire@centre.com");
                nouveau.setTelephone("0102030409");
                nouveau.setPoste(Poste.SECRETAIRE);
                
                Adresse adresse = new Adresse();
                adresse.setRue("Rue du Centre");
                adresse.setNumero("1");
                adresse.setCodePostal("1000");
                adresse.setVille("Bruxelles");
                adresse.setPays("Belgique");
                nouveau.setAdresse(adresse);
                
                return personnelRepository.save(nouveau);
            });
        
        Utilisateur secretaire = new Utilisateur();
        secretaire.setUsername("secretaire");
        secretaire.setPassword(passwordEncoder.encode("secretaire123"));
        secretaire.setRole(Role.SECRETAIRE);
        secretaire.setEmail("secretaire@centre.com");
        secretaire.setActif(true);
        secretaire.setPersonnel(secretairePersonnel);
        utilisateurRepository.save(secretaire);
        
        System.out.println("✅ Utilisateur SECRETAIRE créé: secretaire / secretaire123");
    }
    
    private void creerEducateur() {
        System.out.println("➕ Création de l'utilisateur ÉDUCATEUR...");
        
        Personnel educateurPersonnel = personnelRepository.findByNomAndPrenom("Moreau", "Luc")
            .orElseGet(() -> {
                Personnel nouveau = new Personnel();
                nouveau.setNom("Moreau");
                nouveau.setPrenom("Luc");
                nouveau.setEmail("l.moreau@centre.com");
                nouveau.setTelephone("0102030410");
                nouveau.setPoste(Poste.EDUCATEUR);
                
                Adresse adresse = new Adresse();
                adresse.setRue("Rue du Centre");
                adresse.setNumero("1");
                adresse.setCodePostal("1000");
                adresse.setVille("Bruxelles");
                adresse.setPays("Belgique");
                nouveau.setAdresse(adresse);
                
                return personnelRepository.save(nouveau);
            });
        
        Utilisateur educateur = new Utilisateur();
        educateur.setUsername("educateur");
        educateur.setPassword(passwordEncoder.encode("educateur123"));
        educateur.setRole(Role.EDUCATEUR);
        educateur.setEmail("l.moreau@centre.com");
        educateur.setActif(true);
        educateur.setPersonnel(educateurPersonnel);
        utilisateurRepository.save(educateur);
        
        System.out.println("✅ Utilisateur ÉDUCATEUR créé: educateur / educateur123");
    }
    
    private void creerComptable() {
        System.out.println("➕ Création de l'utilisateur COMPTABLE...");
        
        Personnel comptablePersonnel = personnelRepository.findByNomAndPrenom("Rousseau", "Alice")
            .orElseGet(() -> {
                Personnel nouveau = new Personnel();
                nouveau.setNom("Rousseau");
                nouveau.setPrenom("Alice");
                nouveau.setEmail("a.rousseau@centre.com");
                nouveau.setTelephone("0102030411");
                nouveau.setPoste(Poste.COMPTABLE);
                
                Adresse adresse = new Adresse();
                adresse.setRue("Rue du Centre");
                adresse.setNumero("1");
                adresse.setCodePostal("1000");
                adresse.setVille("Bruxelles");
                adresse.setPays("Belgique");
                nouveau.setAdresse(adresse);
                
                return personnelRepository.save(nouveau);
            });
        
        Utilisateur comptable = new Utilisateur();
        comptable.setUsername("comptable");
        comptable.setPassword(passwordEncoder.encode("comptable123"));
        comptable.setRole(Role.COMPTABLE);
        comptable.setEmail("a.rousseau@centre.com");
        comptable.setActif(true);
        comptable.setPersonnel(comptablePersonnel);
        utilisateurRepository.save(comptable);
        
        System.out.println("✅ Utilisateur COMPTABLE créé: comptable / comptable123");
    }
    
    private void chargerDonneesTest() {
        try {
            // 1. Créer des chambres
            chargerChambres();
            
            // 2. Créer des équipements
            chargerEquipements();
            
            // 3. Créer des résidents
            List<Resident> residents = chargerResidents();
            
            // 4. Créer des contacts d'urgence
            chargerContactsUrgence(residents);
            
            // 5. Créer des dossiers médicaux
            chargerDossiersMedicaux(residents);
            
            // 6. Créer des contrats
            chargerContrats(residents);
            
            // 7. Créer du personnel et utilisateurs
            chargerPersonnelEtUtilisateurs();
            
            // 8. Créer des dons
            chargerDons();
            
            // 9. Afficher les statistiques
            afficherStatistiques();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement des données: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void chargerChambres() {
        if (chambreRepository.count() == 0) {
            System.out.println("🏠 Création des chambres...");
            
            Chambre chambre1 = new Chambre("101", 1, 2, TypeChambre.DOUBLE);
            chambre1.setDescription("Chambre spacieuse avec vue sur le jardin");
            
            Chambre chambre2 = new Chambre("102", 1, 1, TypeChambre.SIMPLE);
            chambre2.setDescription("Suite avec salle de bain adaptée");
            
            Chambre chambre3 = new Chambre("201", 2, 3, TypeChambre.DOUBLE);
            chambre3.setDescription("Chambre familiale");
            
            Chambre chambre4 = new Chambre("202", 2, 2, TypeChambre.SIMPLE);
            chambre4.setDescription("Chambre avec balcon");
            
            chambreRepository.saveAll(List.of(chambre1, chambre2, chambre3, chambre4));
            
            System.out.println("✅ " + chambreRepository.count() + " chambres créées");
        }
    }
    
    private void chargerEquipements() {
        if (equipementRepository.count() == 0) {
            System.out.println("🔧 Création des équipements...");
            
            Equipement litMedical = new Equipement("Lit médical électrique", TypeEquipement.LIT_MEDICALISE);
            litMedical.setDescription("Lit réglable électriquement");
            litMedical.setMarque("Hill-Rom");
            litMedical.setDateAcquisition(LocalDate.now().minusMonths(6));
            
            Equipement fauteuilRoulant = new Equipement("Fauteuil roulant manuel", TypeEquipement.FAUTEUIL_ROULANT);
            fauteuilRoulant.setDescription("Fauteuil roulant standard");
            fauteuilRoulant.setMarque("Invacare");
            
            Equipement moniteur = new Equipement("Moniteur de signes vitaux", TypeEquipement.LEVE_PERSONNE);
            moniteur.setDescription("Surveillance tension et pouls");
            moniteur.setMarque("Philips");
            
            equipementRepository.saveAll(List.of(litMedical, fauteuilRoulant, moniteur));
            
            System.out.println("✅ " + equipementRepository.count() + " équipements créés");
        }
    }
    
    
    private void chargerDossiersMedicaux(List<Resident> residents) {
    if (dossierMedicalRepository.count() == 0 && !residents.isEmpty()) {
        System.out.println("🏥 Création des dossiers médicaux...");
        for (Resident resident : residents) {
            DossierMedical dossier = new DossierMedical(resident);
            dossier.setGroupeSanguin("A+");
            dossier.setPoids(68.0);
            dossier.setTaille(170.0);
            dossier.setAntecedents("Hypertension légère");
            dossier.setTraitementsEnCours("Bêta-bloquants quotidiens");
            dossierMedicalRepository.save(dossier);
        }
        System.out.println("✅ " + dossierMedicalRepository.count() + " dossiers médicaux créés");
    }
}

    
    private void chargerContrats(List<Resident> residents) {
    if (contratRepository.count() == 0 && !residents.isEmpty()) {
        System.out.println("📝 Création des contrats...");
        for (Resident resident : residents) {
            Contrat contrat = new Contrat(resident, resident.getDateAdmission(), 1500.0);
            contrat.setDureeContrat("1 an");
            contrat.setTypeHebergement("Hébergement permanent");
            contrat.setConditions("Pension complète et soins inclus");
            contratRepository.save(contrat);
        }
        System.out.println("✅ " + contratRepository.count() + " contrats créés");
    }
}

    
    private List<Resident> chargerResidents() {
    if (residentRepository.count() == 0) {
        System.out.println("👴 Création des résidents...");
        
        // Récupérer les chambres
        Chambre chambre101 = chambreRepository.findByNumero("101")
            .orElseThrow(() -> new RuntimeException("Chambre 101 non trouvée"));
        Chambre chambre102 = chambreRepository.findByNumero("102")
            .orElseThrow(() -> new RuntimeException("Chambre 102 non trouvée"));
        
        // ✅ CRÉER DES ADRESSES
        Adresse adresse1 = new Adresse();
        adresse1.setRue("Rue de la Paix");
        adresse1.setNumero("12");
        adresse1.setCodePostal("1000");
        adresse1.setVille("Bruxelles");
        adresse1.setPays("Belgique");
        
        Adresse adresse2 = new Adresse();
        adresse2.setRue("Avenue Louise");
        adresse2.setNumero("45");
        adresse2.setCodePostal("1050");
        adresse2.setVille("Ixelles");
        adresse2.setPays("Belgique");
        
        Adresse adresse3 = new Adresse();
        adresse3.setRue("Boulevard Anspach");
        adresse3.setNumero("78");
        adresse3.setCodePostal("1000");
        adresse3.setVille("Bruxelles");
        adresse3.setPays("Belgique");
        
        // Créer des résidents AVEC adresses
        Resident resident1 = new Resident("Dupont", "Marie", LocalDate.of(2024, 1, 15));
        resident1.setDateNaissance(LocalDate.of(1935, 3, 20));
        resident1.setTypeHandicap(TypeHandicap.MOTEUR_LEGER);
        resident1.setDegreHandicap(3);
        resident1.setTelephone("0123456789");
        resident1.setChambre(chambre101);
        resident1.setAdresse(adresse1);  // ✅ AJOUTEZ L'ADRESSE
        
        Resident resident2 = new Resident("Martin", "Pierre", LocalDate.of(2024, 2, 10));
        resident2.setDateNaissance(LocalDate.of(1940, 7, 12));
        resident2.setTypeHandicap(TypeHandicap.MOTEUR_SEVERE);
        resident2.setDegreHandicap(5);
        resident2.setTelephone("0987654321");
        resident2.setChambre(chambre101);
        resident2.setAdresse(adresse2);  // ✅ AJOUTEZ L'ADRESSE
        
        Resident resident3 = new Resident("Bernard", "Jeanne", LocalDate.of(2024, 3, 1));
        resident3.setDateNaissance(LocalDate.of(1938, 11, 5));
        resident3.setTypeHandicap(TypeHandicap.MENTAL_LEGER);
        resident3.setDegreHandicap(7);
        resident3.setBesoinInfirmier(true);
        resident3.setChambre(chambre102);
        resident3.setAdresse(adresse3);  // ✅ AJOUTEZ L'ADRESSE
        
        List<Resident> residents = List.of(resident1, resident2, resident3);
        residentRepository.saveAll(residents);
        
        System.out.println("✅ " + residentRepository.count() + " résidents créés");
        return residents;
    } else {
        return residentRepository.findAll();
    }
}
    
    private void chargerContactsUrgence(List<Resident> residents) {
    if (contactUrgenceRepository.count() == 0 && !residents.isEmpty()) {
        System.out.println("📞 Création des contacts d'urgence...");

        Resident resident1 = residents.get(0);
        Resident resident2 = residents.get(1);

        Adresse adresse1 = new Adresse();
        adresse1.setRue("Rue des Fleurs");
        adresse1.setNumero("5");
        adresse1.setCodePostal("1000");
        adresse1.setVille("Bruxelles");
        adresse1.setPays("Belgique");

        Adresse adresse2 = new Adresse();
        adresse2.setRue("Avenue du Parc");
        adresse2.setNumero("12A");
        adresse2.setCodePostal("1050");
        adresse2.setVille("Ixelles");
        adresse2.setPays("Belgique");

        Adresse adresse3 = new Adresse();
        adresse3.setRue("Boulevard du Midi");
        adresse3.setNumero("20");
        adresse3.setCodePostal("1070");
        adresse3.setVille("Anderlecht");
        adresse3.setPays("Belgique");

        ContactUrgence contact1 = new ContactUrgence("Dupont", "Jean", "0612345678", "Fils");
        contact1.setEmail("jean.dupont@email.com");
        contact1.setContactPrincipal(true);
        contact1.setResident(resident1);
        contact1.setAdresse(adresse1);

        ContactUrgence contact2 = new ContactUrgence("Martin", "Sophie", "0698765432", "Fille");
        contact2.setEmail("sophie.martin@email.com");
        contact2.setContactPrincipal(true);
        contact2.setResident(resident2);
        contact2.setAdresse(adresse2);

        ContactUrgence contact3 = new ContactUrgence("Dupont", "Luc", "0634567890", "Fils");
        contact3.setResident(resident1);
        contact3.setAdresse(adresse3);

        contactUrgenceRepository.saveAll(List.of(contact1, contact2, contact3));

        System.out.println("✅ " + contactUrgenceRepository.count() + " contacts d'urgence créés");
    }
}

    
    private void chargerPersonnelEtUtilisateurs() {
    if (utilisateurRepository.count() == 0) {
        System.out.println("👥 Création du personnel et des utilisateurs...");

        Adresse adressePersonnel = new Adresse();
        adressePersonnel.setRue("Rue du Centre");
        adressePersonnel.setNumero("1");
        adressePersonnel.setCodePostal("1000");
        adressePersonnel.setVille("Bruxelles");
        adressePersonnel.setPays("Belgique");

        // 1️⃣ Création du personnel
        Personnel admin = new Personnel();
        admin.setNom("Système");
        admin.setPrenom("Administrateur");
        admin.setEmail("admin@centre.com");
        admin.setTelephone("0102030405");
        admin.setPoste(Poste.DIRECTEUR);
        admin.setAdresse(adressePersonnel);
        personnelRepository.save(admin);

        Personnel directeur = new Personnel();
        directeur.setNom("Martin");
        directeur.setPrenom("Pierre");
        directeur.setEmail("directeur@centre.com");
        directeur.setTelephone("0102030408");
        directeur.setPoste(Poste.DIRECTEUR);
        directeur.setAdresse(adressePersonnel);
        personnelRepository.save(directeur);

        Personnel secretaire = new Personnel();
        secretaire.setNom("Dubois");
        secretaire.setPrenom("Marie");
        secretaire.setEmail("secretariat@centre.com");
        secretaire.setTelephone("0102030409");
        secretaire.setPoste(Poste.SECRETAIRE);
        secretaire.setAdresse(adressePersonnel);
        personnelRepository.save(secretaire);

        Personnel infirmier = new Personnel();
        infirmier.setNom("Legrand");
        infirmier.setPrenom("Sophie");
        infirmier.setEmail("s.legrand@centre.com");
        infirmier.setTelephone("0102030406");
        infirmier.setPoste(Poste.INFIRMIER);
        infirmier.setAdresse(adressePersonnel);
        personnelRepository.save(infirmier);

        Personnel aideSoignant = new Personnel();
        aideSoignant.setNom("Petit");
        aideSoignant.setPrenom("Marc");
        aideSoignant.setEmail("m.petit@centre.com");
        aideSoignant.setTelephone("0102030407");
        aideSoignant.setPoste(Poste.AIDE_SOIGNANT);
        aideSoignant.setAdresse(adressePersonnel);
        personnelRepository.save(aideSoignant);

        // ✅ AJOUTER LE PERSONNEL MANQUANT
        Personnel educateur = new Personnel();
        educateur.setNom("Moreau");
        educateur.setPrenom("Luc");
        educateur.setEmail("l.moreau@centre.com");
        educateur.setTelephone("0102030410");
        educateur.setPoste(Poste.EDUCATEUR);
        educateur.setAdresse(adressePersonnel);
        personnelRepository.save(educateur);

        Personnel comptable = new Personnel();
        comptable.setNom("Rousseau");
        comptable.setPrenom("Alice");
        comptable.setEmail("a.rousseau@centre.com");
        comptable.setTelephone("0102030411");
        comptable.setPoste(Poste.COMPTABLE);
        comptable.setAdresse(adressePersonnel);
        personnelRepository.save(comptable);

        // 2️⃣ Création des utilisateurs
        Utilisateur adminUser = new Utilisateur();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setEmail("admin@centre.com");
        adminUser.setActif(true);
        adminUser.setPersonnel(admin);
        utilisateurRepository.save(adminUser);

        Utilisateur directeurUser = new Utilisateur();
        directeurUser.setUsername("directeur");
        directeurUser.setPassword(passwordEncoder.encode("directeur123"));
        directeurUser.setRole(Role.DIRECTEUR);
        directeurUser.setEmail("directeur@centre.com");
        directeurUser.setActif(true);
        directeurUser.setPersonnel(directeur);
        utilisateurRepository.save(directeurUser);

        Utilisateur secretaireUser = new Utilisateur();
        secretaireUser.setUsername("secretaire");
        secretaireUser.setPassword(passwordEncoder.encode("secretaire123"));
        secretaireUser.setRole(Role.SECRETAIRE);
        secretaireUser.setEmail("secretariat@centre.com");
        secretaireUser.setActif(true);
        secretaireUser.setPersonnel(secretaire);
        utilisateurRepository.save(secretaireUser);

        Utilisateur infirmierUser = new Utilisateur();
        infirmierUser.setUsername("infirmier");
        infirmierUser.setPassword(passwordEncoder.encode("infirmier123"));
        infirmierUser.setRole(Role.INFIRMIER);
        infirmierUser.setEmail("s.legrand@centre.com");
        infirmierUser.setActif(true);
        infirmierUser.setPersonnel(infirmier);
        utilisateurRepository.save(infirmierUser);
        
        Utilisateur aideUser = new Utilisateur();
        aideUser.setUsername("aidesoignant");
        aideUser.setPassword(passwordEncoder.encode("aide123"));
        aideUser.setRole(Role.AIDE_SOIGNANT);
        aideUser.setEmail("m.petit@centre.com");
        aideUser.setActif(true);
        aideUser.setPersonnel(aideSoignant);
        utilisateurRepository.save(aideUser);

        // ✅ CRÉER LES UTILISATEURS MANQUANTS
        Utilisateur educateurUser = new Utilisateur();
        educateurUser.setUsername("educateur");
        educateurUser.setPassword(passwordEncoder.encode("educateur123"));
        educateurUser.setRole(Role.EDUCATEUR);
        educateurUser.setEmail("l.moreau@centre.com");
        educateurUser.setActif(true);
        educateurUser.setPersonnel(educateur);
        utilisateurRepository.save(educateurUser);

        Utilisateur comptableUser = new Utilisateur();
        comptableUser.setUsername("comptable");
        comptableUser.setPassword(passwordEncoder.encode("comptable123"));
        comptableUser.setRole(Role.COMPTABLE);
        comptableUser.setEmail("a.rousseau@centre.com");
        comptableUser.setActif(true);
        comptableUser.setPersonnel(comptable);
        utilisateurRepository.save(comptableUser);

        System.out.println("✅ " + utilisateurRepository.count() + " utilisateurs créés");
    } else {
        System.out.println("ℹ️ Les utilisateurs existent déjà, aucun ajout.");
    }
}

    
    private void chargerDons() {
    if (donRepository.count() == 0) {
        System.out.println("💰 Création des dons...");
        
        // ✅ Créer une adresse pour les dons
        Adresse adresseDon1 = new Adresse();
        adresseDon1.setRue("Rue des Donateurs");
        adresseDon1.setNumero("10");
        adresseDon1.setCodePostal("1050");
        adresseDon1.setVille("Ixelles");
        adresseDon1.setPays("Belgique");
        
        Adresse adresseDon2 = new Adresse();
        adresseDon2.setRue("Avenue de la Solidarité");
        adresseDon2.setNumero("25");
        adresseDon2.setCodePostal("1000");
        adresseDon2.setVille("Bruxelles");
        adresseDon2.setPays("Belgique");
        
        Don don1 = new Don(TypeDonateur.PARTICULIER, 500.0, TypeDon.FINANCIER);
        don1.setNomDonateur("Durand");
        don1.setPrenomDonateur("Paul");
        don1.setEmail("paul.durand@email.com");
        don1.setMessage("Pour soutenir votre belle initiative");
        don1.setAdresse(adresseDon1);  // ✅ AJOUTEZ L'ADRESSE
        
        Don don2 = new Don(TypeDonateur.ENTREPRISE, 2000.0, TypeDon.FINANCIER);
        don2.setOrganisationDonateur("SARL Solidarité");
        don2.setEmail("contact@sarl-solidarite.fr");
        don2.setRecuFiscalDemande(true);
        don2.setAdresse(adresseDon2);  // ✅ AJOUTEZ L'ADRESSE
        
        donRepository.saveAll(List.of(don1, don2));
        
        System.out.println("✅ " + donRepository.count() + " dons créés");
    }
}
    
    private void afficherStatistiques() {
        System.out.println("\n📊 === STATISTIQUES FINALES ===");
        System.out.println("🏠 Chambres: " + chambreRepository.count());
        System.out.println("👴 Résidents: " + residentRepository.count());
        System.out.println("📞 Contacts urgence: " + contactUrgenceRepository.count());
        System.out.println("🏥 Dossiers médicaux: " + dossierMedicalRepository.count());
        System.out.println("📝 Contrats: " + contratRepository.count());
        System.out.println("🔧 Équipements: " + equipementRepository.count());
        System.out.println("👥 Personnel: " + personnelRepository.count());
        System.out.println("🔐 Utilisateurs: " + utilisateurRepository.count());
        System.out.println("💰 Dons: " + donRepository.count());
        
        System.out.println("\n🔐 COMPTES DE TEST DISPONIBLES:");
        System.out.println(" - Administrateur: admin / admin123");
        System.out.println(" - Directeur: directeur / directeur123");
        System.out.println(" - Secrétaire: secretaire / secretaire123");
        System.out.println(" - Infirmier: infirmier / infirmier123");
        System.out.println(" - Aide-soignant: aidesoignant / aide123");
        System.out.println(" - Éducateur: educateur / educateur123");
        System.out.println(" - Comptable: comptable / comptable123");
    }
}