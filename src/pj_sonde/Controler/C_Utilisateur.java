/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pj_sonde.Controler;

import java.util.LinkedHashMap;
import pj_sonde.Db_mariadb;
import pj_sonde.Model.M_Role;
import pj_sonde.Model.M_Salle;
import pj_sonde.Model.M_User;
import pj_sonde.View.V_CMS_Utilisateur;
import pj_sonde.V_Main;
import pj_sonde.View.V_A_Utilisateur;

/**
 *
 * @author kevin
 */
public class C_Utilisateur {

    private final Db_mariadb baseUtilisateur;
    
    private final V_CMS_Utilisateur frm_CMS_Utilisateur;
    private final V_A_Utilisateur frm_A_Utilisateur;
    
    private M_User unUtilisateur;
    private LinkedHashMap<Integer, M_Role> lesRoles;
    
    Integer uneCle;
    int idRole;

    private LinkedHashMap<Integer, M_User> lesUtilisateurs;

    public C_Utilisateur(V_Main frm_Main, Db_mariadb baseSonde) {
        this.baseUtilisateur = baseSonde;
        this.frm_CMS_Utilisateur = new V_CMS_Utilisateur(frm_Main, true);
        this.frm_A_Utilisateur = new V_A_Utilisateur(frm_Main, true);
    }

    public void aff_CMS_Utilisateur(int idRole) throws Exception {
        this.idRole = idRole;
        lesUtilisateurs = M_User.getRecords(baseUtilisateur);
        lesRoles = M_Role.getRecords(baseUtilisateur);
        frm_CMS_Utilisateur.aff_CMS_Utilisateur(this,unUtilisateur,lesUtilisateurs, lesRoles,idRole);
    }

    public void aff_A_Utilisateur() throws Exception {
        lesUtilisateurs = M_User.getRecords(baseUtilisateur);
        lesRoles = M_Role.getRecords(baseUtilisateur);
        frm_A_Utilisateur.aff_A_Utilisateur(this, lesRoles);
    }

    public void add_Utilisateur(String name, String email, String password, String commentaire, int id_Role) throws Exception {
        unUtilisateur = new M_User(baseUtilisateur, name, email, password, commentaire, id_Role);
        lesUtilisateurs.put(uneCle, unUtilisateur);
    }
    
    public void supp_Utilisateur(int idUtilisateur)throws Exception{
        unUtilisateur = new M_User(baseUtilisateur, idUtilisateur);
        unUtilisateur.delete();
        aff_CMS_Utilisateur(idRole);  
    }

    
    public void modif_Utilisateur_Password(int idUtilisateur, String nom, String email, String commentaire, String password, int idRole)throws Exception{
        unUtilisateur = new M_User(baseUtilisateur, idUtilisateur);
        unUtilisateur.setName(nom);
        unUtilisateur.setEmail(email);
        unUtilisateur.setCommentaire(commentaire);
        unUtilisateur.setId_role(idRole);
        unUtilisateur.setPassword(password);
        unUtilisateur.updateWithPassword();
        aff_CMS_Utilisateur(idRole);
    }
    
    public void modif_Utilisateur_No_Password(int idUtilisateur, String nom, String email, String commentaire, int idRole)throws Exception{
        unUtilisateur = new M_User(baseUtilisateur, idUtilisateur);
        unUtilisateur.setName(nom);
        unUtilisateur.setEmail(email);
        unUtilisateur.setCommentaire(commentaire);
        unUtilisateur.setId_role(idRole);
        unUtilisateur.updateWithoutPassword();
        aff_CMS_Utilisateur(idRole);
    }
    
    

    public boolean utilisateurExiste(String email) throws Exception {
        return M_User.existe(baseUtilisateur, email);
    }
    
    public boolean utilisateurExisteModification(int idUtilisateur, String email) throws Exception {
        return M_User.existeModification(baseUtilisateur, idUtilisateur, email);
    }
//    
//    public boolean batimentExisteModification(int idBatiment, String code, String libelle) throws Exception{
//        return M_User.existeModification(baseUtilisateur, idBatiment, code, libelle);
//    }
}
