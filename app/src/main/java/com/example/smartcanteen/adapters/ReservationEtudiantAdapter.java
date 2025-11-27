package com.example.smartcanteen.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.R;
import com.example.smartcanteen.models.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationEtudiantAdapter extends RecyclerView.Adapter<ReservationEtudiantAdapter.ReservationViewHolder> {

    private List<Reservation> reservations;
    private OnReservationClickListener listener;

    // Interface pour communiquer avec l'Activity
    public interface OnReservationClickListener {
        void onCancelReservation(Reservation reservation);
    }

    public ReservationEtudiantAdapter(List<Reservation> reservations, OnReservationClickListener listener) {
        this.reservations = reservations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_etudiant, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);

        // Afficher les infos
        holder.txtNomPlat.setText(reservation.getNomPlat());
        holder.txtDescription.setText(reservation.getDescription());
        holder.txtPrix.setText(reservation.getPrix() + " DT");
        holder.txtDate.setText(formatDate(reservation.getDateReservation()));

        // Afficher le statut avec couleur
        String statut = reservation.getStatut();
        holder.txtStatut.setText(getStatutTexte(statut));
        holder.txtStatut.setTextColor(getStatutCouleur(statut, holder.itemView));

        // Bouton Annuler (visible uniquement si statut = en_attente)
        if (statut.equals("en_attente")) {
            holder.btnAnnuler.setVisibility(View.VISIBLE);
            holder.btnAnnuler.setOnClickListener(v -> listener.onCancelReservation(reservation));
        } else {
            holder.btnAnnuler.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    /**
     * Formater la date (de "2025-11-26 10:30:00" à "26/11/2025 10:30")
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateStr; // Retourner la date brute si erreur
        }
    }

    /**
     * Convertir le statut en texte lisible
     */
    private String getStatutTexte(String statut) {
        switch (statut) {
            case "en_attente": return "⏳ En attente";
            case "validee": return "✅ Validée";
            case "refusee": return "❌ Refusée";
            case "servie": return "🍽️ Servie";
            default: return statut;
        }
    }

    /**
     * Couleur selon le statut
     */
    private int getStatutCouleur(String statut, View view) {
        switch (statut) {
            case "en_attente": return view.getContext().getResources().getColor(android.R.color.holo_orange_dark);
            case "validee": return view.getContext().getResources().getColor(android.R.color.holo_green_dark);
            case "refusee": return view.getContext().getResources().getColor(android.R.color.holo_red_dark);
            case "servie": return view.getContext().getResources().getColor(android.R.color.holo_blue_dark);
            default: return view.getContext().getResources().getColor(android.R.color.black);
        }
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView txtNomPlat, txtDescription, txtPrix, txtDate, txtStatut;
        Button btnAnnuler;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNomPlat = itemView.findViewById(R.id.txtNomPlat);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrix = itemView.findViewById(R.id.txtPrix);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatut = itemView.findViewById(R.id.txtStatut);
            btnAnnuler = itemView.findViewById(R.id.btnAnnuler);
        }
    }
}