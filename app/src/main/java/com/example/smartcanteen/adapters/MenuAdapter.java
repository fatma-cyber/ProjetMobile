package com.example.smartcanteen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.R;
import com.example.smartcanteen.models.Menu;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<Menu> menus;
    private OnMenuItemClickListener listener;

    public MenuAdapter(List<Menu> menus) {
    }

    public interface OnMenuItemClickListener {
        void onEditMenu(Menu menu);
        void onDeleteMenu(Menu menu);
        void onToggleAvailability(Menu menu, boolean disponible);
    }

    public MenuAdapter(List<Menu> menus, OnMenuItemClickListener listener) {
        this.menus = menus;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_personnel, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menus.get(position);
        holder.txtName.setText(menu.getNomPlat());
        holder.txtDesc.setText(menu.getDescription());
        holder.txtPrice.setText(menu.getPrix() + " DT");
        holder.switchAvailable.setChecked(menu.isDisponible());

        holder.btnEdit.setOnClickListener(v -> listener.onEditMenu(menu));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteMenu(menu));
        holder.switchAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onToggleAvailability(menu, isChecked));
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDesc, txtPrice;
        Button btnEdit, btnDelete;
        Switch switchAvailable;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtMenuName);
            txtDesc = itemView.findViewById(R.id.txtMenuDescription);
            txtPrice = itemView.findViewById(R.id.txtMenuPrice);
            btnEdit = itemView.findViewById(R.id.btnEditMenu);
            btnDelete = itemView.findViewById(R.id.btnDeleteMenu);
            switchAvailable = itemView.findViewById(R.id.switchAvailable);
        }
    }
}
