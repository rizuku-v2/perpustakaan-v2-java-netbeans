package Crud;

import Koneksi.Koneksi;
import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Users {
    Connection conn;
    public Users() {
        try {
            conn = Koneksi.getKoneksi();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // =========================================
    // INSERT USER
    // VERIFIED OTOMATIS
    // =========================================
    public void simpan(
            String nama,
            String username,
            String pass,
            String email,
            String nik
    ) {
        try {
            String sql = "INSERT INTO users "
                    + "(nama,username,pass,email,nik,verif) "
                    + "VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, username);
            pst.setString(3, pass);
            pst.setString(4, email);
            pst.setString(5, nik);
            pst.setString(6, "verified");
            pst.executeUpdate();
            JOptionPane.showMessageDialog(
                    null, "Data user berhasil disimpan"
            );
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // =========================================
    // UPDATE USER
    // =========================================
    public void update(
            int id,
            String nama,
            String username,
            String pass,
            String email,
            String nik,
            String verif
    ) {
        try {
            String sql = "UPDATE users SET "
                    + "nama=?, "
                    + "username=?, "
                    + "pass=?, "
                    + "email=?, "
                    + "nik=?, "
                    + "verif=? "
                    + "WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, username);
            pst.setString(3, pass);
            pst.setString(4, email);
            pst.setString(5, nik);
            pst.setString(6, verif);
            pst.setInt(7, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(
                    null, "Data user berhasil diupdate"
            );
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // =========================================
    // DELETE USER
    // =========================================
    public void hapus(int id) {
        try {
            String sql =
                    "DELETE FROM users WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(
                    null, "Data user berhasil dihapus"
            );
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // =========================================
    // TAMPIL DATA USER
    // =========================================
    public void tampil(JTable table) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Username");
        model.addColumn("Password");
        model.addColumn("Email");
        model.addColumn("NIK");
        model.addColumn("Verifikasi");
        try {
            String sql = "SELECT * FROM users";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("nama"),
                    rs.getString("username"),
                    rs.getString("pass"),
                    rs.getString("email"),
                    rs.getString("nik"),
                    rs.getString("verif")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // =========================================
    // SEARCH USER
    // =========================================
    public void cari(
            JTable table,
            String keyword
    ) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Username");
        model.addColumn("Password");
        model.addColumn("Email");
        model.addColumn("NIK");
        model.addColumn("Verifikasi");
        try {
            String sql = "SELECT * FROM users "
                    + "WHERE "
                    + "nama LIKE ? "
                    + "OR username LIKE ? "
                    + "OR email LIKE ? "
                    + "OR nik LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            pst.setString(3, "%" + keyword + "%");
            pst.setString(4, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("nama"),
                    rs.getString("username"),
                    rs.getString("pass"),
                    rs.getString("email"),
                    rs.getString("nik"),
                    rs.getString("verif")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}