package Crud;
import Koneksi.Koneksi;
import java.awt.HeadlessException;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import Helper.ComboItem;

public class CrudPengembalian {
    Connection conn;
    public CrudPengembalian() {
        try {
            conn = Koneksi.getKoneksi();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void simpan(
        int peminjaman,
        String statusPengembalian
    ) {try {

            String ambil =
                    "SELECT "
                    + "buku_id, "
                    + "kondisi, "
                    + "tgl_jatuh_tempo "
                    + "FROM peminjaman "
                    + "WHERE id=?";

            PreparedStatement pstAmbil =
                    conn.prepareStatement(ambil);

            pstAmbil.setInt(1, peminjaman);

            ResultSet rs =
                    pstAmbil.executeQuery();

            if(rs.next()){

                int buku =
                        rs.getInt("buku_id");

                String kondisiAwal =
                        rs.getString("kondisi");

                LocalDate jatuhTempo =
                        rs.getDate("tgl_jatuh_tempo")
                        .toLocalDate();

                LocalDate hariIni =
                        LocalDate.now();

                int denda = 0;

                // denda terlambat
                if(hariIni.isAfter(jatuhTempo)){

                    denda += 20000;

                }
                // denda rusak
                if(statusPengembalian
                        .equalsIgnoreCase("rusak")){

                    denda += 100000;

                }

                // denda hilang
                if(statusPengembalian
                        .equalsIgnoreCase("hilang")){

                    denda += 100000;

                }

                // simpan pengembalian
                String sql =
                        "INSERT INTO pengembalian "
                        + "(denda,peminjaman_id,status) "
                        + "VALUES (?,?,?)";

                PreparedStatement pst =
                        conn.prepareStatement(sql);

                pst.setInt(1, denda);
                pst.setInt(2, peminjaman);
                pst.setString(3, statusPengembalian);

                pst.executeUpdate();

                // update status peminjaman
                String updatePeminjaman =
                        "UPDATE peminjaman "
                        + "SET status='dikembalikan' "
                        + "WHERE id=?";

                PreparedStatement pstStatus =
                        conn.prepareStatement(updatePeminjaman);

                pstStatus.setInt(1, peminjaman);

                pstStatus.executeUpdate();

                // jika hilang stok tidak kembali
                if(statusPengembalian
                        .equalsIgnoreCase("hilang")){

                    JOptionPane.showMessageDialog(
                            null,
                            "Buku hilang\nDenda : "
                            + denda
                    );

                    return;
                }

                // kondisi awal baik
                if(kondisiAwal
                        .equalsIgnoreCase("baik")){

                    // kembali rusak
                    if(statusPengembalian
                            .equalsIgnoreCase("rusak")){

                        String update =
                                "UPDATE buku "
                                + "SET j_rusak=j_rusak+1 "
                                + "WHERE id=?";

                        PreparedStatement pstUpdate =
                                conn.prepareStatement(update);

                        pstUpdate.setInt(1, buku);

                        pstUpdate.executeUpdate();

                    }else{

                        // kembali baik

                        String update =
                                "UPDATE buku "
                                + "SET j_baik=j_baik+1 "
                                + "WHERE id=?";

                        PreparedStatement pstUpdate =
                                conn.prepareStatement(update);

                        pstUpdate.setInt(1, buku);

                        pstUpdate.executeUpdate();

                    }

                }

                // kondisi awal rusak
                else if(kondisiAwal
                        .equalsIgnoreCase("rusak")){

                    String update =
                            "UPDATE buku "
                            + "SET j_rusak=j_rusak+1 "
                            + "WHERE id=?";

                    PreparedStatement pstUpdate =
                            conn.prepareStatement(update);

                    pstUpdate.setInt(1, buku);

                    pstUpdate.executeUpdate();

                }

                JOptionPane.showMessageDialog(
                        null,
                        "Pengembalian berhasil"
                        + "\nDenda : "
                        + denda
                );

            }

        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void tampil(JTable table){

        DefaultTableModel model =
                new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("User");
        model.addColumn("Buku");
        model.addColumn("Status");
        model.addColumn("Tanggal");
        model.addColumn("Denda");

        try{

            String sql =
                    "SELECT "
                    + "pengembalian.id, "
                    + "users.nama AS user, "
                    + "buku.nama AS buku, "
                    + "pengembalian.status, "
                    + "pengembalian.tgl, "
                    + "pengembalian.denda "
                    + "FROM pengembalian "
                    + "JOIN peminjaman "
                    + "ON pengembalian.peminjaman_id = peminjaman.id "
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
                    rs.getString("status"),
                    rs.getString("tgl"),
                    rs.getInt("denda")

                });

            }

            table.setModel(model);

        }catch(SQLException e){

            System.out.println(e.getMessage());

        }
    }
    public void loadStatus(JComboBox dStatus){

        dStatus.removeAllItems();
        dStatus.addItem("baik");
        dStatus.addItem("rusak");
        dStatus.addItem("hilang");

    }
    
    public void loadPeminjaman(JComboBox dPeminjaman){

        try{

            dPeminjaman.removeAllItems();
           
            String sql =
                    "SELECT "
                    + "peminjaman.id, "
                    + "CONCAT("
                    + "users.nama,"
                    + "' - ',"
                    + "buku.nama"
                    + ") AS nama "
                    + "FROM peminjaman "
                    + "JOIN users "
                    + "ON peminjaman.user_id=users.id "
                    + "JOIN buku "
                    + "ON peminjaman.buku_id=buku.id "
                    + "WHERE peminjaman.status='dipinjam'";

            Statement st =
                    conn.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            while(rs.next()){

                dPeminjaman.addItem(

                        new ComboItem(

                                rs.getInt("id"),

                                rs.getString("nama")

                        )

                );

            }

        }catch(SQLException e){

            System.out.println(
                    e.getMessage()
            );

        }

    }
}