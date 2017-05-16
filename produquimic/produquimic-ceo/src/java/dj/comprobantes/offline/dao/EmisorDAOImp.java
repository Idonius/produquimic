/*
 *********************************************************************
 Objetivo: Servicio que implementa interface EmisorDAO
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dao;

import dj.comprobantes.offline.conexion.ConexionCEO;
import dj.comprobantes.offline.dto.Emisor;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.util.UtilitarioCeo;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.Stateless;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class EmisorDAOImp implements EmisorDAO {

    private final UtilitarioCeo utilitario = new UtilitarioCeo();
    private Emisor emisor = null;

    @Override
    public Emisor getEmisor() throws GenericException {
        if (emisor == null) {
            ConexionCEO conn = new ConexionCEO();
            ResultSet resultSet = null;
            try {
                resultSet = conn.consultar("select * from sri_emisor a inner join sis_empresa b on a.ide_empr=b.ide_empr where a.ide_empr=0");
                if (resultSet.next()) {
                    emisor = new Emisor();
                    emisor.setCodigoemisor(resultSet.getInt("ide_sremi"));
                    emisor.setRuc(resultSet.getString("identificacion_empr"));
                    emisor.setRazonsocial(resultSet.getString("nom_empr"));
                    emisor.setNombrecomercial(resultSet.getString("nom_corto_empr"));
                    emisor.setDirmatriz(resultSet.getString("direccion_empr"));
                    emisor.setContribuyenteespecial(resultSet.getString("contribuyenteespecial_empr"));
                    emisor.setObligadocontabilidad(resultSet.getString("obligadocontabilidad_empr"));
                    emisor.setTiempomaxespera(resultSet.getInt("tiempo_espera_sremi"));
                    emisor.setAmbiente(resultSet.getInt("ambiente_sremi"));
                    emisor.setWsdlrecepcion(resultSet.getString("wsdl_recep_offline_sremi"));
                    emisor.setWsdlautirizacion(resultSet.getString("wsdl_autori_offline_sremi"));
                }
            } catch (SQLException e) {
                throw new GenericException("ERROR. No se puede retornar el Emisor", e);
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (Exception e) {
                    }
                }
                conn.desconectar();
            }
        }
        if (emisor == null) {
            throw new GenericException("ERROR. No existe Emisor");
        }
        return emisor;
    }
}
