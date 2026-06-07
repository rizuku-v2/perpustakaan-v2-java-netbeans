package Crud;
import Koneksi.Koneksi;
import java.awt.HeadlessException;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Pengembalian {
    Connection conn;
    public Pengembalian() {
        try {
            conn = Koneksi.getKoneksi();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void simpan(
            int peminjaman,
            String statusPengembalian
    ) {
        try {
            String ambil = "SELECT "
                    + "peminjaman.buku_id, "
                    + "peminjaman.status, "
                    + "peminjaman.tgl_jatuh_tempo "
                    + "FROM peminjaman "
                    + "WHERE id=?";
            PreparedStatement pstAmbil = conn.prepareStatement(ambil);
            pstAmbil.setInt(1, peminjaman);
            ResultSet rs = pstAmbil.executeQuery();
            if (rs.next()) {
                int buku = rs.getInt("buku_id");
                String statusBuku = rs.getString("status");
                LocalDate jatuhTempo = rs.getDate("tgl_jatuh_tempo").toLocalDate();
                LocalDate hariIni = LocalDate.now();
                int denda = 0;
                if (hariIni.isAfter(jatuhTempo)) {
                    denda = 20000;
                }     
                if (statusPengembalian.equals("rusak")||statusPengembalian.equals("hilang")
                ) {
                    denda = 100000;
                }
                String sql =
                        "INSERT INTO pengembalian "
                        + "(denda,peminjaman_id,status_pengembalian) "
                        + "VALUES (?,?,?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, denda);
                pst.setInt(2, peminjaman);
                pst.setString(3, statusPengembalian);
                pst.executeUpdate();
                if (statusPengembalian.equals("hilang")) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Buku hilang, denda 100000"
                    );
                    return;
                }              
                if (statusBuku.equals("baik")) {
                    if (statusPengembalian.equals("rusak")) {
                        String update = "UPDATE buku SET "
                                + "j_rusak = j_rusak + 1 "
                                + "WHERE id=?";
                        PreparedStatement pstUpdate =conn.prepareStatement(update);
                        pstUpdate.setInt(1, buku);
                        pstUpdate.executeUpdate();
                    }
                    else {
                        String update =
                                "UPDATE buku SET "
                                + "j_baik = j_baik + 1 "
                                + "WHERE id=?";
                        PreparedStatement pstUpdate = conn.prepareStatement(update);
                        pstUpdate.setInt(1, buku);
                        pstUpdate.executeUpdate();
                    }
                }
                else if (statusBuku.equals("rusak")) {
                    String update =
                            "UPDATE buku SET "
                            + "j_rusak = j_rusak + 1 "
                            + "WHERE id=?";
                    PreparedStatement pstUpdate = conn.prepareStatement(update);
                    pstUpdate.setInt(1, buku);
                    pstUpdate.executeUpdate();
                }
                JOptionPane.showMessageDialog(
                        null,"Pengembalian berhasil " + "Denda : " + denda
                );
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void hapus(int id) {
        try {
            String sql = "DELETE FROM pengembalian WHERE id=?";
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
        model.addColumn("Status");
        model.addColumn("Tanggal");
        model.addColumn("Denda");
        try {
            String sql = "SELECT "
                    + "pengembalian.id, "
                    + "users.nama AS user, "
                    + "buku.nama AS buku, "
                    + "pengembalian.status_pengembalian, "
                    + "pengembalian.tgl, "
                    + "pengembalian.denda "
                    + "FROM pengembalian "
                    + "JOIN peminjaman "
                    + "ON pengembalian.peminjaman_id = peminjaman.id "
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
                    rs.getString("status_pengembalian"),
                    rs.getString("tgl"),
                    rs.getString("denda")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}