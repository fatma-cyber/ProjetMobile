package com.example.smartcanteen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.smartcanteen.models.Menu;
import com.example.smartcanteen.models.Reservation;
import com.example.smartcanteen.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import com.example.smartcanteen.models.Avis;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartCanteen.db";
    private static final int DATABASE_VERSION = 3;

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
        // Table users
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

        // Insérer des menus par défaut
        insertDefaultMenus(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS avis");
        db.execSQL("DROP TABLE IF EXISTS reservations");
        db.execSQL("DROP TABLE IF EXISTS menus");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Hash mot de passe
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

    // =================== USERS ===================
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, user.getNom());
        values.put(COLUMN_PRENOM, user.getPrenom());
        values.put(COLUMN_NUMERO_ETUDIANT, user.getNumeroEtudiant());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, hashPassword(user.getMotDePasse()));
        values.put(COLUMN_ROLE, user.getRole());
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkNumeroEtudiantExists(String numeroEtudiant) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_NUMERO_ETUDIANT + "=?", new String[]{numeroEtudiant}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public User loginUserByEmail(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        String hashedPassword = hashPassword(password);
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, hashedPassword}, null, null, null);
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRENOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMERO_ETUDIANT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    "",
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
            );
        }
        cursor.close();
        db.close();
        return user;
    }

    // =================== MENUS ===================
    private void insertDefaultMenus(SQLiteDatabase db) {
        String[] menus = {
                "INSERT INTO menus (nom_plat, description, prix, disponible) VALUES ('Couscous', 'Couscous traditionnel', 35.0, 1)",
                "INSERT INTO menus (nom_plat, description, prix, disponible) VALUES ('Pizza Margherita', 'Pizza classique', 25.0, 1)",
                "INSERT INTO menus (nom_plat, description, prix, disponible) VALUES ('Salade César', 'Salade fraîche', 20.0, 1)"
        };
        for (String sql : menus) db.execSQL(sql);
    }

    public List<Menu> getAllMenus() {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM menus WHERE disponible = 1", null);
        if (cursor.moveToFirst()) {
            do {
                menus.add(new Menu(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nom_plat")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("prix")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("disponible")) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow("date_ajout"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return menus;
    }

    public List<Menu> getAllMenusForPersonnel() {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM menus", null);
        if (cursor.moveToFirst()) {
            do {
                menus.add(new Menu(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nom_plat")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("prix")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("disponible")) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow("date_ajout"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return menus;
    }

    public boolean addMenu(String nomPlat, String description, double prix) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom_plat", nomPlat);
        values.put("description", description);
        values.put("prix", prix);
        values.put("disponible", 1);
        long result = db.insert("menus", null, values);
        db.close();
        return result != -1;
    }

    public boolean updateMenu(int id, String nomPlat, String description, double prix) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom_plat", nomPlat);
        values.put("description", description);
        values.put("prix", prix);
        int result = db.update("menus", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean deleteMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("menus", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean setMenuAvailability(int id, boolean disponible) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("disponible", disponible ? 1 : 0);
        int result = db.update("menus", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // =================== RESERVATIONS ===================
    public boolean addReservation(int userId, int menuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("menu_id", menuId);
        long result = db.insert("reservations", null, values);
        db.close();
        return result != -1;
    }

    public boolean cancelReservation(int reservationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("reservations", "id=?", new String[]{String.valueOf(reservationId)});
        db.close();
        return result > 0;
    }

    // =================== AVIS ===================

    public boolean addAvis(int userId, int menuId, int note, String commentaire) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("menu_id", menuId);
        values.put("note", note);
        values.put("commentaire", commentaire);
        long result = db.insert("avis", null, values);
        db.close();
        return result != -1;
    }

    public List<Avis> getAllAvis() {
        List<Avis> avisList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM avis ORDER BY date_avis DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Avis avis = new Avis();
                avis.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                avis.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                avis.setMenuId(cursor.getInt(cursor.getColumnIndexOrThrow("menu_id")));
                avis.setNote(cursor.getInt(cursor.getColumnIndexOrThrow("note")));
                avis.setCommentaire(cursor.getString(cursor.getColumnIndexOrThrow("commentaire")));
                avis.setDateAvis(cursor.getString(cursor.getColumnIndexOrThrow("date_avis")));
                avisList.add(avis);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return avisList;
    }

    public boolean updateAvis(int id, int note, String commentaire) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note", note);
        values.put("commentaire", commentaire);

        int rows = db.update("avis", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public boolean deleteAvis(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("avis", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    // =================== RESERVATIONS (ÉTUDIANT) ===================

    /**
     * Récupère toutes les réservations d'un étudiant avec les infos du menu
     */
    public List<Reservation> getReservationsByUser(int userId) {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 🔍 Requête SQL avec JOIN pour récupérer les infos du menu
        String query = "SELECT r.id, r.user_id, r.menu_id, r.date_reservation, r.statut, " +
                "m.nom_plat, m.description, m.prix " +
                "FROM reservations r " +
                "INNER JOIN menus m ON r.menu_id = m.id " +
                "WHERE r.user_id = ? " +
                "ORDER BY r.date_reservation DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("menu_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date_reservation")),
                        cursor.getString(cursor.getColumnIndexOrThrow("statut")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nom_plat")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("prix"))
                );
                reservations.add(reservation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reservations;
    }

    // Récupérer toutes les réservations avec les infos de l'étudiant et du menu
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.id, r.user_id, r.menu_id, r.date_reservation, r.statut, " +
                "m.nom_plat, m.description, m.prix, " +
                "u.nom, u.prenom, u.numero_etudiant " +
                "FROM reservations r " +
                "INNER JOIN menus m ON r.menu_id = m.id " +
                "INNER JOIN users u ON r.user_id = u.id " +
                "ORDER BY r.date_reservation DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("menu_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date_reservation")),
                        cursor.getString(cursor.getColumnIndexOrThrow("statut")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nom_plat")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("prix"))
                );
                // Ajouter infos étudiant
                reservation.setNomEtudiant(cursor.getString(cursor.getColumnIndexOrThrow("nom")));
                reservation.setPrenomEtudiant(cursor.getString(cursor.getColumnIndexOrThrow("prenom")));
                reservation.setNumeroEtudiant(cursor.getString(cursor.getColumnIndexOrThrow("numero_etudiant")));

                reservations.add(reservation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reservations;
    }

}
