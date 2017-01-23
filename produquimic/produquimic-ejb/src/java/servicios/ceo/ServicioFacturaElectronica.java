/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.ceo;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import servicios.ServicioBase;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless

public class ServicioFacturaElectronica extends ServicioBase {

    public String generarFacturaElectronica(String ide_cccfa) {
        String ide_srcom = null;
        TablaGenerica tab_cabecara = new TablaGenerica();
        tab_cabecara.setSql("SELECT * from sri_comprobante where ide_srcom=-1");
        tab_cabecara.ejecutarSql();
        TablaGenerica tab_detalle = new TablaGenerica();
        tab_detalle.setSql("SELECT * from sri_detalle_comprobante where ide_srdec=-1");
        tab_detalle.ejecutarSql();

        TablaGenerica tab_factura = utilitario.consultar("select a.ide_cccfa,secuencial_cccfa,fecha_emisi_cccfa,serie_ccdaf,base_grabada_cccfa\n"
                + ",base_tarifa0_cccfa,valor_iva_cccfa,total_cccfa,alterno_ats,identificac_geper\n"
                + ",a.ide_geper,ide_cntdo,f.ide_inarti,codigo_inarti,nombre_inarti,cantidad_ccdfa\n"
                + ",precio_ccdfa,iva_inarti_ccdfa,total_ccdfa\n"
                + "from cxc_cabece_factura  a \n"
                + "inner join gen_persona b on a.ide_geper = b.ide_geper  \n"
                + "inner join cxc_deta_factura c on a.ide_cccfa=c.ide_cccfa \n"
                + "inner join cxc_datos_fac d on a.ide_ccdaf=d.ide_ccdaf\n"
                + "inner join con_deta_forma_pago e on a.ide_cndfp=e.ide_cndfp\n"
                + "inner join  inv_articulo f on c.ide_inarti =f.ide_inarti\n"
                + "where a.ide_cccfa=" + ide_cccfa);

        if (tab_factura.isEmpty() == false) {
            //Inserta cabecera
            tab_cabecara.insertar();
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.setValor("", "");
            tab_cabecara.guardar();
            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
                tab_detalle.insertar();
                tab_detalle.setValor("", "");
                tab_detalle.setValor("", "");
                tab_detalle.setValor("", "");
                tab_detalle.setValor("", "");
                tab_detalle.setValor("", "");
                tab_detalle.setValor("", "");
                tab_detalle.setValor("", "");
                tab_detalle.setValor("", "");
                tab_detalle.setValor("", "");
                tab_detalle.setValor("", "");
            }
        }

        return ide_srcom;
    }
}
