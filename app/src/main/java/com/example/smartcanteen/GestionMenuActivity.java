package com.example.smartcanteen;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.adapters.MenuAdapter;
import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.Menu;

import java.util.List;

public class GestionMenuActivity extends AppCompatActivity implements MenuAdapter.OnMenuItemClickListener {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private DatabaseHelper dbHelper;
    private Button buttonAddMenu;
    private Button buttonRetourDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_menu);

        recyclerView = findViewById(R.id.recyclerViewMenus);
        buttonAddMenu = findViewById(R.id.buttonAddMenu);
        buttonRetourDashboard = findViewById(R.id.buttonRetourDashboard);

        dbHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Charger les plats
        loadMenus();

        // Bouton ajouter un plat
        buttonAddMenu.setOnClickListener(v -> showAddMenuDialog());

        // Bouton retour
        buttonRetourDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(GestionMenuActivity.this, DashboardPersonnelActivity.class);
            startActivity(intent);
        });
    }

    private void loadMenus() {
        List<Menu> menus = dbHelper.getAllMenusForPersonnel();
        menuAdapter = new MenuAdapter(menus, this);
        recyclerView.setAdapter(menuAdapter);
    }

    // ------------------------------------------------------------
    //  DIALOG : AJOUTER UN PLAT
    // ------------------------------------------------------------
    private void showAddMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_menu, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Champs du formulaire
        EditText editName = dialogView.findViewById(R.id.editMenuName);
        EditText editDescription = dialogView.findViewById(R.id.editMenuDescription);
        EditText editPrice = dialogView.findViewById(R.id.editMenuPrice);

        // Boutons
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        // Titre (CORRECT : TextView)
        TextView title = dialogView.findViewById(R.id.dialogTitle);
        title.setText("Ajouter un plat");

        btnConfirm.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String description = editDescription.getText().toString().trim();

            double price;
            try {
                price = Double.parseDouble(editPrice.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Prix invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean result = dbHelper.addMenu(name, description, price);

            if (result) {
                Toast.makeText(this, "Plat ajouté", Toast.LENGTH_SHORT).show();
                loadMenus();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // ------------------------------------------------------------
    //  DIALOG : MODIFIER UN PLAT
    // ------------------------------------------------------------
    @Override
    public void onEditMenu(Menu menu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_menu, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText editName = dialogView.findViewById(R.id.editMenuName);
        EditText editDescription = dialogView.findViewById(R.id.editMenuDescription);
        EditText editPrice = dialogView.findViewById(R.id.editMenuPrice);

        // Pré-remplissage
        editName.setText(menu.getNomPlat());
        editDescription.setText(menu.getDescription());
        editPrice.setText(String.valueOf(menu.getPrix()));

        // Boutons
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        // Titre (CORRECT : TextView)
        TextView title = dialogView.findViewById(R.id.dialogTitle);
        title.setText("Modifier le plat");

        btnConfirm.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String description = editDescription.getText().toString().trim();

            double price;
            try {
                price = Double.parseDouble(editPrice.getText().toString().trim());
            } catch (Exception e) {
                Toast.makeText(this, "Prix invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean result = dbHelper.updateMenu(menu.getId(), name, description, price);

            if (result) {
                Toast.makeText(this, "Plat modifié", Toast.LENGTH_SHORT).show();
                loadMenus();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // ------------------------------------------------------------
    //  SUPPRIMER UN PLAT
    // ------------------------------------------------------------

    @Override
    public void onDeleteMenu(Menu menu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_delete_menu, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button btnCancel = dialogView.findViewById(R.id.btnCancelDelete);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirmDelete);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            boolean result = dbHelper.deleteMenu(menu.getId());
            if (result) {
                Toast.makeText(this, "Plat supprimé", Toast.LENGTH_SHORT).show();
                loadMenus();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    // ------------------------------------------------------------
    //  DISPONIBILITÉ
    // ------------------------------------------------------------
    @Override
    public void onToggleAvailability(Menu menu, boolean disponible) {
        boolean result = dbHelper.setMenuAvailability(menu.getId(), disponible);

        if (result) {
            Toast.makeText(this, "Disponibilité mise à jour", Toast.LENGTH_SHORT).show();
            loadMenus();
        } else {
            Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
        }
    }
}
