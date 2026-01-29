package pj_sonde.Model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_Unite {

    private Db_mariadb db;
    private String code;
    private String libelle;
    private String commentaire;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public M_Unite(Db_mariadb db, String code, String libelle, String commentaire,
            LocalDateTime created_at, LocalDateTime updated_at) {
        this.db = db;
        this.code = code;
        this.libelle = libelle;
        this.commentaire = commentaire;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public M_Unite(Db_mariadb db, String code, String libelle, String commentaire) throws SQLException {
        this.db = db;
        this.code = code;
        this.libelle = libelle;
        this.commentaire = commentaire;

        String sql = "INSERT INTO mcd_unites (code, libelle, commentaire) VALUES ("
                + "'" + code + "', "
                + "'" + libelle + "', "
                + (commentaire != null ? "'" + commentaire + "'" : "NULL")
                + ");";
        db.sqlExec(sql);

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_unites WHERE code='" + code + "';");
        if (res.first()) {
            Timestamp c = res.getTimestamp("created_at");
            Timestamp u = res.getTimestamp("updated_at");
            this.created_at = c != null ? c.toLocalDateTime() : null;
            this.updated_at = u != null ? u.toLocalDateTime() : null;
        }
    }

    public M_Unite(Db_mariadb db, String code) throws SQLException {
        this.db = db;
        this.code = code;

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_unites WHERE code='" + code + "';");
        if (!res.first()) {
            throw new SQLException("Unité introuvable code=" + code);
        }

        this.libelle = res.getString("libelle");
        this.commentaire = res.getString("commentaire");
        Timestamp c = res.getTimestamp("created_at");
        Timestamp u = res.getTimestamp("updated_at");
        this.created_at = c != null ? c.toLocalDateTime() : null;
        this.updated_at = u != null ? u.toLocalDateTime() : null;
    }

    public void update() throws SQLException {
        String sql = "UPDATE mcd_unites SET "
                + "libelle='" + libelle + "', "
                + "commentaire=" + (commentaire != null ? "'" + commentaire + "'" : "NULL")
                + " WHERE code='" + code + "';";
        db.sqlExec(sql);
    }

    public void delete() throws SQLException {
        db.sqlExec("DELETE FROM mcd_unites WHERE code='" + code + "';");
    }

    public static LinkedHashMap<String, M_Unite> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1=1");
    }

    public static LinkedHashMap<String, M_Unite> getRecords(Db_mariadb db, String where) throws SQLException {
        LinkedHashMap<String, M_Unite> map = new LinkedHashMap<>();

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_unites WHERE " + where + " ORDER BY libelle;");
        while (res.next()) {
            String code = res.getString("code");
            Timestamp c = res.getTimestamp("created_at");
            Timestamp u = res.getTimestamp("updated_at");
            M_Unite uObj = new M_Unite(
                    db,
                    code,
                    res.getString("libelle"),
                    res.getString("commentaire"),
                    c != null ? c.toLocalDateTime() : null,
                    u != null ? u.toLocalDateTime() : null
            );
            map.put(code, uObj);
        }

        return map;
    }

    @Override
    public String toString() {
        return "Unite{code='" + code + '\''
                + ", libelle='" + libelle + '\''
                + ", commentaire='" + commentaire + '\''
                + ", created_at=" + created_at
                + ", updated_at=" + updated_at + "}\n";
    }

    

//    public static void main(String[] args) throws Exception {
//        Db_mariadb base = new Db_mariadb(Cl_Connection.url, Cl_Connection.login, Cl_Connection.password);
//
//        System.out.println("=== INSERT UNITE ===");
//        M_Unite u1 = new M_Unite(base, "T", "Température", "Unité de test");
//        System.out.println(u1);
//
//        System.out.println("=== SELECT UNITE ===");
//        M_Unite u2 = new M_Unite(base, "T");
//        System.out.println(u2);
//
//        System.out.println("=== UPDATE UNITE ===");
//        u2.setLibelle("Température modifiée");
//        u2.update();
//        System.out.println(new M_Unite(base, "T"));
//
//        System.out.println("=== GET ALL UNITES ===");
//        for (M_Unite u : M_Unite.getRecords(base).values()) {
//            System.out.println(u);
//        }
//
//        // u2.delete(); // si tu veux supprimer
//
//        base.close();
//    }

    public Db_mariadb getDb() {
        return db;
    }

    public void setDb(Db_mariadb db) {
        this.db = db;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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
}
