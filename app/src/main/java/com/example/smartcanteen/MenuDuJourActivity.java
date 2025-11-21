package com.example.smartcanteen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.adapters.MenuAdapter;
import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.Menu;
import java.util.List;

public class MenuDuJourActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMenuDuJour;
    private DatabaseHelper databaseHelper;
    private Button buttonRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_du_jour);

        databaseHelper = new DatabaseHelper(this);

        recyclerViewMenuDuJour = findViewById(R.id.recyclerViewMenuDuJour);
        recyclerViewMenuDuJour.setLayoutManager(new LinearLayoutManager(this));

        buttonRetour = findViewById(R.id.buttonRetour);
        buttonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // retourne au dashboard étudiant
            }
        });

        loadMenuDuJour();
    }
    private void loadMenuDuJour() {
        List<Menu> menus = databaseHelper.getAllMenus(); // Récupérer tous les menus disponibles
        MenuAdapter adapter = new MenuAdapter(menus); // adaptateur simple
        recyclerViewMenuDuJour.setAdapter(adapter);
    }
}


