/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.conexion;

import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.UtilitarioCeo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
public class ConexionCPanel {

    private final UtilitarioCeo utilitario = new UtilitarioCeo();
    public Connection conCPanel;

    public ConexionCPanel() throws GenericException {
        conectar();
    }

    /**
     * Crea la conexión a la base de datos utilizando el pool de conexiones
     *
     * @throws GenericException
     */
    private void conectar() throws GenericException {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://198.15.70.74:3306/produqui_ceo";
        String USER = "produqui_diego";
        String PASS = "sami2008";

        try {
            Class.forName(JDBC_DRIVER);
            this.conCPanel = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException se) {
            se.printStackTrace();
        }

    }

    /**
     * Ejecuta una consulta SQL
     *
     * @param sql
     * @return
     * @throws GenericException
     */
    public ResultSet consultar(String sql) throws GenericException {
        ResultSet resultSet = null;
        try {
            //System.out.println(sql);
            Statement sentensia = conCPanel.createStatement();
            resultSet = sentensia.executeQuery(sql);
        } catch (SQLException e) {
            throw new GenericException("ERROR. al consultar sql: " + sql, e);
        }
        return resultSet;
    }

    /**
     * Ejecuta una sentencia SQL
     *
     * @param sql
     * @return
     * @throws GenericException
     */
    public String ejecutar(String sql) throws GenericException {
        Statement sentensia = null;
        try {
            sentensia = conCPanel.createStatement();
            sentensia.executeUpdate(sql);
        } catch (SQLException e) {
            throw new GenericException("ERROR. al ejecutar sql: " + sql, e);
        } finally {
            if (sentensia != null) {
                try {
                    sentensia.close();
                } catch (Exception e) {
                }
            }
        }
        return "";
    }

    /**
     * Retorna un PreparedStatement de una sentencia sql
     *
     * @param sql
     * @return
     * @throws GenericException
     */
    public PreparedStatement getPreparedStatement(String sql) throws GenericException {
        try {
            return conCPanel.prepareStatement(sql);
        } catch (SQLException e) {
            throw new GenericException(e);
        }
    }

    /**
     * Desconecta la base de datos
     */
    public void desconectar() {
        try {
            if (conCPanel != null) {
                conCPanel.close();
            }
        } catch (Exception e) {
        }
    }

    public Connection getConnection() {
        return conCPanel;
    }

    public void setConnection(Connection cobCentral) {
        this.conCPanel = cobCentral;
    }
}
