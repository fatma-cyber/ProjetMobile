package com.example.smartcanteen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialiser la base de données
        databaseHelper = new DatabaseHelper(this);

        // Lier les vues
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        // Événement du bouton Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Aller vers la page d'inscription
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // ===== Validations =====
        if (email.isEmpty()) {
            editTextEmail.setError("L'email est requis");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email invalide");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Le mot de passe est requis");
            editTextPassword.requestFocus();
            return;
        }

        // ===== Récupérer l'utilisateur depuis SQLite =====
        User user = databaseHelper.getUserByEmailAndPassword(email, password);

        if (user != null) {
            Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

            // Sauvegarder la session
            saveUserSession(user);

            // ===== Redirection selon le rôle =====
            String role = user.getRole();
            Intent intent;

            if ("personnel".equalsIgnoreCase(role) || "admin".equalsIgnoreCase(role)) {
                intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, HomeStudentActivity.class);
            }

            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    // ===== Sauvegarde de session dans SharedPreferences =====
    private void saveUserSession(User user) {
        android.content.SharedPreferences prefs =
                getSharedPreferences("smart_canteen_prefs", MODE_PRIVATE);

        android.content.SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("user_id", user.getId());
        editor.putString("user_role", user.getRole());
        editor.putString("user_nom", user.getNom());
        editor.putString("user_prenom", user.getPrenom());
        editor.putString("user_email", user.getEmail());
        editor.putBoolean("is_logged_in", true);

        editor.apply();
    }

}
