package pj_sonde.Model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_Salle {

    private Db_mariadb db;
    private int id;
    private String code;
    private String libelle;
    private String commentaire;
    private int id_batiment;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // Constructeur complet
    public M_Salle(Db_mariadb db, int id, String code, String libelle,
            String commentaire, int id_batiment,
            LocalDateTime created_at, LocalDateTime updated_at) {
        this.db = db;
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.commentaire = commentaire;
        this.id_batiment = id_batiment;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Constructeur INSERT
    public M_Salle(Db_mariadb db, String code, String libelle,
            String commentaire, int id_batiment) throws SQLException {
        this.db = db;
        this.code = code;
        this.libelle = libelle;
        this.commentaire = commentaire;
        this.id_batiment = id_batiment;

        String sql = "INSERT INTO mcd_salles (code, libelle, commentaire, created_at, updated_at, id_batiment) VALUES ("
                + (code != null ? "'" + code + "'" : "NULL") + ", "
                + "'" + libelle + "', "
                + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), "
                + id_batiment + ");";

        db.sqlExec(sql);
        ResultSet res = db.sqlLastId();
        if (res.first()) {
            this.id = res.getInt("id");
        }

        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    // Constructeur SELECT
    public M_Salle(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_salles WHERE id=" + id + ";");
        if (!res.first()) {
            throw new SQLException("Salle introuvable id=" + id);
        }

        this.code = res.getString("code");
        this.libelle = res.getString("libelle");
        this.commentaire = res.getString("commentaire");
        this.id_batiment = res.getInt("id_batiment");

        Timestamp c = res.getTimestamp("created_at");
        this.created_at = (c != null ? c.toLocalDateTime() : null);

        Timestamp u = res.getTimestamp("updated_at");
        this.updated_at = (u != null ? u.toLocalDateTime() : null);
    }

    // UPDATE
    public void update() throws SQLException {
        String sql = "UPDATE mcd_salles SET "
                + "code=" + (code != null ? "'" + code + "'" : "NULL") + ", "
                + "libelle='" + libelle + "', "
                + "commentaire=" + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "id_batiment=" + id_batiment + ", "
                + "updated_at=CURRENT_TIMESTAMP() "
                + "WHERE id=" + id + ";";

        db.sqlExec(sql);
    }

    // DELETE
    public void delete() throws SQLException {
        db.sqlExec("DELETE FROM mcd_salles WHERE id=" + id + ";");
    }

    // GET RECORDS
    public static LinkedHashMap<Integer, M_Salle> getRecords(Db_mariadb db, String where) throws SQLException {
        LinkedHashMap<Integer, M_Salle> map = new LinkedHashMap<>();
        ResultSet res = db.sqlSelect("SELECT * FROM mcd_salles WHERE " + where + " ORDER BY id;");

        while (res.next()) {
            int id = res.getInt("id");
            Timestamp c = res.getTimestamp("created_at");
            Timestamp u = res.getTimestamp("updated_at");

            M_Salle s = new M_Salle(
                    db,
                    id,
                    res.getString("code"),
                    res.getString("libelle"),
                    res.getString("commentaire"),
                    res.getInt("id_batiment"),
                    (c != null ? c.toLocalDateTime() : null),
                    (u != null ? u.toLocalDateTime() : null)
            );

            map.put(id, s);
        }

        return map;
    }

    public static LinkedHashMap<Integer, M_Salle> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1=1");
    }

    public static boolean existe(Db_mariadb db, String code, String libelle) throws Exception {
        LinkedHashMap<Integer, M_Salle> result = getRecords(db, "code = '" + code + "' OR libelle ='" + libelle + "'");
        return !result.isEmpty();
    }

    public static boolean existeModification(Db_mariadb db, int idSalle, String code, String libelle) throws Exception {
        LinkedHashMap<Integer, M_Salle> result = getRecords(db, "id != " + idSalle + " AND (code = '" + code + "' OR libelle = '" + libelle + "')");
        return !result.isEmpty();
    }

    @Override
    public String toString() {
        return "Salle{id=" + id
                + ", code='" + code + '\''
                + ", libelle='" + libelle + '\''
                + ", commentaire='" + commentaire + '\''
                + ", id_batiment=" + id_batiment
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
//        // ATTENTION : id_batiment=1 doit exister dans mcd_batiments
//        System.out.println("=== INSERT SALLE ===");
//        M_Salle s1 = new M_Salle(base,
//                "S" + System.currentTimeMillis(),
//                "Salle Test",
//                "Commentaire salle",
//                1
//        );
//        System.out.println(s1);
//
//        System.out.println("=== SELECT SALLE ===");
//        M_Salle s2 = new M_Salle(base, s1.getId());
//        System.out.println(s2);
//
//        System.out.println("=== UPDATE SALLE ===");
//        s2.commentaire = "Commentaire modifié";
//        s2.update();
//        M_Salle s3 = new M_Salle(base, s2.getId());
//        System.out.println(s3);
//
//        System.out.println("=== GET RECORDS SALLE ===");
//        for (M_Salle ss : M_Salle.getRecords(base).values()) {
//            System.out.println(ss);
//        }
//
//        // s3.delete();
//
//        base.close();
//    }
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

    public int getId_batiment() {
        return id_batiment;
    }

    public void setId_batiment(int id_batiment) {
        this.id_batiment = id_batiment;
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
