package pj_sonde.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_Sonde {

    private Db_mariadb db;
    private int id;
    private int id_type; // correspond à la colonne id_types dans la BDD
    private String code;
    private String nom;
    private String adresse_ip;
    private String adresse_mac;
    private String commentaire;
    private String code_unite;
    private LocalDate date_achat;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // -------------------------------------------------------------------
    // CONSTRUCTEUR COMPLET (objet déjà existant en BDD)
    // -------------------------------------------------------------------
    public M_Sonde(Db_mariadb db,
                   int id,
                   int id_type,
                   String code,
                   String nom,
                   String adresse_ip,
                   String adresse_mac,
                   String commentaire,
                   String code_unite,
                   LocalDate date_achat,
                   LocalDateTime created_at,
                   LocalDateTime updated_at) {

        this.db = db;
        this.id = id;
        this.id_type = id_type;
        this.code = code;
        this.nom = nom;
        this.adresse_ip = adresse_ip;
        this.adresse_mac = adresse_mac;
        this.commentaire = commentaire;
        this.code_unite = code_unite;
        this.date_achat = date_achat;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // -------------------------------------------------------------------
    // CONSTRUCTEUR D'INSERTION (ordre EXACT de la BDD)
    // colonnes : code, nom, adresse_ip, adresse_mac, date_achat,
    //            commentaire, created_at, updated_at, id_types, code_unite
    // -------------------------------------------------------------------
    public M_Sonde(Db_mariadb db,
                   String code,
                   String nom,
                   String adresse_ip,
                   String adresse_mac,
                   LocalDate date_achat,
                   String commentaire,
                   int id_type,
                   String code_unite) throws SQLException {

        this.db = db;
        this.code = code;
        this.nom = nom;
        this.adresse_ip = adresse_ip;
        this.adresse_mac = adresse_mac;
        this.date_achat = date_achat;
        this.commentaire = commentaire;
        this.id_type = id_type;
        this.code_unite = code_unite;

        String sql = "INSERT INTO mcd_sondes "
                + "(code, nom, adresse_ip, adresse_mac, date_achat, commentaire, "
                + "created_at, updated_at, id_types, code_unite) "
                + "VALUES ('" + code + "', "
                + "'" + nom + "', "
                + (adresse_ip != null ? "'" + adresse_ip + "'" : "NULL") + ", "
                + (adresse_mac != null ? "'" + adresse_mac + "'" : "NULL") + ", "
                + (date_achat != null ? "'" + Date.valueOf(date_achat) + "'" : "NULL") + ", "
                + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(),"
                + id_type + ", "
                + "'" + code_unite + "');";

        db.sqlExec(sql);

        ResultSet res = db.sqlLastId();
        if (res.first()) {
            this.id = res.getInt("id");
        }
        res.close();
    }

    // -------------------------------------------------------------------
    // CONSTRUCTEUR DE CHARGEMENT PAR ID
    // -------------------------------------------------------------------
    public M_Sonde(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        String sql = "SELECT * FROM mcd_sondes WHERE id = " + id + ";";
        ResultSet res = db.sqlSelect(sql);

        if (!res.first()) {
            throw new SQLException("Aucune sonde trouvée avec l'id " + id);
        }

        this.code = res.getString("code");
        this.nom = res.getString("nom");
        this.adresse_ip = res.getString("adresse_ip");
        this.adresse_mac = res.getString("adresse_mac");

        Date d = res.getDate("date_achat");
        this.date_achat = (d != null) ? d.toLocalDate() : null;

        this.commentaire = res.getString("commentaire");

        Timestamp c = res.getTimestamp("created_at");
        this.created_at = (c != null) ? c.toLocalDateTime() : null;

        Timestamp u = res.getTimestamp("updated_at");
        this.updated_at = (u != null) ? u.toLocalDateTime() : null;

        this.id_type = res.getInt("id_types");
        this.code_unite = res.getString("code_unite");
        res.close();
    }

    // -------------------------------------------------------------------
    // GETTERS / SETTERS
    // -------------------------------------------------------------------
    public int getId() {
        return id;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse_ip() {
        return adresse_ip;
    }

    public void setAdresse_ip(String adresse_ip) {
        this.adresse_ip = adresse_ip;
    }

    public String getAdresse_mac() {
        return adresse_mac;
    }

    public void setAdresse_mac(String adresse_mac) {
        this.adresse_mac = adresse_mac;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getCode_unite() {
        return code_unite;
    }

    public void setCode_unite(String code_unite) {
        this.code_unite = code_unite;
    }

    public LocalDate getDate_achat() {
        return date_achat;
    }

    public void setDate_achat(LocalDate date_achat) {
        this.date_achat = date_achat;
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

    // -------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------
    public void update() throws SQLException {
        String sql = "UPDATE mcd_sondes SET "
                + "code = '" + code + "', "
                + "nom = '" + nom + "', "
                + "adresse_ip = " + (adresse_ip != null ? "'" + adresse_ip + "'" : "NULL") + ", "
                + "adresse_mac = " + (adresse_mac != null ? "'" + adresse_mac + "'" : "NULL") + ", "
                + "date_achat = " + (date_achat != null ? "'" + Date.valueOf(date_achat) + "'" : "NULL") + ", "
                + "commentaire = " + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "updated_at = '" + LocalDateTime.now() + "', "
                + "id_types = " + id_type + ", "
                + "code_unite = '" + code_unite + "' "
                + "WHERE id = " + id + ";";

        db.sqlExec(sql);
    }

    // -------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_sondes WHERE id = " + id + ";";
        db.sqlExec(sql);
    }

    // -------------------------------------------------------------------
    // GET RECORDS
    // -------------------------------------------------------------------
    public static LinkedHashMap<Integer, M_Sonde> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1 = 1");
    }

    public static LinkedHashMap<Integer, M_Sonde> getRecords(Db_mariadb db, String clauseWhere) throws SQLException {
        LinkedHashMap<Integer, M_Sonde> map = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_sondes WHERE " + clauseWhere + " ORDER BY nom;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            int id = res.getInt("id");

            Date d = res.getDate("date_achat");
            LocalDate lDate = (d != null) ? d.toLocalDate() : null;

            Timestamp c = res.getTimestamp("created_at");
            LocalDateTime lCreated = (c != null) ? c.toLocalDateTime() : null;

            Timestamp u = res.getTimestamp("updated_at");
            LocalDateTime lUpdated = (u != null) ? u.toLocalDateTime() : null;

            M_Sonde s = new M_Sonde(
                    db,
                    id,
                    res.getInt("id_types"),
                    res.getString("code"),
                    res.getString("nom"),
                    res.getString("adresse_ip"),
                    res.getString("adresse_mac"),
                    res.getString("commentaire"),
                    res.getString("code_unite"),
                    lDate,
                    lCreated,
                    lUpdated
            );

            map.put(id, s);
        }
        res.close();
        return map;
    }

    // -------------------------------------------------------------------
    // TO STRING
    // -------------------------------------------------------------------
    @Override
    public String toString() {
        return "Sonde{"
                + "id=" + id
                + ", code='" + code + '\''
                + ", nom='" + nom + '\''
                + ", adresse_ip='" + adresse_ip + '\''
                + ", adresse_mac='" + adresse_mac + '\''
                + ", date_achat=" + date_achat
                + ", commentaire='" + commentaire + '\''
                + ", created_at=" + created_at
                + ", updated_at=" + updated_at
                + ", id_type=" + id_type
                + ", code_unite='" + code_unite + '\''
                + "}\n";
    }

    // -------------------------------------------------------------------
    // MAIN : TESTS COMPLETS
    // -------------------------------------------------------------------
//    public static void main(String[] args) throws Exception {
//
//        Db_mariadb maBase = new Db_mariadb(Cl_Connection.url, Cl_Connection.login, Cl_Connection.password);
//
//        // Pour éviter les problèmes d'unicité sur code/nom si tu relances plusieurs fois
//        String testCode = "TEST_" + System.currentTimeMillis();
//        String testNom = "SONDE_" + System.currentTimeMillis();
//
//        System.out.println("=== TEST INSERT ===");
//        M_Sonde s1 = new M_Sonde(
//                maBase,
//                testCode,                // code
//                testNom,                 // nom
//                null,                    // adresse_ip
//                null,                    // adresse_mac
//                null,                    // date_achat
//                null,                    // commentaire
//                LocalDateTime.now(),     // created_at
//                LocalDateTime.now(),     // updated_at
//                1,                       // id_type (colonne id_types, doit exister dans mcd_types)
//                "T"                      // code_unite (doit exister dans mcd_unites)
//        );
//        System.out.println(s1);
//
//        System.out.println("=== TEST SELECT PAR ID ===");
//        M_Sonde s2 = new M_Sonde(maBase, s1.getId());
//        System.out.println(s2);
//
//        System.out.println("=== TEST SETTERS + UPDATE ===");
//        s2.setNom(s2.getNom() + "_MODIF");
//        s2.setAdresse_ip("192.168.0.10");
//        s2.setCommentaire("Commentaire modifié");
//        s2.update();
//
//        M_Sonde s3 = new M_Sonde(maBase, s2.getId());
//        System.out.println(s3);
//
//        System.out.println("=== TEST GETRECORDS WHERE id_types = 1 ===");
//        LinkedHashMap<Integer, M_Sonde> map1 = M_Sonde.getRecords(maBase, "id_types = 1");
//        for (M_Sonde s : map1.values()) {
//            System.out.println(s);
//        }
//
//        System.out.println("=== TEST GETRECORDS (ALL) ===");
//        LinkedHashMap<Integer, M_Sonde> map2 = M_Sonde.getRecords(maBase);
//        for (M_Sonde s : map2.values()) {
//            System.out.println(s);
//        }
//
//        System.out.println("=== TEST DELETE ===");
//        s3.delete();
//        System.out.println("Sonde supprimée : id=" + s3.getId());
//
//        maBase.close();
//    }
}
