/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.integracion;

import framework.aplicacion.TablaGenerica;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import persistencia.Conexion;
import servicios.ServicioBase;
import servicios.inventario.ServicioInventario;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless

public class ServicioIntegracion extends ServicioBase {

    @EJB
    private ServicioInventario ser_inventario;

    public Conexion getConexionEscritorio() {
        Conexion con_conecta = new Conexion();
        con_conecta.setUnidad_persistencia("sistemaEscritotio");
        return con_conecta;
    }

    /**
     * Retorna sql de kardex de un cliente
     *
     * @param identificacion
     * @return
     */
    public String getSqlKardexCliente(String identificacion) {
        return "SELECT CODIGOKC,a.COD_CLIE,DATE_FORMAT(fecha,'%d/%m/%Y') as FECHA_MOVI ,FACTURA,DETALLE,INGRESO,EGRESO,TOTAL FROM KARDEXCLIENTES a\n"
                + "inner join CLIENTES B on a.COD_CLIE=B.COD_CLIE\n"
                + "where CEDULA ='" + identificacion + "' ORDER BY CODIGOKC";
    }

    /**
     * Retorna sql de kardex de un producto
     *
     * @param codigoProducto
     * @return
     */
    public String getSqlKardexProducto(String codigoProducto) {
        return "SELECT CODIGOKP,COD_PROD,DATE_FORMAT(fecha,'%d/%m/%Y')as FECHA_MOVI,FACTURA,DETALLE,INGRESO,EGRESO,TOTAL FROM KARDEXPRODUCTOS where COD_PROD ='" + codigoProducto + "' ORDER BY CODIGOKP";
    }

    public Double getUltimoPrecioCliente(String codigoProducto, String codigoCliente) {
        String sql = "SELECT PRECIO FROM DETALLE_FACTURAS,FACTURAS WHERE COD_PROD='" + codigoProducto + "' and cod_clie='" + codigoCliente + "' and FACTURAS.NUM_FACTURA=DETALLE_FACTURAS.NUM_FACTURA order by DETALLE_FACTURAS.num_factura desc";
        return null;
    }

    public String importarClientes() {
        String str_ide_geper = "";
        TablaGenerica tab_cod = new TablaGenerica();
        tab_cod.setTabla("gen_persona", "ide_geper", -1);
        tab_cod.setCondicion("es_cliente_geper=true and gen_ide_geper=572");
        tab_cod.getColumna("identificac_geper").setQuitarCaracteresEspeciales(true);
        tab_cod.setGenerarPrimaria(false);
        tab_cod.ejecutarSql();
        tab_cod.getColumna("ide_geper").setExterna(false);
        String str_cod = tab_cod.getStringColumna("identificac_geper");
        tab_cod.limpiar();

        Conexion con_conecta = getConexionEscritorio();
        TablaGenerica tab_clie = new TablaGenerica();
        tab_clie.setConexion(con_conecta);
        if (str_cod != null && str_cod.isEmpty() == false) {
            tab_clie.setSql("select * from clientes where cedula not in(" + str_cod + ",'')");
        } else {
            tab_clie.setCampoPrimaria("cod_clie");
            tab_clie.setSql("select * from clientes");
        }
        tab_clie.ejecutarSql();
        long int_maximo_cliente = utilitario.getConexion().getMaximo("gen_persona", "ide_geper", tab_clie.getTotalFilas());
        TablaGenerica tab_cabcxc = new TablaGenerica();
        tab_cabcxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
        tab_cabcxc.setCondicion("ide_ccctr=-1");
        tab_cabcxc.setGenerarPrimaria(false);
        tab_cabcxc.getColumna("ide_ccctr").setExterna(false);
        tab_cabcxc.ejecutarSql();
        long int_maximo_cab = utilitario.getConexion().getMaximo("cxc_cabece_transa", "ide_ccctr", tab_clie.getTotalFilas());

        TablaGenerica tab_detcxc = new TablaGenerica();
        tab_detcxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
        tab_detcxc.setCondicion("ide_ccdtr=-1");
        tab_detcxc.setGenerarPrimaria(false);
        tab_detcxc.getColumna("ide_ccdtr").setExterna(false);
        tab_detcxc.ejecutarSql();
        long int_maximo_det = utilitario.getConexion().getMaximo("cxc_detall_transa", "ide_ccdtr", tab_clie.getTotalFilas());

        for (int i = 0; i < tab_clie.getTotalFilas(); i++) {

            if (tab_clie.getValor(i, "nom_clie") == null || tab_clie.getValor(i, "nom_clie").isEmpty()) {
                continue;
            }
            tab_cod.insertar();
            tab_cod.setValor("ide_geper", String.valueOf(int_maximo_cliente));
            if (str_ide_geper.isEmpty() == false) {
                str_ide_geper += ",";
            }
            str_ide_geper += int_maximo_cliente;
            tab_cod.setValor("gen_ide_geper", "572");
            tab_cod.setValor("ide_empr", "0");
            tab_cod.setValor("ide_sucu", "0");
            String ide_getid = tab_clie.getValor(i, "cedula");
            if (ide_getid != null) {
                if (ide_getid.length() > 10) {
                    ide_getid = "1";//RUC
                } else {
                    ide_getid = "2";//CEDULA    
                }
            }
            tab_cod.setValor("ide_getid", ide_getid);
            tab_cod.setValor("identificac_geper", tab_clie.getValor(i, "cedula"));
            tab_cod.setValor("nom_geper", tab_clie.getValor(i, "nom_clie"));
            tab_cod.setValor("nombre_compl_geper", tab_clie.getValor(i, "nom_clie"));
            tab_cod.setValor("direccion_geper", tab_clie.getValor(i, "dir_clie"));
            tab_cod.setValor("telefono_geper", tab_clie.getValor(i, "telef1"));
            tab_cod.setValor("fax_geper", tab_clie.getValor(i, "telef2"));
            tab_cod.setValor("contacto_geper", tab_clie.getValor(i, "contacto"));
            tab_cod.setValor("correo_geper", tab_clie.getValor(i, "correo"));
            tab_cod.setValor("observacion_geper", tab_clie.getValor(i, "observaciones"));
            tab_cod.setValor("nivel_geper", "HIJO");
            tab_cod.setValor("es_cliente_geper", "true");

            String ide_cntco = tab_clie.getValor(i, "tipo");
            if (ide_cntco != null) {
                ide_cntco = ide_cntco.toUpperCase();
                if (ide_cntco.indexOf("OBLIGADO A LLEVAR CONTABILIDAD") > 0) {
                    ide_cntco = "3";
                } else if (ide_cntco.indexOf("PERSONA NATURAL") > 0) {
                    ide_cntco = "2";
                }
                if (ide_cntco.indexOf("CONTRIBUYENTE ESPECIAL") > 0) {
                    ide_cntco = "1";
                }
                if (ide_cntco.indexOf("ESPECIALES-EXPORTADORES") > 0) {
                    ide_cntco = "6";
                } else {
                    ide_cntco = "2";
                }
            }
            tab_cod.setValor("ide_cntco", ide_cntco);

            double dou_saldo_inicial = 0;
            try {
                dou_saldo_inicial = Double.parseDouble(tab_clie.getValor(i, "existencia"));
            } catch (Exception e) {
            }

            if (dou_saldo_inicial != 0) {
                //Creo la transaccion
                tab_cabcxc.insertar();
                tab_cabcxc.setValor("ide_ccctr", String.valueOf(int_maximo_cab));
                tab_cabcxc.setValor("ide_ccttr", "13");
                tab_cabcxc.setValor("ide_sucu", "0");
                tab_cabcxc.setValor("ide_empr", "0");
                tab_cabcxc.setValor("ide_geper", String.valueOf(int_maximo_cliente));
                tab_cabcxc.setValor("fecha_trans_ccctr", utilitario.getFechaActual());
                tab_cabcxc.setValor("observacion_ccctr", "Saldo inicial al " + utilitario.getFechaActual());

                tab_detcxc.insertar();
                tab_detcxc.setValor("ide_ccdtr", String.valueOf(int_maximo_det));
                tab_detcxc.setValor("ide_ccctr", String.valueOf(int_maximo_cab));
                tab_detcxc.setValor("ide_ccttr", "13");
                tab_detcxc.setValor("ide_sucu", "0");
                tab_detcxc.setValor("ide_empr", "0");
                tab_detcxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
                tab_detcxc.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
                tab_detcxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
                tab_detcxc.setValor("numero_pago_ccdtr", "0");
                tab_detcxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(dou_saldo_inicial));
                tab_detcxc.setValor("observacion_ccdtr", "Saldo inicial al " + utilitario.getFechaActual());

                int_maximo_cab++;
                int_maximo_det++;

            }
            int_maximo_cliente++;

        }

        tab_cod.guardar();
        tab_cabcxc.guardar();
        tab_detcxc.guardar();

        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
            if (tab_clie.getTotalFilas() > 0) {
                utilitario.agregarMensaje("Se importaron correctamente ", tab_clie.getTotalFilas() + " CLIENTES del sistema de facturación");
            }
        }

        return str_ide_geper;
    }

    public void importarFacturas(String fecha) {
        fecha = "2016-12-01";//utilitario.getFechaActual();
        importarClientes();
        importarProductos();
//cabeceras
        TablaGenerica tab_temp_cabecera = new TablaGenerica();
        tab_temp_cabecera.setTabla("cxc_cabece_factura", "ide_cccfa", 0);
        tab_temp_cabecera.setCondicion("fact_mig_cccfa is not null and fecha_emisi_cccfa>='" + fecha + "'");
        tab_temp_cabecera.setGenerarPrimaria(false);
        tab_temp_cabecera.getColumna("ide_cccfa").setExterna(false);
        tab_temp_cabecera.ejecutarSql();
        String fact_mig_cccfa = tab_temp_cabecera.getStringColumna("fact_mig_cccfa");
        if (!fact_mig_cccfa.isEmpty()) {
            fact_mig_cccfa = "and NUM_FACTURA NOT IN (" + fact_mig_cccfa + ")";
        }
        tab_temp_cabecera.limpiar();
        Conexion con_conecta = getConexionEscritorio();

        TablaGenerica tab_aux_cab = new TablaGenerica();
        tab_aux_cab.setConexion(con_conecta);

        //Importo todas las facturas desde -01-01-2015
        tab_aux_cab.setSql("select * from facturas \n"
                + "inner join clientes on facturas.COD_CLIE=clientes.COD_CLIE\n"
                + "where FECHA >='" + fecha + "' "
                + fact_mig_cccfa
                + " order by NUM_FACTURA limit 25");
        tab_aux_cab.ejecutarSql();

        long long_max = utilitario.getConexion().getMaximo("cxc_cabece_factura", "ide_cccfa", tab_aux_cab.getTotalFilas());
        String str_iva = "1"; //por defecto si iva
        for (int i = 0; i < tab_aux_cab.getTotalFilas(); i++) {
            ///Buscamos el ideGeper si existe inserta
            TablaGenerica tab_clie = utilitario.consultar("Select ide_geper,ide_geper from gen_persona where identificac_geper='" + tab_aux_cab.getValor(i, "CEDULA") + "'");
            if (tab_clie.getTotalFilas() == 0) {
                continue;
            }
            tab_temp_cabecera.insertar();
            tab_temp_cabecera.setValor("ide_cccfa", long_max + "");
            tab_temp_cabecera.setValor("ide_cntdo", utilitario.getVariable("p_con_tipo_documento_factura"));// factura 

            String str_anulada = tab_aux_cab.getValor(i, "TOTAL");
            if (str_anulada != null) {
                tab_temp_cabecera.setValor("ide_ccefa", utilitario.getVariable("p_cxc_estado_factura_normal"));
                if (!tab_aux_cab.getValor(i, "VENTAS0").equals("0")) {
                    str_iva = "0"; //no iva
                }
            } else {
                tab_temp_cabecera.setValor("ide_ccefa", utilitario.getVariable("p_cxc_estado_factura_anulada"));
            }

            tab_temp_cabecera.setValor("ide_geper", tab_clie.getValor("ide_geper"));
            tab_temp_cabecera.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            //Forma de pago
            tab_temp_cabecera.setValor("ide_cndfp", "");
            String str_forma_pago = "0";  //por defecto 0 
//            if (str_forma == null || str_forma.isEmpty()) {
//                str_forma = "CONTADO";
//            }
//            //EFECTIVO =0
//            //CREDITO 30 =2
//            //CREDITO 45 =3
//            //CREDITO 60 =6
//            String str_cod_forma_pago = "";
//            if (str_forma.equalsIgnoreCase("CONTADO")) {
//                str_cod_forma_pago = "0";
//            } else if (str_forma.equalsIgnoreCase("CRÉDITO 30 DIAS")) {
//                str_cod_forma_pago = "0";
//            }
//            if (str_forma.equalsIgnoreCase("CRÉDITO 45 DIAS")) {
//                str_cod_forma_pago = "0";
//            }
//            if (str_forma.equalsIgnoreCase("CRÉDITO 60 DIAS")) {
//                str_forma = "0";
//            }
            tab_temp_cabecera.setValor("ide_cndfp", str_forma_pago);
            tab_temp_cabecera.setValor("ide_ccdaf", "0");//datos factura
            tab_temp_cabecera.setValor("fecha_emisi_cccfa", tab_aux_cab.getValor(i, "FECHA"));
            tab_temp_cabecera.setValor("fecha_trans_cccfa", utilitario.getFechaActual());
            tab_temp_cabecera.setValor("secuencial_cccfa", utilitario.generarCero(9 - tab_aux_cab.getValor(i, "NUM_FACTURA").length()) + tab_aux_cab.getValor(i, "NUM_FACTURA"));
            tab_temp_cabecera.setValor("fact_mig_cccfa", tab_aux_cab.getValor(i, "NUM_FACTURA"));
            tab_temp_cabecera.setValor("direccion_cccfa", tab_aux_cab.getValor(i, "DIR_CLIE"));
            tab_temp_cabecera.setValor("telefono_cccfa", tab_aux_cab.getValor(i, "TELEF1"));
            tab_temp_cabecera.setValor("observacion_cccfa", "Venta de Quimicos - " + tab_aux_cab.getValor(i, "FORMA"));
            tab_temp_cabecera.setValor("base_no_objeto_iva_cccfa", "0");
            if (str_iva.equals("1")) {
                tab_temp_cabecera.setValor("base_tarifa0_cccfa", tab_aux_cab.getValor(i, "SUBTOTAL"));
            } else {
                tab_temp_cabecera.setValor("base_grabada_cccfa", tab_aux_cab.getValor(i, "SUBTOTAL"));
            }

            tab_temp_cabecera.setValor("valor_iva_cccfa", tab_aux_cab.getValor(i, "IVA"));
            tab_temp_cabecera.setValor("total_cccfa", tab_aux_cab.getValor(i, "TOTAL"));
            tab_temp_cabecera.setValor("pagado_cccfa", "false");
            tab_temp_cabecera.setValor("solo_guardar_cccfa", "true");
            long_max++;
        }

        //Inserta los detalles  
        TablaGenerica tab_temp_detalle = new TablaGenerica();
        tab_temp_detalle.setTabla("cxc_deta_factura", "ide_ccdfa", 0);
        tab_temp_detalle.setCondicion("ide_ccdfa=-1");
        tab_temp_detalle.ejecutarSql();

        TablaGenerica tab_aux_deta = new TablaGenerica();
        tab_aux_deta.setConexion(con_conecta);
        for (int i = 0; i < tab_temp_cabecera.getTotalFilas(); i++) {
            tab_aux_deta.setSql("select * from detalle_facturas \n"
                    + "inner JOIN productos on detalle_facturas.COD_PROD=productos.COD_PROD\n"
                    + "where NUM_FACTURA=" + tab_temp_cabecera.getValor(i, "fact_mig_cccfa"));
            tab_aux_deta.ejecutarSql();
            for (int j = 0; j < tab_aux_deta.getTotalFilas(); j++) {
                //Busco el ide del articulo 
                TablaGenerica tab_producto = utilitario.consultar("select * from inv_articulo where codigo_inarti='" + tab_aux_deta.getValor(j, "COD_PROD") + "'");
                if (tab_producto.getTotalFilas() > 0) {
                    tab_temp_detalle.insertar();
                    tab_temp_detalle.setValor("ide_cccfa", tab_temp_cabecera.getValor(i, "ide_cccfa"));
                    tab_temp_detalle.setValor("ide_inarti", tab_producto.getValor("ide_inarti"));
                    tab_temp_detalle.setValor("cantidad_ccdfa", tab_aux_deta.getValor(j, "CANTIDAD"));
                    tab_temp_detalle.setValor("precio_ccdfa", tab_aux_deta.getValor(j, "PRECIO"));
                    tab_temp_detalle.setValor("total_ccdfa", tab_aux_deta.getValor(j, "PTOTAL"));
                    tab_temp_detalle.setValor("iva_inarti_ccdfa", str_iva);
                    tab_temp_detalle.setValor("observacion_ccdfa", tab_aux_deta.getValor(j, "DETALLE"));
                    tab_temp_detalle.setValor("alterno_ccdfa", "00");
                    tab_temp_detalle.setValor("credito_tributario_ccdfa", "false");
                    tab_temp_detalle.setValor("precio_promedio_ccdfa", tab_aux_deta.getValor(j, "PRECIO"));
                }
            }
        }

        tab_temp_cabecera.guardar();
        tab_temp_detalle.guardar();
        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
            //guardo correctamente
            String str_facturas = tab_aux_cab.getStringColumna("NUM_FACTURA");
            //pone estado migrado
//            if (str_facturas.isEmpty() == false) {
//                con_conecta.ejecutarSql("UPDATE facturas set MIGRADA=1 where NUM_FACTURA in (" + str_facturas + ")");
//            }

            if (tab_aux_cab.getTotalFilas() > 0) {
                utilitario.agregarMensaje("Se importaron correctamente ", tab_aux_cab.getTotalFilas() + " FACTURAS DE VENTAS del sistema de facturación");

            } else {
                utilitario.agregarMensajeInfo("El sistema tiene todas las facturas de ventas importadas", "");
            }

        }
    }

    public String importarProductos() {
        String str_ide_inarti = "";
        TablaGenerica tab_cod = new TablaGenerica();
        tab_cod.setTabla("inv_articulo", "ide_inarti", -1);
        tab_cod.setCondicion("inv_ide_inarti=46");
        tab_cod.setCampoOrden("codigo_inarti");
        tab_cod.setGenerarPrimaria(false);
        tab_cod.ejecutarSql();
        tab_cod.getColumna("ide_inarti").setExterna(false);
        String str_cod = tab_cod.getStringColumna("codigo_inarti");
        tab_cod.limpiar();

        Conexion con_conecta = getConexionEscritorio();

        TablaGenerica tab_quimi = new TablaGenerica();
        tab_quimi.setConexion(con_conecta);
        if (str_cod != null && str_cod.isEmpty() == false) {
            tab_quimi.setSql("select * from productos where cod_prod not in(" + str_cod + ",'')");
        } else {
            tab_quimi.setCampoPrimaria("cod_prod");
            tab_quimi.setSql("select * from productos");
        }

        tab_quimi.ejecutarSql();

        long int_maximo_articulo = utilitario.getConexion().getMaximo("inv_articulo", "ide_inarti", tab_quimi.getTotalFilas());

        TablaGenerica tab_inventario = new TablaGenerica();
        tab_inventario.setTabla("inv_det_comp_inve", "ide_indci", -1);
        tab_inventario.setCondicion("ide_indci=-1");

        TablaGenerica tab_cab_inventario = new TablaGenerica();
        tab_cab_inventario.setTabla("inv_cab_comp_inve", "ide_incci", -1);
        tab_cab_inventario.setCondicion("ide_incci=-1");
        tab_cab_inventario.ejecutarSql();
        tab_cab_inventario.insertar();
        tab_cab_inventario.setValor("ide_geper", "542");
        tab_cab_inventario.setValor("ide_inepi", "1");
        tab_cab_inventario.setValor("ide_intti", "30");
        tab_cab_inventario.setValor("ide_sucu", "0");
        tab_cab_inventario.setValor("ide_empr", "0");
        tab_cab_inventario.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
        tab_cab_inventario.setValor("ide_inbod", "1");
        tab_cab_inventario.setValor("numero_incci", ser_inventario.getSecuencialComprobanteInventario("1"));
        tab_cab_inventario.setValor("fecha_trans_incci", utilitario.getFechaActual());
        tab_cab_inventario.setValor("observacion_incci", "Importa Productos Saldo inicial al " + utilitario.getFechaActual());
        tab_cab_inventario.setValor("fecha_siste_incci", utilitario.getFechaActual());
        tab_cab_inventario.setValor("hora_sistem_incci", utilitario.getHoraActual());
        tab_cab_inventario.guardar();
        for (int i = 0; i < tab_quimi.getTotalFilas(); i++) {
            tab_cod.insertar();
            if (str_ide_inarti.isEmpty() == false) {
                str_ide_inarti += ",";
            }
            str_ide_inarti += int_maximo_articulo;
            tab_cod.setValor("ide_inarti", String.valueOf(int_maximo_articulo));
            tab_cod.setValor("inv_ide_inarti", "46");
            tab_cod.setValor("ide_empr", "0");
            tab_cod.setValor("ide_sucu", "0");
            String str_presentacion = tab_quimi.getValor(i, "presentacion");
            if (str_presentacion != null) {
                str_presentacion = str_presentacion.toUpperCase();
                if (str_presentacion.indexOf("KIL") > 0) {
                    str_presentacion = "5";
                } else if (str_presentacion.indexOf("UNIDA") > 0) {
                    str_presentacion = "0";
                } else if (str_presentacion.indexOf("LITRO") > 0) {
                    str_presentacion = "6";
                } else if (str_presentacion.indexOf("CAJA") > 0) {
                    str_presentacion = "1";
                } else if (str_presentacion.indexOf("FRASC") > 0) {
                    str_presentacion = "7";
                } else if (str_presentacion.indexOf("GALO") > 0) {
                    str_presentacion = "6";
                } else if (str_presentacion.indexOf("FUND") > 0) {
                    str_presentacion = "4";
                } else {
                    str_presentacion = "5";
                }
            }

            tab_cod.setValor("ide_inuni", str_presentacion);
            tab_cod.setValor("codigo_inarti", tab_quimi.getValor(i, "cod_prod"));
            tab_cod.setValor("nombre_inarti", tab_quimi.getValor(i, "nom_prod"));
            tab_cod.setValor("iva_inarti", "1");
            tab_cod.setValor("observacion_inarti", tab_quimi.getValor(i, "observa"));
            tab_cod.setValor("nivel_inarti", "HIJO");

            if (tab_quimi.getValor(i, "controla") != null && tab_quimi.getValor(i, "controla").equals("true")) {
                tab_cod.setValor("hace_kardex_inarti", "true");
            } else {
                tab_cod.setValor("hace_kardex_inarti", "false");
            }

            tab_inventario.insertar();
            tab_inventario.setValor("ide_inarti", String.valueOf(int_maximo_articulo));
            tab_inventario.setValor("ide_empr", "0");
            tab_inventario.setValor("ide_sucu", "0");
            tab_inventario.setValor("ide_incci", tab_cab_inventario.getValor("ide_incci"));
            tab_inventario.setValor("precio_promedio_indci", tab_quimi.getValor(i, "precio_compra"));
            tab_inventario.setValor("precio_indci", tab_quimi.getValor(i, "precio_venta"));
            tab_inventario.setValor("cantidad_indci", tab_quimi.getValor(i, "existencia"));
            tab_inventario.setValor("observacion_indci", "Saldo anterior sistema de Inventario");
            try {
                tab_inventario.setValor("valor_indci", utilitario.getFormatoNumero(Double.parseDouble(tab_quimi.getValor(i, "existencia")) * Double.parseDouble(tab_quimi.getValor(i, "precio_compra"))));
            } catch (Exception e) {
            }

            int_maximo_articulo++;
        }
        tab_cod.guardar();
        tab_inventario.guardar();

        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
            if (tab_quimi.getTotalFilas() > 0) {
                utilitario.agregarMensaje("Se importaron correctamente ", tab_quimi.getTotalFilas() + " PRODUCTOS QUIMICOS del sistema de facturación");

            }
        }

        return str_ide_inarti;

    }

}