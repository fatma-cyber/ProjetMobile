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
    private static final int DATABASE_VERSION = 3; // ← CHANGEZ À 2 SI C'ÉTAIT 1

    // Table Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_PRENOM = "prenom";
    private static final String COLUMN_NUMERO_ETUDIANT = "numero_etudiant";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "mot_de_passe";
    private static final String COLUMN_ROLE = "role"; // ← NOUVEAU
    private static final String COLUMN_DATE_CREATION = "date_creation";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table users (existante)
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

        // ======= NOUVELLES TABLES À AJOUTER ICI =======

        // Table menus
        String CREATE_MENUS_TABLE = "CREATE TABLE menus ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nom_plat TEXT NOT NULL,"
                + "description TEXT,"
                + "prix REAL NOT NULL,"
                + "disponible INTEGER DEFAULT 1,"
                + "date_ajout DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
        db.execSQL(CREATE_MENUS_TABLE);
        Log.d("DATABASE", "Table menus créée");

        // Table reservations
        String CREATE_RESERVATIONS_TABLE = "CREATE TABLE reservations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"
                + "menu_id INTEGER NOT NULL,"
                + "date_reservation DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "statut TEXT DEFAULT 'en_attente',"
                + "FOREIGN KEY (user_id) REFERENCES users(id),"
                + "FOREIGN KEY (menu_id) REFERENCES menus(id)"
                + ")";
        db.execSQL(CREATE_RESERVATIONS_TABLE);
        Log.d("DATABASE", "Table reservations créée");

        // Table avis
        String CREATE_AVIS_TABLE = "CREATE TABLE avis ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"
                + "menu_id INTEGER NOT NULL,"
                + "note INTEGER NOT NULL CHECK(note >= 1 AND note <= 5),"
                + "commentaire TEXT,"
                + "date_avis DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (user_id) REFERENCES users(id),"
                + "FOREIGN KEY (menu_id) REFERENCES menus(id)"
                + ")";
        db.execSQL(CREATE_AVIS_TABLE);
        Log.d("DATABASE", "Table avis créée");

        // Insérer des menus par défaut (pour avoir des données de test)
        insertDefaultMenus(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DATABASE", "Mise à jour de la base de données de version " + oldVersion + " à " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS avis");
        db.execSQL("DROP TABLE IF EXISTS reservations");
        db.execSQL("DROP TABLE IF EXISTS menus");
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
            values.put(COLUMN_ROLE, user.getRole()); // ← AJOUTEZ CETTE LIGNE

            long result = db.insert(TABLE_USERS, null, values);

            // Logs pour déboguer
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

    // Vérifier email + mot de passe
    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID, COLUMN_ROLE},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, hashedPassword},
                null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }
    // Insérer des menus par défaut pour les tests
    private void insertDefaultMenus(SQLiteDatabase db) {
        String[] menus = {
                "INSERT INTO menus (nom_plat, description, prix, disponible) VALUES ('Couscous', 'Couscous traditionnel avec légumes et viande', 35.0, 1)",
                "INSERT INTO menus (nom_plat, description, prix, disponible) VALUES ('Pizza Margherita', 'Pizza classique sauce tomate et mozzarella', 25.0, 1)",
                "INSERT INTO menus (nom_plat, description, prix, disponible) VALUES ('Salade César', 'Salade fraîche avec poulet grillé', 20.0, 1)",
                "INSERT INTO menus (nom_plat, description, prix, disponible) VALUES ('Pâtes Carbonara', 'Pâtes à la crème et lardons', 28.0, 1)",
                "INSERT INTO menus (nom_plat, description, prix, disponible) VALUES ('Tajine Poulet', 'Tajine marocain aux olives et citron', 32.0, 1)"
        };

        for (String sql : menus) {
            db.execSQL(sql);
        }
        Log.d("DATABASE", "5 menus par défaut insérés");
    }
    // Vérifier email + mot de passe ET retourner l'utilisateur complet
    public User loginUserByEmail(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        try {
            // Hasher le mot de passe entré
            String hashedPassword = hashPassword(password);

            // Requête pour trouver l'utilisateur
            Cursor cursor = db.query(
                    TABLE_USERS,
                    null, // Toutes les colonnes
                    COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                    new String[]{email, hashedPassword},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                // Utilisateur trouvé - créer l'objet User
                user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRENOM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMERO_ETUDIANT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        "", // Ne pas retourner le mot de passe
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
                );

                Log.d("DATABASE", "Connexion réussie : " + user.getPrenom() + " (Rôle: " + user.getRole() + ")");
            } else {
                Log.d("DATABASE", "Échec connexion pour : " + email);
            }

            cursor.close();
        } catch (Exception e) {
            Log.e("DATABASE", "Erreur connexion : " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }

        return user; // null si pas trouvé
    }
}

