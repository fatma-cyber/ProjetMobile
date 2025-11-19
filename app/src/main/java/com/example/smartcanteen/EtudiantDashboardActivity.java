package com.example.smartcanteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.database.DatabaseHelper;

public class EtudiantDashboardActivity extends AppCompatActivity {

    private TextView textViewWelcome;
    private RecyclerView recyclerViewMenus;
    private Button buttonMesReservations;
    private ImageButton buttonMenu;
    private DatabaseHelper databaseHelper;

    private String userName;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant_dashboard);

        databaseHelper = new DatabaseHelper(this);

        userName = getIntent().getStringExtra("USER_NAME");
        userId = getIntent().getIntExtra("USER_ID", -1);

        textViewWelcome = findViewById(R.id.textViewWelcome);
        recyclerViewMenus = findViewById(R.id.recyclerViewMenus);
        buttonMesReservations = findViewById(R.id.buttonMesReservations);
        buttonMenu = findViewById(R.id.buttonMenu);

        textViewWelcome.setText("Bonjour, " + userName + " !");

        recyclerViewMenus.setLayoutManager(new LinearLayoutManager(this));

        Toast.makeText(this, "Chargement des menus...", Toast.LENGTH_SHORT).show();

        // Bouton Mes Réservations
        buttonMesReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EtudiantDashboardActivity.this, "Mes réservations (à implémenter)", Toast.LENGTH_SHORT).show();
            }
        });

        // Menu dropdown (3 points)
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_dashboard, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_mon_profil) {
                    Toast.makeText(EtudiantDashboardActivity.this, "Mon Profil (à implémenter)", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (id == R.id.menu_mes_reservations) {
                    Toast.makeText(EtudiantDashboardActivity.this, "Mes Réservations", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (id == R.id.menu_parametres) {
                    Toast.makeText(EtudiantDashboardActivity.this, "Paramètres (à implémenter)", Toast.LENGTH_SHORT).show();
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

    private void deconnexion() {
        Intent intent = new Intent(EtudiantDashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "👋 À bientôt " + userName + " !", Toast.LENGTH_SHORT).show();
        finish();
    }
}