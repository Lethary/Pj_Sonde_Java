package pj_sonde.Model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_Releve {

    private Db_mariadb db;
    private int id;
    private LocalDateTime date_releve;
    private Double valeur;
    private String commentaire;
    private int id_salle;
    private String code_unite;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // Constructeur complet
    public M_Releve(Db_mariadb db, int id,
                    LocalDateTime date_releve,
                    Double valeur,
                    String commentaire,
                    int id_salle,
                    String code_unite,
                    LocalDateTime created_at,
                    LocalDateTime updated_at) {

        this.db = db;
        this.id = id;
        this.date_releve = date_releve;
        this.valeur = valeur;
        this.commentaire = commentaire;
        this.id_salle = id_salle;
        this.code_unite = code_unite;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Constructeur INSERT
    public M_Releve(Db_mariadb db,
                    LocalDateTime date_releve,
                    Double valeur,
                    String commentaire,
                    int id_salle,
                    String code_unite) throws SQLException {

        this.db = db;
        this.date_releve = date_releve;
        this.valeur = valeur;
        this.commentaire = commentaire;
        this.id_salle = id_salle;
        this.code_unite = code_unite;

        String sql = "INSERT INTO mcd_releves (date_releve, valeur, commentaire, created_at, updated_at, id_salle, code_unite) VALUES ("
                + "'" + Timestamp.valueOf(date_releve) + "', "
                + (valeur != null ? valeur : "NULL") + ", "
                + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), "
                + id_salle + ", "
                + "'" + code_unite + "');";

        db.sqlExec(sql);
        ResultSet res = db.sqlLastId();
        if (res.first()) {
            this.id = res.getInt("id");
        }

        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    // Constructeur SELECT
    public M_Releve(Db_mariadb db, int id) throws SQLException {
        this.db = db;
        this.id = id;

        ResultSet res = db.sqlSelect("SELECT * FROM mcd_releves WHERE id=" + id + ";");
        if (!res.first()) {
            throw new SQLException("Relevé introuvable id=" + id);
        }

        Timestamp d = res.getTimestamp("date_releve");
        this.date_releve = (d != null ? d.toLocalDateTime() : null);

        this.valeur = res.getObject("valeur") != null ? res.getDouble("valeur") : null;
        this.commentaire = res.getString("commentaire");
        this.id_salle = res.getInt("id_salle");
        this.code_unite = res.getString("code_unite");

        Timestamp c = res.getTimestamp("created_at");
        this.created_at = (c != null ? c.toLocalDateTime() : null);

        Timestamp u = res.getTimestamp("updated_at");
        this.updated_at = (u != null ? u.toLocalDateTime() : null);
    }

    // UPDATE
    public void update() throws SQLException {
        String sql = "UPDATE mcd_releves SET "
                + "date_releve='" + Timestamp.valueOf(date_releve) + "', "
                + "valeur=" + (valeur != null ? valeur : "NULL") + ", "
                + "commentaire=" + (commentaire != null ? "'" + commentaire + "'" : "NULL") + ", "
                + "id_salle=" + id_salle + ", "
                + "code_unite='" + code_unite + "', "
                + "updated_at=CURRENT_TIMESTAMP() "
                + "WHERE id=" + id + ";";

        db.sqlExec(sql);
    }

    // DELETE
    public void delete() throws SQLException {
        db.sqlExec("DELETE FROM mcd_releves WHERE id=" + id + ";");
    }

    // GET RECORDS
    public static LinkedHashMap<Integer, M_Releve> getRecords(Db_mariadb db, String where) throws SQLException {
        LinkedHashMap<Integer, M_Releve> map = new LinkedHashMap<>();
        ResultSet res = db.sqlSelect("SELECT * FROM mcd_releves WHERE " + where + " ORDER BY date_releve;");

        while (res.next()) {
            int id = res.getInt("id");

            Timestamp d = res.getTimestamp("date_releve");
            Timestamp c = res.getTimestamp("created_at");
            Timestamp u = res.getTimestamp("updated_at");

            Double val = (res.getObject("valeur") != null) ? res.getDouble("valeur") : null;

            M_Releve r = new M_Releve(
                    db,
                    id,
                    (d != null ? d.toLocalDateTime() : null),
                    val,
                    res.getString("commentaire"),
                    res.getInt("id_salle"),
                    res.getString("code_unite"),
                    (c != null ? c.toLocalDateTime() : null),
                    (u != null ? u.toLocalDateTime() : null)
            );

            map.put(id, r);
        }
        return map;
    }

    public static LinkedHashMap<Integer, M_Releve> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1=1");
    }

    @Override
    public String toString() {
        return "Releve{id=" + id
                + ", date_releve=" + date_releve
                + ", valeur=" + valeur
                + ", commentaire='" + commentaire + '\''
                + ", id_salle=" + id_salle
                + ", code_unite='" + code_unite + '\''
                + ", created_at=" + created_at
                + ", updated_at=" + updated_at + "}\n";
    }

    // TESTS
//    public static void main(String[] args) throws Exception {
//        Db_mariadb base = new Db_mariadb(Cl_Connection.url, Cl_Connection.login, Cl_Connection.password);
//
//        System.out.println("=== INSERT RELEVE ===");
//        // ATTENTION : id_salle=1 doit exister, code_unite='T' aussi
//        LocalDateTime now = LocalDateTime.now().withNano(0);
//        M_Releve r1 = new M_Releve(base, now, 12.5, "Premier relevé", 1, "T");
//        System.out.println(r1);
//
//        System.out.println("=== SELECT RELEVE ===");
//        M_Releve r2 = new M_Releve(base, r1.id);
//        System.out.println(r2);
//
//        System.out.println("=== UPDATE RELEVE ===");
//        r2.valeur = 15.0;
//        r2.commentaire = "Valeur modifiée";
//        r2.update();
//        M_Releve r3 = new M_Releve(base, r2.id);
//        System.out.println(r3);
//
//        System.out.println("=== GET RECORDS RELEVE ===");
//        for (M_Releve rr : M_Releve.getRecords(base).values()) {
//            System.out.println(rr);
//        }
//
//        // r3.delete();
//
//        base.close();
//    }
}
