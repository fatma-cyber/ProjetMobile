package com.example.smartcanteen.models;

public class Avis {
    private int id;
    private int userId;
    private int menuId;
    private int note;
    private String commentaire;
    private String dateAvis;

    // Constructeurs
    public Avis() {}
    public Avis(int userId, int menuId, int note, String commentaire) {
        this.userId = userId;
        this.menuId = menuId;
        this.note = note;
        this.commentaire = commentaire;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }
    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public String getDateAvis() { return dateAvis; }
    public void setDateAvis(String dateAvis) { this.dateAvis = dateAvis; }
}


