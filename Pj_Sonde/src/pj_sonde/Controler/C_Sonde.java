/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pj_sonde.Controler;

import pj_sonde.V_Main;
import java.sql.SQLException;
import pj_sonde.View.*;
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
    private M_Sonde uneSonde;
    private final Db_mariadb baseSonde;
    private LinkedHashMap<Integer, M_Sonde> lesSondes;
    private LinkedHashMap<Integer, M_Type> lesTypes;
    private LinkedHashMap<String, M_Unite> lesUnites;
    
    public C_Sonde(V_Main frm_Main, Db_mariadb baseSonde) {
        this.baseSonde = baseSonde;
        this.frm_CMS_Sonde = new V_CMS_Sonde(frm_Main, true);
    }

    public void aff_CMS_Sonde() throws SQLException {
        lesSondes = M_Sonde.getRecords(baseSonde);
        lesTypes = M_Type.getRecords(baseSonde);
        lesUnites = M_Unite.getRecords(baseSonde);
        frm_CMS_Sonde.aff_CMS_Sonde(this, uneSonde, baseSonde, lesSondes, lesTypes, lesUnites);
    }
}
