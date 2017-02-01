/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.conexion.ConexionCPanel;
import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author djacome
 */
@Stateless
public class CPanelServiceImp implements CPanelService {

    @EJB
    private ArchivoService archivoService;

    @Override
    public boolean guardarComprobanteNube(Comprobante comprobante) throws GenericException {
        boolean guardo = false;
        //guarda en la nuebe el comprobante AUTORIZADO
        if (comprobante.getCodigoestado().equals(EstadoComprobanteEnum.AUTORIZADO.getCodigo())) {
            if (comprobante.getEnNube() == false) {
                ConexionCPanel con = new ConexionCPanel();
                //Crea usuario 
                if (isExisteCliente(comprobante.getCliente().getIdentificacion()) == false) {

                }
                //Guarda comprobante
                if (isExisteComprobante(comprobante.getCodigocomprobante()) == false) {
                    PreparedStatement ps = null;
                    try {
                        String sql = "insert into COMPROBANTE(PK_CODIGO_COMP,CODIGO_DOCUMENTO,	ESTADO,CLAVE_ACCESO,"
                                + "SECUENCIAL,CLIENTE,IDENTIFICACION,FECHA_EMISION,NUM_AUTORIZACION,FECHA_AUTORIZACION,"
                                + "ESTABLECIM,PTO_EMISION) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                        ps = con.getPreparedStatement(sql);
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
                    } catch (GenericException | SQLException e) {
                        if (ps != null) {
                            try {
                                ps.close();
                            } catch (Exception e1) {
                            }
                        }
                        con.desconectar();
                        guardo = false;
                        throw new GenericException(e);
                    }
                }

                //Guarda archivos ride
                if (isExisteArchivoComprobante(comprobante.getCodigocomprobante()) == false) {
                    PreparedStatement ps = null;
                    try {
                        byte[] pdf = archivoService.getPdf(comprobante);
                        byte[] xml = archivoService.getXml(comprobante);
                        File filPDF = File.createTempFile(comprobante.getClaveacceso(), ".pdf", null);
                        FileOutputStream fosPDF = new FileOutputStream(filPDF);
                        fosPDF.write(pdf);
                        fosPDF.close();
                        File filXML = File.createTempFile(comprobante.getClaveacceso(), ".xml", null);
                        FileOutputStream fosXML = new FileOutputStream(filXML);
                        fosXML.write(xml);
                        fosXML.close();
                        FileInputStream fisPDF = new FileInputStream(filPDF);
                        FileInputStream fisXML = new FileInputStream(filXML);
                        String sql = "insert into RIDE_COMPROBANTE(PK_CODIGO_COMP,ARCHIVO_PDF,ARCHIVO_XML) values (?,?,?)";
                        ps = con.getPreparedStatement(sql);
                        ps.setLong(1, comprobante.getCodigocomprobante());
                        ps.setBinaryStream(2, fisPDF, (int) filPDF.length());
                        ps.setBinaryStream(3, fisXML, (int) filXML.length());
                        ps.executeUpdate();
                    } catch (GenericException | IOException | SQLException e) {
                        e.printStackTrace();
                        if (ps != null) {
                            try {
                                ps.close();
                            } catch (Exception e1) {
                            }
                        }
                        con.desconectar();
                        guardo = false;
                        throw new GenericException(e);
                    }
                }

                guardo = true;
                con.desconectar();
            } else {
                guardo = true;
            }
        }
        return guardo;
    }

    public boolean isExisteComprobante(Long codigoComprobante) throws GenericException {
        boolean existe = false;
        ConexionCPanel con = new ConexionCPanel();
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            String sql = "SELECT PK_CODIGO_COMP FROM COMPROBANTE  WHERE PK_CODIGO_COMP = ?";
            ps = con.getPreparedStatement(sql);
            ps.setLong(1, codigoComprobante);
            res = ps.executeQuery();
            if (res.next()) {
                existe = true;
            }
        } catch (SQLException e) {
            throw new GenericException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (res != null) {
                    res.close();
                }
            } catch (Exception e) {
            }
            con.desconectar();
        }
        return existe;
    }

    public boolean isExisteArchivoComprobante(Long codigoComprobante) throws GenericException {
        boolean existe = false;
        ConexionCPanel con = new ConexionCPanel();
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            String sql = "SELECT PK_CODIGO_COMP FROM RIDE_COMPROBANTE  WHERE PK_CODIGO_COMP = ?";
            ps = con.getPreparedStatement(sql);
            ps.setLong(1, codigoComprobante);
            res = ps.executeQuery();
            if (res.next()) {
                existe = true;
            }
        } catch (SQLException e) {
            throw new GenericException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (res != null) {
                    res.close();
                }
            } catch (Exception e) {
            }
            con.desconectar();
        }
        return existe;
    }

    public boolean isExisteCliente(String identificacion) throws GenericException {
        boolean existe = false;
        return existe;
    }
}
