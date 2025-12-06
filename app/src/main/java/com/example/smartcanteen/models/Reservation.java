package com.example.smartcanteen.models;

public class Reservation {
    private int id;
    private int userId;
    private int menuId;
    private String dateReservation;
    private String statut;  // "en_attente", "validee", "refusee"

    // Infos supplémentaires du menu (via JOIN)
    private String nomPlat;
    private String description;
    private double prix;
    private String nomEtudiant;
    private String prenomEtudiant;
    private String numeroEtudiant;

    // Getters et setters
    public String getNomEtudiant() { return nomEtudiant; }
    public void setNomEtudiant(String nomEtudiant) { this.nomEtudiant = nomEtudiant; }

    public String getPrenomEtudiant() { return prenomEtudiant; }
    public void setPrenomEtudiant(String prenomEtudiant) { this.prenomEtudiant = prenomEtudiant; }

    public String getNumeroEtudiant() { return numeroEtudiant; }
    public void setNumeroEtudiant(String numeroEtudiant) { this.numeroEtudiant = numeroEtudiant; }


    // ════════════════════════════════════════════════════════════════
    // CONSTRUCTEUR complet
    // ════════════════════════════════════════════════════════════════
    public Reservation(int id, int userId, int menuId, String dateReservation,
                       String statut, String nomPlat, String description, double prix) {
        this.id = id;
        this.userId = userId;
        this.menuId = menuId;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.nomPlat = nomPlat;
        this.description = description;
        this.prix = prix;
    }

    // ════════════════════════════════════════════════════════════════
    // GETTERS & SETTERS
    // ════════════════════════════════════════════════════════════════
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public String getDateReservation() { return dateReservation; }
    public void setDateReservation(String dateReservation) { this.dateReservation = dateReservation; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getNomPlat() { return nomPlat; }
    public void setNomPlat(String nomPlat) { this.nomPlat = nomPlat; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
}