package Auth;

import Koneksi.Koneksi;
import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class Visitor {
    Connection conn;
    public Visitor() {
        try {
            conn = Koneksi.getKoneksi();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void simpanUser(int userId) {
        try {
            String sql =
                    "INSERT INTO visitor "
                    + "(user_id, tipe) "
                    + "VALUES (?,?)";
            PreparedStatement pst =
                    conn.prepareStatement(sql);
            pst.setInt(1, userId);
            pst.setString(2, "User");
            pst.executeUpdate();
            JOptionPane.showMessageDialog(
                    null,
                    "Visitor user berhasil ditambahkan"
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void simpanGuest(String namaGuest) {
        try {
            String sql =
                    "INSERT INTO visitor "
                    + "(nama_guest, tipe) "
                    + "VALUES (?,?)";
            PreparedStatement pst =
                    conn.prepareStatement(sql);
            pst.setString(1, namaGuest);
            pst.setString(2, "Guest");
            pst.executeUpdate();
            JOptionPane.showMessageDialog(
                    null, "Visitor guest berhasil ditambahkan"
            );
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void hapus(int id) {
        try {
            String sql = "DELETE FROM visitor WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(
                    null, "Data visitor berhasil dihapus"
            );
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void tampil(JTable table) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Tipe");
        model.addColumn("Nama");
        model.addColumn("Tanggal");
        try {
            String sql =
                    "SELECT "
                    + "visitor.id, "
                    + "visitor.tipe, "
                    + "COALESCE(users.nama, visitor.nama_guest) "
                    + "AS nama, "
                    + "visitor.tgl "
                    + "FROM visitor "
                    + "LEFT JOIN users "
                    + "ON visitor.user_id = users.id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("tipe"),
                    rs.getString("nama"),
                    rs.getString("tgl")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void cari(
            JTable table,
            String keyword
    ) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Tipe");
        model.addColumn("Nama");
        model.addColumn("Tanggal");
        try {
            String sql = "SELECT "
                    + "visitor.id, "
                    + "visitor.tipe, "
                    + "COALESCE(users.nama, visitor.nama_guest) "
                    + "AS nama, "
                    + "visitor.tgl "
                    + "FROM visitor "
                    + "LEFT JOIN users "
                    + "ON visitor.user_id = users.id "
                    + "WHERE "
                    + "users.nama LIKE ? "
                    + "OR visitor.nama_guest LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("tipe"),
                    rs.getString("nama"),
                    rs.getString("tgl")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}