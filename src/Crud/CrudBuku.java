package Crud;

import Koneksi.Koneksi;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CrudBuku {

    private Connection conn;

    public CrudBuku() {
        try {
            conn = Koneksi.getKoneksi();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void simpan(
            String nama,
            int rusak,
            int baik,
            String kategori,
            String penulis,
            String penerbit) {

        try {
            String sql = "INSERT INTO buku "
                    + "(nama,j_rusak,j_baik,kategori,penulis,penerbit) "
                    + "VALUES (?,?,?,?,?,?)";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, nama);
            pst.setInt(2, rusak);
            pst.setInt(3, baik);
            pst.setString(4, kategori);
            pst.setString(5, penulis);
            pst.setString(6, penerbit);

            pst.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(
            int id,
            String nama,
            int rusak,
            int baik,
            String kategori,
            String penulis,
            String penerbit) {

        try {
            String sql = "UPDATE buku SET "
                    + "nama=?, "
                    + "j_rusak=?, "
                    + "j_baik=?, "
                    + "kategori=?, "
                    + "penulis=?, "
                    + "penerbit=? "
                    + "WHERE id=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, nama);
            pst.setInt(2, rusak);
            pst.setInt(3, baik);
            pst.setString(4, kategori);
            pst.setString(5, penulis);
            pst.setString(6, penerbit);
            pst.setInt(7, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void hapus(int id) {

        try {
            String sql = "DELETE FROM buku WHERE id=?";

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
        model.addColumn("Nama Buku");
        model.addColumn("Rusak");
        model.addColumn("Baik");
        model.addColumn("Kategori");
        model.addColumn("Penulis");
        model.addColumn("Penerbit");

        try {

            String sql = "SELECT * FROM buku";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getInt("j_rusak"),
                    rs.getInt("j_baik"),
                    rs.getString("kategori"),
                    rs.getString("penulis"),
                    rs.getString("penerbit")
                });
            }

            table.setModel(model);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}