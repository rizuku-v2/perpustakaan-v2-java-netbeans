package Koneksi;
import java.sql.*;
import java.util.logging.*;

public class Koneksi {
    private static final Logger LOGGER = Logger.getLogger(Koneksi.class.getName());
    private static Connection con;

    public static Connection getKoneksi() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL Driver tidak ditemukan!", e);
            throw new SQLException("Driver tidak ditemukan: " + e.getMessage());
        }
        if (con == null || con.isClosed()) {
            String db   = "jdbc:mysql://localhost:3306/db_perpustakaan"
                        + "?useSSL=false"
                        + "&allowPublicKeyRetrieval=true"
                        + "&serverTimezone=Asia/Jakarta";
            String user = "root";
            String pass = "";
            con = DriverManager.getConnection(db, user, pass);
            LOGGER.info("Koneksi database berhasil.");
        }
        return con;
    }
}