package com.example.smartcanteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.database.DatabaseHelper;

public class EtudiantDashboardActivity extends AppCompatActivity {

    private TextView textViewWelcome;
    private RecyclerView recyclerViewMenus;
    private Button buttonMesReservations, buttonDeconnexion;
    private DatabaseHelper databaseHelper;

    private String userName; // Nom de l'utilisateur connecté
    private int userId; // ID de l'utilisateur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant_dashboard);

        databaseHelper = new DatabaseHelper(this);

        // Récupérer les données passées depuis LoginActivity
        userName = getIntent().getStringExtra("USER_NAME");
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Lier les vues
        textViewWelcome = findViewById(R.id.textViewWelcome);
        recyclerViewMenus = findViewById(R.id.recyclerViewMenus);
        buttonMesReservations = findViewById(R.id.buttonMesReservations);
        buttonDeconnexion = findViewById(R.id.buttonDeconnexion);

        // Afficher le message de bienvenue
        textViewWelcome.setText("Bonjour, " + userName + " !");

        // Configurer RecyclerView (à faire dans l'étape suivante)
        recyclerViewMenus.setLayoutManager(new LinearLayoutManager(this));

        // TODO: Charger les menus (étape suivante)
        Toast.makeText(this, "Chargement des menus...", Toast.LENGTH_SHORT).show();

        // Bouton Mes Réservations
        buttonMesReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EtudiantDashboardActivity.this, "Mes réservations (à implémenter)", Toast.LENGTH_SHORT).show();
            }
        });

        // Bouton Déconnexion
        buttonDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retour à l'écran de connexion
                Intent intent = new Intent(EtudiantDashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}