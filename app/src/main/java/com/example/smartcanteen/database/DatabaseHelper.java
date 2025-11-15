package com.example.smartcanteen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.smartcanteen.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartCanteen.db";
    private static final int DATABASE_VERSION = 2;

    // Table Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_PRENOM = "prenom";
    private static final String COLUMN_NUMERO_ETUDIANT = "numero_etudiant";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "mot_de_passe";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_DATE_CREATION = "date_creation";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOM + " TEXT NOT NULL,"
                + COLUMN_PRENOM + " TEXT NOT NULL,"
                + COLUMN_NUMERO_ETUDIANT + " TEXT UNIQUE NOT NULL,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_ROLE + " TEXT NOT NULL,"
                + COLUMN_DATE_CREATION + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
        Log.d("DATABASE", "Table users créée avec succès");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DATABASE", "Mise à jour de la base de données de version " + oldVersion + " à " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Méthode pour hasher le mot de passe
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }

    // Ajouter un utilisateur
    public boolean addUser(User user) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_NOM, user.getNom());
            values.put(COLUMN_PRENOM, user.getPrenom());
            values.put(COLUMN_NUMERO_ETUDIANT, user.getNumeroEtudiant());
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_PASSWORD, hashPassword(user.getMotDePasse()));
            values.put(COLUMN_ROLE, user.getRole());

            long result = db.insert(TABLE_USERS, null, values);

            Log.d("DATABASE", "Tentative d'insertion utilisateur : " + user.getNom() + " " + user.getPrenom());
            Log.d("DATABASE", "Rôle : " + user.getRole());
            Log.d("DATABASE", "Résultat de l'insertion : " + result);

            return result != -1;
        } catch (Exception e) {
            Log.e("DATABASE", "Erreur lors de l'insertion : " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    // Vérifier si le numéro étudiant existe déjà
    public boolean checkNumeroEtudiantExists(String numeroEtudiant) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_NUMERO_ETUDIANT + "=?",
                new String[]{numeroEtudiant},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Vérifier si l'email existe déjà
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}