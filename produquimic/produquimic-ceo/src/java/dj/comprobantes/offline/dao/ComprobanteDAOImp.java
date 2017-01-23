/*
 *********************************************************************
 Objetivo: Servicio que implementa interface ComprobanteDAO
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dao;

import dj.comprobantes.offline.conexion.ConexionCEO;
import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class ComprobanteDAOImp implements ComprobanteDAO {

    private final Utilitario utilitario = new Utilitario();

    @Override
    public List<Comprobante> getComprobantesPorEstado(EstadoComprobanteEnum estado) throws GenericException {
        List<Comprobante> listaComprobantes = new ArrayList<>();
        ConexionCEO con = new ConexionCEO();
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            String sql = "SELECT * FROM sri_comprobante  WHERE ide_sresc =?";
            ps = con.getPreparedStatement(sql);
            ps.setInt(1, estado.getCodigo());
            res = ps.executeQuery();
            while (res.next()) {
                listaComprobantes.add(new Comprobante(res));
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
        return listaComprobantes;
    }

    @Override
    public Comprobante getComprobantePorClaveAcceso(String claveAcceso) throws GenericException {
        Comprobante comprobante = null;
        ConexionCEO con = new ConexionCEO();
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            String sql = "SELECT * FROM sri_comprobante  WHERE claveacceso_srcom = ?";
            ps = con.getPreparedStatement(sql);
            ps.setString(1, claveAcceso);
            res = ps.executeQuery();
            if (res.next()) {
                comprobante = new Comprobante(res);
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
        return comprobante;
    }

    @Override
    public List<Comprobante> getComprobantesCliente(String identificacion, TipoComprobanteEnum codigoDocumento) throws GenericException {
        List<Comprobante> listaComprobantes = new ArrayList<>();
        ConexionCEO con = new ConexionCEO();
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            String sql = "SELECT * FROM sri_comprobante WHERE identificacion_srcom= ? and coddoc_srcom = ? and ide_sresc='" + EstadoComprobanteEnum.AUTORIZADO.getCodigo() + "' order by secuencial_srcom desc";
            ps = con.getPreparedStatement(sql);
            ps.setString(1, identificacion);
            ps.setString(2, codigoDocumento.getCodigo());
            res = ps.executeQuery();
            while (res.next()) {
                listaComprobantes.add(new Comprobante(res));
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
        return listaComprobantes;
    }

    @Override
    public void actualizar(Comprobante comprobante) throws GenericException {
        PreparedStatement preparedStatement = null;
        ConexionCEO con = new ConexionCEO();
        String sql = "UPDATE cob_remesas..sri_comprobante set"
                + " ide_sresc=?"
                + " ,claveacceso_srcom=?"
                + " ,ide_srfid=?"
                + " ,autorizacion_srcomn=?"
                + " ,tipoemision_srcom=?"
                + " ,fechaautoriza_srcom=?"
                + " WHERE ide_srcom=?";

        try {
            preparedStatement = con.getPreparedStatement(sql);
            preparedStatement.setInt(1, comprobante.getCodigoestado());
            preparedStatement.setString(2, comprobante.getClaveacceso());
            preparedStatement.setInt(3, comprobante.getCodigofirma().getCodigofirma());
            preparedStatement.setString(4, comprobante.getNumAutorizacion());
            preparedStatement.setString(5, comprobante.getTipoemision());
            preparedStatement.setString(6, utilitario.getFormatoFecha(comprobante.getFechaautoriza()));
            preparedStatement.setLong(7, comprobante.getCodigocomprobante());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new GenericException("ERROR. No se puede actualizar el Comprobante", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception e) {
                }
            }
            con.desconectar();
        }
    }

}