package pj_sonde.Model;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;

public class M_Autoriser {

    private final Db_mariadb db;
    private final int idAutorisation,idRole;

    public M_Autoriser(Db_mariadb db, int idAutorisation, int idRole) {
        this.db = db;
        this.idAutorisation = idAutorisation;
        this.idRole = idRole;
    }

    public int getIdAutorisation() {
        return idAutorisation;
    }

    public int getIdRole() {
        return idRole;
    }

    /* =====================================================
       RÉCUPÉRER LES AUTORISATIONS D’UN RÔLE (MENU / DROITS)
       ===================================================== */
    public static LinkedHashMap<Integer, M_Autorisation> getLesAutorisations(Db_mariadb db, int idRole) throws Exception {

        LinkedHashMap<Integer, M_Autorisation> liste = new LinkedHashMap<>();

        String sql = """
            SELECT a.id, a.code
            FROM mcd_autorisations a
            INNER JOIN mcd_autoriser ar
                ON a.id = ar.id_autorisation
            WHERE ar.id_roles = """ + idRole + """
            ORDER BY a.id
        """;

        ResultSet res = db.sqlSelect(sql);

        while (res.next()) {
            int id = res.getInt("id");
            String code = res.getString("code");

            liste.put(id, new M_Autorisation(db, id, code, null));
        }

        res.close();
        return liste;
    }
    public static LinkedHashMap<Integer, M_Autoriser> getRecords(Db_mariadb db) throws Exception {
        LinkedHashMap<Integer, M_Autoriser> liste = new LinkedHashMap<>();

        String sql = "SELECT id_autorisation, id_roles FROM mcd_autoriser";
        ResultSet res = db.sqlSelect(sql);

        int cle = 0;
        while (res.next()) {
            cle++;
            liste.put(cle,
                    new M_Autoriser(
                            db,
                            res.getInt("id_autorisation"),
                            res.getInt("id_roles")
                    )
            );
        }

        res.close();
        return liste;
    }

    @Override
    public String toString() {
        return "Autoriser{idAutorisation=" + idAutorisation
                + ", idRole=" + idRole + "}";
    }
    
        public static void main(String[] args) throws Exception {
        Db_mariadb maBase = new Db_mariadb(Cl_Connection.host, Cl_Connection.user, Cl_Connection.password);
//        M_Autoriser unAutoriser;
//
//        unAutoriser = new M_Autoriser(maBase, 2, 3);
//        System.out.println(unAutoriser.toString());

//        LinkedHashMap<Integer, M_Autoriser> dicoAutoriser;
//        M_Autoriser unAutoriser;
//        
//        dicoAutoriser = getRecords(maBase);
//        for (Integer uneCle : dicoAutoriser.keySet()) {
//            unAutoriser = dicoAutoriser.get(uneCle);
//            System.out.println(unAutoriser);
//       }
//    
        LinkedHashMap<Integer, M_Autorisation> listeAutorisation;
        M_Autorisation uneAutorisation;
        
        listeAutorisation = getLesAutorisations(maBase,2);
        for (Integer uneCle : listeAutorisation.keySet()) {
            uneAutorisation = listeAutorisation.get(uneCle);
            System.out.println(uneAutorisation);
       }
    }
}
