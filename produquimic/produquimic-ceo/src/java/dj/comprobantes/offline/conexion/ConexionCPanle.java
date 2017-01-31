/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.conexion;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import persistencia.Conexion;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class ConexionCPanle {

    public Connection getConexionCpanel() {
        System.getProperties().setProperty("http.proxyHost", "172.16.2.167");
        System.getProperties().setProperty("http.proxyPort", "8080");
        System.getProperties().setProperty("https.proxyHost", "172.16.2.167");
        System.getProperties().setProperty("https.proxyPort", "8080");
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", "172.16.2.167"); //PROXY TEMPORAL
        System.getProperties().put("proxyPort", "8080");

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

//          String sqlQuery = "insert into tu_tabla(campo_foto) values ( ?)";
//
//    FileInputStream fis = null;
//    PreparedStatement ps = null;
//    try {
//      conn.setAutoCommit(false);
//      File archivo = new File("myPhoto.png");
//      FileInputStream fis  = new FileInputStream(archivo);
//      ps = conn.prepareStatement(sqlQuery);
//      ps.setBinaryStream(1, fis, (int) file.length());
//      ps.executeUpdate();
//      conn.commit();
//    } finally {
//      ps.close();
//      fis.close();
//    }
    }

    public void guardarComprobanteNube(Comprobante comprobante) {
        //guarda en la nuebe el comprobante AUTORIZADO

        if (comprobante.getCodigoestado().equals(EstadoComprobanteEnum.AUTORIZADO.getCodigo())) {
            PreparedStatement ps = null;
            Connection conn = getConexionCPanel().getConnection();
            try {
                String sql = "insert into COMPROBANTE(PK_CODIGO_COMP,CODIGO_DOCUMENTO,	ESTADO,CLAVE_ACCESO,"
                        + "SECUENCIAL,CLIENTE,IDENTIFICACION,FECHA_EMISION,NUM_AUTORIZACION,FECHA_AUTORIZACION,"
                        + "ESTABLECIM,PTO_EMISION) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setLong(1, comprobante.getCodigocomprobante());
                ps.setString(2, comprobante.getCoddoc());
                ps.setString(3, EstadoComprobanteEnum.AUTORIZADO.getDescripcion());
                ps.setString(4, comprobante.getClaveacceso());
                ps.setString(5, comprobante.getSecuencial());
                ps.setString(6, comprobante.getCliente().getNombreCliente());
                ps.setString(7, comprobante.getCliente().getIdentificacion());
                ps.setDate(8, new java.sql.Date(comprobante.getFechaemision().getTime()));
                ps.setString(9, comprobante.getNumAutorizacion());
                ps.setDate(10, new java.sql.Date(comprobante.getFechaautoriza().getTime()));
                ps.setString(11, comprobante.getEstab());
                ps.setString(12, comprobante.getPtoemi());
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                    }
                }
            }

            try {
                File filPDF = null;
                File filXML = null;
                FileInputStream fisPDF = new FileInputStream(filPDF);
                FileInputStream fisXML = new FileInputStream(filXML);
                String sql = "insert into RIDE_COMPROBANTE(PK_CODIGO_COMP,ARCHIVO_PDF,ARCHIVO_XML) values (?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setLong(1, comprobante.getCodigocomprobante());
                ps.setBinaryStream(2, fisPDF, (int) filPDF.length());
                ps.setBinaryStream(3, fisPDF, (int) filXML.length());
                ps.executeUpdate();
            } catch (FileNotFoundException | SQLException e) {
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                    }
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }

        }
    }

    public Conexion getConexionCPanel() {
        Conexion con_conecta = new Conexion();
        con_conecta.setUnidad_persistencia("cPanel");
        return con_conecta;
    }

}
