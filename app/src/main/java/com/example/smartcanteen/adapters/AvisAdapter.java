package com.example.smartcanteen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.R;
import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.Avis;

import java.util.List;

public class AvisAdapter extends RecyclerView.Adapter<AvisAdapter.AvisViewHolder> {

    private List<Avis> avisList;
    private DatabaseHelper dbHelper;

    public AvisAdapter(List<Avis> avisList, DatabaseHelper dbHelper) {
        this.avisList = avisList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public AvisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avis, parent, false);
        return new AvisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvisViewHolder holder, int position) {
        Avis avis = avisList.get(position);

        holder.txtNote.setText("Note : " + avis.getNote());
        holder.txtCommentaire.setText("Commentaire : " + avis.getCommentaire());
        holder.txtDate.setText(avis.getDateAvis());

        // Bouton Modifier
        holder.btnEdit.setOnClickListener(v -> {
            // Ici tu peux ajouter la logique pour modifier l'avis (dialog ou nouvelle activity)
            Toast.makeText(v.getContext(), "Modifier l'avis " + avis.getId(), Toast.LENGTH_SHORT).show();
        });

        // Bouton Supprimer
        holder.btnDelete.setOnClickListener(v -> {
            boolean success = dbHelper.deleteAvis(avis.getId());
            if (success) {
                avisList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(v.getContext(), "Avis supprimé", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return avisList.size();
    }

    static class AvisViewHolder extends RecyclerView.ViewHolder {
        TextView txtNote, txtCommentaire, txtDate;
        Button btnEdit, btnDelete;

        public AvisViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNote = itemView.findViewById(R.id.tvNote);
            txtCommentaire = itemView.findViewById(R.id.tvCommentaire);
            txtDate = itemView.findViewById(R.id.tvDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
