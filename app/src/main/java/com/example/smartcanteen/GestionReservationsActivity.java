package com.example.smartcanteen;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.adapters.ReservationPersonnelAdapter;
import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.Reservation;

import java.util.List;

public class GestionReservationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_reservations);

        // Initialisation du RecyclerView
        recyclerView = findViewById(R.id.recyclerViewReservationsPersonnel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de la base de données
        dbHelper = new DatabaseHelper(this);

        // Charger les réservations depuis la base
        loadReservations();

        // Bouton retour vers Dashboard Personnel
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish()); // Ferme l'activité et revient


    }

    /**
     * Charge toutes les réservations et les affiche dans le RecyclerView
     */
    private void loadReservations() {
        List<Reservation> reservations = dbHelper.getAllReservations();

        if (reservations.isEmpty()) {
            Toast.makeText(this, "Aucune réservation pour le moment", Toast.LENGTH_SHORT).show();
        } else {
            ReservationPersonnelAdapter adapter = new ReservationPersonnelAdapter(reservations);
            recyclerView.setAdapter(adapter);
        }
    }
}
