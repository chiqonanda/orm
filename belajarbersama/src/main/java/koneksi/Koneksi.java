// ===========================================================
// Topik: Integrasi Database
// Letakkan file ini pada src/config/Koneksi.java
// ===========================================================

package Koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {

    private static final String URL = "jdbc:mysql://localhost:3306/perpas";
    private static final String USER = "root";
    private static final String PASS = "";

    //variabel statis untuk menyimpan koneksi
    private static Connection conn;

    public static Connection getConnection() {
        try {
            // memeriksa koneksi belum ada atau sudah terputus
            if (conn == null || conn.isClosed()) {
                // Jika belum ada, membuat koneksi baru
                conn = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (SQLException e) {
            System.out.println("Koneksi GAGAL: " + e.getMessage());
        }

        //Kembalikan koneksi yang sudah aman
        return conn;
    }
}