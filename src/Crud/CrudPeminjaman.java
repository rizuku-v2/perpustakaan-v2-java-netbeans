package Crud;

import Helper.ComboItem;
import Koneksi.Koneksi;
import java.sql.*;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CrudPeminjaman {

    Connection conn;

    public CrudPeminjaman() {

        try {

            conn = Koneksi.getKoneksi();

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    // =========================
    // SIMPAN
    // =========================
    public void simpan(
            int userId,
            int bukuId
    ) {

        try {

            String kondisi = "";

            String cek =
                    "SELECT j_baik,j_rusak "
                    + "FROM buku "
                    + "WHERE id=?";

            PreparedStatement pstCek =
                    conn.prepareStatement(cek);

            pstCek.setInt(1, bukuId);

            ResultSet rs =
                    pstCek.executeQuery();

            if(rs.next()){

                int baik =
                        rs.getInt("j_baik");

                int rusak =
                        rs.getInt("j_rusak");

                // PRIORITAS BUKU BAIK
                if(baik > 0){

                    kondisi = "baik";

                    String update =
                            "UPDATE buku "
                            + "SET j_baik=j_baik-1 "
                            + "WHERE id=?";

                    PreparedStatement pst =
                            conn.prepareStatement(update);

                    pst.setInt(1, bukuId);

                    pst.executeUpdate();

                }

                // JIKA BAIK HABIS
                else if(rusak > 0){

                    kondisi = "rusak";

                    String update =
                            "UPDATE buku "
                            + "SET j_rusak=j_rusak-1 "
                            + "WHERE id=?";

                    PreparedStatement pst =
                            conn.prepareStatement(update);

                    pst.setInt(1, bukuId);

                    pst.executeUpdate();

                }

                else{

                    JOptionPane.showMessageDialog(
                            null,
                            "Stok buku habis"
                    );

                    return;

                }

            }

            String sql =
                    "INSERT INTO peminjaman "
                    + "(user_id,buku_id,status,"
                    + "tgl_jatuh_tempo,kondisi) "
                    + "VALUES "
                    + "(?,?,'dipinjam',"
                    + "DATE_ADD(CURDATE(),INTERVAL 14 DAY),?)";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setInt(1, userId);
            pst.setInt(2, bukuId);
            pst.setString(3, kondisi);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    null,
                    "Peminjaman berhasil"
            );

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    // =========================
    // UPDATE
    // =========================
    public void update(
            int id,
            int userId,
            int bukuId
    ) {

        try {

            String sql =
                    "UPDATE peminjaman SET "
                    + "user_id=?, "
                    + "buku_id=? "
                    + "WHERE id=?";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setInt(1, userId);
            pst.setInt(2, bukuId);
            pst.setInt(3, id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    null,
                    "Data berhasil diubah"
            );

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    // =========================
    // DELETE
    // =========================
    public void hapus(int id) {

        try {

            String sql =
                    "DELETE FROM peminjaman "
                    + "WHERE id=?";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setInt(1, id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(
                    null,
                    "Data berhasil dihapus"
            );

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    // =========================
    // TAMPIL
    // =========================
    public void tampil(JTable table) {

        DefaultTableModel model =
                new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("User");
        model.addColumn("Buku");
        model.addColumn("Tanggal");
        model.addColumn("Jatuh Tempo");
        model.addColumn("Status");
        model.addColumn("Kondisi");

        try {

            String sql =
                    "SELECT "
                    + "peminjaman.id, "
                    + "users.nama AS user, "
                    + "buku.nama AS buku, "
                    + "peminjaman.tgl, "
                    + "peminjaman.tgl_jatuh_tempo, "
                    + "peminjaman.status, "
                    + "peminjaman.kondisi "
                    + "FROM peminjaman "
                    + "JOIN users "
                    + "ON peminjaman.user_id = users.id "
                    + "JOIN buku "
                    + "ON peminjaman.buku_id = buku.id";

            Statement st =
                    conn.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            while(rs.next()){

                model.addRow(new Object[]{

                    rs.getInt("id"),
                    rs.getString("user"),
                    rs.getString("buku"),
                    rs.getString("tgl"),
                    rs.getString("tgl_jatuh_tempo"),
                    rs.getString("status"),
                    rs.getString("kondisi")

                });

            }

            table.setModel(model);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    // =========================
    // GET BY ID
    // =========================
    public ResultSet getById(int id){

        try{

            String sql =
                    "SELECT * "
                    + "FROM peminjaman "
                    + "WHERE id=?";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setInt(1, id);

            return pst.executeQuery();

        }catch(SQLException e){

            System.out.println(e.getMessage());

        }

        return null;
    }
   
    public void loadUser(JComboBox dUser){

        try{

            dUser.removeAllItems();

            String sql =
                    "SELECT id,nama "
                    + "FROM users "
                    + "ORDER BY nama";

            Statement st =
                    conn.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            while(rs.next()){

                dUser.addItem(
                        new ComboItem(
                                rs.getInt("id"),
                                rs.getString("nama")
                        )
                );

            }

        }catch(SQLException e){

            System.out.println(e.getMessage());

        }

    }
    
    public void loadBuku(JComboBox dBuku){
        try{

            dBuku.removeAllItems();

            String sql =
                    "SELECT id,nama "
                    + "FROM buku "
                    + "ORDER BY nama";

            Statement st =
                    conn.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            while(rs.next()){

                dBuku.addItem(
                        new ComboItem(
                                rs.getInt("id"),
                                rs.getString("nama")
                        )
                );

            }

        }catch(SQLException e){

            System.out.println(e.getMessage());

        }

    }
}