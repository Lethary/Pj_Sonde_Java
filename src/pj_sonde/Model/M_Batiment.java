package pj_sonde.Model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_Batiment {

    private Db_mariadb db;
    private int id;
    private String code;
    private String libelle;
    private String commentaire;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // Constructeur complet
    public M_Batiment(Db_mariadb db, int id, String code, String libelle,
            String commentaire, LocalDateTime created_at,
            LocalDateTime updated_at) {
        this.db = db;
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.commentaire = commentaire;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Constructeur INSERT
    public M_Batiment(Db_mariadb db, String code, String libelle,
            String commentaire) throws SQLException {
        this.db = db;
        this.code = code;
        this.libelle = libelle;
        this.commentaire = commentaire;

        String sql = "INSERT INTO mcd_batiments (code, libelle, commentaire, created_at) VALUES ("
                + (code != null ? "'" + code + "'" : "NULL") + ", "
                + "'" + libelle + "', "
                + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "CURRENT_TIMESTAMP());";

        db.sqlExec(sql);
        ResultSet res = db.sqlLastId();
        if (res.first()) {
            this.id = res.getInt("id");
        }

        this.created_at = LocalDateTime.now();
        //this.updated_at = LocalDateTime.now();
    }

    // Constructeur SELECT
    public M_Batiment(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_batiments WHERE id=" + id + ";");
        if (!res.first()) {
            throw new SQLException("Bâtiment introuvable id=" + id);
        }

        this.code = res.getString("code");
        this.libelle = res.getString("libelle");
        this.commentaire = res.getString("commentaire");

        Timestamp c = res.getTimestamp("created_at");
        this.created_at = (c != null ? c.toLocalDateTime() : null);

        Timestamp u = res.getTimestamp("updated_at");
        this.updated_at = (u != null ? u.toLocalDateTime() : null);
    }

    // UPDATE
    public void update() throws SQLException {
        String sql = "UPDATE mcd_batiments SET "
                + "code=" + (code != null ? "'" + code + "'" : "NULL") + ", "
                + "libelle='" + libelle + "', "
                + "commentaire=" + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "updated_at=CURRENT_TIMESTAMP() "
                + "WHERE id=" + id + ";";

        db.sqlExec(sql);
    }

    // DELETE
    public void delete() throws SQLException {
        db.sqlExec("DELETE FROM mcd_batiments WHERE id=" + id + ";");
    }

    // GET RECORDS
    public static LinkedHashMap<Integer, M_Batiment> getRecords(Db_mariadb db, String where) throws SQLException {
        LinkedHashMap<Integer, M_Batiment> map = new LinkedHashMap<>();
        ResultSet res = db.sqlSelect("SELECT * FROM mcd_batiments WHERE " + where + " ORDER BY libelle;");

        while (res.next()) {
            int id = res.getInt("id");
            Timestamp c = res.getTimestamp("created_at");
            Timestamp u = res.getTimestamp("updated_at");

            M_Batiment b = new M_Batiment(
                    db,
                    id,
                    res.getString("code"),
                    res.getString("libelle"),
                    res.getString("commentaire"),
                    (c != null ? c.toLocalDateTime() : null),
                    (u != null ? u.toLocalDateTime() : null)
            );

            map.put(id, b);
        }

        return map;
    }

    public static LinkedHashMap<Integer, M_Batiment> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1=1");
    }

    public static boolean existe(Db_mariadb db, String code, String libelle) throws SQLException {
        LinkedHashMap<Integer, M_Batiment> result = getRecords(db, "code = '" + code + "' OR libelle ='" + libelle +"'");
        return !result.isEmpty();
    }
    
    public static boolean existeModification(Db_mariadb db,int idBatiment, String code, String libelle) throws SQLException {
        LinkedHashMap<Integer, M_Batiment> result = getRecords(db,"id != " + idBatiment + " AND code = '" + code + "' OR libelle ='" + libelle +"'");
        return !result.isEmpty();
    }

    @Override
    public String toString() {
        return "Batiment{id=" + id
                + ", code='" + code + '\''
                + ", libelle='" + libelle + '\''
                + ", commentaire='" + commentaire + '\''
                + ", created_at=" + created_at
                + ", updated_at=" + updated_at + "}\n";
    }

    public int getId() {
        return id;
    }

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
    

    // TESTS
//    public static void main(String[] args) throws Exception {
//        Db_mariadb base = new Db_mariadb(Cl_Connection.url, Cl_Connection.login, Cl_Connection.password);
//
//        System.out.println("=== INSERT BATIMENT ===");
//        M_Batiment b1 = new M_Batiment(base,
//                "BAT" + System.currentTimeMillis(),
//                "Bâtiment Tdddddst",
//                "Commentaire bâtiment");
//        System.out.println(b1);
//
//        System.out.println("=== SELECT BATIMENT ===");
//        M_Batiment b2 = new M_Batiment(base, b1.getId());
//        System.out.println(b2);
//
////        System.out.println("=== UPDATE BATIMENT ===");
////        b2.commentaire = "Commentaire modifié";
////        b2.update();
////        M_Batiment b3 = new M_Batiment(base, b2.getId());
////        System.out.println(b3);
//        System.out.println("=== GET RECORDS BATIMENT ===");
//        for (M_Batiment bb : M_Batiment.getRecords(base).values()) {
//            System.out.println(bb);
//        }
//
//        // b3.delete(); // si tu veux tester
//        base.close();
//    }
}
