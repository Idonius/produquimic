/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.exception.GenericException;
import javax.ejb.Local;

/**
 *
 * @author djacome
 */
@Local
public interface CPanelService {

    /**
     * Guarda un comprbante en la nube CPanel
     *
     * @param comprobante
     * @return
     * @throws dj.comprobantes.offline.exception.GenericException
     */
    public boolean guardarComprobanteNube(Comprobante comprobante) throws GenericException;

    /**
     * Sube los comprobantes Autorizados que no se encuentran en la nube
     *
     * @throws GenericException
     */
    public void subirComprobantesPendientes() throws GenericException;

    /**
     * Sube un comprobante Autorizado a la nube
     *
     * @throws GenericException
     */
    public void subirComprobante(Comprobante comprobanteActual) throws GenericException;
}
