/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.service;

import javax.ejb.Local;
import persistencia.Conexion;

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
    public Conexion getConexionCPanel();

}
