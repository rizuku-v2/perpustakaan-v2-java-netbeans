package Crud;

import Koneksi.Koneksi;
import java.sql.*;

public class CrudDashboard {

    Connection conn;

    public CrudDashboard() {
        try {
            conn = Koneksi.getKoneksi();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int jumlahPengunjung() {

        try {

            String sql =
                    "SELECT COUNT(*) total "
                    + "FROM visitor";

            Statement st =
                    conn.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            if(rs.next()){

                return rs.getInt("total");

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return 0;
    }

    public int jumlahPeminjaman() {

        try {

            String sql =
                    "SELECT COUNT(*) total "
                    + "FROM peminjaman";

            Statement st =
                    conn.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            if(rs.next()){

                return rs.getInt("total");

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return 0;
    }

    public int jumlahJenisBuku() {

        try {

            String sql =
                    "SELECT "
                    + "SUM(j_baik + j_rusak) total "
                    + "FROM buku";

            Statement st =
                    conn.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            if(rs.next()){

                return rs.getInt("total");

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return 0;
    }

    public int jumlahUser() {

        try {

            String sql =
                    "SELECT COUNT(*) total "
                    + "FROM users "
                    + "WHERE verif='ya'";

            Statement st =
                    conn.createStatement();

            ResultSet rs =
                    st.executeQuery(sql);

            if(rs.next()){

                return rs.getInt("total");

            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return 0;
    }
}