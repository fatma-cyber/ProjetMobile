package com.example.smartcanteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardPersonnelActivity extends AppCompatActivity {

    private TextView txtWelcome;
    private LinearLayout cardGestionMenu, cardReservations, cardAvis;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_personnel);

        // Initialisation des vues
        txtWelcome = findViewById(R.id.txtWelcome);
        cardGestionMenu = findViewById(R.id.cardGestionMenu);
        cardReservations = findViewById(R.id.cardReservations);
        cardAvis = findViewById(R.id.cardAvis);
        buttonLogout = findViewById(R.id.buttonLogout);

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
            // startActivity(new Intent(this, GestionReservationsActivity.class));
        });

        // TODO: redirection vers Gestion Avis
        cardAvis.setOnClickListener(v -> {
            // startActivity(new Intent(this, GestionAvisActivity.class));
        });

        // Déconnexion
        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Appliquer animation dynamique aux cards
        addCardAnimation(cardGestionMenu);
        addCardAnimation(cardReservations);
        addCardAnimation(cardAvis);
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
