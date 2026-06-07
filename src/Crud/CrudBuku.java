package Crud;
import Koneksi.Koneksi;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CrudBuku {
    Connection conn;
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
            int kategori,
            int penerbit,
            int penulis
    ) {
        try {
            String sql = "INSERT INTO buku "
                    + "(nama,j_rusak,j_baik,kategori_id,penerbit_id,penulis_id) "
                    + "VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setInt(2, rusak);
            pst.setInt(3, baik);
            pst.setInt(4, kategori);
            pst.setInt(5, penerbit);
            pst.setInt(6, penulis);
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
            int kategori,
            int penerbit,
            int penulis
    ) {
        try {
            String sql = "UPDATE buku SET "
                    + "nama=?, "
                    + "j_rusak=?, "
                    + "j_baik=?, "
                    + "kategori_id=?, "
                    + "penerbit_id=?, "
                    + "penulis_id=? "
                    + "WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setInt(2, rusak);
            pst.setInt(3, baik);
            pst.setInt(4, kategori);
            pst.setInt(5, penerbit);
            pst.setInt(6, penulis);
            pst.setInt(7, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // =========================
    // DELETE
    // =========================
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
    // =========================
    // TAMPIL DATA
    // =========================
    public void tampil(JTable table) {

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama Buku");
        model.addColumn("Rusak");
        model.addColumn("Baik");
        model.addColumn("Kategori");
        model.addColumn("Penerbit");
        model.addColumn("Penulis");
        try {
            String sql = "SELECT "
                    + "buku.id, "
                    + "buku.nama, "
                    + "buku.j_rusak, "
                    + "buku.j_baik, "
                    + "kategori.nama AS kategori, "
                    + "penerbit.nama AS penerbit, "
                    + "penulis.nama AS penulis "
                    + "FROM buku "
                    + "JOIN kategori "
                    + "ON buku.kategori_id = kategori.id "
                    + "JOIN penerbit "
                    + "ON buku.penerbit_id = penerbit.id "
                    + "JOIN penulis "
                    + "ON buku.penulis_id = penulis.id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("nama"),
                    rs.getString("j_rusak"),
                    rs.getString("j_baik"),
                    rs.getString("kategori"),
                    rs.getString("penerbit"),
                    rs.getString("penulis")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}