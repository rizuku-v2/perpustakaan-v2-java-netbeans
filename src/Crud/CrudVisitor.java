package Crud;
import Koneksi.Koneksi;
import java.sql.*;
import Crud.CrudVisitor;
import Helper.ComboItem;
import javax.swing.JComboBox;

public class CrudVisitor {
    Connection conn;
    public CrudVisitor() {
        try {
            conn = Koneksi.getKoneksi();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void simpan(
        String nama,
        Integer userId
    ){

        try{
            

            String sql =
                    "INSERT INTO visitors "
                    + "(nama,user_id) "
                    + "VALUES (?,?)";

            PreparedStatement pst =
                    conn.prepareStatement(sql);
            pst.setString(1, nama);

            if(userId == null){
                pst.setNull(2,java.sql.Types.INTEGER);
            }else{
                pst.setInt(2, userId);
            }

            pst.executeUpdate();

        }catch(SQLException e){

            System.out.println(e.getMessage());

        }
    }
    public void loadJenis(JComboBox<String> dJenis){

        dJenis.removeAllItems();

        dJenis.addItem("Guest");
        dJenis.addItem("User");
    }
    public void loadUser(JComboBox dUser){

        try{

            dUser.removeAllItems();

            String sql =
                    "SELECT id,nama "
                    + "FROM users"
                    + " ORDER BY nama";

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
    

    /**
     * Tampilkan semua data visitor di JTable.
     * Kolom: No | Nama | Jenis | Tanggal Kunjungan
     */
    public void loadData(javax.swing.JTable table) {

        javax.swing.table.DefaultTableModel model =
            new javax.swing.table.DefaultTableModel(
                new Object[]{"No", "Nama", "Jenis", "Tanggal Kunjungan"}, 0
            ) {
                public boolean isCellEditable(int r, int c) { return false; }
            };

        String sql =
                "SELECT v.nama, "
                + "CASE WHEN v.user_id IS NULL "
                + "     THEN 'Guest' ELSE 'Member' END AS jenis, "
                + "DATE_FORMAT(v.tgl, '%d/%m/%Y %H:%i') AS tgl "
                + "FROM visitors v "
                + "ORDER BY v.tgl DESC";

        try (
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery()
        ) {
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("nama"),
                    rs.getString("jenis"),
                    rs.getString("tgl")
                });
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Gagal memuat data visitor: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }

        table.setModel(model);

        // Atur lebar kolom
        javax.swing.table.TableColumnModel cm = table.getColumnModel();
        if (cm.getColumnCount() >= 4) {
            cm.getColumn(0).setPreferredWidth(40);   // No
            cm.getColumn(1).setPreferredWidth(220);  // Nama
            cm.getColumn(2).setPreferredWidth(80);   // Jenis
            cm.getColumn(3).setPreferredWidth(170);  // Tanggal
        }
    }

}