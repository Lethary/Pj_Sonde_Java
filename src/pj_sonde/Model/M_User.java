package pj_sonde.Model;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_User {

    private Db_mariadb db;
    private int id;
    private String name;
    private String email;
    private String password;
    private String commentaire;
    private Integer id_role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // ----------------------------------------------------------
    // CONSTRUCTEUR COMPLET
    // ----------------------------------------------------------
    public M_User(Db_mariadb db, int id, String name, String email, String password, String commentaire, Integer id_role,
            LocalDateTime created_at, LocalDateTime updated_at) {

        this.db = db;
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.commentaire = commentaire;
        this.id_role = id_role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // ----------------------------------------------------------
    // CONSTRUCTEUR INSERT
    // ----------------------------------------------------------
    public M_User(Db_mariadb db,
            String name,
            String email,
            String password,
            String remember_token,
            String commentaire,
            Integer id_role) throws SQLException {

        this.db = db;
        this.name = name;
        this.email = email;
        this.password = password;
        this.commentaire = commentaire;
        this.id_role = id_role;

        String sql = "INSERT INTO mcd_users "
                + "(name, email, password, remember_token, commentaire, id_role, created_at, updated_at) VALUES ("
                + "'" + name + "', "
                + "'" + email + "', "
                + "'" + password + "', "
                + (remember_token != null ? "'" + remember_token + "'" : "NULL") + ", "
                + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + id_role + ", "
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
    // CONSTRUCTEUR SELECT ID
    // ----------------------------------------------------------
    public M_User(Db_mariadb db, int id) throws SQLException {

        this.db = db;
        this.id = id;

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_users WHERE id=" + id + ";");

        if (!res.first()) {
            throw new SQLException("Utilisateur introuvable");
        }

        this.name = res.getString("name");
        this.email = res.getString("email");
        this.password = res.getString("password");
        this.commentaire = res.getString("commentaire");
        this.id_role = res.getInt("id_role");

        Timestamp c = res.getTimestamp("created_at");
        this.created_at = (c != null ? c.toLocalDateTime() : null);

        Timestamp u = res.getTimestamp("updated_at");
        this.updated_at = (u != null ? u.toLocalDateTime() : null);
    }

    // ----------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------
    public void update() throws SQLException {
        String sql = "UPDATE mcd_users SET "
                + "name='" + name + "', "
                + "email='" + email + "', "
                + "password='" + password + "', "
                + "commentaire=" + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "id_role=" + id_role + ", "
                + "updated_at=CURRENT_TIMESTAMP() "
                + "WHERE id=" + id + ";";

        db.sqlExec(sql);
    }

    // ----------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------
    public void delete() throws SQLException {
        db.sqlExec("DELETE FROM mcd_users WHERE id=" + id + ";");
    }

    // ----------------------------------------------------------
    // GET RECORDS
    // ----------------------------------------------------------
    public static LinkedHashMap<Integer, M_User> getRecords(Db_mariadb db, String where) throws SQLException {

        LinkedHashMap<Integer, M_User> map = new LinkedHashMap<>();

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_users WHERE " + where + " ORDER BY name;");

        while (res.next()) {

            Timestamp e = res.getTimestamp("email_verified_at");
            Timestamp c = res.getTimestamp("created_at");
            Timestamp u = res.getTimestamp("updated_at");

            M_User s = new M_User(
                    db,
                    res.getInt("id"),
                    res.getString("name"),
                    res.getString("email"),
                    res.getString("password"),
                    res.getString("commentaire"),
                    res.getInt("id_role"),
                    (c != null ? c.toLocalDateTime() : null),
                    (u != null ? u.toLocalDateTime() : null)
            );

            map.put(res.getInt("id"), s);
        }

        return map;
    }

    public static LinkedHashMap<Integer, M_User> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1=1");
    }

    public static M_User connexion_log(Db_mariadb db, String mp, String login) throws Exception {
        String sql;
        M_User unUtil = null;
        sql = "SELECT * FROM mcd_users WHERE name = '" + login + "';";
        ResultSet res;
        res = db.sqlSelect(sql);
        if (res.next()) {
            String mpHash = res.getString("password");

            if (BCrypt.verifyer().verify(mp.toCharArray(), mpHash).verified) {
                int id = res.getInt("id");
                unUtil = new M_User(db, id);
            }
        }

        res.close();

        return unUtil;
    }

    @Override
    public String toString() {
        return "User{id=" + id
                + ", name='" + name + '\''
                + ", email='" + email + '\''
                + ", role=" + id_role
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
//        System.out.println("=== INSERT ===");
//        M_User u1 = new M_User(base,
//                "User Test",
//                "test" + System.currentTimeMillis() + "@mail.com",
//                "password",
//                null,
//                "commentaire ici",
//                1  // doit exister dans mcd_roles
//        );
//        System.out.println(u1);
//
//        System.out.println("=== SELECT ===");
//        M_User u2 = new M_User(base, u1.getId());
//        System.out.println(u2);
//
//        System.out.println("=== UPDATE ===");
//        u2.setName("User Modifié");
//        u2.update();
//        M_User u3 = new M_User(base, u2.getId());
//        System.out.println(u3);
//
//        System.out.println("=== GET RECORDS ===");
//        for (M_User u : M_User.getRecords(base).values()) {
//            System.out.println(u);
//        }
//
//        System.out.println("=== DELETE ===");
//        u3.delete();
//
//        base.close();
//    }
//
//    // -------- GETTERS / SETTERS --------
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Integer getId_role() {
        return id_role;
    }

    public void setId_role(Integer id_role) {
        this.id_role = id_role;
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
