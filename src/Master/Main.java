/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Master;

import java.awt.CardLayout;

/**
 *
 * @author user
 */
public class Main extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Main.class.getName());
    private final CardLayout card;

    private final Dashboard pDashboard = new Dashboard();
    private final User pUser = new User();
    private final Buku pBuku = new Buku();
    private final Penulis pPenulis = new Penulis();
    private final Kategori pKategori = new Kategori();
    private final Penerbit pPenerbit = new Penerbit();
    private final Peminjaman pPeminjaman = new Peminjaman();
    private final Pengembalian pPengembalian = new Pengembalian();
    public Main() {
        initComponents();
        setLocationRelativeTo(null); // tengah layar
        setResizable(false); 
            
            card = new CardLayout();
            panelUtama.setLayout(card);

            panelUtama.add(pDashboard, "dashboard");
            panelUtama.add(pUser, "user");
            panelUtama.add(pBuku, "buku");
            panelUtama.add(pPenulis, "penulis");
            panelUtama.add(pKategori, "kategori");
            panelUtama.add(pPenerbit, "penerbit");          
            panelUtama.add(pPeminjaman, "peminjaman");
            panelUtama.add(pPengembalian, "pengembalian");
            card.show(panelUtama, "dashboard");
            }

    private void bukaPanel(String namaPanel) {
       card.show(panelUtama, namaPanel);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        penulis = new javax.swing.JButton();
        gambar = new javax.swing.JLabel();
        dashboard = new javax.swing.JButton();
        penerbit = new javax.swing.JButton();
        kategori = new javax.swing.JButton();
        buku = new javax.swing.JButton();
        peminjaman = new javax.swing.JButton();
        pengembalian = new javax.swing.JButton();
        panelUtama = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1018, 594));
        setResizable(false);
        setSize(new java.awt.Dimension(1010, 556));

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(180, 524));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Perpustakaan");

        penulis.setBackground(new java.awt.Color(102, 102, 255));
        penulis.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        penulis.setForeground(new java.awt.Color(255, 255, 255));
        penulis.setText("Penulis");
        penulis.setBorder(null);
        penulis.setBorderPainted(false);
        penulis.setContentAreaFilled(false);
        penulis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        penulis.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        penulis.addActionListener(this::penulisActionPerformed);

        gambar.setBackground(new java.awt.Color(51, 255, 102));
        gambar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/logo.png"))); // NOI18N
        gambar.setPreferredSize(new java.awt.Dimension(50, 50));

        dashboard.setBackground(new java.awt.Color(102, 102, 255));
        dashboard.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dashboard.setForeground(new java.awt.Color(255, 255, 255));
        dashboard.setText("Dashboard");
        dashboard.setBorder(null);
        dashboard.setBorderPainted(false);
        dashboard.setContentAreaFilled(false);
        dashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        dashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dashboard.addActionListener(this::dashboardActionPerformed);

        penerbit.setBackground(new java.awt.Color(102, 102, 255));
        penerbit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        penerbit.setForeground(new java.awt.Color(255, 255, 255));
        penerbit.setText("Penerbit");
        penerbit.setBorder(null);
        penerbit.setBorderPainted(false);
        penerbit.setContentAreaFilled(false);
        penerbit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        penerbit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        penerbit.addActionListener(this::penerbitActionPerformed);

        kategori.setBackground(new java.awt.Color(102, 102, 255));
        kategori.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        kategori.setForeground(new java.awt.Color(255, 255, 255));
        kategori.setText("Kategori");
        kategori.setBorder(null);
        kategori.setBorderPainted(false);
        kategori.setContentAreaFilled(false);
        kategori.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        kategori.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        kategori.addActionListener(this::kategoriActionPerformed);

        buku.setBackground(new java.awt.Color(102, 102, 255));
        buku.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        buku.setForeground(new java.awt.Color(255, 255, 255));
        buku.setText("Buku");
        buku.setBorder(null);
        buku.setBorderPainted(false);
        buku.setContentAreaFilled(false);
        buku.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        buku.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        buku.addActionListener(this::bukuActionPerformed);

        peminjaman.setBackground(new java.awt.Color(102, 102, 255));
        peminjaman.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        peminjaman.setForeground(new java.awt.Color(255, 255, 255));
        peminjaman.setText("Peminjaman");
        peminjaman.setBorder(null);
        peminjaman.setBorderPainted(false);
        peminjaman.setContentAreaFilled(false);
        peminjaman.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        peminjaman.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        peminjaman.addActionListener(this::peminjamanActionPerformed);

        pengembalian.setBackground(new java.awt.Color(102, 102, 255));
        pengembalian.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        pengembalian.setForeground(new java.awt.Color(255, 255, 255));
        pengembalian.setText("Pengembalian");
        pengembalian.setBorder(null);
        pengembalian.setBorderPainted(false);
        pengembalian.setContentAreaFilled(false);
        pengembalian.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pengembalian.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pengembalian.addActionListener(this::pengembalianActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(penulis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(penerbit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(kategori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buku, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(peminjaman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pengembalian, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(gambar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penulis, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buku, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(peminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        panelUtama.setBackground(new java.awt.Color(251, 246, 255));
        panelUtama.setMinimumSize(new java.awt.Dimension(760, 524));
        panelUtama.setPreferredSize(new java.awt.Dimension(760, 524));

        javax.swing.GroupLayout panelUtamaLayout = new javax.swing.GroupLayout(panelUtama);
        panelUtama.setLayout(panelUtamaLayout);
        panelUtamaLayout.setHorizontalGroup(
            panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 823, Short.MAX_VALUE)
        );
        panelUtamaLayout.setVerticalGroup(
            panelUtamaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 823, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelUtama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelUtama.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void penulisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penulisActionPerformed
        bukaPanel("penulis");
    }//GEN-LAST:event_penulisActionPerformed

    private void dashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardActionPerformed
        bukaPanel("dashboard");
    }//GEN-LAST:event_dashboardActionPerformed

    private void penerbitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penerbitActionPerformed
        bukaPanel("penerbit");
    }//GEN-LAST:event_penerbitActionPerformed

    private void kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kategoriActionPerformed
        bukaPanel("kategori");
    }//GEN-LAST:event_kategoriActionPerformed

    private void bukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bukuActionPerformed
        pBuku.refreshData();
        bukaPanel("buku");
    }//GEN-LAST:event_bukuActionPerformed

    private void peminjamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_peminjamanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_peminjamanActionPerformed

    private void pengembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pengembalianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pengembalianActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Main().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buku;
    private javax.swing.JButton dashboard;
    private javax.swing.JLabel gambar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton kategori;
    private javax.swing.JPanel panelUtama;
    private javax.swing.JButton peminjaman;
    private javax.swing.JButton penerbit;
    private javax.swing.JButton pengembalian;
    private javax.swing.JButton penulis;
    // End of variables declaration//GEN-END:variables
}
