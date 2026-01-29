/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pj_sonde.Controler;

import java.util.LinkedHashMap;
import pj_sonde.Cl_Connection;
import pj_sonde.Db_mariadb;
import pj_sonde.Model.M_Batiment;
import pj_sonde.Model.M_Salle;
import pj_sonde.V_Main;
import pj_sonde.View.V_A_Salle;
import pj_sonde.View.V_CMS_Salle;

/**
 *
 * @author kevin
 */
public class C_Salle {

    private final V_A_Salle frm_A_Salle;
    private final V_CMS_Salle frm_CMS_Salle;
    
    private final Db_mariadb db;
    
    private Integer uneCle;
    
    private LinkedHashMap<Integer, M_Batiment> lesBatiments;
    private LinkedHashMap<Integer, M_Salle> lesSalles;
    private M_Salle uneSalle;
    
    private int idRole;

    public C_Salle(V_Main frm_Main, Db_mariadb db) {
        this.db = db;
        this.frm_A_Salle = new V_A_Salle(frm_Main, true);
        this.frm_CMS_Salle = new V_CMS_Salle(frm_Main, true);
    }

    public void aff_A_Salle() throws Exception {
        lesSalles = M_Salle.getRecords(db);
        lesBatiments = M_Batiment.getRecords(db);
        frm_A_Salle.aff_A_Salle(this, lesSalles, lesBatiments);
    }

    public void aff_CMS_Salle(int idRole) throws Exception {
        this.idRole = idRole;
        lesSalles = M_Salle.getRecords(db);
        lesBatiments = M_Batiment.getRecords(db);
        frm_CMS_Salle.aff_CMS_Salle(this, uneSalle, db, lesSalles, lesBatiments, idRole);
    }

    public void add_Salle(String code, String libelle, String commentaire, int idBatiment) throws Exception {
        uneSalle = new M_Salle(db, code, libelle, commentaire, idBatiment);
        lesSalles.put(uneCle, uneSalle);
    }

    public void edit_Salle(int idSalle, String code, int idBatiment, String libelle, String commentaire) throws Exception {
        uneSalle = new M_Salle(db, idSalle);
        uneSalle.setCode(code);
        uneSalle.setLibelle(libelle);
        uneSalle.setId_batiment(idBatiment);
        uneSalle.setCommentaire(commentaire);
        uneSalle.update();
        aff_CMS_Salle(idRole);
    }

    public void delete_Salle(int idSalle) throws Exception{
        uneSalle = new M_Salle(db, idSalle);
        uneSalle.delete();
        aff_CMS_Salle(idRole);
    }
    
    public boolean salleExiste(String code, String libelle) throws Exception {
        return M_Salle.existe(db, code, libelle);
    }

    public boolean salleExisteModification(int idSalle, String code, String libelle) throws Exception {
        return M_Salle.existeModification(db, idSalle, code, libelle);
    }
}
