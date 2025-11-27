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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.adapters.MenuEtudiantAdapter;
import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.Menu;

import java.util.List;

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
        buttonMesReservations = findViewById(R.id.buttonMesReservations);
        buttonMenu = findViewById(R.id.buttonMenu);
        CardView cardVoirMenu = findViewById(R.id.cardVoirMenu);  // ✅ AJOUT

        textViewWelcome.setText("Bonjour, " + userName + " !");

        // ✅ CLIC SUR LA CARTE "VOIR LE MENU"
        cardVoirMenu.setOnClickListener(v -> {
            Intent intent = new Intent(EtudiantDashboardActivity.this, MenuCompletActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Bouton Mes Réservations
        buttonMesReservations.setOnClickListener(v -> {
            Intent intent = new Intent(EtudiantDashboardActivity.this, MesReservationsActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        });

        // Menu dropdown
        buttonMenu.setOnClickListener(v -> showPopupMenu(v));
    }

    /**
     * 📌 Charge les menus disponibles depuis la base de données
     */
    private void loadMenus() {
        List<Menu> menus = databaseHelper.getAllMenus(); // Récupère UNIQUEMENT les menus disponibles

        if (menus.isEmpty()) {
            Toast.makeText(this, "Aucun menu disponible pour le moment", Toast.LENGTH_SHORT).show();
        } else {
            MenuEtudiantAdapter adapter = new MenuEtudiantAdapter(menus, userId, databaseHelper);
            recyclerViewMenus.setAdapter(adapter);
            Toast.makeText(this, menus.size() + " plat(s) disponible(s)", Toast.LENGTH_SHORT).show();
        }
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
                    // ✅ NAVIGATION VERS MesReservationsActivity
                    Intent intent = new Intent(EtudiantDashboardActivity.this, MesReservationsActivity.class);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("USER_NAME", userName);
                    startActivity(intent);
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