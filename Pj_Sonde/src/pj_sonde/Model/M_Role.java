package pj_sonde.Model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_Role {

    private Db_mariadb db;
    private int id;
    private String code;
    private String nom;
    private String commentaire;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // ----------------------------------------------------------
    // Constructeur complet (objet déjà en BDD)
    // ----------------------------------------------------------
    public M_Role(Db_mariadb db, int id, String code, String nom,
                  String commentaire, LocalDateTime created_at,
                  LocalDateTime updated_at) {

        this.db = db;
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.commentaire = commentaire;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // ----------------------------------------------------------
    // Constructeur INSERT
    // ----------------------------------------------------------
    public M_Role(Db_mariadb db, String code, String nom, String commentaire) throws SQLException {

        this.db = db;
        this.code = code;
        this.nom = nom;
        this.commentaire = commentaire;

        String sql = "INSERT INTO mcd_roles (code, nom, commentaire, created_at, updated_at) VALUES ("
                + (code != null ? "'" + code + "'" : "NULL") + ", "
                + "'" + nom + "', "
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

    // ----------------------------------------------------------
    // Constructeur SELECT ID
    // ----------------------------------------------------------
    public M_Role(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        String sql = "SELECT * FROM mcd_roles WHERE id = " + id + ";";
        ResultSet res = db.sqlSelect(sql);

        if (!res.first()) {
            throw new SQLException("Aucun rôle trouvé avec id " + id);
        }

        this.code = res.getString("code");
        this.nom = res.getString("nom");
        this.commentaire = res.getString("commentaire");

        Timestamp c = res.getTimestamp("created_at");
        this.created_at = (c != null) ? c.toLocalDateTime() : null;

        Timestamp u = res.getTimestamp("updated_at");
        this.updated_at = (u != null) ? u.toLocalDateTime() : null;
    }

    // ----------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------
    public void update() throws SQLException {
        String sql = "UPDATE mcd_roles SET "
                + "code=" + (code != null ? "'" + code + "'" : "NULL") + ", "
                + "nom='" + nom + "', "
                + "commentaire=" + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "updated_at=CURRENT_TIMESTAMP() "
                + "WHERE id=" + id + ";";

        db.sqlExec(sql);
    }

    // ----------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------
    public void delete() throws SQLException {
        db.sqlExec("DELETE FROM mcd_roles WHERE id=" + id + ";");
    }

    // ----------------------------------------------------------
    // GET RECORDS
    // ----------------------------------------------------------
    public static LinkedHashMap<Integer, M_Role> getRecords(Db_mariadb db, String where) throws SQLException {
        LinkedHashMap<Integer, M_Role> map = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_roles WHERE " + where + " ORDER BY nom;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            int id = res.getInt("id");

            Timestamp c = res.getTimestamp("created_at");
            Timestamp u = res.getTimestamp("updated_at");

            M_Role r = new M_Role(
                    db,
                    id,
                    res.getString("code"),
                    res.getString("nom"),
                    res.getString("commentaire"),
                    (c != null ? c.toLocalDateTime() : null),
                    (u != null ? u.toLocalDateTime() : null)
            );

            map.put(id, r);
        }

        return map;
    }

    public static LinkedHashMap<Integer, M_Role> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1=1");
    }

    @Override
    public String toString() {
        return "Role{id=" + id
                + ", code='" + code + '\''
                + ", nom='" + nom + '\''
                + ", commentaire='" + commentaire + '\''
                + ", created_at=" + created_at
                + ", updated_at=" + updated_at + "}\n";
    }

    // ----------------------------------------------------------
    // TESTS
    // ----------------------------------------------------------
//    public static void main(String[] args) throws Exception {
//
//        Db_mariadb base = new Db_mariadb(Cl_Connection.url, Cl_Connection.login, Cl_Connection.password);
//
//        String codeTest = "R" + System.currentTimeMillis();
//
//        System.out.println("=== INSERT ===");
//        M_Role r1 = new M_Role(base, codeTest, "ROLE_TEST", "Commentaire ici");
//        System.out.println(r1);
//
//        System.out.println("=== SELECT ===");
//        M_Role r2 = new M_Role(base, r1.getId());
//        System.out.println(r2);
//
//        System.out.println("=== UPDATE ===");
//        r2.setNom("ROLE_MODIFIE");
//        r2.update();
//
//        M_Role r3 = new M_Role(base, r2.getId());
//        System.out.println(r3);
//
//        System.out.println("=== GET RECORDS ===");
//        for (M_Role r : M_Role.getRecords(base).values()) {
//            System.out.println(r);
//        }
//
//        System.out.println("=== DELETE ===");
//        r3.delete();
//
//        base.close();
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setNom(String nom) {
//        this.nom = nom;
//    }
}
