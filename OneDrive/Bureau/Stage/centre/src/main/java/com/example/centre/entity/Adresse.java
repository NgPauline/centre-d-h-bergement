/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.centre.entity;


import jakarta.persistence.*;

@Embeddable
public class Adresse {
    
    @Column(nullable = false)
    private String rue;
    
    private String numero;
    
    @Column(nullable = false)
    private String codePostal;
    
    @Column(nullable = false)
    private String ville;
    
    private String pays = "Belgique";
    
    // Constructeurs
    public Adresse() {}
    
    public Adresse(String rue, String numero, String codePostal, String ville) {
        this.rue = rue;
        this.numero = numero;
        this.codePostal = codePostal;
        this.ville = ville;
    }
    
    // Getters et Setters id; }
    
    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    
      // Méthodes utilitaires
    public String getAdresseComplete() {
        return rue + " " + numero + ", " + codePostal + " " + ville + (pays != null ? ", " + pays : "");
    }
}