/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pj_sonde.View;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import pj_sonde.Model.*;
import pj_sonde.Controler.C_Utilisateur;

/**
 *
 * @author kevin
 */
public class V_CMS_Utilisateur extends javax.swing.JDialog {

    private C_Utilisateur gestionUtilisateur;
    private M_User unUtilisateur;
    private LinkedHashMap<Integer, M_User> lesUtilisateurs;
    private LinkedHashMap<Integer, M_Role> lesRoles;
    private int idUtilisateur;
    private boolean modeEdition;
    
    private DefaultTableModel dm_tb_utilisateur;
    private DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private boolean selectionListenerInitialized = false;

    public V_CMS_Utilisateur(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void aff_CMS_Utilisateur(
            C_Utilisateur gestionUtilisateur,
            M_User unUtilisateur,
            LinkedHashMap<Integer, M_User> lesUtilisateurs,
            LinkedHashMap<Integer, M_Role> lesRoles,
            int idRole) {

        this.gestionUtilisateur = gestionUtilisateur;
        this.unUtilisateur = unUtilisateur;
        this.lesUtilisateurs = lesUtilisateurs;
        this.lesRoles = lesRoles;

        setTitle("Consultation, modification et suppression des utilisateurs");
        setSize(1080, 720);
        setLocationRelativeTo(null);

        // État initial
        pn_CMS_Batiment.setVisible(false);
        pn_btn.setVisible(false);
        btn_save.setVisible(false);

        btn_modif.setVisible(idRole != 3);
        btn_supp.setVisible(idRole != 3);

        // Chargement des données$
        affComboBox();
        aff_Tableau();

        setVisible(true);
    }

    public String getNomById(int idRole) {
        // Sécurisation : évite NullPointerException
        M_Role role = lesRoles.get(idRole);
        return (role != null) ? role.getNom(): null;
    }
    private int getIdByNom(String nomRole) {
        int res = -1;
        for (M_Role role : lesRoles.values()) {
            if (role.getNom().equals(nomRole)) {
                res = role.getId();
            }
        }
        return res;
    }
    
    public void affComboBox() {
        cb_role.removeAllItems();
        for (M_Role role : lesRoles.values()) {
            cb_role.addItem(role.getNom());
        }
    }
    
    private void aff_Tableau() {
        int ligne = 0;

        tb_utilisateur.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tb_utilisateur.setAutoCreateRowSorter(true);
        tb_utilisateur.setDefaultEditor(Object.class, null);

        dm_tb_utilisateur = (DefaultTableModel) tb_utilisateur.getModel();
        dm_tb_utilisateur.setRowCount(lesUtilisateurs.size());

        // Remplissage du tableau
        for (Integer uneCle : lesUtilisateurs.keySet()) {
            unUtilisateur = lesUtilisateurs.get(uneCle);
            dm_tb_utilisateur.setValueAt(unUtilisateur.getId(), ligne, 0);
            dm_tb_utilisateur.setValueAt(unUtilisateur.getName(), ligne, 1);
            dm_tb_utilisateur.setValueAt(getNomById(unUtilisateur.getId_role()), ligne, 2);
            ligne++;
        }
        ftf_password.setVisible(false);
        lb_password.setVisible(false);
        // Listener ajouté UNE SEULE FOIS
        if (!selectionListenerInitialized) {
            tb_utilisateur.getSelectionModel().addListSelectionListener(e -> {
                int i = tb_utilisateur.getSelectedRow();
                if (i != -1) {

                    int id = (Integer) tb_utilisateur.getValueAt(i, 0);
                    M_User UtilisateurSelected = lesUtilisateurs.get(id);

                    pn_btn.setVisible(true);
                    idUtilisateur = UtilisateurSelected.getId();

                    ftf_id.setText(String.valueOf(idUtilisateur));
                    ftf_name.setText(UtilisateurSelected.getName());
                    ftf_email.setText(UtilisateurSelected.getEmail());

                    String nomRole = getNomById(UtilisateurSelected.getId_role());
                    if (nomRole != null) {
                        cb_role.setSelectedItem(nomRole);
                    }
                    if (UtilisateurSelected.getCommentaire() != null) {
                        pn_commentaire.setVisible(true);
                        ta_commentaire.setText(UtilisateurSelected.getCommentaire());
                    } else {
                        pn_commentaire.setVisible(false);
                        ta_commentaire.setText("");
                    }

                    if (UtilisateurSelected.getCreated_at() != null) {
                        ftf_created_at.setText(
                                UtilisateurSelected.getCreated_at().format(formatterLocalDateTime)
                        );
                    } else {
                        ftf_created_at.setText("");
                    }

                    if (UtilisateurSelected.getUpdated_at() != null) {
                        ftf_updated_at.setText(
                                UtilisateurSelected.getUpdated_at().format(formatterLocalDateTime)
                        );
                    } else {
                        ftf_updated_at.setText("");
                    }
                }
            });

            selectionListenerInitialized = true;
        }
    }

    private void exit() {
        pn_table.setVisible(true);
        pn_btn.setVisible(false);
        tb_utilisateur.clearSelection();
        pn_CMS_Batiment.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pn_CMS_Batiment = new javax.swing.JPanel();
        lb_titre = new javax.swing.JLabel();
        lb_id = new javax.swing.JLabel();
        lb_code = new javax.swing.JLabel();
        lb_nom = new javax.swing.JLabel();
        lb_created_at = new javax.swing.JLabel();
        lb_update_at = new javax.swing.JLabel();
        ftf_name = new javax.swing.JFormattedTextField();
        ftf_id = new javax.swing.JFormattedTextField();
        ftf_email = new javax.swing.JFormattedTextField();
        ftf_created_at = new javax.swing.JFormattedTextField();
        ftf_updated_at = new javax.swing.JFormattedTextField();
        pn_commentaire = new javax.swing.JPanel();
        lb_commentaire = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ta_commentaire = new javax.swing.JTextArea();
        btn_exit = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        lb_password = new javax.swing.JLabel();
        ftf_password = new javax.swing.JFormattedTextField();
        lb_role = new javax.swing.JLabel();
        cb_role = new javax.swing.JComboBox<>();
        pn_table = new javax.swing.JPanel();
        sp_batiment = new javax.swing.JScrollPane();
        tb_utilisateur = new javax.swing.JTable();
        pn_btn = new javax.swing.JPanel();
        btn_details = new javax.swing.JButton();
        btn_modif = new javax.swing.JButton();
        btn_supp = new javax.swing.JButton();
        mb_menu = new javax.swing.JMenuBar();
        mn_fichier = new javax.swing.JMenu();
        mi_fermer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lb_titre.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lb_titre.setText("Détails de l'utilisateur");

        lb_id.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_id.setText("Id :");

        lb_code.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_code.setText("Nom :");

        lb_nom.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_nom.setText("Email :");

        lb_created_at.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_created_at.setText("Créer le :");

        lb_update_at.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_update_at.setText("Mis à jour le :");

        lb_commentaire.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_commentaire.setText("Commentaire :");

        ta_commentaire.setColumns(20);
        ta_commentaire.setRows(5);
        jScrollPane2.setViewportView(ta_commentaire);

        javax.swing.GroupLayout pn_commentaireLayout = new javax.swing.GroupLayout(pn_commentaire);
        pn_commentaire.setLayout(pn_commentaireLayout);
        pn_commentaireLayout.setHorizontalGroup(
            pn_commentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_commentaireLayout.createSequentialGroup()
                .addComponent(lb_commentaire)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
        );
        pn_commentaireLayout.setVerticalGroup(
            pn_commentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_commentaireLayout.createSequentialGroup()
                .addGroup(pn_commentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_commentaire)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        btn_exit.setText("Fermer");
        btn_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exitActionPerformed(evt);
            }
        });

        btn_save.setText("Save");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        lb_password.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_password.setText("Mot de passe :");

        lb_role.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_role.setText("Role :");

        javax.swing.GroupLayout pn_CMS_BatimentLayout = new javax.swing.GroupLayout(pn_CMS_Batiment);
        pn_CMS_Batiment.setLayout(pn_CMS_BatimentLayout);
        pn_CMS_BatimentLayout.setHorizontalGroup(
            pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_CMS_BatimentLayout.createSequentialGroup()
                .addContainerGap(381, Short.MAX_VALUE)
                .addComponent(lb_titre)
                .addGap(381, 381, 381))
            .addGroup(pn_CMS_BatimentLayout.createSequentialGroup()
                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pn_CMS_BatimentLayout.createSequentialGroup()
                        .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_id, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lb_code, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lb_nom, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pn_CMS_BatimentLayout.createSequentialGroup()
                                .addGap(262, 262, 262)
                                .addComponent(lb_password)))
                        .addGap(26, 26, 26)
                        .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ftf_name, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                            .addComponent(ftf_email)
                            .addComponent(ftf_id)
                            .addComponent(ftf_password)))
                    .addGroup(pn_CMS_BatimentLayout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pn_CMS_BatimentLayout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_CMS_BatimentLayout.createSequentialGroup()
                                .addComponent(lb_role)
                                .addGap(18, 18, 18)
                                .addComponent(cb_role, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pn_CMS_BatimentLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(pn_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pn_CMS_BatimentLayout.createSequentialGroup()
                                        .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lb_created_at)
                                            .addComponent(lb_update_at))
                                        .addGap(26, 26, 26)
                                        .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(ftf_created_at, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                                            .addComponent(ftf_updated_at))))))))
                .addContainerGap(423, Short.MAX_VALUE))
        );
        pn_CMS_BatimentLayout.setVerticalGroup(
            pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_CMS_BatimentLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(lb_titre)
                .addGap(82, 82, 82)
                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_id)
                    .addComponent(ftf_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_code)
                    .addComponent(ftf_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_nom)
                    .addComponent(ftf_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_password)
                    .addComponent(ftf_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cb_role, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_role))
                .addGap(21, 21, 21)
                .addComponent(pn_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_created_at)
                    .addComponent(ftf_created_at, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_update_at)
                    .addComponent(ftf_updated_at, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(pn_CMS_BatimentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(118, Short.MAX_VALUE))
        );

        tb_utilisateur.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Name", "Role"
            }
        ));
        tb_utilisateur.getTableHeader().setReorderingAllowed(false);
        sp_batiment.setViewportView(tb_utilisateur);

        btn_details.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btn_details.setText("Détails");
        btn_details.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detailsActionPerformed(evt);
            }
        });

        btn_modif.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btn_modif.setText("Modifier");
        btn_modif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modifActionPerformed(evt);
            }
        });

        btn_supp.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btn_supp.setText("Supprimer");
        btn_supp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suppActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_btnLayout = new javax.swing.GroupLayout(pn_btn);
        pn_btn.setLayout(pn_btnLayout);
        pn_btnLayout.setHorizontalGroup(
            pn_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_btnLayout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addGroup(pn_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_modif)
                    .addComponent(btn_details)
                    .addComponent(btn_supp))
                .addGap(44, 44, 44))
        );
        pn_btnLayout.setVerticalGroup(
            pn_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_btnLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(btn_details)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_modif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_supp)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pn_tableLayout = new javax.swing.GroupLayout(pn_table);
        pn_table.setLayout(pn_tableLayout);
        pn_tableLayout.setHorizontalGroup(
            pn_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_tableLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(sp_batiment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82)
                .addComponent(pn_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(377, Short.MAX_VALUE))
        );
        pn_tableLayout.setVerticalGroup(
            pn_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_tableLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(sp_batiment, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pn_tableLayout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(pn_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mn_fichier.setText("Fichier");

        mi_fermer.setText("Fermer");
        mi_fermer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi_fermerActionPerformed(evt);
            }
        });
        mn_fichier.add(mi_fermer);

        mb_menu.add(mn_fichier);

        setJMenuBar(mb_menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(pn_table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pn_CMS_Batiment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pn_CMS_Batiment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pn_table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mi_fermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_fermerActionPerformed
        pn_table.setVisible(true);
        pn_CMS_Batiment.setVisible(false);
        setVisible(false);
    }//GEN-LAST:event_mi_fermerActionPerformed

    private void btn_detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detailsActionPerformed
        pn_table.setVisible(false);
        ftf_name.setEditable(false);
        ftf_created_at.setEditable(false);
        ftf_id.setEditable(false);
        ftf_email.setEditable(false);
        ftf_updated_at.setEditable(false);
        ta_commentaire.setEditable(false);
        pn_CMS_Batiment.setVisible(true);
        cb_role.setEnabled(false);
    }//GEN-LAST:event_btn_detailsActionPerformed

    private void btn_modifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modifActionPerformed
        if (tb_utilisateur.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Aucun utilisateur n'est sélectionné.",
                    "Erreur de sélection",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            modeEdition = true;
            lb_password.setVisible(true);
            ftf_password.setVisible(true);
            pn_table.setVisible(true);
            ftf_id.setEditable(false);
            ftf_name.setEditable(true);
            ftf_email.setEditable(true);
            pn_commentaire.setVisible(true);
            ta_commentaire.setEditable(true);
            cb_role.setEnabled(true);
            ftf_created_at.setEditable(false);
            ftf_updated_at.setEditable(false);

            btn_save.setVisible(true);
            btn_exit.setText("Annuler");
            pn_CMS_Batiment.setVisible(true);
        }
    }//GEN-LAST:event_btn_modifActionPerformed

    private void btn_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exitActionPerformed
        if (modeEdition) {
            int reponse = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir annuler votre saisie ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            affComboBox();
            if (reponse == JOptionPane.YES_OPTION) {
                exit();
                btn_exit.setText("Fermer");
                modeEdition = false;
            }
        } else {
            exit();
        }
    }//GEN-LAST:event_btn_exitActionPerformed

    private void btn_suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suppActionPerformed
        if (tb_utilisateur.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Aucun utilisateur n'est sélectionné.",
                    "Erreur de sélection",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir supprimer cet utilisateur ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    gestionUtilisateur.supp_Utilisateur(idUtilisateur);
                } catch (Exception ex) {
                    Logger.getLogger(V_CMS_Utilisateur.class.getName()).log(Level.SEVERE, null, ex);
                }
                    lesUtilisateurs.remove(idUtilisateur);
                    aff_Tableau();
                
            }
        }

    }//GEN-LAST:event_btn_suppActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        String vName = ftf_name.getText();
        String vEmail = ftf_email.getText();
        String vCommentaire = ta_commentaire.getText();
        int idRole = getIdByNom((String) cb_role.getSelectedItem());
        String password = "";
        int vId;
        // Vérification des champs texte
        JTextField[] champs = new JTextField[]{ftf_id, ftf_name, ftf_email};
        for (JTextField champ : champs) {
            if (champ.getText() == null || champ.getText().isBlank()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Veuillez remplir tous les champs",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (ta_commentaire.getText() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez remplir tous les cshamps",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        // Vérification et conversion de l'ID
        try {
            vId = Integer.parseInt(ftf_id.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "ID invalide",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        // Appel de la modification
        try {
            if (gestionUtilisateur.utilisateurExisteModification(idUtilisateur, vEmail)) {
                JOptionPane.showMessageDialog(this, "L'email est déjà utilisé", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(V_CMS_Utilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if(ftf_password.getText().isBlank()){
            gestionUtilisateur.modif_Utilisateur_No_Password(idUtilisateur, vName, vEmail, vCommentaire, idRole);
            }else{
                password = BCrypt.withDefaults().hashToString(12, ftf_password.getText().toCharArray());
                gestionUtilisateur.modif_Utilisateur_Password(idUtilisateur, vName, vEmail, vCommentaire, password, idRole);
            }
            btn_exit.setText("Fermer");
            modeEdition = false;
        } catch (Exception ex) {
            Logger.getLogger(V_CMS_Utilisateur.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btn_saveActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Utilisateur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Utilisateur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Utilisateur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Utilisateur.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                V_CMS_Utilisateur dialog = new V_CMS_Utilisateur(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_details;
    private javax.swing.JButton btn_exit;
    private javax.swing.JButton btn_modif;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_supp;
    private javax.swing.JComboBox<String> cb_role;
    private javax.swing.JFormattedTextField ftf_created_at;
    private javax.swing.JFormattedTextField ftf_email;
    private javax.swing.JFormattedTextField ftf_id;
    private javax.swing.JFormattedTextField ftf_name;
    private javax.swing.JFormattedTextField ftf_password;
    private javax.swing.JFormattedTextField ftf_updated_at;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_code;
    private javax.swing.JLabel lb_commentaire;
    private javax.swing.JLabel lb_created_at;
    private javax.swing.JLabel lb_id;
    private javax.swing.JLabel lb_nom;
    private javax.swing.JLabel lb_password;
    private javax.swing.JLabel lb_role;
    private javax.swing.JLabel lb_titre;
    private javax.swing.JLabel lb_update_at;
    private javax.swing.JMenuBar mb_menu;
    private javax.swing.JMenuItem mi_fermer;
    private javax.swing.JMenu mn_fichier;
    private javax.swing.JPanel pn_CMS_Batiment;
    private javax.swing.JPanel pn_btn;
    private javax.swing.JPanel pn_commentaire;
    private javax.swing.JPanel pn_table;
    private javax.swing.JScrollPane sp_batiment;
    private javax.swing.JTextArea ta_commentaire;
    private javax.swing.JTable tb_utilisateur;
    // End of variables declaration//GEN-END:variables
}
