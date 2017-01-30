/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.integracion;

import javax.ejb.Stateless;
import persistencia.Conexion;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless

public class ServicioCPanel {

    public Conexion getConexionCPanel() {
        Conexion con_conecta = new Conexion();
        con_conecta.setUnidad_persistencia("cPanel");
        return con_conecta;
    }

}
