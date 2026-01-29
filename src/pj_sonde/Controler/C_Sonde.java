/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pj_sonde.Controler;

import pj_sonde.V_Main;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import pj_sonde.View.V_A_Sonde;
import pj_sonde.View.V_CMS_Sonde;
import pj_sonde.Model.*;

import java.util.LinkedHashMap;
import pj_sonde.Db_mariadb;

/**
 *
 * @author kevin
 */
public class C_Sonde {

    /**
     * @param args the command line arguments
     */
    private final V_CMS_Sonde frm_CMS_Sonde;
    private final V_A_Sonde frm_A_Sonde;

    private M_Sonde uneSonde;
    private final Db_mariadb db;
    private Integer uneCle;

    private int idRole;

    private LinkedHashMap<Integer, M_Sonde> lesSondes;
    private LinkedHashMap<Integer, M_Type> lesTypes;
    private LinkedHashMap<String, M_Unite> lesUnites;
    private LinkedHashMap<Integer, M_Batiment> lesBatiments;
    private LinkedHashMap<Integer, M_Salle> lesSalles;

    public C_Sonde(V_Main frm_Main, Db_mariadb db) {
        this.db = db;
        this.frm_CMS_Sonde = new V_CMS_Sonde(frm_Main, true);
        this.frm_A_Sonde = new V_A_Sonde(frm_Main, true);
    }

    public void aff_CMS_Sonde(int idRole) throws SQLException {
        this.idRole = idRole;
        lesSondes = M_Sonde.getRecords(db);
        lesTypes = M_Type.getRecords(db);
        lesUnites = M_Unite.getRecords(db);
        frm_CMS_Sonde.aff_CMS_Sonde(this, uneSonde, db, lesSondes, lesTypes, lesUnites, idRole);
    }

    public void aff_A_Sonde() throws Exception {
        lesSondes = M_Sonde.getRecords(db);
        lesBatiments = M_Batiment.getRecords(db);
        lesSalles = M_Salle.getRecords(db);
        lesTypes = M_Type.getRecords(db);
        lesUnites = M_Unite.getRecords(db);
        frm_A_Sonde.aff_A_Sonde(this, lesBatiments, lesSalles, lesUnites, lesTypes);
    }

    public void add_Sonde(String code, String nom, String adresse_ip, String adresse_mac, LocalDate date_achat, String commentaire, int idType, String codeUnite) throws SQLException {
        uneSonde = new M_Sonde(db, code, nom, adresse_ip, adresse_mac, date_achat, commentaire, idType, codeUnite);
        lesSondes.put(uneCle, uneSonde);
    }
}
