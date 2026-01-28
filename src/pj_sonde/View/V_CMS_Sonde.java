/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pj_sonde.View;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import pj_sonde.Controler.C_Sonde;
import pj_sonde.Db_mariadb;
import pj_sonde.Model.*;

/**
 *
 * @author kevin
 */
public class V_CMS_Sonde extends javax.swing.JDialog {

    C_Sonde gestionSonde;
    M_Sonde uneSonde;
    M_Type unType;
    M_Unite uneUnite;
    LinkedHashMap<Integer, M_Sonde> lesSondes;
    LinkedHashMap<Integer, M_Type> lesTypes;
    LinkedHashMap<String, M_Unite> lesUnites;

    Db_mariadb baseType;
    int idRole;
    DefaultTableModel dm_tb_sonde;
    DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void aff_CMS_Sonde(
            C_Sonde gestionSonde,
            M_Sonde uneSonde,
            Db_mariadb baseType,
            LinkedHashMap<Integer, M_Sonde> lesSondes,
            LinkedHashMap<Integer, M_Type> lesTypes,
            LinkedHashMap<String, M_Unite> lesUnites,
            int idRole) {
        this.idRole = idRole;
        this.gestionSonde = gestionSonde;
        this.lesTypes = lesTypes;
        this.uneSonde = uneSonde;
        this.lesUnites = lesUnites;
        this.lesSondes = lesSondes;
        this.baseType = baseType;
        this.setTitle("Consultation & Modification & Suppression des sondes");
        this.setSize(1080, 720);
        this.setLocationRelativeTo(null);
        pn_CMS_Sonde.setVisible(false);
        aff_Tableau();
        btn_modif.setVisible(true);
        btn_supp.setVisible(true);
        if (idRole == 3) {
            btn_modif.setVisible(false);
            btn_supp.setVisible(false);
        }
        setVisible(true);
    }

    public void aff_Tableau() {
        int ligne;
        tb_Sonde.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dm_tb_sonde = (DefaultTableModel) tb_Sonde.getModel();
        dm_tb_sonde.setRowCount(lesSondes.size());
        ligne = 0;
        for (Integer uneCle : lesSondes.keySet()) {
            uneSonde = lesSondes.get(uneCle);
            dm_tb_sonde.setValueAt(uneSonde.getId(), ligne, 0);
            dm_tb_sonde.setValueAt(uneSonde.getNom(), ligne, 1);
            dm_tb_sonde.setValueAt(uneSonde.getAdresse_ip(), ligne, 2);
            ligne++;
        }
        tb_Sonde.getSelectionModel().addListSelectionListener(e -> {
            int i, id, idType;
            String code;
            i = tb_Sonde.getSelectedRow();
            if (i != -1) {
                id = (Integer) tb_Sonde.getValueAt(i, 0);
                M_Sonde sondeSelected = lesSondes.get(id);

                ftf_id.setText(String.valueOf(sondeSelected.getId()));
                ftf_code.setText(sondeSelected.getCode());
                ftf_nom.setText(sondeSelected.getNom());

                if (sondeSelected.getAdresse_ip() != null) {
                    ftf_ip.setText(sondeSelected.getAdresse_ip());
                }
                if (sondeSelected.getAdresse_mac() != null) {
                    ftf_mac.setText(sondeSelected.getAdresse_mac());
                }
                if (sondeSelected.getDate_achat() != null) {
                    ftf_date_achat.setText(sondeSelected.getDate_achat().format(formatterLocalDate));
                }

                if (sondeSelected.getCommentaire() != null) {
                    pn_commentaire.setVisible(true);
                    ta_commentaire.setText(sondeSelected.getCommentaire());
                } else {
                    pn_commentaire.setVisible(false);
                }
                if (sondeSelected.getCreated_at() != null) {
                    ftf_created_at.setText(sondeSelected.getCreated_at().format(formatterLocalDateTime));
                }
                if (sondeSelected.getUpdated_at() != null) {
                    ftf_updated_at.setText(sondeSelected.getUpdated_at().format(formatterLocalDateTime));
                }

                idType = sondeSelected.getId_type();
                try {
                    lesTypes = M_Type.getRecords(baseType, "id = " + idType, "*");
                } catch (SQLException ex) {
                    Logger.getLogger(V_CMS_Sonde.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (Integer uneCle : lesTypes.keySet()) {
                    unType = lesTypes.get(uneCle);
                }
                ftf_types.setText(unType.getLibelle());

                code = sondeSelected.getCode_unite();
            }
        });
    }

    public V_CMS_Sonde(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tb_Sonde = new javax.swing.JTable();
        pn_CMS_Sonde = new javax.swing.JPanel();
        lb_titre = new javax.swing.JLabel();
        lb_id = new javax.swing.JLabel();
        lb_code = new javax.swing.JLabel();
        lb_nom = new javax.swing.JLabel();
        lb_ip = new javax.swing.JLabel();
        lb_mac = new javax.swing.JLabel();
        lb_date_achat = new javax.swing.JLabel();
        lb_created_at = new javax.swing.JLabel();
        lb_update_at = new javax.swing.JLabel();
        lb_type = new javax.swing.JLabel();
        lb_unite = new javax.swing.JLabel();
        ftf_code = new javax.swing.JFormattedTextField();
        ftf_id = new javax.swing.JFormattedTextField();
        ftf_nom = new javax.swing.JFormattedTextField();
        ftf_ip = new javax.swing.JFormattedTextField();
        ftf_mac = new javax.swing.JFormattedTextField();
        ftf_date_achat = new javax.swing.JFormattedTextField();
        ftf_created_at = new javax.swing.JFormattedTextField();
        ftf_updated_at = new javax.swing.JFormattedTextField();
        ftf_types = new javax.swing.JFormattedTextField();
        ftf_unite = new javax.swing.JFormattedTextField();
        pn_commentaire = new javax.swing.JPanel();
        lb_commentaire = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ta_commentaire = new javax.swing.JTextArea();
        btn_details = new javax.swing.JButton();
        btn_supp = new javax.swing.JButton();
        btn_modif = new javax.swing.JButton();
        mb_menu = new javax.swing.JMenuBar();
        mn_fichier = new javax.swing.JMenu();
        mi_fermer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tb_Sonde.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Nom", "Ip"
            }
        ));
        jScrollPane1.setViewportView(tb_Sonde);
        if (tb_Sonde.getColumnModel().getColumnCount() > 0) {
            tb_Sonde.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        lb_titre.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lb_titre.setText("Détails de la sonde");

        lb_id.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_id.setText("Id :");

        lb_code.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_code.setText("Code :");

        lb_nom.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_nom.setText("Nom :");

        lb_ip.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_ip.setText("Adresse ip :");

        lb_mac.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_mac.setText("Adresse mac :");

        lb_date_achat.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_date_achat.setText("Date d'achat :");

        lb_created_at.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_created_at.setText("Créer le :");

        lb_update_at.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_update_at.setText("Mis à jour le :");

        lb_type.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_type.setText("Type :");

        lb_unite.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_unite.setText("Unité :");

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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        pn_commentaireLayout.setVerticalGroup(
            pn_commentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_commentaireLayout.createSequentialGroup()
                .addGroup(pn_commentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_commentaire)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pn_CMS_SondeLayout = new javax.swing.GroupLayout(pn_CMS_Sonde);
        pn_CMS_Sonde.setLayout(pn_CMS_SondeLayout);
        pn_CMS_SondeLayout.setHorizontalGroup(
            pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_CMS_SondeLayout.createSequentialGroup()
                .addContainerGap(381, Short.MAX_VALUE)
                .addComponent(lb_titre)
                .addGap(381, 381, 381))
            .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                        .addGap(203, 203, 203)
                        .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lb_id)
                            .addComponent(lb_code)
                            .addComponent(lb_nom)
                            .addComponent(lb_date_achat)
                            .addComponent(lb_mac)
                            .addComponent(lb_ip))
                        .addGap(26, 26, 26)
                        .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ftf_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftf_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftf_nom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftf_ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftf_mac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftf_date_achat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                        .addGap(196, 196, 196)
                        .addComponent(pn_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lb_unite)
                                .addComponent(lb_type))
                            .addComponent(lb_created_at)
                            .addComponent(lb_update_at))
                        .addGap(26, 26, 26)
                        .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ftf_created_at, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftf_updated_at, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftf_types, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ftf_unite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pn_CMS_SondeLayout.setVerticalGroup(
            pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(lb_titre)
                .addGap(82, 82, 82)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_id)
                    .addComponent(ftf_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_code)
                    .addComponent(ftf_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_nom)
                    .addComponent(ftf_nom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_ip)
                    .addComponent(ftf_ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_mac)
                    .addComponent(ftf_mac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_date_achat)
                    .addComponent(ftf_date_achat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_created_at)
                    .addComponent(ftf_created_at, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_update_at)
                    .addComponent(ftf_updated_at, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_type)
                    .addComponent(ftf_types, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_unite)
                    .addComponent(ftf_unite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(90, Short.MAX_VALUE))
        );

        btn_details.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btn_details.setText("Détails");
        btn_details.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detailsActionPerformed(evt);
            }
        });

        btn_supp.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btn_supp.setText("Supprimer");
        btn_supp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suppActionPerformed(evt);
            }
        });

        btn_modif.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btn_modif.setText("Modifier");
        btn_modif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_modifActionPerformed(evt);
            }
        });

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_details)
                    .addComponent(btn_modif)
                    .addComponent(btn_supp))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(pn_CMS_Sonde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pn_CMS_Sonde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(btn_details)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_modif)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_supp)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mi_fermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_fermerActionPerformed
        setVisible(false);
    }//GEN-LAST:event_mi_fermerActionPerformed

    private void btn_detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detailsActionPerformed
        pn_CMS_Sonde.setVisible(true);
    }//GEN-LAST:event_btn_detailsActionPerformed

    private void btn_suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suppActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_suppActionPerformed

    private void btn_modifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modifActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_modifActionPerformed

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
            java.util.logging.Logger.getLogger(V_CMS_Sonde.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Sonde.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Sonde.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Sonde.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                V_CMS_Sonde dialog = new V_CMS_Sonde(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btn_modif;
    private javax.swing.JButton btn_supp;
    private javax.swing.JFormattedTextField ftf_code;
    private javax.swing.JFormattedTextField ftf_created_at;
    private javax.swing.JFormattedTextField ftf_date_achat;
    private javax.swing.JFormattedTextField ftf_id;
    private javax.swing.JFormattedTextField ftf_ip;
    private javax.swing.JFormattedTextField ftf_mac;
    private javax.swing.JFormattedTextField ftf_nom;
    private javax.swing.JFormattedTextField ftf_types;
    private javax.swing.JFormattedTextField ftf_unite;
    private javax.swing.JFormattedTextField ftf_updated_at;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_code;
    private javax.swing.JLabel lb_commentaire;
    private javax.swing.JLabel lb_created_at;
    private javax.swing.JLabel lb_date_achat;
    private javax.swing.JLabel lb_id;
    private javax.swing.JLabel lb_ip;
    private javax.swing.JLabel lb_mac;
    private javax.swing.JLabel lb_nom;
    private javax.swing.JLabel lb_titre;
    private javax.swing.JLabel lb_type;
    private javax.swing.JLabel lb_unite;
    private javax.swing.JLabel lb_update_at;
    private javax.swing.JMenuBar mb_menu;
    private javax.swing.JMenuItem mi_fermer;
    private javax.swing.JMenu mn_fichier;
    private javax.swing.JPanel pn_CMS_Sonde;
    private javax.swing.JPanel pn_commentaire;
    private javax.swing.JTextArea ta_commentaire;
    private javax.swing.JTable tb_Sonde;
    // End of variables declaration//GEN-END:variables
}
