package com.example.smartcanteen;

import android.os.Bundle;
import android.widget.Button;
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

        recyclerView = findViewById(R.id.recyclerViewReservationsPersonnel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        loadReservations();

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadReservations() {
        List<Reservation> reservations = dbHelper.getAllReservations();

        if (reservations.isEmpty()) {
            Toast.makeText(this, "Aucune réservation pour le moment", Toast.LENGTH_SHORT).show();
        } else {
            ReservationPersonnelAdapter adapter = new ReservationPersonnelAdapter(reservations);
            recyclerView.setAdapter(adapter);

            adapter.setOnReservationActionListener(new ReservationPersonnelAdapter.OnReservationActionListener() {
                @Override
                public void onValidate(Reservation reservation) {
                    dbHelper.updateReservationStatus(reservation.getId(), "validee");
                    loadReservations();
                    Toast.makeText(GestionReservationsActivity.this, "Réservation validée", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRefuse(Reservation reservation) {
                    dbHelper.updateReservationStatus(reservation.getId(), "refusee");
                    loadReservations();
                    Toast.makeText(GestionReservationsActivity.this, "Réservation refusée", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
