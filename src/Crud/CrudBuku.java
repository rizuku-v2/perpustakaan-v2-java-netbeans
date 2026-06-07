package Crud;
import Helper.ComboItem;
import Koneksi.Koneksi;
import java.sql.*;
import javax.swing.JComboBox;
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
    // =========================
    // CREATE
    // =========================
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
    // =========================
    // TAMPIL DATA
    // =========================
    public void tampil(JTable table) {

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama Buku");
        model.addColumn("Baik");
        model.addColumn("Rusak");
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
                    rs.getString("j_baik"),
                    rs.getString("j_rusak"),
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
    // =========================
    // UPDATE
    // =========================
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
    // LOAD DATA COMBO BOX
    // =========================
    public void loadKategori(JComboBox dKategori) {
        try {
            dKategori.removeAllItems();
            String sql = "SELECT * FROM kategori ORDER BY nama";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                dKategori.addItem(
                        new ComboItem(
                                rs.getInt("id"),
                                rs.getString("nama")
                        )
                );
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void loadPenerbit(JComboBox dPenerbit) {
        try {
            dPenerbit.removeAllItems();
            String sql = "SELECT * FROM penerbit ORDER BY nama";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                dPenerbit.addItem(
                        new ComboItem(
                                rs.getInt("id"),
                                rs.getString("nama")
                        )
                );
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void loadPenulis(JComboBox dPenulis) {
        try {
            dPenulis.removeAllItems();
            String sql = "SELECT * FROM penulis ORDER BY nama";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                dPenulis.addItem(
                        new ComboItem(
                                rs.getInt("id"),
                                rs.getString("nama")
                        )
                );
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }  
    
    public ResultSet getById(int id){
        try{
            String sql = "SELECT * FROM buku WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            return pst.executeQuery();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}