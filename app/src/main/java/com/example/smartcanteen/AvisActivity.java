package com.example.smartcanteen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smartcanteen.database.DatabaseHelper;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.adapters.AvisAdapter;
import com.example.smartcanteen.models.Avis;

import java.util.List;

public class AvisActivity extends AppCompatActivity {

    private RecyclerView recyclerAvis;
    private Button btnAddAvis;
    private DatabaseHelper db;
    private AvisAdapter adapter;
    private List<Avis> avisList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avis);

        db = new DatabaseHelper(this);
        recyclerAvis = findViewById(R.id.recyclerAvis);
        btnAddAvis = findViewById(R.id.btnAddAvis);

        avisList = db.getAllAvis();
        adapter = new AvisAdapter(avisList, db);

        recyclerAvis.setLayoutManager(new LinearLayoutManager(this));
        recyclerAvis.setAdapter(adapter);

        btnAddAvis.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter Avis");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_avis, null);
        EditText etNote = view.findViewById(R.id.etNote);
        EditText etCommentaire = view.findViewById(R.id.etCommentaire);

        builder.setView(view);
        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            int note = Integer.parseInt(etNote.getText().toString());
            String commentaire = etCommentaire.getText().toString();

            db.addAvis(1, 1, note, commentaire); // userId et menuId par défaut
            avisList.clear();
            avisList.addAll(db.getAllAvis());
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Avis ajouté", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }
}
