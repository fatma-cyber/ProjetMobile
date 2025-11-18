package com.example.smartcanteen.models;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String numeroEtudiant;
    private String email;
    private String motDePasse;
    private String role;

    // Constructeur complet (avec ID)
    public User(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroEtudiant = numeroEtudiant;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // --- Constructeur utilisé à l'inscription ---
    public User(String nom, String prenom, String numeroEtudiant, String email, String motDePasse, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroEtudiant = numeroEtudiant;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // --- Constructeur utilisé au login (sans mot de passe) ---
    public User(int id, String nom, String prenom, String numeroEtudiant, String email, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroEtudiant = numeroEtudiant;
        this.email = email;
        this.role = role;
    }
    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}