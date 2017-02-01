/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import java.sql.Connection;
import javax.ejb.Local;

/**
 *
 * @author djacome
 */
@Local
public interface CPanelService {

    /**
     * Retorna la Conexion del CPanel
     *
     * @return
     */
    public Connection getConexionCPanel();

    /**
     * Guarda un comprbante en la nube CPanel
     *
     * @param comprobante
     * @return
     */
    public boolean guardarComprobanteNube(Comprobante comprobante);
}
