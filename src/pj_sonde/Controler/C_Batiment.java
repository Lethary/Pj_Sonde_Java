/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pj_sonde.Controler;

import java.util.LinkedHashMap;
import pj_sonde.Db_mariadb;
import pj_sonde.Model.M_Batiment;
import pj_sonde.View.V_CMS_Batiment;
import pj_sonde.V_Main;
import pj_sonde.View.V_A_Batiment;

/**
 *
 * @author kevin
 */
public class C_Batiment {

    private final Db_mariadb baseBatiment;
    
    private final V_CMS_Batiment frm_CMS_Batiment;
    private final V_A_Batiment frm_A_Batiment;
    
    private M_Batiment unBatiment;
    
    Integer uneCle;
    int idRole;

    private LinkedHashMap<Integer, M_Batiment> lesBatiments;

    public C_Batiment(V_Main frm_Main, Db_mariadb baseSonde) {
        this.baseBatiment = baseSonde;
        this.frm_CMS_Batiment = new V_CMS_Batiment(frm_Main, true);
        this.frm_A_Batiment = new V_A_Batiment(frm_Main, true);
    }

    public void aff_CMS_Batiment(int idRole) throws Exception {
        this.idRole = idRole;
        lesBatiments = M_Batiment.getRecords(baseBatiment);
        frm_CMS_Batiment.aff_CMS_Batiment(this,unBatiment,lesBatiments,idRole);
    }

    public void aff_A_Batiment() throws Exception {
        lesBatiments = M_Batiment.getRecords(baseBatiment);
        frm_A_Batiment.aff_A_Batiment(this);
    }

    public void add_Batiment(String code, String libelle, String commentaire) throws Exception {
        unBatiment = new M_Batiment(baseBatiment, code, libelle, commentaire);
        lesBatiments.put(uneCle, unBatiment);
    }
    
    public void supp_Batiment(int idBatiment)throws Exception{
        unBatiment = new M_Batiment(baseBatiment, idBatiment);
        unBatiment.delete();
        aff_CMS_Batiment(idRole);  
    }
    
    public void modif_batiment(int idBatiment, String code, String libelle, String commentaire)throws Exception{
        unBatiment = new M_Batiment(baseBatiment, idBatiment);
        unBatiment.setCode(code);
        unBatiment.setLibelle(libelle);
        unBatiment.setCommentaire(commentaire);
        unBatiment.update();
        aff_CMS_Batiment(idRole);
    }

    public boolean batimentExiste(String code, String libelle) throws Exception {
        return M_Batiment.existe(baseBatiment, code, libelle);
    }
    
    public boolean batimentExisteModification(int idBatiment, String code, String libelle) throws Exception{
        return M_Batiment.existeModification(baseBatiment, idBatiment, code, libelle);
    }
}
