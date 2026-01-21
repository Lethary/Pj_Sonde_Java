/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pj_sonde;

import pj_sonde.Controler.C_Sonde;
import java.util.LinkedHashMap;
import pj_sonde.Db_mariadb;
import pj_sonde.Model.*;
import pj_sonde.View.*;
import pj_sonde.Controler.C_Batiment;

/**
 *
 * @author kevin
 */
public class C_Main {

    /**
     * @param args the command line arguments
     */
    private final V_Main frm_Main;
    private M_Sonde uneSonde;
    private Db_mariadb baseSonde;
    private LinkedHashMap<Integer, M_Sonde> lesSondes;
    private LinkedHashMap<Integer, M_Type> lesTypes;
    private LinkedHashMap<String, M_Unite> lesUnites;
    private C_Sonde gestionSonde;
    private C_Batiment gestionbBatiment;

    public C_Main() throws Exception {
        connection();
        frm_Main = new V_Main(this);
        gestionSonde = new C_Sonde(frm_Main, baseSonde);
        gestionbBatiment = new C_Batiment(frm_Main, baseSonde);
        frm_Main.afficher(gestionSonde, gestionbBatiment);

    }

    private void connection() throws Exception {
        baseSonde = new Db_mariadb(Cl_Connection.host, Cl_Connection.user, Cl_Connection.password);

    }

    public void close() {
        if (baseSonde != null) {
            baseSonde.close();
        }
    }

    public static void main(String[] args) throws Exception {
        C_Main leControleur = new C_Main();
    }

}
