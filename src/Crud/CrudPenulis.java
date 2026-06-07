package Crud;
import Koneksi.Koneksi;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
public class CrudPenulis {
    Connection conn;
    public CrudPenulis() {
        try {
            conn = Koneksi.getKoneksi();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void simpan(String nama) {
        try {
            String sql = "INSERT INTO penulis (nama) VALUES (?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void update(int id, String nama) {
        try {
            String sql = "UPDATE penulis SET nama=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void hapus(int id) {
        try {
            String sql = "DELETE FROM penulis WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void tampil(JTable table) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama Penulis");
        try {
            String sql = "SELECT * FROM penulis";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("nama")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}