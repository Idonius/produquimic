/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.escritorio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class ConexionCPanle {

    public Connection getConexionCpanel() {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://198.15.70.74:3306/produqui_ceo";
        String USER = "produqui_diego";
        String PASS = "sami2008";
        Connection conn = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException se) {
            se.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        ConexionCPanle c = new ConexionCPanle();

        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = c.getConexionCpanel();
            // STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM COMPROBANTE";
            ResultSet rs = stmt.executeQuery(sql);

            // STEP 5: Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                int id = rs.getInt(1);

                // Display values
                System.out.print("ID: " + id);

            }
            // STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }

    }

}
