/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pj_sonde;

import pj_sonde.Controler.C_Sonde;
import java.util.LinkedHashMap;
import pj_sonde.Model.*;
import pj_sonde.Controler.C_Batiment;
import pj_sonde.Controler.C_Salle;
import static pj_sonde.Model.M_Autoriser.*;

/**
 *
 * @author kevin
 */
public class C_Main {

    /**
     * @param args the command line arguments
     */
    private Db_mariadb baseSonde;

    private final V_Main frm_Main;

    private final C_Sonde gestionSonde;
    private final C_Batiment gestionbBatiment;
    private final C_Salle gestionSalle;

    private M_User UtilConnecte;

    private LinkedHashMap<Integer, M_Sonde> lesSondes;
    private LinkedHashMap<Integer, M_Type> lesTypes;
    private LinkedHashMap<String, M_Unite> lesUnites;
    private LinkedHashMap<Integer, M_Batiment> lesBatiments;

    public C_Main() throws Exception {
        connection();
        frm_Main = new V_Main(this);
        gestionSonde = new C_Sonde(frm_Main, baseSonde);
        gestionbBatiment = new C_Batiment(frm_Main, baseSonde);
        gestionSalle = new C_Salle(frm_Main, baseSonde);

        frm_Main.afficher(gestionSonde, gestionbBatiment, gestionSalle);

    }

    private void connection() throws Exception {
        baseSonde = new Db_mariadb(Cl_Connection.host, Cl_Connection.user, Cl_Connection.password);
    }

    public void close() {
        if (baseSonde != null) {
            baseSonde.close();
        }
    }

    public LinkedHashMap<Integer, M_Autorisation> getAutorisation(int idRole) throws Exception {
        return getLesAutorisations(baseSonde, idRole);
    }

    public M_User verifLogin(String mdp, String login) throws Exception {
        M_User Util = null;
        UtilConnecte = M_User.connexion_log(baseSonde, mdp, login);
        if (UtilConnecte != null) {
            Util = UtilConnecte;
        }
        return Util;
    }

    public M_User deconnection() {
        UtilConnecte = null;
        return UtilConnecte;
    }

    public static void main(String[] args) throws Exception {
        
        C_Main leControleur = new C_Main();
    }

}
