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

        // Validations
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

        // Vérifier dans la base de données et récupérer l'utilisateur
        User user = databaseHelper.loginUserByEmail(email, password);

        if (user != null) {
            // Connexion réussie
            Toast.makeText(this, "✅ Bienvenue " + user.getPrenom() + " !", Toast.LENGTH_LONG).show();

            // Rediriger selon le rôle
            if (user.getRole().equals("personnel")) {
                // Redirection vers le dashboard du personnel
                Intent intent = new Intent(this, DashboardPersonnelActivity.class);
                intent.putExtra("USER_NAME", user.getPrenom());
                intent.putExtra("USER_ID", user.getId());
                startActivity(intent);
            } else if (user.getRole().equals("etudiant")) {
                // Redirection vers le dashboard de l'étudiant
                Intent intent = new Intent(this, EtudiantDashboardActivity.class);
                intent.putExtra("USER_NAME", user.getPrenom());
                intent.putExtra("USER_ID", user.getId());
                startActivity(intent);
            }

            finish(); // ferme LoginActivity pour ne pas revenir en arrière
        } else {
            Toast.makeText(this, "❌ Email ou mot de passe incorrect", Toast.LENGTH_LONG).show();
            editTextPassword.setText("");
        }
    }
}
