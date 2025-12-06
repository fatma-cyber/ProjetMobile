package com.example.smartcanteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardPersonnelActivity extends AppCompatActivity {

    private TextView txtWelcome;
    private LinearLayout cardGestionMenu, cardReservations, cardAvis;
    private ImageButton buttonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_personnel);

        // Initialisation des vues
        txtWelcome = findViewById(R.id.txtWelcome);
        cardGestionMenu = findViewById(R.id.cardGestionMenu);
        cardReservations = findViewById(R.id.cardReservations);
        cardAvis = findViewById(R.id.cardAvis);
        buttonMenu = findViewById(R.id.buttonMenu);

        // Récupération du nom utilisateur depuis l'Intent
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null) {
            txtWelcome.setText("Bienvenue " + userName + " !");
        }

        // Redirection vers Gestion Menu
        cardGestionMenu.setOnClickListener(v ->
                startActivity(new Intent(this, GestionMenuActivity.class))
        );

        // TODO: redirection vers Gestion Réservations
        cardReservations.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardPersonnelActivity.this, GestionReservationsActivity.class);
            startActivity(intent);
        });


        // TODO: redirection vers Gestion Avis
        cardAvis.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardPersonnelActivity.this, AvisActivity.class);
            startActivity(intent);
        });

        // 🔥 Menu déroulant (3 points)
        buttonMenu.setOnClickListener(v -> showPopupMenu(v));

        // Appliquer animation dynamique aux cards
        addCardAnimation(cardGestionMenu);
        addCardAnimation(cardReservations);
        addCardAnimation(cardAvis);
    }

    /**
     * 📌 Affiche le menu déroulant
     */
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_dashboard_personnel, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_mon_profil) {
                    // TODO: Implémenter profil personnel
                    return true;
                }
                else if (id == R.id.menu_parametres) {
                    // TODO: Implémenter paramètres
                    return true;
                }
                else if (id == R.id.menu_deconnexion) {
                    deconnexion();
                    return true;
                }

                return false;
            }
        });

        popupMenu.show();
    }

    /**
     * 📌 Déconnexion
     */
    private void deconnexion() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Ajoute un effet "scale" quand on touche la card pour un rendu dynamique
     */
    private void addCardAnimation(LinearLayout card) {
        card.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(120).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(120).start();
                    break;
            }
            return false;
        });
    }
}