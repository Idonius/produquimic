/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.service;

import javax.ejb.Stateless;
import persistencia.Conexion;

/**
 *
 * @author djacome
 */
@Stateless
public class CPanelServiceImp implements CPanelService {

    @Override
    public Conexion getConexionCPanel() {
        Conexion con_conecta = new Conexion();
        con_conecta.setUnidad_persistencia("cPanel");
        return con_conecta;
    }

}
