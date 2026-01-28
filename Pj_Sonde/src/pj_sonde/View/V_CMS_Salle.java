/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pj_sonde.View;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import pj_sonde.Controler.C_Salle;
import pj_sonde.Db_mariadb;
import pj_sonde.Model.M_Salle;
import javax.swing.table.DefaultTableModel;
import pj_sonde.Model.M_Batiment;

/**
 *
 * @author kevin
 */
public class V_CMS_Salle extends javax.swing.JDialog {

    private C_Salle gestionSalles;

    private M_Salle uneSalle;
    private M_Batiment unBatiment;

    private LinkedHashMap<Integer, M_Salle> lesSalles;
    private LinkedHashMap<Integer, M_Batiment> lesBatiments;

    private int idSalle;
    private boolean modeEdition;

    private DefaultTableModel dm_tb_salle;
    private final DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private boolean selectionListenerInitialized = false;

    public V_CMS_Salle(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void aff_CMS_Salle(
            C_Salle gestionSalles,
            M_Salle uneSalle,
            Db_mariadb db,
            LinkedHashMap<Integer, M_Salle> lesSalles,
            LinkedHashMap<Integer, M_Batiment> lesBatiments,
            int idRole) {

        // Injection des dépendances
        this.gestionSalles = gestionSalles;
        this.uneSalle = uneSalle;
        this.lesSalles = lesSalles;
        this.lesBatiments = lesBatiments;

        // Configuration de la fenêtre
        setTitle("Consultation, modification et suppression des salles");
        setSize(1080, 720);
        setLocationRelativeTo(null);

        // État initial de l’UI
        pn_CMS_Salle.setVisible(false);
        pn_btn.setVisible(false);
        btn_save.setVisible(false);

        // Chargement des données
        affComboBox();
        affTableau();

        // Gestion des droits
        btn_modif.setVisible(true);
        btn_supp.setVisible(true);
        if (idRole == 3) { // rôle guest
            btn_modif.setVisible(false);
            btn_supp.setVisible(false);
        }

        setVisible(true);
    }

    public String getCodeById(int idBatiment) {
        // Sécurisation : évite NullPointerException
        M_Batiment bat = lesBatiments.get(idBatiment);
        return (bat != null) ? bat.getCode() : null;
    }

    private int getIdByCode(String codeBatiment) {
        int res = -1;
        for (M_Batiment batiment : lesBatiments.values()) {
            if (batiment.getCode().equals(codeBatiment)) {
                res = batiment.getId();
            }
        }
        return res;
    }

    public void affComboBox() {
        cb_bat_lie.removeAllItems();
        for (M_Batiment batiment : lesBatiments.values()) {
            cb_bat_lie.addItem(batiment.getCode());
        }
    }

    public void affTableau() {
        int ligne = 0;

        // Configuration JTable
        tb_Salle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tb_Salle.setAutoCreateRowSorter(true);
        tb_Salle.setDefaultEditor(Object.class, null); // lecture seule

        dm_tb_salle = (DefaultTableModel) tb_Salle.getModel();
        dm_tb_salle.setRowCount(lesSalles.size());

        // Masquage de la colonne ID
        tb_Salle.getColumnModel().getColumn(0).setMinWidth(0);
        tb_Salle.getColumnModel().getColumn(0).setMaxWidth(0);
        tb_Salle.getColumnModel().getColumn(0).setWidth(0);

        // Remplissage du tableau
        for (M_Salle salle : lesSalles.values()) {
            dm_tb_salle.setValueAt(salle.getId(), ligne, 0);
            dm_tb_salle.setValueAt(salle.getCode(), ligne, 1);
            dm_tb_salle.setValueAt(salle.getLibelle(), ligne, 2);
            dm_tb_salle.setValueAt(
                    getCodeById(salle.getId_batiment()), ligne, 3
            );
            ligne++;
        }

        // Listener ajouté UNE SEULE FOIS
        if (!selectionListenerInitialized) {
            tb_Salle.getSelectionModel().addListSelectionListener(e -> {
                int i = tb_Salle.getSelectedRow();
                if (i != -1) {
                    int id = (Integer) tb_Salle.getValueAt(i, 0);
                    M_Salle salleSelected = lesSalles.get(id);

                    // Mise à jour UI
                    pn_btn.setVisible(true);
                    idSalle = salleSelected.getId();

                    ftf_id.setText(String.valueOf(idSalle));
                    ftf_code.setText(salleSelected.getCode());
                    ftf_nom.setText(salleSelected.getLibelle());

                    String codeBat = getCodeById(salleSelected.getId_batiment());
                    if (codeBat != null) {
                        cb_bat_lie.setSelectedItem(codeBat);
                    }

                    // Commentaire
                    if (salleSelected.getCommentaire() != null) {
                        pn_commentaire.setVisible(true);
                        ta_commentaire.setText(salleSelected.getCommentaire());
                    } else {
                        pn_commentaire.setVisible(false);
                        ta_commentaire.setText("");
                    }

                    // Dates
                    ftf_created_at.setText(
                            salleSelected.getCreated_at() != null
                            ? salleSelected.getCreated_at().format(formatterLocalDateTime)
                            : ""
                    );

                    ftf_updated_at.setText(
                            salleSelected.getUpdated_at() != null
                            ? salleSelected.getUpdated_at().format(formatterLocalDateTime)
                            : ""
                    );
                }
            }
            );

            selectionListenerInitialized = true;
        }
    }

    private void exit() {
        pn_table.setVisible(true);
        pn_btn.setVisible(false);
        tb_Salle.clearSelection();
        pn_CMS_Salle.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pn_CMS_Salle = new javax.swing.JPanel();
        lb_titre = new javax.swing.JLabel();
        lb_id = new javax.swing.JLabel();
        lb_code = new javax.swing.JLabel();
        lb_nom = new javax.swing.JLabel();
        lb_created_at = new javax.swing.JLabel();
        lb_update_at = new javax.swing.JLabel();
        ftf_code = new javax.swing.JFormattedTextField();
        ftf_id = new javax.swing.JFormattedTextField();
        ftf_nom = new javax.swing.JFormattedTextField();
        ftf_created_at = new javax.swing.JFormattedTextField();
        ftf_updated_at = new javax.swing.JFormattedTextField();
        pn_commentaire = new javax.swing.JPanel();
        lb_commentaire = new javax.swing.JLabel();
        sp_commentaire = new javax.swing.JScrollPane();
        ta_commentaire = new javax.swing.JTextArea();
        btn_exit = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        lb_bat_lie = new javax.swing.JLabel();
        cb_bat_lie = new javax.swing.JComboBox<>();
        pn_table = new javax.swing.JPanel();
        sp_Salle = new javax.swing.JScrollPane();
        tb_Salle = new javax.swing.JTable();
        pn_btn = new javax.swing.JPanel();
        btn_details = new javax.swing.JButton();
        btn_modif = new javax.swing.JButton();
        btn_supp = new javax.swing.JButton();
        mb_menu = new javax.swing.JMenuBar();
        mn_fichier = new javax.swing.JMenu();
        mi_fermer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lb_titre.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lb_titre.setText("Détails de la salle");

        lb_id.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_id.setText("Id :");

        lb_code.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_code.setText("Code :");

        lb_nom.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_nom.setText("Nom :");

        lb_created_at.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_created_at.setText("Créer le :");

        lb_update_at.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_update_at.setText("Mis à jour le :");

        lb_commentaire.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_commentaire.setText("Commentaire :");

        ta_commentaire.setColumns(20);
        ta_commentaire.setRows(5);
        sp_commentaire.setViewportView(ta_commentaire);

        javax.swing.GroupLayout pn_commentaireLayout = new javax.swing.GroupLayout(pn_commentaire);
        pn_commentaire.setLayout(pn_commentaireLayout);
        pn_commentaireLayout.setHorizontalGroup(
            pn_commentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_commentaireLayout.createSequentialGroup()
                .addComponent(lb_commentaire)
                .addGap(26, 26, 26)
                .addComponent(sp_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        pn_commentaireLayout.setVerticalGroup(
            pn_commentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_commentaireLayout.createSequentialGroup()
                .addGroup(pn_commentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_commentaire)
                    .addComponent(sp_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        lb_bat_lie.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lb_bat_lie.setText("Appartient au bâtiment : ");

        javax.swing.GroupLayout pn_CMS_SalleLayout = new javax.swing.GroupLayout(pn_CMS_Salle);
        pn_CMS_Salle.setLayout(pn_CMS_SalleLayout);
        pn_CMS_SalleLayout.setHorizontalGroup(
            pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_CMS_SalleLayout.createSequentialGroup()
                .addContainerGap(381, Short.MAX_VALUE)
                .addComponent(lb_titre)
                .addGap(381, 381, 381))
            .addGroup(pn_CMS_SalleLayout.createSequentialGroup()
                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_CMS_SalleLayout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pn_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pn_CMS_SalleLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lb_created_at)
                                    .addComponent(lb_update_at))
                                .addGap(26, 26, 26)
                                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ftf_updated_at, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                                    .addComponent(ftf_created_at)))))
                    .addGroup(pn_CMS_SalleLayout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pn_CMS_SalleLayout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lb_id)
                            .addComponent(lb_code)
                            .addComponent(lb_nom)
                            .addComponent(lb_bat_lie))
                        .addGap(26, 26, 26)
                        .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ftf_id)
                            .addComponent(ftf_code)
                            .addComponent(ftf_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_bat_lie, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(253, Short.MAX_VALUE))
        );
        pn_CMS_SalleLayout.setVerticalGroup(
            pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_CMS_SalleLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(lb_titre)
                .addGap(84, 84, 84)
                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_id)
                    .addComponent(ftf_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_code)
                    .addComponent(ftf_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_nom)
                    .addComponent(ftf_nom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_bat_lie)
                    .addComponent(cb_bat_lie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pn_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_created_at)
                    .addComponent(ftf_created_at, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_update_at)
                    .addComponent(ftf_updated_at, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(pn_CMS_SalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(157, Short.MAX_VALUE))
        );

        tb_Salle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Code", "Libelle", "Code Batiment"
            }
        ));
        tb_Salle.getTableHeader().setReorderingAllowed(false);
        sp_Salle.setViewportView(tb_Salle);

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
                .addGap(34, 34, 34)
                .addComponent(sp_Salle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addComponent(pn_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(377, Short.MAX_VALUE))
        );
        pn_tableLayout.setVerticalGroup(
            pn_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_tableLayout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(pn_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(167, Short.MAX_VALUE))
            .addGroup(pn_tableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sp_Salle, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addContainerGap())
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
                        .addComponent(pn_CMS_Salle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(pn_CMS_Salle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pn_table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mi_fermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_fermerActionPerformed
        pn_table.setVisible(true);
        pn_CMS_Salle.setVisible(false);
        setVisible(false);
    }//GEN-LAST:event_mi_fermerActionPerformed

    private void btn_detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detailsActionPerformed
        pn_table.setVisible(false);
        ftf_code.setEditable(false);
        ftf_created_at.setEditable(false);
        ftf_id.setEditable(false);
        ftf_nom.setEditable(false);
        cb_bat_lie.setEnabled(false);
        ftf_updated_at.setEditable(false);
        ta_commentaire.setEditable(false);
        pn_CMS_Salle.setVisible(true);

    }//GEN-LAST:event_btn_detailsActionPerformed

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

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        pn_table.setVisible(true);
        pn_CMS_Salle.setVisible(false);
        setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void btn_modifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modifActionPerformed
        if (tb_Salle.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Aucune salle n'est sélectionné.",
                    "Erreur de sélection",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            modeEdition = true;
            pn_table.setVisible(false);
            ftf_id.setEditable(false);
            ftf_code.setEditable(true);
            ftf_nom.setEditable(true);
            cb_bat_lie.setEnabled(true);
            ta_commentaire.setEditable(true);

            ftf_created_at.setVisible(false);
            ftf_updated_at.setEditable(false);

            btn_save.setVisible(true);
            btn_exit.setText("Annuler");
            pn_CMS_Salle.setVisible(true);
        }
    }//GEN-LAST:event_btn_modifActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        String code, libelle, commentaire;
        int idBatiment;

        JTextField[] champs = {ftf_code, ftf_nom};

        for (JTextField champ : champs) {
            if (champ.getText().isEmpty() || champ.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (ftf_nom.getText().length() > 50) {
            JOptionPane.showMessageDialog(this, "Le libellé doit contenir moins de 50 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        libelle = ftf_nom.getText();

        if (ftf_code.getText().length() > 50) {
            JOptionPane.showMessageDialog(this, "Le code doit contenir moins de 50 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        code = ftf_code.getText();

        // Si commentaire renseigné, vérification de la longueur
        if (ta_commentaire.getText().length() > 250) {
            JOptionPane.showMessageDialog(this, "Le commentaire doit contenir moins de 250 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        idBatiment = getIdByCode((String) cb_bat_lie.getSelectedItem());
        commentaire = ta_commentaire.getText().isEmpty() ? null : ta_commentaire.getText();
        try {
            if (gestionSalles.salleExisteModification(idSalle, code, libelle)) {
                JOptionPane.showMessageDialog(this, "Le code ou le libelle est déjà utilisé", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(V_A_Batiment.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            gestionSalles.edit_Salle(idSalle, code, idBatiment, libelle, commentaire);
        } catch (Exception ex) {
            Logger.getLogger(V_CMS_Salle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suppActionPerformed
        if (tb_Salle.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Aucune salle n'est sélectionné.",
                    "Erreur de sélection",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int reponse = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir supprimer cette sall ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (reponse == JOptionPane.YES_OPTION) {
                try {
                    gestionSalles.delete_Salle(idSalle);
                    lesSalles.remove(idSalle);
                    affTableau();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Impossible de supprimer la salle (contraintes en base de données).",
                            "Erreur de suppression",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btn_suppActionPerformed

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
            java.util.logging.Logger.getLogger(V_CMS_Salle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Salle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Salle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_CMS_Salle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                V_CMS_Salle dialog = new V_CMS_Salle(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> cb_bat_lie;
    private javax.swing.JFormattedTextField ftf_code;
    private javax.swing.JFormattedTextField ftf_created_at;
    private javax.swing.JFormattedTextField ftf_id;
    private javax.swing.JFormattedTextField ftf_nom;
    private javax.swing.JFormattedTextField ftf_updated_at;
    private javax.swing.JLabel lb_bat_lie;
    private javax.swing.JLabel lb_code;
    private javax.swing.JLabel lb_commentaire;
    private javax.swing.JLabel lb_created_at;
    private javax.swing.JLabel lb_id;
    private javax.swing.JLabel lb_nom;
    private javax.swing.JLabel lb_titre;
    private javax.swing.JLabel lb_update_at;
    private javax.swing.JMenuBar mb_menu;
    private javax.swing.JMenuItem mi_fermer;
    private javax.swing.JMenu mn_fichier;
    private javax.swing.JPanel pn_CMS_Salle;
    private javax.swing.JPanel pn_btn;
    private javax.swing.JPanel pn_commentaire;
    private javax.swing.JPanel pn_table;
    private javax.swing.JScrollPane sp_Salle;
    private javax.swing.JScrollPane sp_commentaire;
    private javax.swing.JTextArea ta_commentaire;
    private javax.swing.JTable tb_Salle;
    // End of variables declaration//GEN-END:variables
}
