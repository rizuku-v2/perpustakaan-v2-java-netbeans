package Crud;
import Koneksi.Koneksi;
import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Peminjaman {
    Connection conn;
    public Peminjaman() {
        try {
            conn = Koneksi.getKoneksi();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }
    public void simpan(
            int user,
            int buku,
            String jatuhTempo
    ) {
        try {
            // =========================================
            // CEK STOK
            // =========================================
            String cek =
                    "SELECT j_baik, j_rusak "
                    + "FROM buku "
                    + "WHERE id=?";
            PreparedStatement pstCek = conn.prepareStatement(cek);
            pstCek.setInt(1, buku);
            ResultSet rs = pstCek.executeQuery();
            if (rs.next()) {
                int baik = rs.getInt("j_baik");
                int rusak = rs.getInt("j_rusak");
                if (baik > 0) {
                    String sql =
                            "INSERT INTO peminjaman "
                            + "(user_id,buku_id,status,tgl_jatuh_tempo) "
                            + "VALUES (?,?,?,?)";
                    PreparedStatement pst =
                            conn.prepareStatement(sql);
                    pst.setInt(1, user);
                    pst.setInt(2, buku);
                    pst.setString(3, "baik");
                    pst.setString(4, jatuhTempo);
                    pst.executeUpdate();
                    // KURANGI STOK BAIK
                    String update = "UPDATE buku SET "
                            + "j_baik = j_baik - 1 "
                            + "WHERE id=?";
                    PreparedStatement pstUpdate = conn.prepareStatement(update);
                    pstUpdate.setInt(1, buku);
                    pstUpdate.executeUpdate();
                    JOptionPane.showMessageDialog(
                            null, "Peminjaman berhasil (Buku Baik)"
                    );
                }
                else if (rusak > 0) {
                    String sql = "INSERT INTO peminjaman "
                            + "(user_id,buku_id,status,tgl_jatuh_tempo) "
                            + "VALUES (?,?,?,?)";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, user);
                    pst.setInt(2, buku);
                    pst.setString(3, "rusak");
                    pst.setString(4, jatuhTempo);
                    pst.executeUpdate();
                    String update = "UPDATE buku SET "
                            + "j_rusak = j_rusak - 1 "
                            + "WHERE id=?";
                    PreparedStatement pstUpdate = conn.prepareStatement(update);
                    pstUpdate.setInt(1, buku);
                    pstUpdate.executeUpdate();
                    JOptionPane.showMessageDialog(
                            null, "Peminjaman berhasil (Buku Rusak)"
                    );
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Stok buku habis"
                    );
                }
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void update(
            int id,
            int user,
            int buku,
            String jatuhTempo
    ) {

        try {

            String sql = "UPDATE peminjaman SET "
                    + "user_id=?, "
                    + "buku_id=?, "
                    + "tgl_jatuh_tempo=? "
                    + "WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, user);
            pst.setInt(2, buku);
            pst.setString(3, jatuhTempo);
            pst.setInt(4, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(
                    null, "Data berhasil diupdate"
            );
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void hapus(int id) {
        try {
            String sql = "DELETE FROM peminjaman WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(
                    null, "Data berhasil dihapus"
            );
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void tampil(JTable table) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("User");
        model.addColumn("Buku");
        model.addColumn("Status Buku");
        model.addColumn("Tanggal");
        model.addColumn("Jatuh Tempo");
        try {
            String sql ="SELECT "
                    + "peminjaman.id, "
                    + "users.nama AS user, "
                    + "buku.nama AS buku, "
                    + "peminjaman.status, "
                    + "peminjaman.tgl, "
                    + "peminjaman.tgl_jatuh_tempo "
                    + "FROM peminjaman "
                    + "JOIN users "
                    + "ON peminjaman.user_id = users.id "
                    + "JOIN buku "
                    + "ON peminjaman.buku_id = buku.id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("user"),
                    rs.getString("buku"),
                    rs.getString("status"),
                    rs.getString("tgl"),
                    rs.getString("tgl_jatuh_tempo")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}