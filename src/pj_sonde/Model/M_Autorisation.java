package pj_sonde.Model;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import pj_sonde.Db_mariadb;

public class M_Autorisation {

    private final Db_mariadb db;
    private final int idAutorisation;
    private final String code, description;

    public M_Autorisation(Db_mariadb db, int idAutorisation, String code, String description) {
        this.db = db;
        this.idAutorisation = idAutorisation;
        this.code = code;
        this.description = description;
    }

    public int getIdAutorisation() {
        return idAutorisation;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /* ==============================
       GET ALL AUTORISATIONS
       ============================== */
    public static LinkedHashMap<Integer, M_Autorisation> getRecords(Db_mariadb db) throws Exception {
        LinkedHashMap<Integer, M_Autorisation> liste = new LinkedHashMap<>();
        String sql = """
            SELECT id, code, description
            FROM mcd_autorisations
            ORDER BY code
        """;
        try (ResultSet res = db.sqlSelect(sql)) {
            while (res.next()) {
                int id = res.getInt("id");
                String code = res.getString("code");
                String description = res.getString("description");

                liste.put(id, new M_Autorisation(db, id, code, description));
            }
        }
        return liste;
    }

    @Override
    public String toString() {
        return "Autorisation{id=" + idAutorisation
                + ", code='" + code + '\''
                + ", description='" + description + "'}";
    }
}
