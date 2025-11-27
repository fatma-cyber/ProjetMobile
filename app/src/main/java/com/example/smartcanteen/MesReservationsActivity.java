package com.example.smartcanteen;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.adapters.ReservationEtudiantAdapter;
import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.Reservation;

import java.util.List;

public class MesReservationsActivity extends AppCompatActivity
        implements ReservationEtudiantAdapter.OnReservationClickListener {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private int userId;
    private Button buttonRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_reservations);

        // Initialiser la base de données
        dbHelper = new DatabaseHelper(this);

        // Récupérer l'ID utilisateur depuis l'Intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Lier les vues
        recyclerView = findViewById(R.id.recyclerViewReservations);
        buttonRetour = findViewById(R.id.buttonRetour);

        // Configurer le RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Charger les réservations
        loadReservations();

        // Bouton retour
        buttonRetour.setOnClickListener(v -> finish());
    }

    /**
     * Charge les réservations de l'étudiant
     */
    private void loadReservations() {
        List<Reservation> reservations = dbHelper.getReservationsByUser(userId);

        if (reservations.isEmpty()) {
            Toast.makeText(this, "Vous n'avez aucune réservation", Toast.LENGTH_SHORT).show();
        } else {
            ReservationEtudiantAdapter adapter = new ReservationEtudiantAdapter(reservations, this);
            recyclerView.setAdapter(adapter);
            Toast.makeText(this, reservations.size() + " réservation(s)", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Interface callback : Annuler une réservation
     */
    @Override
    public void onCancelReservation(Reservation reservation) {
        boolean success = dbHelper.cancelReservation(reservation.getId());
        if (success) {
            Toast.makeText(this, "✅ Réservation annulée", Toast.LENGTH_SHORT).show();
            loadReservations(); // Recharger la liste
        } else {
            Toast.makeText(this, "❌ Erreur lors de l'annulation", Toast.LENGTH_SHORT).show();
        }
    }
}