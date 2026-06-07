package Auth;

import Koneksi.Koneksi;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Register {
    Connection conn;
    public Register() {
        try {
            conn = Koneksi.getKoneksi();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // =========================================
    // REGISTER USER
    // VERIF OTOMATIS UNVERIFIED
    // =========================================
    public void register(
            String nama,
            String username,
            String pass,
            String email,
            String nik
    ) {
        try {
            // =========================================
            // CEK USERNAME
            // =========================================
            String cek =
                    "SELECT * FROM users "
                    + "WHERE username=?";
            PreparedStatement pstCek = conn.prepareStatement(cek);
            pstCek.setString(1, username);
            ResultSet rs = pstCek.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(
                        null, "Username sudah digunakan"
                );
                return;
            }
            // =========================================
            // INSERT REGISTER
            // =========================================
            String sql = "INSERT INTO users "
                    + "(nama,username,pass,email,nik,verif) "
                    + "VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, username);
            pst.setString(3, pass);
            pst.setString(4, email);
            pst.setString(5, nik);
            pst.setString(6, "unverified");
            pst.executeUpdate();
            JOptionPane.showMessageDialog(
                    null, "Register berhasil"
            );
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
