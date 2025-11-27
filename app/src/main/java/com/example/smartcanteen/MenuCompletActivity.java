package com.example.smartcanteen;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.adapters.MenuEtudiantAdapter;
import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.Menu;

import java.util.List;

public class MenuCompletActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMenus;
    private Button buttonRetour;
    private DatabaseHelper databaseHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_complet);

        databaseHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        recyclerViewMenus = findViewById(R.id.recyclerViewMenus);
        buttonRetour = findViewById(R.id.buttonRetour);

        recyclerViewMenus.setLayoutManager(new LinearLayoutManager(this));

        // Charger les menus
        loadMenus();

        // Bouton retour
        buttonRetour.setOnClickListener(v -> finish());
    }

    private void loadMenus() {
        List<Menu> menus = databaseHelper.getAllMenus();

        if (menus.isEmpty()) {
            Toast.makeText(this, "Aucun menu disponible", Toast.LENGTH_SHORT).show();
        } else {
            MenuEtudiantAdapter adapter = new MenuEtudiantAdapter(menus, userId, databaseHelper);
            recyclerViewMenus.setAdapter(adapter);
            Toast.makeText(this, menus.size() + " plat(s) disponible(s)", Toast.LENGTH_SHORT).show();
        }
    }
}