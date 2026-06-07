package Koneksi;
import java.sql.*;
public class Koneksi {
    private static Connection con;
    public static Connection getKoneksi() throws SQLException {        
        String db = "jdbc:mysql://localhost:3306/db_perpustakaan";
        String user = "root";
        String pass = "";
        if(con == null){
            con = DriverManager.getConnection(db,user,pass);
        }
        return con;
    }
}