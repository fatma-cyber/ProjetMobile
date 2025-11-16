package com.example.smartcanteen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcanteen.database.DatabaseHelper;
import com.example.smartcanteen.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextNom, editTextPrenom, editTextNumeroEtudiant,
            editTextEmail, editTextPassword, editTextConfirmPassword;
    private RadioGroup radioGroupRole;
    private Button buttonRegister;
    private TextView textViewLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialiser la base de données
        databaseHelper = new DatabaseHelper(this);

        // Lier les vues
        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextNumeroEtudiant = findViewById(R.id.editTextNumeroEtudiant);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        // Événement du bouton S'inscrire
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Événement pour aller vers la page de connexion
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // pour ne pas revenir à l'inscription avec "back"
            }
        });

    }

    private void registerUser() {
        // Récupérer les valeurs
        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();
        String numeroEtudiant = editTextNumeroEtudiant.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Récupérer le rôle sélectionné
        int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();
        String role = "etudiant"; // Par défaut

        if (selectedRoleId == R.id.radioPersonnel) {
            role = "personnel";
        } else if (selectedRoleId == R.id.radioEtudiant) {
            role = "etudiant";
        }

        // Validations
        if (nom.isEmpty()) {
            editTextNom.setError("Le nom est requis");
            editTextNom.requestFocus();
            return;
        }

        if (prenom.isEmpty()) {
            editTextPrenom.setError("Le prénom est requis");
            editTextPrenom.requestFocus();
            return;
        }

        if (numeroEtudiant.isEmpty()) {
            editTextNumeroEtudiant.setError("Le numéro étudiant est requis");
            editTextNumeroEtudiant.requestFocus();
            return;
        }

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

        if (password.length() < 6) {
            editTextPassword.setError("Le mot de passe doit contenir au moins 6 caractères");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Les mots de passe ne correspondent pas");
            editTextConfirmPassword.requestFocus();
            return;
        }

        // Vérifier si le numéro étudiant existe déjà
        if (databaseHelper.checkNumeroEtudiantExists(numeroEtudiant)) {
            editTextNumeroEtudiant.setError("Ce numéro étudiant existe déjà");
            editTextNumeroEtudiant.requestFocus();
            return;
        }

        // Vérifier si l'email existe déjà
        if (databaseHelper.checkEmailExists(email)) {
            editTextEmail.setError("Cet email existe déjà");
            editTextEmail.requestFocus();
            return;
        }

        // Créer un nouvel utilisateur avec le rôle
        User newUser = new User(nom, prenom, numeroEtudiant, email, password, role);

        // Ajouter à la base de données
        boolean success = databaseHelper.addUser(newUser);

        if (success) {
            String roleText = role.equals("personnel") ? "Personnel" : "Étudiant";
            Toast.makeText(this, "✅ Compte " + roleText + " créé avec succès !", Toast.LENGTH_LONG).show();

            // Log pour vérification (optionnel)
            android.util.Log.d("REGISTER", "Utilisateur créé : " + nom + " " + prenom + " (" + roleText + ")");

            // Vider les champs
            clearFields();

            // TODO: Rediriger vers LoginActivity après inscription

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "❌ Erreur lors de l'inscription. Réessayez.", Toast.LENGTH_LONG).show();
        }
    }

    private void clearFields() {
        editTextNom.setText("");
        editTextPrenom.setText("");
        editTextNumeroEtudiant.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");
        radioGroupRole.check(R.id.radioEtudiant); // Remettre sur Étudiant par défaut
    }
}