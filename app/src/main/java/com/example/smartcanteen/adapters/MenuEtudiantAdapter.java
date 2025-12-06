package com.example.smartcanteen.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.AvisActivity;
import com.example.smartcanteen.R;
import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.Menu;

import java.util.List;

public class MenuEtudiantAdapter extends RecyclerView.Adapter<MenuEtudiantAdapter.MenuViewHolder> {

    private List<Menu> menus;
    private int userId;
    private DatabaseHelper dbHelper;

    public MenuEtudiantAdapter(List<Menu> menus, int userId, DatabaseHelper dbHelper) {
        this.menus = menus;
        this.userId = userId;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_etudiant, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menus.get(position);

        holder.txtName.setText(menu.getNomPlat());
        holder.txtDesc.setText(menu.getDescription());
        holder.txtPrice.setText(menu.getPrix() + " DT");

        // Bouton Réserver
        holder.btnReserver.setOnClickListener(v -> {
            boolean success = dbHelper.addReservation(userId, menu.getId());
            if (success) {
                Toast.makeText(v.getContext(), "✅ Réservation effectuée pour " + menu.getNomPlat(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "❌ Erreur lors de la réservation", Toast.LENGTH_SHORT).show();
            }
        });
        // ✅ BOUTON VOIR LES AVIS
        holder.btnVoirAvis.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AvisActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("MENU_ID", menu.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDesc, txtPrice;
        Button btnReserver, btnVoirAvis; // ✅ AJOUT

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtMenuName);
            txtDesc = itemView.findViewById(R.id.txtMenuDescription);
            txtPrice = itemView.findViewById(R.id.txtMenuPrice);
            btnReserver = itemView.findViewById(R.id.btnReserver);
            btnVoirAvis = itemView.findViewById(R.id.btnVoirAvis); // ✅ AJOUT
        }
    }
}