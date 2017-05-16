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
import dj.comprobantes.offline.service.ArchivoService;
import dj.comprobantes.offline.service.ComprobanteService;
import framework.aplicacion.TablaGenerica;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import servicios.ServicioBase;
import servicios.contabilidad.ServicioConfiguracion;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless

public class ServicioGuiaRemisionElectronica extends ServicioBase {

    @EJB
    private ServicioConfiguracion ser_configuracion;

    @EJB
    private ComprobanteService comprobanteService;
    @EJB
    private ArchivoService archivoService;

    /**
     * Genera un comprobante electrónico a partir de una factura ya guardada
     *
     * @param ide_cpcno
     * @return
     */
    public String generarNotaCreditoElectronica(String ide_cpcno) {
        String ide_srcom = "-1";
        TablaGenerica tab_factura = utilitario.consultar("select a.ide_cpcno,numero_cpcno,fecha_emisi_cpcno,serie_ccdaf,base_grabada_cpcno\n"
                + ",base_tarifa0_cpcno,valor_iva_cpcno,total_cpcno,alterno_ats,identificac_geper\n"
                + ",a.ide_geper,ide_cntdo,f.ide_inarti,codigo_inarti,observacion_cpdno,nombre_inarti,cantidad_cpdno\n"
                + ",precio_cpdno,iva_inarti_cpdno,valor_cpdno,ide_srcom,nombre_inuni,nombre_cpmno,num_doc_mod_cpcno,fecha_emision_mod_cpcno,valor_mod_cpcno \n"
                + "from cxp_cabecera_nota  a \n"
                + "inner join gen_persona b on a.ide_geper = b.ide_geper  \n"
                + "inner join cxp_detalle_nota c on a.ide_cpcno=c.ide_cpcno \n"
                + "inner join cxc_datos_fac d on a.ide_ccdaf=d.ide_ccdaf\n"
                + "inner join con_deta_forma_pago e on a.ide_cndfp=e.ide_cndfp\n"
                + "inner join  inv_articulo f on c.ide_inarti =f.ide_inarti\n"
                + "left join  inv_unidad g on c.ide_inuni =g.ide_inuni\n"
                + "inner join  cxp_motivo_nota h on a.ide_cpmno =h.ide_cpmno\n"
                + "where a.ide_cpcno=" + ide_cpcno);
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
            } else {
                tab_cabecara.modificar(tab_cabecara.getFilaActual());
            }
            double dou_base0 = 0;
            double dou_basegraba = 0;
            double dou_subtotal = 0;
            try {
                dou_base0 = Double.parseDouble(tab_factura.getValor("base_tarifa0_cpcno"));
            } catch (Exception e) {
            }
            try {
                dou_basegraba = Double.parseDouble(tab_factura.getValor("base_grabada_cpcno"));
            } catch (Exception e) {
            }
            dou_subtotal = dou_base0 + dou_basegraba;

            tab_cabecara.setValor("num_doc_mod_srcom", tab_factura.getValor("num_doc_mod_cpcno"));
            tab_cabecara.setValor("fecha_emision_mod_srcom", tab_factura.getValor("fecha_emision_mod_cpcno"));
            tab_cabecara.setValor("valor_mod_srcom", tab_factura.getValor("valor_mod_cpcno"));
            tab_cabecara.setValor("codigo_docu_mod_srcom", TipoComprobanteEnum.FACTURA.getCodigo());
            tab_cabecara.setValor("motivo_srcom", tab_factura.getValor("nombre_cpmno"));

            tab_cabecara.setValor("ide_sresc", String.valueOf(EstadoComprobanteEnum.PENDIENTE.getCodigo()));
            tab_cabecara.setValor("coddoc_srcom", TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo());
            tab_cabecara.setValor("tipoemision_srcom", TipoEmisionEnum.NORMAL.getCodigo());
            tab_cabecara.setValor("estab_srcom", tab_factura.getValor("serie_ccdaf").substring(0, 3));
            tab_cabecara.setValor("ptoemi_srcom", tab_factura.getValor("serie_ccdaf").substring(3, 6));
            tab_cabecara.setValor("fechaemision_srcom", tab_factura.getValor("fecha_emisi_cpcno"));
            tab_cabecara.setValor("reutiliza_srcom", "false");//no reutiliza por defecto
            tab_cabecara.setValor("fecha_sistema_srcom", utilitario.getFechaActual());
            tab_cabecara.setValor("ip_genera_srcom", utilitario.getIp());
            tab_cabecara.setValor("subtotal0_srcom", utilitario.getFormatoNumero(dou_base0));
            tab_cabecara.setValor("base_grabada_srcom", utilitario.getFormatoNumero(dou_basegraba));
            tab_cabecara.setValor("subtotal_srcom", utilitario.getFormatoNumero(dou_subtotal));
            tab_cabecara.setValor("iva_srcom", tab_factura.getValor("valor_iva_cpcno"));
            tab_cabecara.setValor("descuento_srcom", "0.00");
            tab_cabecara.setValor("total_srcom", tab_factura.getValor("total_cpcno"));
            tab_cabecara.setValor("identificacion_srcom", tab_factura.getValor("identificac_geper"));
            tab_cabecara.setValor("en_nube_srcom", "false");
            tab_cabecara.setValor("ide_geper", tab_factura.getValor("ide_geper"));
            tab_cabecara.setValor("ide_cntdo", tab_factura.getValor("ide_cntdo"));
            tab_cabecara.setValor("forma_cobro_srcom", tab_factura.getValor("alterno_ats"));
            tab_cabecara.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cabecara.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            //tab_cabecara.setValor("numero_cpcno", tab_factura.getValor("secuencial_cccfa")); !!!!!SOLO PARA PRUEBAS COMENTADO

            tab_cabecara.guardar();
            ide_srcom = tab_cabecara.getValor("ide_srcom");

            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
                tab_detalle.insertar();
                tab_detalle.setValor("ide_srcom", ide_srcom);
                tab_detalle.setValor("codigo_principal_srdec", tab_factura.getValor(i, "ide_inarti"));
                tab_detalle.setValor("codigo_auxiliar_srdec", tab_factura.getValor(i, "codigo_inarti"));
                String descripcion = "";
                if (tab_factura.getValor(i, "nombre_inuni") != null) {
                    descripcion += tab_factura.getValor(i, "nombre_inuni");
                }
                descripcion += " " + tab_factura.getValor(i, "nombre_inarti");
                tab_detalle.setValor("descripcion_srdec", descripcion.trim());
                tab_detalle.setValor("cantidad_srdec", tab_factura.getValor(i, "cantidad_cpdno"));
                tab_detalle.setValor("precio_srdec", tab_factura.getValor(i, "precio_cpdno"));
                tab_detalle.setValor("descuento_detalle_srdec", "0.00");
                tab_detalle.setValor("total_detalle_srdec", tab_factura.getValor(i, "valor_cpdno"));

                if (tab_factura.getValor(i, "iva_inarti_cpdno").equalsIgnoreCase("1")) {
                    tab_detalle.setValor("porcentaje_iva_srdec", String.valueOf((ser_configuracion.getPorcentajeIva() * 100)));
                } else {
                    tab_detalle.setValor("porcentaje_iva_srdec", "0");
                }
            }
            if (tab_cabecara.isFilaModificada()) {
                //elimina detalles si modifica
                utilitario.getConexion().agregarSqlPantalla("delete from sri_detalle_comprobante where ide_srcom is null");
            }
            tab_detalle.guardar();

            if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
                //Asigna secuencial a la factura
                String strSecuencialF = getSecuencialNotaCredito();
                utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET secuencial_srcom='" + strSecuencialF + "' where ide_srcom=" + ide_srcom);
                utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET reutiliza_srcom= false where secuencial_srcom='" + strSecuencialF + "' and reutiliza_srcom=true and coddoc_srcom='" + TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo() + "'");

                try {
                    Comprobante comprobanteNotaC = comprobanteService.getComprobantePorId(Integer.parseInt(ide_srcom));
                    String strClaveAcceso = comprobanteService.getClaveAcceso(comprobanteNotaC);
                    utilitario.getConexion().ejecutarSql("UPDATE sri_comprobante SET claveacceso_srcom='" + strClaveAcceso + "' where ide_srcom=" + ide_srcom);
                    utilitario.getConexion().ejecutarSql("UPDATE cxp_cabecera_nota SET  ide_srcom=" + ide_srcom + ", numero_cpcno='" + strSecuencialF + "' where ide_cpcno=" + ide_cpcno);
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
    public String getSecuencialNotaCredito() {
        long maximo = 0;
        //reutiliza secuenciales si existe
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT secuencial_srcom FROM sri_comprobante where coddoc_srcom='").append(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo()).append("' and reutiliza_srcom=true order by secuencial_srcom limit 1");
            List lisResultado = utilitario.getConexion().consultar(sql.toString());
            return (String.valueOf(lisResultado.get(0)));
        } catch (Exception e) {
        }

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT MAX(secuencial_srcom) FROM sri_comprobante where coddoc_srcom='").append(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo()).append("'");
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

    public String enviarComprobante(String claveAcceso) {
        String mensaje = "";
        try {
            comprobanteService.enviarComprobante(claveAcceso);
        } catch (Exception e) {
            mensaje = e.getMessage() + "";
        }
        return mensaje;
    }

    public void getRIDE(String ide_srcom) throws GenericException {
        try {
            byte[] ar = archivoService.getPdf(comprobanteService.getComprobantePorId(new Integer(ide_srcom)));
            //crea el archivo
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            String realPath = (String) ec.getRealPath("/reportes");
            File fil_reporte = new File(realPath + "/reporte" + utilitario.getVariable("IDE_USUA") + ".pdf");
            FileOutputStream fileOuputStream = new FileOutputStream(fil_reporte);
            fileOuputStream.write(ar);
        } catch (NumberFormatException | GenericException | IOException e) {
            e.printStackTrace();
        }
    }

}
