/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package pj_sonde.View;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
    private boolean modeEdition;

    int idRole, idSonde;
    DefaultTableModel dm_tb_sonde;
    DateTimeFormatter formatterLocalDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterLocalDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    ZoneId defaultZoneId = ZoneId.systemDefault();
    LocalDate localDate;

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
        affComboBox();
        btn_modif.setVisible(true);
        btn_supp.setVisible(true);
        pn_btn.setVisible(false);
        btn_save.setVisible(false);
        if (idRole == 3) {
            btn_modif.setVisible(false);
            btn_supp.setVisible(false);
        }
        setVisible(true);
    }

    public void affComboBox() {
        cb_type.removeAllItems();
        cb_unite.removeAllItems();
        for (M_Unite unite : lesUnites.values()) {
            cb_unite.addItem(unite.getLibelle());
        }
        for (M_Type type : lesTypes.values()) {
            cb_type.addItem(type.getLibelle());
        }
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
            int i, id;
            String code;
            i = tb_Sonde.getSelectedRow();
            if (i != -1) {
                id = (Integer) tb_Sonde.getValueAt(i, 0);
                M_Sonde sondeSelected = lesSondes.get(id);
                pn_btn.setVisible(true);
                ftf_id.setText(String.valueOf(sondeSelected.getId()));
                ftf_code.setText(sondeSelected.getCode());
                ftf_nom.setText(sondeSelected.getNom());
                idSonde = sondeSelected.getId();
                if (sondeSelected.getAdresse_ip() != null) {
                    ftf_ip.setText(sondeSelected.getAdresse_ip());
                }
                if (sondeSelected.getAdresse_mac() != null) {
                    ftf_mac.setText(sondeSelected.getAdresse_mac());
                }
                if (sondeSelected.getDate_achat() != null) {
                    localDate = sondeSelected.getDate_achat();
                    Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                    dc_date_achat.setDate(date);
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

//                idType = sondeSelected.getId_type();
//                try {
//                    lesTypes = M_Type.getRecords(baseType, "id = " + idType, "*");
//                } catch (SQLException ex) {
//                    Logger.getLogger(V_CMS_Sonde.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                for (Integer uneCle : lesTypes.keySet()) {
//                    unType = lesTypes.get(uneCle);
//                }
//                ftf_types.setText(unType.getLibelle());
                String codeUnite = getCodeUniteById(sondeSelected.getId());
                if (codeUnite != null) {
                    M_Unite unite = lesUnites.get(codeUnite);
                    if (unite != null) {
                        cb_unite.setSelectedItem(unite.getLibelle());
                    }
                }

                int idType = getIdTypeById(sondeSelected.getId());
                M_Type type = lesTypes.get(idType);
                if (type != null) {
                    cb_type.setSelectedItem(type.getLibelle());
                }
            }
        });
    }

    public String getCodeUniteById(int idSonde) {
        M_Sonde sonde = lesSondes.get(idSonde);
        return (sonde != null) ? sonde.getCode_unite() : null;
    }

    public int getIdTypeById(int idSonde) {
        M_Sonde sonde = lesSondes.get(idSonde);
        return (sonde != null) ? sonde.getId_type() : null;
    }

    private void exit() {
        pn_btn.setVisible(true);
        pn_btn.setVisible(false);
        tb_Sonde.clearSelection();
        pn_CMS_Sonde.setVisible(false);
    }

    public V_CMS_Sonde(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private int getIdByCode(String libelleType) {
        int res = -1;
        for (M_Type type : lesTypes.values()) {
            if (type.getLibelle().equals(libelleType)) {
                res = type.getId();
            }
        }
        return res;
    }

    private String getCodeByLibelle(String libelleUnite) {
        String res = "";
        for (M_Unite unite : lesUnites.values()) {
            if (unite.getLibelle().equals(libelleUnite)) {
                res = unite.getCode();
            }
        }
        return res;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        ftf_created_at = new javax.swing.JFormattedTextField();
        ftf_updated_at = new javax.swing.JFormattedTextField();
        pn_commentaire = new javax.swing.JPanel();
        lb_commentaire = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ta_commentaire = new javax.swing.JTextArea();
        btn_save = new javax.swing.JButton();
        btn_exit = new javax.swing.JButton();
        cb_type = new javax.swing.JComboBox<>();
        cb_unite = new javax.swing.JComboBox<>();
        dc_date_achat = new com.toedter.calendar.JDateChooser();
        pn_table = new javax.swing.JPanel();
        pn_btn = new javax.swing.JPanel();
        btn_details = new javax.swing.JButton();
        btn_supp = new javax.swing.JButton();
        btn_modif = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_Sonde = new javax.swing.JTable();
        mb_menu = new javax.swing.JMenuBar();
        mn_fichier = new javax.swing.JMenu();
        mi_fermer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
        );
        pn_commentaireLayout.setVerticalGroup(
            pn_commentaireLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_commentaireLayout.createSequentialGroup()
                .addComponent(lb_commentaire)
                .addGap(0, 76, Short.MAX_VALUE))
            .addGroup(pn_commentaireLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_save.setText("Save");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_exit.setText("Fermer");
        btn_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pn_CMS_SondeLayout = new javax.swing.GroupLayout(pn_CMS_Sonde);
        pn_CMS_Sonde.setLayout(pn_CMS_SondeLayout);
        pn_CMS_SondeLayout.setHorizontalGroup(
            pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_CMS_SondeLayout.createSequentialGroup()
                .addContainerGap(381, Short.MAX_VALUE)
                .addComponent(lb_titre)
                .addGap(381, 381, 381))
            .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                        .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lb_id)
                            .addComponent(lb_code)
                            .addComponent(lb_nom)
                            .addComponent(lb_date_achat)
                            .addComponent(lb_mac)
                            .addComponent(lb_ip))
                        .addGap(26, 26, 26)
                        .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(ftf_ip, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                            .addComponent(ftf_nom, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ftf_code, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ftf_id, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ftf_mac)
                            .addComponent(dc_date_achat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lb_unite)
                                        .addComponent(lb_type))
                                    .addComponent(lb_created_at)
                                    .addComponent(lb_update_at))
                                .addGap(26, 26, 26)
                                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(ftf_updated_at, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                    .addComponent(ftf_created_at, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cb_type, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_unite, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pn_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(450, Short.MAX_VALUE))
        );
        pn_CMS_SondeLayout.setVerticalGroup(
            pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pn_CMS_SondeLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(lb_titre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lb_date_achat)
                    .addComponent(dc_date_achat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pn_commentaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                    .addComponent(cb_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_unite)
                    .addComponent(cb_unite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(pn_CMS_SondeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(704, 704, 704))
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

        javax.swing.GroupLayout pn_btnLayout = new javax.swing.GroupLayout(pn_btn);
        pn_btn.setLayout(pn_btnLayout);
        pn_btnLayout.setHorizontalGroup(
            pn_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 153, Short.MAX_VALUE)
            .addGroup(pn_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pn_btnLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(pn_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btn_details)
                        .addComponent(btn_modif)
                        .addComponent(btn_supp))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pn_btnLayout.setVerticalGroup(
            pn_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 141, Short.MAX_VALUE)
            .addGroup(pn_btnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pn_btnLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btn_details)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btn_modif, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btn_supp)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

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

        javax.swing.GroupLayout pn_tableLayout = new javax.swing.GroupLayout(pn_table);
        pn_table.setLayout(pn_tableLayout);
        pn_tableLayout.setHorizontalGroup(
            pn_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pn_tableLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                .addComponent(pn_btn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
        );
        pn_tableLayout.setVerticalGroup(
            pn_tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
            .addGroup(pn_tableLayout.createSequentialGroup()
                .addGap(204, 204, 204)
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
                .addComponent(pn_CMS_Sonde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 237, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pn_table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pn_CMS_Sonde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(pn_table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mi_fermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mi_fermerActionPerformed
        setVisible(false);
    }//GEN-LAST:event_mi_fermerActionPerformed

    private void btn_detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detailsActionPerformed
        pn_CMS_Sonde.setVisible(true);
        ftf_code.setEditable(false);
        ftf_created_at.setEditable(false);
        ftf_id.setEditable(false);
        ftf_nom.setEditable(false);
        ftf_updated_at.setEditable(false);
        ta_commentaire.setEditable(false);
        dc_date_achat.setEnabled(false);
        cb_type.setEnabled(false);
        cb_unite.setEnabled(false);
        pn_CMS_Sonde.setVisible(true);
        ftf_ip.setEditable(false);
        ftf_mac.setEditable(false);
    }//GEN-LAST:event_btn_detailsActionPerformed

    private void btn_suppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suppActionPerformed
        if (tb_Sonde.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Aucune sonde n'est sélectionné.",
                    "Erreur de sélection",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int reponse = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir supprimer cette sonde ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (reponse == JOptionPane.YES_OPTION) {
                try {
                    gestionSonde.deleteSonde(idSonde);
                    lesSondes.remove(idSonde);
                    aff_Tableau();
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

    private void btn_modifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_modifActionPerformed
        if (tb_Sonde.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Aucune sonde n'est sélectionné.",
                    "Erreur de sélection",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            modeEdition = true;
            pn_CMS_Sonde.setVisible(true);
            ftf_id.setEditable(false);
            pn_btn.setVisible(true);
            ftf_code.setEditable(true);
            ftf_nom.setEditable(true);
            ftf_mac.setEditable(true);
            dc_date_achat.setEnabled(true);
            ftf_ip.setEditable(true);

            cb_type.setEnabled(true);
            cb_unite.setEnabled(true);

            pn_commentaire.setVisible(true);
            ta_commentaire.setEditable(true);

            ftf_created_at.setEditable(false);
            ftf_updated_at.setEditable(false);

            btn_save.setVisible(true);
            btn_exit.setText("Annuler");
            pn_CMS_Sonde.setVisible(true);
        }
    }//GEN-LAST:event_btn_modifActionPerformed

    private void btn_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exitActionPerformed
        if (modeEdition) {
            int reponse = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir annuler votre saisie ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (reponse == JOptionPane.YES_OPTION) {
                exit();
                btn_exit.setText("Fermer");
                modeEdition = false;
            }
        } else {
            exit();
        }
    }//GEN-LAST:event_btn_exitActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        int vId;
        String rexIp = "^(?:(?:25[0-5]|2[0-4]\\d|1?\\d{1,2})(?:\\.(?!$)|$)){4}$";
        String rexMac = "^(?:[0-9A-Fa-f]{2}([:-]))(?:[0-9A-Fa-f]{2}\\1){4}[0-9A-Fa-f]{2}$";
        JTextField[] champs = new JTextField[]{ftf_id, ftf_code, ftf_ip, ftf_mac, ftf_nom};
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
        if (ftf_code.getText().length() > 10) {
            JOptionPane.showMessageDialog(this, "Le code doit contenir moins de 10 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ftf_nom.getText().length() > 100) {
            JOptionPane.showMessageDialog(this, "Le nom doit contenir moins de 100 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (ftf_ip.getText().length() > 15) {
            JOptionPane.showMessageDialog(this, "L'adresse IP doit contenir moins de 15 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ftf_ip.getText().matches(rexIp)) {
            JOptionPane.showMessageDialog(this, "L'adresse IP doit être au format X.X.X.X (exemple : 192.168.1.1)", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ftf_mac.getText().length() > 17) {
            JOptionPane.showMessageDialog(this, "L'adresse mac doit contenir moins de 17 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ftf_mac.getText().matches(rexMac)) {
            JOptionPane.showMessageDialog(this, "L'adresse mac doit être au format XX-XX-XX-XX-XX-XX avec des caractère héxadécimal (exemple : FF-FF-FF-FF-FF-FF)", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ta_commentaire.getText().length() > 250) {
            JOptionPane.showMessageDialog(this, "Le commentaire doit contenir moins de 250 caractères", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDate vDateAchat = dc_date_achat.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        String vCode = ftf_code.getText();
        String vNom = ftf_nom.getText();
        String vMac = ftf_mac.getText();
        String vIp = ftf_ip.getText();
        String vCommentaire = ta_commentaire.getText();

        String vUnite = getCodeByLibelle(String.valueOf(cb_unite.getSelectedItem()));
        int vType = getIdByCode(String.valueOf(cb_type.getSelectedItem()));
        // Vérification des champs texte

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
//        LocalDate dateAchat = dc.getDate()
//                .toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
        // Appel de la modification
        try {
            if (gestionSonde.sondeExisteModification(vId, vCode, vNom, vMac)) {
                JOptionPane.showMessageDialog(this, "Le code, l'adresse mac ou le libelle est déjà utilisé", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(V_CMS_Batiment.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            gestionSonde.modif_sonde(vId, vCode, vNom, vIp, vMac, vDateAchat, vCommentaire, vType, vUnite);
            btn_exit.setText("Fermer");
            modeEdition = false;
        } catch (Exception ex) {
            Logger.getLogger(V_CMS_Batiment.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JButton btn_exit;
    private javax.swing.JButton btn_modif;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_supp;
    private javax.swing.JComboBox<String> cb_type;
    private javax.swing.JComboBox<String> cb_unite;
    private com.toedter.calendar.JDateChooser dc_date_achat;
    private javax.swing.JFormattedTextField ftf_code;
    private javax.swing.JFormattedTextField ftf_created_at;
    private javax.swing.JFormattedTextField ftf_id;
    private javax.swing.JFormattedTextField ftf_ip;
    private javax.swing.JFormattedTextField ftf_mac;
    private javax.swing.JFormattedTextField ftf_nom;
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
    private javax.swing.JPanel pn_btn;
    private javax.swing.JPanel pn_commentaire;
    private javax.swing.JPanel pn_table;
    private javax.swing.JTextArea ta_commentaire;
    private javax.swing.JTable tb_Sonde;
    // End of variables declaration//GEN-END:variables
}
