package com.example.smartcanteen.adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcanteen.R;
import com.example.smartcanteen.models.Reservation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationPersonnelAdapter extends RecyclerView.Adapter<ReservationPersonnelAdapter.ViewHolder> {

    private List<Reservation> reservations;

    public ReservationPersonnelAdapter(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    // Listener pour les actions valider/refuser
    public interface OnReservationActionListener {
        void onValidate(Reservation reservation);
        void onRefuse(Reservation reservation);
    }

    private OnReservationActionListener listener;

    public void setOnReservationActionListener(OnReservationActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_personnel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation r = reservations.get(position);

        holder.txtNomMenu.setText(r.getNomPlat());
        holder.txtEtudiant.setText(r.getNomEtudiant() + " " + r.getPrenomEtudiant() + " (" + r.getNumeroEtudiant() + ")");
        holder.txtDate.setText(formatDate(r.getDateReservation()));
        holder.txtStatut.setText(r.getStatut());

        int color;
        switch (r.getStatut()) {
            case "en_attente": color = holder.itemView.getResources().getColor(R.color.orange_500); break;
            case "validee": color = holder.itemView.getResources().getColor(R.color.green_500); break;
            case "refusee": color = holder.itemView.getResources().getColor(R.color.red_500); break;
            default: color = holder.itemView.getResources().getColor(R.color.gray);
        }
        holder.txtStatut.setBackgroundTintList(ColorStateList.valueOf(color));

        // Animation au toucher
        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.97f).scaleY(0.97f).alpha(0.9f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(100).start();
                    break;
            }
            return false;
        });

        // Actions des boutons
        holder.btnValider.setOnClickListener(v -> {
            if (listener != null) listener.onValidate(r);
        });

        holder.btnRefuser.setOnClickListener(v -> {
            if (listener != null) listener.onRefuse(r);
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNomMenu, txtEtudiant, txtDate, txtStatut;
        Button btnValider, btnRefuser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNomMenu = itemView.findViewById(R.id.txtNomMenu);
            txtEtudiant = itemView.findViewById(R.id.txtEtudiant);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatut = itemView.findViewById(R.id.txtStatut);
            btnValider = itemView.findViewById(R.id.btnValider);
            btnRefuser = itemView.findViewById(R.id.btnRefuser);
        }
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateStr;
        }
    }
}
