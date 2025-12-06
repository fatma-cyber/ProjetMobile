package com.example.smartcanteen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.adapters.AvisAdapter;
import com.example.smartcanteen.models.Avis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AvisActivity extends AppCompatActivity {

    private RecyclerView recyclerAvis;
    private Button btnAddAvis, btnRetour;
    private DatabaseHelper db;
    private AvisAdapter adapter;
    private List<Avis> avisList;
    private int currentUserId = -1;
    private int currentMenuId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avis);

        db = new DatabaseHelper(this);
        recyclerAvis = findViewById(R.id.recyclerAvis);
        btnAddAvis = findViewById(R.id.btnAddAvis);
        btnRetour = findViewById(R.id.btnRetour);

        // ✅ RÉCUPÉRER USER_ID et MENU_ID depuis l'intent
        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        currentMenuId = getIntent().getIntExtra("MENU_ID", -1);

        // ✅ SI PERSONNEL (menuId = 0), afficher tous les avis mais désactiver ajout
        if (currentMenuId == 0) {
            btnAddAvis.setVisibility(View.GONE);
            Toast.makeText(this, "👀 Mode consultation (Personnel)", Toast.LENGTH_SHORT).show();
        }
        // ✅ SI ÉTUDIANT sans userId
        else if (currentUserId == -1) {
            btnAddAvis.setEnabled(false);
            Toast.makeText(this, "❌ Utilisateur non défini", Toast.LENGTH_LONG).show();
        }

        // ✅ Charger les avis
        avisList = db.getAllAvis();
        adapter = new AvisAdapter(avisList, db, this::showEditDialog); // ✅ Passer la fonction de modification

        recyclerAvis.setLayoutManager(new LinearLayoutManager(this));
        recyclerAvis.setAdapter(adapter);

        btnAddAvis.setOnClickListener(v -> showAddDialog());

        // ✅ BOUTON RETOUR
        btnRetour.setOnClickListener(v -> finish());
    }

    /**
     * ✅ DIALOG AJOUTER AVIS
     */
    private void showAddDialog() {
        if (currentUserId == -1) {
            Toast.makeText(this, "❌ Utilisateur non défini", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentMenuId == -1) {
            Toast.makeText(this, "❌ Menu non défini", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✍️ Ajouter un avis");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_avis, null);
        EditText etNote = view.findViewById(R.id.etNote);
        EditText etCommentaire = view.findViewById(R.id.etCommentaire);

        builder.setView(view);
        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String noteStr = etNote.getText().toString().trim();
            String commentaire = etCommentaire.getText().toString().trim();

            if (noteStr.isEmpty()) {
                Toast.makeText(this, "⚠️ Veuillez entrer une note (1-5)", Toast.LENGTH_SHORT).show();
                return;
            }

            int note;
            try {
                note = Integer.parseInt(noteStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "❌ Note invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            if (note < 1 || note > 5) {
                Toast.makeText(this, "❌ La note doit être entre 1 et 5", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = db.addAvis(currentUserId, currentMenuId, note, commentaire);
            if (ok) {
                avisList.clear();
                avisList.addAll(db.getAllAvis());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "✅ Avis ajouté avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    /**
     * ✅ DIALOG MODIFIER AVIS
     */
    public void showEditDialog(Avis avis) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✏️ Modifier l'avis");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_avis, null);
        EditText etNote = view.findViewById(R.id.etNote);
        EditText etCommentaire = view.findViewById(R.id.etCommentaire);

        // ✅ Pré-remplir avec les valeurs actuelles
        etNote.setText(String.valueOf(avis.getNote()));
        etCommentaire.setText(avis.getCommentaire());

        builder.setView(view);
        builder.setPositiveButton("Modifier", (dialog, which) -> {
            String noteStr = etNote.getText().toString().trim();
            String commentaire = etCommentaire.getText().toString().trim();

            if (noteStr.isEmpty()) {
                Toast.makeText(this, "⚠️ Veuillez entrer une note (1-5)", Toast.LENGTH_SHORT).show();
                return;
            }

            int note;
            try {
                note = Integer.parseInt(noteStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "❌ Note invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            if (note < 1 || note > 5) {
                Toast.makeText(this, "❌ La note doit être entre 1 et 5", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = db.updateAvis(avis.getId(), note, commentaire);
            if (ok) {
                avisList.clear();
                avisList.addAll(db.getAllAvis());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "✅ Avis modifié", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "❌ Erreur modification", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }
}