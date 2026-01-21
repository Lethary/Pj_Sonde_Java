package pj_sonde.Model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_Type {

    private Db_mariadb db;
    private int id;
    private String libelle;
    private String ref_fournisseur;
    private String url_produit;
    private String commentaire;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // Constructeur complet
    public M_Type(Db_mariadb db, int id, String libelle,
            String ref_fournisseur, String url_produit,
            String commentaire, LocalDateTime created_at,
            LocalDateTime updated_at) {
        this.db = db;
        this.id = id;
        this.libelle = libelle;
        this.ref_fournisseur = ref_fournisseur;
        this.url_produit = url_produit;
        this.commentaire = commentaire;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Constructeur INSERT
    public M_Type(Db_mariadb db, String libelle,
            String ref_fournisseur,
            String url_produit,
            String commentaire) throws SQLException {

        this.db = db;
        this.libelle = libelle;
        this.ref_fournisseur = ref_fournisseur;
        this.url_produit = url_produit;
        this.commentaire = commentaire;

        String sql = "INSERT INTO mcd_types (libelle, ref_fournisseur, url_produit, commentaire, created_at, updated_at) VALUES ("
                + "'" + libelle + "', "
                + (ref_fournisseur != null ? "'" + ref_fournisseur + "'" : "NULL") + ", "
                + (url_produit != null ? "'" + url_produit + "'" : "NULL") + ", "
                + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());";

        db.sqlExec(sql);

        ResultSet res = db.sqlLastId();
        if (res.first()) {
            this.id = res.getInt("id");
        }

        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    // Constructeur SELECT
    public M_Type(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_types WHERE id=" + id + ";");
        if (!res.first()) {
            throw new SQLException("Type introuvable id=" + id);
        }

        this.libelle = res.getString("libelle");
        this.ref_fournisseur = res.getString("ref_fournisseur");
        this.url_produit = res.getString("url_produit");
        this.commentaire = res.getString("commentaire");

        Timestamp c = res.getTimestamp("created_at");
        this.created_at = (c != null ? c.toLocalDateTime() : null);

        Timestamp u = res.getTimestamp("updated_at");
        this.updated_at = (u != null ? u.toLocalDateTime() : null);
    }

    public Db_mariadb getDb() {
        return db;
    }

    public void setDb(Db_mariadb db) {
        this.db = db;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getRef_fournisseur() {
        return ref_fournisseur;
    }

    public void setRef_fournisseur(String ref_fournisseur) {
        this.ref_fournisseur = ref_fournisseur;
    }

    public String getUrl_produit() {
        return url_produit;
    }

    public void setUrl_produit(String url_produit) {
        this.url_produit = url_produit;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    
    // UPDATE
    public void update() throws SQLException {
        String sql = "UPDATE mcd_types SET "
                + "libelle='" + libelle + "', "
                + "ref_fournisseur=" + (ref_fournisseur != null ? "'" + ref_fournisseur + "'" : "NULL") + ", "
                + "url_produit=" + (url_produit != null ? "'" + url_produit + "'" : "NULL") + ", "
                + "commentaire=" + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "updated_at=CURRENT_TIMESTAMP() "
                + "WHERE id=" + id + ";";

        db.sqlExec(sql);
    }

    // DELETE
    public void delete() throws SQLException {
        db.sqlExec("DELETE FROM mcd_types WHERE id=" + id + ";");
    }

    public HashMap<String, Object> requeteOne(String select, String where) throws SQLException {

        String sql = "SELECT " + select + " FROM mcd_types";
        if (where != null && !where.trim().isEmpty()) {
            sql += " WHERE " + where;
        }
        sql += ";";

        ResultSet res = db.sqlSelect(sql);

        HashMap<String, Object> ligne = new HashMap<>();

        if (res.first()) {
            ResultSetMetaData md = res.getMetaData();
            int colonnes = md.getColumnCount();

            for (int i = 1; i <= colonnes; i++) {
                ligne.put(md.getColumnName(i), res.getObject(i));
            }
        }

        return ligne;
    }

    // GET RECORDS
    public static LinkedHashMap<Integer, M_Type> getRecords(Db_mariadb db, String where, String select) throws SQLException {
        LinkedHashMap<Integer, M_Type> map = new LinkedHashMap<>();
        ResultSet res = db.sqlSelect("SELECT " + select + " FROM mcd_types WHERE " + where + " ORDER BY libelle");

        while (res.next()) {
            int id = res.getInt("id");
            Timestamp c = res.getTimestamp("created_at");
            Timestamp u = res.getTimestamp("updated_at");

            M_Type t = new M_Type(
                    db,
                    id,
                    res.getString("libelle"),
                    res.getString("ref_fournisseur"),
                    res.getString("url_produit"),
                    res.getString("commentaire"),
                    (c != null ? c.toLocalDateTime() : null),
                    (u != null ? u.toLocalDateTime() : null)
            );

            map.put(id, t);
        }

        return map;
    }

    public static LinkedHashMap<Integer, M_Type> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1=1", "*");
    }

    @Override
    public String toString() {
        return "Type{id=" + id
                + ", libelle='" + libelle + '\''
                + ", ref_fournisseur='" + ref_fournisseur + '\''
                + ", url_produit='" + url_produit + '\''
                + ", commentaire='" + commentaire + '\''
                + ", created_at=" + created_at
                + ", updated_at=" + updated_at + "}\n";
    }

    public int getId() {
        return id;
    }

    // TESTS
//    public static void main(String[] args) throws Exception {
//        Db_mariadb base = new Db_mariadb(Cl_Connection.url, Cl_Connection.login, Cl_Connection.password);
//
//        System.out.println("=== INSERT TYPE ===");
//        M_Type t1 = new M_Type(base,
//                "Type " + System.currentTimeMillis(),
//                "FournisseurX",
//                "http://example.com",
//                "Commentaire type");
//        System.out.println(t1);
//
//        System.out.println("=== SELECT TYPE ===");
//        M_Type t2 = new M_Type(base, t1.getId());
//        System.out.println(t2);
//
//        System.out.println("=== UPDATE TYPE ===");
//        t2.commentaire = "Commentaire modifié";
//        t2.update();
//        M_Type t3 = new M_Type(base, t2.getId());
//        System.out.println(t3);
//
//        System.out.println("=== GET RECORDS TYPE ===");
//        for (M_Type tt : M_Type.getRecords(base).values()) {
//            System.out.println(tt);
//        }
//
//        // t3.delete(); // à activer si tu veux
//        base.close();
//    }
}
