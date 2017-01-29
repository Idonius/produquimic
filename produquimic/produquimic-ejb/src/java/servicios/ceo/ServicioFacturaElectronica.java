/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.ceo;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoEmisionEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.service.ComprobanteService;
import framework.aplicacion.TablaGenerica;
import java.util.Formatter;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import servicios.ServicioBase;
import servicios.contabilidad.ServicioConfiguracion;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless

public class ServicioFacturaElectronica extends ServicioBase {
    
    @EJB
    private ServicioConfiguracion ser_configuracion;
    
    @EJB
    private ComprobanteService comprobanteService;

    /**
     * Genera un comprobante electrónico a partir de una factura ya guardada
     *
     * @param ide_cccfa
     * @return
     */
    public String generarFacturaElectronica(String ide_cccfa) {
        String ide_srcom = "-1";
        
        TablaGenerica tab_factura = utilitario.consultar("select a.ide_cccfa,secuencial_cccfa,fecha_emisi_cccfa,serie_ccdaf,base_grabada_cccfa\n"
                + ",base_tarifa0_cccfa,valor_iva_cccfa,total_cccfa,alterno_ats,identificac_geper\n"
                + ",a.ide_geper,ide_cntdo,f.ide_inarti,codigo_inarti,observacion_ccdfa,nombre_inarti,cantidad_ccdfa\n"
                + ",precio_ccdfa,iva_inarti_ccdfa,total_ccdfa,ide_srcom\n"
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
            tab_cabecara.setTabla("sri_comprobante", "ide_srcom");
            tab_cabecara.setCondicion("ide_srcom=" + ide_srcom);
            tab_cabecara.ejecutarSql();
            TablaGenerica tab_detalle = new TablaGenerica();
            tab_detalle.setTabla("sri_detalle_comprobante", "ide_srdec");
            tab_detalle.setCondicion("ide_srcom=" + ide_srcom);
            tab_detalle.ejecutarSql();

            //Inserta cabecera
            if (tab_cabecara.isEmpty()) {
                tab_cabecara.insertar();
            }
            double dou_base0 = 0;
            double dou_basegraba = 0;
            double dou_subtotal = 0;
            try {
                dou_base0 = Double.parseDouble(tab_factura.getValor("base_tarifa0_cccfa"));
            } catch (Exception e) {
            }
            try {
                dou_basegraba = Double.parseDouble(tab_factura.getValor("base_grabada_cccfa"));
            } catch (Exception e) {
            }
            dou_subtotal = dou_base0 + dou_basegraba;
            
            tab_cabecara.setValor("ide_sresc", String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()));
            tab_cabecara.setValor("coddoc_srcom", TipoComprobanteEnum.FACTURA.getCodigo());
            tab_cabecara.setValor("tipoemision_srcom", TipoEmisionEnum.NORMAL.getCodigo());
            tab_cabecara.setValor("estab_srcom", tab_factura.getValor("serie_ccdaf").substring(0, 3));
            tab_cabecara.setValor("ptoemi_srcom", tab_factura.getValor("serie_ccdaf").substring(3, 6));
            tab_cabecara.setValor("fechaemision_srcom", tab_factura.getValor("fecha_emisi_cccfa"));
            tab_cabecara.setValor("fecha_sistema_srcom", utilitario.getFechaActual());
            tab_cabecara.setValor("ip_genera_srcom", utilitario.getIp());
            tab_cabecara.setValor("subtotal0_srcom", utilitario.getFormatoNumero(dou_base0));
            tab_cabecara.setValor("base_grabada_srcom", utilitario.getFormatoNumero(dou_basegraba));
            tab_cabecara.setValor("subtotal_srcom", utilitario.getFormatoNumero(dou_subtotal));
            tab_cabecara.setValor("iva_srcom", tab_factura.getValor("valor_iva_cccfa"));
            tab_cabecara.setValor("descuento_srcom", "0.00");
            tab_cabecara.setValor("total_srcom", tab_factura.getValor("total_cccfa"));
            tab_cabecara.setValor("identificacion_srcom", tab_factura.getValor("identificac_geper"));
            tab_cabecara.setValor("en_nube_srcom", "false");
            tab_cabecara.setValor("ide_geper", tab_factura.getValor("ide_geper"));
            tab_cabecara.setValor("ide_cntdo", tab_factura.getValor("ide_cntdo"));
            tab_cabecara.setValor("forma_cobro_srcom", tab_factura.getValor("alterno_ats"));
            tab_cabecara.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cabecara.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            //tab_cabecara.setValor("secuencial_srcom", tab_factura.getValor("secuencial_cccfa")); !!!!!SOLO PARA PRUEBAS COMENTADO

            tab_cabecara.guardar();
            ide_srcom = tab_cabecara.getValor("ide_srcom");
            
            if (tab_detalle.isEmpty() == false) {
                for (int i = 0; i < tab_detalle.getTotalFilas(); i++) {
                    tab_detalle.modificar(i);
                    tab_detalle.setValor("ide_srcom", null);
                }
            }
            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
                tab_detalle.insertar();
                tab_detalle.setValor("ide_srcom", ide_srcom);
                tab_detalle.setValor("codigo_principal_srdec", tab_factura.getValor(i, "ide_inarti"));
                tab_detalle.setValor("codigo_auxiliar_srdec", tab_factura.getValor(i, "codigo_inarti"));
                tab_detalle.setValor("descripcion_srdec", tab_factura.getValor(i, "observacion_ccdfa"));
                tab_detalle.setValor("cantidad_srdec", tab_factura.getValor(i, "cantidad_ccdfa"));
                tab_detalle.setValor("precio_srdec", tab_factura.getValor(i, "precio_ccdfa"));
                tab_detalle.setValor("descuento_detalle_srdec", "0.00");
                tab_detalle.setValor("total_detalle_srdec", tab_factura.getValor(i, "total_ccdfa"));
                
                if (tab_factura.getValor(i, "iva_inarti_ccdfa").equalsIgnoreCase("1")) {
                    tab_detalle.setValor("porcentaje_iva_srdec", String.valueOf((ser_configuracion.getPorcentajeIva() * 100)));
                } else {
                    tab_detalle.setValor("porcentaje_iva_srdec", "0");
                }
            }
            tab_detalle.guardar();
            utilitario.getConexion().agregarSqlPantalla("delete from sri_detalle_comprobante where ide_srcom is null");
            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                //Asigna secuencial a la factura
                String strSecuencialF = getSecuencialFactura();
                utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET secuencial_srcom='" + strSecuencialF + "' where ide_srcom=" + ide_srcom);
                try {
                    Comprobante comprobanteFactura = comprobanteService.getComprobantePorId(Integer.parseInt(ide_srcom));
                    String strClaveAcceso = comprobanteService.getClaveAcceso(comprobanteFactura);
                    utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET claveacceso_srcom='" + strClaveAcceso + "' where ide_srcom=" + ide_srcom);
                    utilitario.getConexion().ejecutarSql("UPDATE cxc_cabece_factura SET  ide_srcom=" + ide_srcom + ", secuencial_cccfa='" + strSecuencialF + "' where ide_cccfa=" + ide_cccfa);
                } catch (NumberFormatException | GenericException e) {
                    e.printStackTrace();
                }
                
            }
        }
        
        return ide_srcom;
    }

    /**
     * Retorna el secuencial de los comprobantes Electrónicos
     *
     * @return
     */
    public String getSecuencialFactura() {
        long maximo = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT MAX(secuencial_srcom) FROM sri_comprobante where coddoc_srcom='").append(TipoComprobanteEnum.FACTURA.getCodigo()).append("'");
            List lisResultado = utilitario.getConexion().consultar(sql.toString());
            maximo = Long.parseLong(String.valueOf(lisResultado.get(0)));
        } catch (Exception e) {
        }
        maximo++;
        Formatter fmt = new Formatter();
        String secuencial = fmt.format("%09d", maximo).toString();
        fmt.close();
        return secuencial;
    }
    
}
