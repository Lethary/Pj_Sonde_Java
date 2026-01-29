package pj_sonde.Model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_Situer {

    private Db_mariadb db;
    private int id_sonde;
    private int id_salle;
    private LocalDate date_debut;
    private LocalDate date_fin;
    private Integer frequence_releve;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // Constructeur complet
    public M_Situer(Db_mariadb db,
                    int id_sonde,
                    int id_salle,
                    LocalDate date_debut,
                    LocalDate date_fin,
                    Integer frequence_releve,
                    LocalDateTime created_at,
                    LocalDateTime updated_at) {
        this.db = db;
        this.id_sonde = id_sonde;
        this.id_salle = id_salle;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.frequence_releve = frequence_releve;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Constructeur INSERT
    public M_Situer(Db_mariadb db,
                    int id_sonde,
                    int id_salle,
                    LocalDate date_debut,
                    LocalDate date_fin,
                    Integer frequence_releve) throws SQLException {

        this.db = db;
        this.id_sonde = id_sonde;
        this.id_salle = id_salle;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.frequence_releve = frequence_releve;

        String sql = "INSERT INTO mcd_situer "
                + "(id_sonde, id_salle, date_debut, date_fin, frequence_releve, created_at, updated_at) VALUES ("
                + id_sonde + ", "
                + id_salle + ", "
                + (date_debut != null ? "'" + Date.valueOf(date_debut) + "'" : "NULL") + ", "
                + (date_fin != null ? "'" + Date.valueOf(date_fin) + "'" : "NULL") + ", "
                + (frequence_releve != null ? frequence_releve : "NULL") + ", "
                + "CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());";

        db.sqlExec(sql);

        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    // Constructeur SELECT
    public M_Situer(Db_mariadb db, int id_sonde, int id_salle) throws SQLException {
        this.db = db;
        this.id_sonde = id_sonde;
        this.id_salle = id_salle;

        String sql = "SELECT * FROM mcd_situer WHERE id_sonde=" + id_sonde + " AND id_salle=" + id_salle + ";";
        ResultSet res = db.sqlSelect(sql);

        if (!res.first()) {
            throw new SQLException("Lien sonde/salle introuvable");
        }

        Date dDeb = res.getDate("date_debut");
        this.date_debut = (dDeb != null ? dDeb.toLocalDate() : null);

        Date dFin = res.getDate("date_fin");
        this.date_fin = (dFin != null ? dFin.toLocalDate() : null);

        this.frequence_releve = res.getObject("frequence_releve") != null ? res.getInt("frequence_releve") : null;

        Timestamp c = res.getTimestamp("created_at");
        this.created_at = (c != null ? c.toLocalDateTime() : null);

        Timestamp u = res.getTimestamp("updated_at");
        this.updated_at = (u != null ? u.toLocalDateTime() : null);
    }

    // UPDATE
    public void update() throws SQLException {
        String sql = "UPDATE mcd_situer SET "
                + "date_debut=" + (date_debut != null ? "'" + Date.valueOf(date_debut) + "'" : "NULL") + ", "
                + "date_fin=" + (date_fin != null ? "'" + Date.valueOf(date_fin) + "'" : "NULL") + ", "
                + "frequence_releve=" + (frequence_releve != null ? frequence_releve : "NULL") + ", "
                + "updated_at=CURRENT_TIMESTAMP() "
                + "WHERE id_sonde=" + id_sonde + " AND id_salle=" + id_salle + ";";

        db.sqlExec(sql);
    }

    // DELETE
    public void delete() throws SQLException {
        String sql = "DELETE FROM mcd_situer WHERE id_sonde=" + id_sonde + " AND id_salle=" + id_salle + ";";
        db.sqlExec(sql);
    }

    // GET RECORDS
    public static LinkedHashMap<String, M_Situer> getRecords(Db_mariadb db, String where) throws SQLException {
        LinkedHashMap<String, M_Situer> map = new LinkedHashMap<>();

        String sql = "SELECT * FROM mcd_situer WHERE " + where + " ORDER BY id_sonde, id_salle;";
        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            int id_sonde = res.getInt("id_sonde");
            int id_salle = res.getInt("id_salle");

            Date dDeb = res.getDate("date_debut");
            LocalDate lDeb = (dDeb != null ? dDeb.toLocalDate() : null);

            Date dFin = res.getDate("date_fin");
            LocalDate lFin = (dFin != null ? dFin.toLocalDate() : null);

            Integer freq = res.getObject("frequence_releve") != null ? res.getInt("frequence_releve") : null;

            Timestamp c = res.getTimestamp("created_at");
            LocalDateTime lC = (c != null ? c.toLocalDateTime() : null);

            Timestamp u = res.getTimestamp("updated_at");
            LocalDateTime lU = (u != null ? u.toLocalDateTime() : null);

            M_Situer s = new M_Situer(
                    db,
                    id_sonde,
                    id_salle,
                    lDeb,
                    lFin,
                    freq,
                    lC,
                    lU
            );

            String key = id_sonde + "-" + id_salle;
            map.put(key, s);
        }

        return map;
    }

    public static LinkedHashMap<String, M_Situer> getRecords(Db_mariadb db) throws SQLException {
        return getRecords(db, "1=1");
    }

    @Override
    public String toString() {
        return "Situer{id_sonde=" + id_sonde
                + ", id_salle=" + id_salle
                + ", date_debut=" + date_debut
                + ", date_fin=" + date_fin
                + ", frequence_releve=" + frequence_releve
                + ", created_at=" + created_at
                + ", updated_at=" + updated_at + "}\n";
    }

    // TESTS
//    public static void main(String[] args) throws Exception {
//        Db_mariadb base = new Db_mariadb(Cl_Connection.url, Cl_Connection.login, Cl_Connection.password);
//
//        // ATTENTION : id_sonde=1 et id_salle=1 doivent exister
//        System.out.println("=== INSERT SITUER ===");
//        M_Situer s1 = new M_Situer(
//                base,
//                1, // id_sonde
//                1, // id_salle
//                LocalDate.now(),
//                null,
//                60
//        );
//        System.out.println(s1);
//
//        System.out.println("=== SELECT SITUER ===");
//        M_Situer s2 = new M_Situer(base, 1, 1);
//        System.out.println(s2);
//
//        System.out.println("=== UPDATE SITUER ===");
//        s2.frequence_releve = 120;
//        s2.update();
//        M_Situer s3 = new M_Situer(base, 1, 1);
//        System.out.println(s3);
//
//        System.out.println("=== GET RECORDS SITUER ===");
//        for (M_Situer sit : M_Situer.getRecords(base).values()) {
//            System.out.println(sit);
//        }
//
//        // s3.delete();
//
//        base.close();
//    }
}
