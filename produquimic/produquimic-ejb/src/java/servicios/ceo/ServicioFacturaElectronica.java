/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.ceo;

import framework.aplicacion.TablaGenerica;
import javax.ejb.Stateless;
import servicios.ServicioBase;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless

public class ServicioFacturaElectronica extends ServicioBase {

    public String generarFacturaElectronica(String ide_cccfa) {
        String ide_srcom = "-1";

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
            if (tab_factura.getValor("ide_srcom") != null) {
                ide_srcom = tab_factura.getValor("ide_srcom");
            }
            TablaGenerica tab_cabecara = new TablaGenerica();
            tab_cabecara.setSql("SELECT * from sri_comprobante where ide_srcom=" + ide_srcom);
            tab_cabecara.ejecutarSql();
            TablaGenerica tab_detalle = new TablaGenerica();
            tab_detalle.setSql("SELECT * from sri_detalle_comprobante where ide_srcom=" + ide_srcom);
            tab_detalle.ejecutarSql();

            //Inserta cabecera
            if (tab_cabecara.isEmpty()) {
                tab_cabecara.insertar();
            }
            tab_cabecara.setValor("ide_sresc", "");
            tab_cabecara.setValor("coddoc_srcom", "");
            tab_cabecara.setValor("tipoemision_srcom", "");
            tab_cabecara.setValor("estab_srcom", "");
            tab_cabecara.setValor("ptoemi_srcom", "");
            tab_cabecara.setValor("fechaemision_srcom", "");
            tab_cabecara.setValor("fecha_sistema_srcom", "");
            tab_cabecara.setValor("ip_genera_srcom", "");
            tab_cabecara.setValor("subtotal_srcom", "");
            tab_cabecara.setValor("subtotal0_srcom", "");
            tab_cabecara.setValor("base_grabada_srcom", "");
            tab_cabecara.setValor("iva_srcom", "");
            tab_cabecara.setValor("descuento_srcom", "");
            tab_cabecara.setValor("total_srcom", "");
            tab_cabecara.setValor("identificacion_srcom", "");
            tab_cabecara.setValor("en_nube_srcom", "false");
            tab_cabecara.setValor("ide_geper", "");
            tab_cabecara.setValor("ide_cntdo", "");
            tab_cabecara.setValor("ide_empr", "");
            tab_cabecara.setValor("ide_sucu", "");
           
            tab_cabecara.guardar();
            ide_srcom = tab_factura.getValor("ide_srcom");
            if (tab_detalle.isEmpty() == false) {
                for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
                    tab_factura.eliminar();
                }
            }
            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
                tab_detalle.insertar();
                tab_detalle.setValor("ide_srcom", ide_srcom);
                tab_detalle.setValor("codigo_principal_srdec", "");
                tab_detalle.setValor("codigo_auxiliar_srdec", "");
                tab_detalle.setValor("descripcion_srdec", "");
                tab_detalle.setValor("cantidad_srdec", "");
                tab_detalle.setValor("precio_srdec", "");
                tab_detalle.setValor("descuento_detalle_srdec", "");
                tab_detalle.setValor("total_detalle_srdec", "");
                tab_detalle.setValor("porcentaje_iva_srdec", "");
            }
            tab_detalle.guardar();
        }

        return ide_srcom;
    }
}
