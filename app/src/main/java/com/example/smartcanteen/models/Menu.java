package com.example.smartcanteen.models;

public class Menu {
    private int id;
    private String nomPlat;
    private String description;
    private double prix;
    private boolean disponible;
    private String dateAjout;

    // Constructeur complet
    public Menu(int id, String nomPlat, String description, double prix, boolean disponible, String dateAjout) {
        this.id = id;
        this.nomPlat = nomPlat;
        this.description = description;
        this.prix = prix;
        this.disponible = disponible;
        this.dateAjout = dateAjout;
    }

    // Constructeur sans ID
    public Menu(int id, String nomPlat, String description, double prix) {
        this.nomPlat = nomPlat;
        this.description = description;
        this.prix = prix;
        this.disponible = true;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomPlat() { return nomPlat; }
    public void setNomPlat(String nomPlat) { this.nomPlat = nomPlat; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getDateAjout() { return dateAjout; }
    public void setDateAjout(String dateAjout) { this.dateAjout = dateAjout; }
}