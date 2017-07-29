/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios.integracion;

import framework.aplicacion.TablaGenerica;
import java.util.Date;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import persistencia.Conexion;
import servicios.ServicioBase;

/**
 *
 * @author DIEGOFERNANDOJACOMEG
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ServicioIntegracion extends ServicioBase {

    public Conexion getConexionEscritorio() {
        Conexion con_conecta = new Conexion();
        con_conecta.setUnidad_persistencia("sistemaEscritotio");
        return con_conecta;
    }

    /**
     * Retorna sql de kardex de un cliente
     *
     * @param codigo_geper
     * @return
     */
    public String getSqlKardexCliente_Escritorio(String codigo_geper) {
        return "SELECT CODIGOKC,COD_CLIE,DATE_FORMAT(fecha,'%d/%m/%Y') as FECHA_MOVI ,FACTURA,DETALLE,INGRESO,EGRESO,TOTAL FROM KARDEXCLIENTES a\n"
                + "where COD_CLIE ='" + codigo_geper + "' ORDER BY CODIGOKC";
    }

    /**
     * Retorna sql de kardex de un proveedor
     *
     * @param codigo_geper
     * @return
     */
    public String getSqlKardexProveedor_Escritorio(String codigo_geper) {
        return "SELECT CODIGOKPV,COD_PROVE,DATE_FORMAT(fecha,'%d/%m/%Y') as FECHA_MOVI ,FACTURA,DETALLE,INGRESO,EGRESO,TOTAL FROM KARDEXPROVEEDORES a\n"
                + "where COD_PROVE ='" + codigo_geper + "' ORDER BY CODIGOKPV";
    }

    /**
     * Retorna sql de kardex de un producto
     *
     * @param codigoProducto
     * @return
     */
    public String getSqlKardexProducto_Escritorio(String codigoProducto) {
        return "SELECT CODIGOKP,COD_PROD,DATE_FORMAT(fecha,'%d/%m/%Y')as FECHA_MOVI,FACTURA,DETALLE,COSTO,INGRESO,EGRESO,TOTAL FROM KARDEXPRODUCTOS where COD_PROD ='" + codigoProducto + "' ORDER BY CODIGOKP";
    }

    /**
     * Retorna el ultimo precio de un producto facturado a un cliente
     *
     * @param codigoProducto
     * @param codigoCliente
     * @return
     */
    public Double getUltimoPrecioCliente_Escritorio(String codigoProducto, String codigoCliente) {
        String sql = "SELECT PRECIO,COD_PROD FROM DETALLE_FACTURAS,FACTURAS WHERE COD_PROD='" + codigoProducto + "' and cod_clie='" + codigoCliente + "' and FACTURAS.NUM_FACTURA=DETALLE_FACTURAS.NUM_FACTURA order by DETALLE_FACTURAS.num_factura desc  LIMIT 1";
        TablaGenerica tab = new TablaGenerica();
        Conexion con_conecta = getConexionEscritorio();
        tab.setConexion(con_conecta);
        tab.setSql(sql);
        tab.ejecutarSql();
        double dou_precio = 0;
        if (tab.isEmpty() == false) {
            try {
                dou_precio = Double.parseDouble(tab.getValor("PRECIO"));
            } catch (Exception e) {
            }
        }
        return dou_precio;
    }

    /**
     * Retorna el codigo de compra para ser insertado
     *
     * @return
     */
    private int getCodigoCompra() {
        String sql = "(SELECT MAX(k.COD_COMPRA)+1 as COD_COMPRA,'1' as TOTAL  FROM compras K)";
        TablaGenerica tab = new TablaGenerica();
        Conexion con_conecta = getConexionEscritorio();
        tab.setConexion(con_conecta);
        tab.setSql(sql);
        tab.ejecutarSql();
        int codCompra = 0;
        if (tab.isEmpty() == false) {
            try {
                codCompra = Integer.parseInt(tab.getValor("COD_COMPRA"));
            } catch (Exception e) {
            }
        }
        return codCompra;
    }

    public String importarClientes() {
        String str_ide_geper = "";
        TablaGenerica tab_cod = new TablaGenerica();
        tab_cod.setTabla("gen_persona", "ide_geper", -1);
        tab_cod.setCondicion("es_cliente_geper=true and gen_ide_geper=572");
        tab_cod.getColumna("codigo_geper").setQuitarCaracteresEspeciales(true);
        tab_cod.setGenerarPrimaria(false);
        tab_cod.ejecutarSql();
        tab_cod.getColumna("ide_geper").setExterna(false);
        String str_cod = tab_cod.getStringColumna("codigo_geper");
        tab_cod.limpiar();

        Conexion con_conecta = getConexionEscritorio();
        TablaGenerica tab_clie = new TablaGenerica();
        tab_clie.setConexion(con_conecta);
        if (str_cod != null && str_cod.isEmpty() == false) {
            tab_clie.setSql("select * from clientes where cod_clie not in(" + str_cod + ",'')");
        } else {
            tab_clie.setCampoPrimaria("cod_clie");
            tab_clie.setSql("select * from clientes");
        }
        tab_clie.ejecutarSql();
        long int_maximo_cliente = utilitario.getConexion().getMaximo("gen_persona", "ide_geper", tab_clie.getTotalFilas());
//////        TablaGenerica tab_cabcxc = new TablaGenerica();
//////        tab_cabcxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
//////        tab_cabcxc.setCondicion("ide_ccctr=-1");
//////        tab_cabcxc.setGenerarPrimaria(false);
//////        tab_cabcxc.getColumna("ide_ccctr").setExterna(false);
//////        tab_cabcxc.ejecutarSql();
//////        long int_maximo_cab = utilitario.getConexion().getMaximo("cxc_cabece_transa", "ide_ccctr", tab_clie.getTotalFilas());
//////
//////        TablaGenerica tab_detcxc = new TablaGenerica();
//////        tab_detcxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
//////        tab_detcxc.setCondicion("ide_ccdtr=-1");
//////        tab_detcxc.setGenerarPrimaria(false);
//////        tab_detcxc.getColumna("ide_ccdtr").setExterna(false);
//////        tab_detcxc.ejecutarSql();
//////        long int_maximo_det = utilitario.getConexion().getMaximo("cxc_detall_transa", "ide_ccdtr", tab_clie.getTotalFilas());

        for (int i = 0; i < tab_clie.getTotalFilas(); i++) {

            if (tab_clie.getValor(i, "nom_clie") == null || tab_clie.getValor(i, "nom_clie").isEmpty()) {
                continue;
            }

            tab_cod.insertar();
            tab_cod.setValor("codigo_geper", tab_clie.getValor(i, "cod_clie"));
            tab_cod.setValor("ide_geper", String.valueOf(int_maximo_cliente));
            if (str_ide_geper.isEmpty() == false) {
                str_ide_geper += ",";
            }
            str_ide_geper += int_maximo_cliente;
            tab_cod.setValor("gen_ide_geper", "572");
            tab_cod.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cod.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tab_cod.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            String ide_getid = tab_clie.getValor(i, "cedula");
            if (ide_getid != null) {
                if (ide_getid.length() > 10) {
                    ide_getid = "1";//RUC
                } else {
                    ide_getid = "0";//CEDULA    
                }
            }
            tab_cod.setValor("ide_getid", ide_getid);
            tab_cod.setValor("identificac_geper", tab_clie.getValor(i, "cedula").replace("-", ""));
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
            tab_cod.setValor("fecha_ingre_geper", utilitario.getFechaActual());

            String ide_cntco = tab_clie.getValor(i, "tipo");
            if (ide_cntco != null) {
                ide_cntco = ide_cntco.toUpperCase();
                if (ide_cntco.startsWith("OBLIGADO A LLEVAR CONTABILIDAD")) {
                    ide_cntco = "3";
                } else if (ide_cntco.startsWith("PERSONA NATURAL")) {
                    ide_cntco = "2";
                } else if (ide_cntco.startsWith("CONTRIBUYENTE ESPECIAL")) {
                    ide_cntco = "1";
                } else if (ide_cntco.startsWith("ESPECIALES-EXPORTADORES")) {
                    ide_cntco = "6";
                } else {
                    ide_cntco = "2"; 
                }
            }
            tab_cod.setValor("ide_cntco", ide_cntco);

//////            double dou_saldo_inicial = 0;
//////            try {
//////                dou_saldo_inicial = Double.parseDouble(tab_clie.getValor(i, "existencia"));
//////            } catch (Exception e) {
//////            }
//////            if (dou_saldo_inicial != 0) {
//////                //Creo la transaccion
//////                tab_cabcxc.insertar();
//////                tab_cabcxc.setValor("ide_ccctr", String.valueOf(int_maximo_cab));
//////                tab_cabcxc.setValor("ide_ccttr", "13");
//////                tab_cabcxc.setValor("ide_sucu", "0");
//////                tab_cabcxc.setValor("ide_empr", "0");
//////                tab_cabcxc.setValor("ide_geper", String.valueOf(int_maximo_cliente));
//////                tab_cabcxc.setValor("fecha_trans_ccctr", utilitario.getFechaActual());
//////                tab_cabcxc.setValor("observacion_ccctr", "Saldo inicial al " + utilitario.getFechaActual());
//////
//////                tab_detcxc.insertar();
//////                tab_detcxc.setValor("ide_ccdtr", String.valueOf(int_maximo_det));
//////                tab_detcxc.setValor("ide_ccctr", String.valueOf(int_maximo_cab));
//////                tab_detcxc.setValor("ide_ccttr", "13");
//////                tab_detcxc.setValor("ide_sucu", "0");
//////                tab_detcxc.setValor("ide_empr", "0");
//////                tab_detcxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
//////                tab_detcxc.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
//////                tab_detcxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
//////                tab_detcxc.setValor("numero_pago_ccdtr", "0");
//////                tab_detcxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(dou_saldo_inicial));
//////                tab_detcxc.setValor("observacion_ccdtr", "Saldo inicial al " + utilitario.getFechaActual());
//////
//////                int_maximo_cab++;
//////                int_maximo_det++;
//////
//////            }
            int_maximo_cliente++;

        }

        tab_cod.guardar();
//////        tab_cabcxc.guardar();
//////        tab_detcxc.guardar();

        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
            if (tab_cod.getTotalFilas() > 0) {
                utilitario.agregarMensaje("Se importaron correctamente ", tab_cod.getTotalFilas() + " CLIENTES del sistema de facturación");
            }
        }

        return str_ide_geper;
    }

    public String importarProveedores() {
        String str_ide_geper = "";
        TablaGenerica tab_cod = new TablaGenerica();
        tab_cod.setTabla("gen_persona", "ide_geper", -1);
        tab_cod.setCondicion("es_proveedo_geper=true and gen_ide_geper=438");
        tab_cod.getColumna("codigo_geper").setQuitarCaracteresEspeciales(true);
        tab_cod.setGenerarPrimaria(false);
        tab_cod.ejecutarSql();
        tab_cod.getColumna("ide_geper").setExterna(false);
        String str_cod = tab_cod.getStringColumna("codigo_geper");
        tab_cod.limpiar();

        Conexion con_conecta = getConexionEscritorio();
        TablaGenerica tab_clie = new TablaGenerica();
        tab_clie.setConexion(con_conecta);
        if (str_cod != null && str_cod.isEmpty() == false) {
            tab_clie.setSql("select * from proveedores where cod_prove not in(" + str_cod + ",'')");
        } else {
            tab_clie.setCampoPrimaria("cod_prove");
            tab_clie.setSql("select * from proveedores");
        }
        tab_clie.ejecutarSql();
        long int_maximo_cliente = utilitario.getConexion().getMaximo("gen_persona", "ide_geper", tab_clie.getTotalFilas());
//////        TablaGenerica tab_cabcxc = new TablaGenerica();
//////        tab_cabcxc.setTabla("cxc_cabece_transa", "ide_ccctr", -1);
//////        tab_cabcxc.setCondicion("ide_ccctr=-1");
//////        tab_cabcxc.setGenerarPrimaria(false);
//////        tab_cabcxc.getColumna("ide_ccctr").setExterna(false);
//////        tab_cabcxc.ejecutarSql();
//////        long int_maximo_cab = utilitario.getConexion().getMaximo("cxc_cabece_transa", "ide_ccctr", tab_clie.getTotalFilas());
//////
//////        TablaGenerica tab_detcxc = new TablaGenerica();
//////        tab_detcxc.setTabla("cxc_detall_transa", "ide_ccdtr", -1);
//////        tab_detcxc.setCondicion("ide_ccdtr=-1");
//////        tab_detcxc.setGenerarPrimaria(false);
//////        tab_detcxc.getColumna("ide_ccdtr").setExterna(false);
//////        tab_detcxc.ejecutarSql();
//////        long int_maximo_det = utilitario.getConexion().getMaximo("cxc_detall_transa", "ide_ccdtr", tab_clie.getTotalFilas());

        for (int i = 0; i < tab_clie.getTotalFilas(); i++) {

            if (tab_clie.getValor(i, "nom_prove") == null || tab_clie.getValor(i, "nom_prove").isEmpty()) {
                continue;
            }

            tab_cod.insertar();
            tab_cod.setValor("codigo_geper", tab_clie.getValor(i, "cod_prove"));
            tab_cod.setValor("ide_geper", String.valueOf(int_maximo_cliente));
            if (str_ide_geper.isEmpty() == false) {
                str_ide_geper += ",";
            }
            str_ide_geper += int_maximo_cliente;
            tab_cod.setValor("gen_ide_geper", "438");
            tab_cod.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cod.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));
            tab_cod.setValor("ide_usua", utilitario.getVariable("ide_usua"));
            String ide_getid = tab_clie.getValor(i, "ruc_prove");
            if (ide_getid != null) {
                if (ide_getid.length() > 10) {
                    ide_getid = "1";//RUC
                } else {
                    ide_getid = "0";//CEDULA    
                }
            }
            tab_cod.setValor("ide_getid", ide_getid);
            tab_cod.setValor("identificac_geper", tab_clie.getValor(i, "ruc_prove").replace("-", ""));
            tab_cod.setValor("nom_geper", tab_clie.getValor(i, "nom_prove"));
            tab_cod.setValor("nombre_compl_geper", tab_clie.getValor(i, "nom_prove"));
            tab_cod.setValor("direccion_geper", tab_clie.getValor(i, "dir_prove"));
            tab_cod.setValor("telefono_geper", tab_clie.getValor(i, "telef_prove"));
            tab_cod.setValor("fax_geper", tab_clie.getValor(i, "telef2"));
            tab_cod.setValor("contacto_geper", tab_clie.getValor(i, "contacto"));
            tab_cod.setValor("observacion_geper", tab_clie.getValor(i, "observaciones"));
            tab_cod.setValor("nivel_geper", "HIJO");
            tab_cod.setValor("es_proveedo_geper", "true");

//////            String ide_cntco = tab_clie.getValor(i, "tipo");
//////            if (ide_cntco != null) {
//////                ide_cntco = ide_cntco.toUpperCase();
//////                if (ide_cntco.startsWith("OBLIGADO A LLEVAR CONTABILIDAD")) {
//////                    ide_cntco = "3";
//////                } else if (ide_cntco.startsWith("PERSONA NATURAL")) {
//////                    ide_cntco = "2";
//////                } else if (ide_cntco.startsWith("CONTRIBUYENTE ESPECIAL")) {
//////                    ide_cntco = "1";
//////                } else if (ide_cntco.startsWith("ESPECIALES-EXPORTADORES")) {
//////                    ide_cntco = "6";
//////                } else {
//////                    ide_cntco = "2";
//////                }
//////            }
            String ide_cntco = "1"; // por defecto CONTRIBUYENTE ESPECIAL
            tab_cod.setValor("ide_cntco", ide_cntco);

//////            double dou_saldo_inicial = 0;
//////            try {
//////                dou_saldo_inicial = Double.parseDouble(tab_clie.getValor(i, "existencia"));
//////            } catch (Exception e) {
//////            }
//////            if (dou_saldo_inicial != 0) {
//////                //Creo la transaccion
//////                tab_cabcxc.insertar();
//////                tab_cabcxc.setValor("ide_ccctr", String.valueOf(int_maximo_cab));
//////                tab_cabcxc.setValor("ide_ccttr", "13");
//////                tab_cabcxc.setValor("ide_sucu", "0");
//////                tab_cabcxc.setValor("ide_empr", "0");
//////                tab_cabcxc.setValor("ide_geper", String.valueOf(int_maximo_cliente));
//////                tab_cabcxc.setValor("fecha_trans_ccctr", utilitario.getFechaActual());
//////                tab_cabcxc.setValor("observacion_ccctr", "Saldo inicial al " + utilitario.getFechaActual());
//////
//////                tab_detcxc.insertar();
//////                tab_detcxc.setValor("ide_ccdtr", String.valueOf(int_maximo_det));
//////                tab_detcxc.setValor("ide_ccctr", String.valueOf(int_maximo_cab));
//////                tab_detcxc.setValor("ide_ccttr", "13");
//////                tab_detcxc.setValor("ide_sucu", "0");
//////                tab_detcxc.setValor("ide_empr", "0");
//////                tab_detcxc.setValor("ide_usua", utilitario.getVariable("ide_usua"));
//////                tab_detcxc.setValor("fecha_trans_ccdtr", utilitario.getFechaActual());
//////                tab_detcxc.setValor("fecha_venci_ccdtr", utilitario.getFechaActual());
//////                tab_detcxc.setValor("numero_pago_ccdtr", "0");
//////                tab_detcxc.setValor("valor_ccdtr", utilitario.getFormatoNumero(dou_saldo_inicial));
//////                tab_detcxc.setValor("observacion_ccdtr", "Saldo inicial al " + utilitario.getFechaActual());
//////
//////                int_maximo_cab++;
//////                int_maximo_det++;
//////
//////            }
            int_maximo_cliente++;

        }

        tab_cod.guardar();
//////        tab_cabcxc.guardar();
//////        tab_detcxc.guardar();

        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
            if (tab_cod.getTotalFilas() > 0) {
                utilitario.agregarMensaje("Se importaron correctamente ", tab_cod.getTotalFilas() + " PROVEEDORES del sistema de facturación");
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

//////        TablaGenerica tab_inventario = new TablaGenerica();
//////        tab_inventario.setTabla("inv_det_comp_inve", "ide_indci", -1);
//////        tab_inventario.setCondicion("ide_indci=-1");
//////
//////        TablaGenerica tab_cab_inventario = new TablaGenerica();
//////        tab_cab_inventario.setTabla("inv_cab_comp_inve", "ide_incci", -1);
//////        tab_cab_inventario.setCondicion("ide_incci=-1");
//////        tab_cab_inventario.ejecutarSql();
//////        tab_cab_inventario.insertar();
//////        tab_cab_inventario.setValor("ide_geper", "542");
//////        tab_cab_inventario.setValor("ide_inepi", "1");
//////        tab_cab_inventario.setValor("ide_intti", "30");
//////        tab_cab_inventario.setValor("ide_sucu", "0");
//////        tab_cab_inventario.setValor("ide_empr", "0");
//////        tab_cab_inventario.setValor("ide_usua", utilitario.getVariable("IDE_USUA"));
//////        tab_cab_inventario.setValor("ide_inbod", "1");
//////        tab_cab_inventario.setValor("numero_incci", ser_inventario.getSecuencialComprobanteInventario("1"));
//////        tab_cab_inventario.setValor("fecha_trans_incci", utilitario.getFechaActual());
//////        tab_cab_inventario.setValor("observacion_incci", "Importa Productos Saldo inicial al " + utilitario.getFechaActual());
//////        tab_cab_inventario.setValor("fecha_siste_incci", utilitario.getFechaActual());
//////        tab_cab_inventario.setValor("hora_sistem_incci", utilitario.getHoraActual());
//////        tab_cab_inventario.guardar();
        for (int i = 0; i < tab_quimi.getTotalFilas(); i++) {
            tab_cod.insertar();
            if (str_ide_inarti.isEmpty() == false) {
                str_ide_inarti += ",";
            }
            str_ide_inarti += int_maximo_articulo;
            tab_cod.setValor("ide_inarti", String.valueOf(int_maximo_articulo));
            tab_cod.setValor("inv_ide_inarti", "46");
            tab_cod.setValor("ide_empr", utilitario.getVariable("ide_empr"));
            tab_cod.setValor("ide_sucu", utilitario.getVariable("ide_sucu"));

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

//////            tab_inventario.insertar();
//////            tab_inventario.setValor("ide_inarti", String.valueOf(int_maximo_articulo));
//////            tab_inventario.setValor("ide_empr", "0");
//////            tab_inventario.setValor("ide_sucu", "0");
//////            tab_inventario.setValor("ide_incci", tab_cab_inventario.getValor("ide_incci"));
//////            tab_inventario.setValor("precio_promedio_indci", tab_quimi.getValor(i, "precio_compra"));
//////            tab_inventario.setValor("precio_indci", tab_quimi.getValor(i, "precio_venta"));
//////            tab_inventario.setValor("cantidad_indci", tab_quimi.getValor(i, "existencia"));
//////            tab_inventario.setValor("observacion_indci", "Saldo anterior sistema de Inventario");
//////            try {
//////                tab_inventario.setValor("valor_indci", utilitario.getFormatoNumero(Double.parseDouble(tab_quimi.getValor(i, "existencia")) * Double.parseDouble(tab_quimi.getValor(i, "precio_compra"))));
//////            } catch (Exception e) {
//////            }
            int_maximo_articulo++;
        }
        tab_cod.guardar();
//////        tab_inventario.guardar();

        if (utilitario.getConexion().ejecutarListaSql().isEmpty()) {
            if (tab_cod.getTotalFilas() > 0) {
                utilitario.agregarMensaje("Se importaron correctamente ", tab_cod.getTotalFilas() + " PRODUCTOS QUIMICOS del sistema de facturación");
            }
        }

        return str_ide_inarti;

    }

    /**
     * Guarda la factura en el kardex de clientes y productos del sistema de
     * escritorio
     *
     * @param ide_cccfa
     */
    public void guardarKardexFactura(String ide_cccfa) {
        TablaGenerica tab_factura = utilitario.consultar("select codigo_inarti,cantidad_ccdfa,precio_ccdfa,total_ccdfa,"
                + "codigo_geper,nom_geper,ide_cntco,serie_ccdaf || secuencial_cccfa as numfactura,\n"
                + "base_grabada_cccfa+base_tarifa0_cccfa+base_no_objeto_iva_cccfa as subtotal,valor_iva_cccfa,total_cccfa,"
                + "secuencial_cccfa,a.ide_cccfa,base_grabada_cccfa,base_tarifa0_cccfa+base_no_objeto_iva_cccfa as ventas0,"
                + "CASE WHEN dias_credito_cccfa=0 THEN 'CONTADO'\n"
                + "             ELSE 'CRÉDITO '||dias_credito_cccfa||' DÍAS'\n"
                + "       END as nombre_cndfp,COALESCE(nombre_inuni,'')  ||' '|| nombre_inarti as nombre_inarti,nombre_vgven "
                + "from cxc_deta_factura a\n"
                + "inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa\n"
                + "inner join cxc_datos_fac c on b.ide_ccdaf=c.ide_ccdaf\n"
                + "inner join gen_persona d on b.ide_geper=d.ide_geper\n"
                + "inner join inv_articulo e on a.ide_inarti= e.ide_inarti\n"
                + "left join  inv_unidad g on a.ide_inuni =g.ide_inuni\n"
                + "inner join ven_vendedor h on b.ide_vgven=h.ide_vgven\n"
                + "where a.ide_cccfa=" + ide_cccfa);
        //Guarda Kardex de productos de todos detalles de la factura

        if (tab_factura.isEmpty() == false) {
            Conexion con_conecta = getConexionEscritorio();
            String fechae = utilitario.getFormatoFecha(new Date(), "yyyy-MM-dd");
            String numFactura = tab_factura.getValor("numfactura");

            //Guarda Kardex de clientes
            // String ide_cntco = tab_factura.getValor("ide_cntco");
            double retFuente = 0, retIva = 0;
            String codClie = tab_factura.getValor("codigo_geper");
            if (codClie == null) {
                codClie = "";
            }
            double saldoInicialClie = getSaldoCliente_Escritorio(codClie);
            double saldoNuevoClie = 0;
            double subtotal = Double.parseDouble(tab_factura.getValor("subtotal"));
            double tiva = Double.parseDouble(tab_factura.getValor("valor_iva_cccfa"));
            double total = Double.parseDouble(tab_factura.getValor("total_cccfa"));

            //Busca tipo de contribuyente del sistema de escritorio
            String ide_cntco = getTipoCliente(tab_factura.getValor("codigo_geper"));
            switch (ide_cntco) {
                case "2": {
                    //PERSONA NATURAL
                    retFuente = 0;
                    retIva = 0;
                    saldoNuevoClie = saldoInicialClie + total;
                    String sql = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','F.E - COMPRA DE PRODUCTOS '," + total + ",0," + saldoNuevoClie + ")";
                    con_conecta.agregarSql(sql);
                    break;
                }
                case "3": {
                    //OBLIGADO A LLEVAR CONTABILIDAD
                    retFuente = subtotal * 0.01;
                    retIva = 0;
                    saldoNuevoClie = saldoInicialClie + total;
                    String sql = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','F.E - COMPRA DE PRODUCTOS '," + total + ",0," + saldoNuevoClie + ")";
                    saldoNuevoClie = saldoNuevoClie - retFuente;
                    String sql1 = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','  Retencion a la Fuente 1% ',0," + retFuente + "," + saldoNuevoClie + ")";
                    con_conecta.agregarSql(sql);
                    con_conecta.agregarSql(sql1);
                    break;
                }
                case "1": {
                    //CONTRIBUYENTE ESPECIAL
                    retFuente = subtotal * 0.01;
                    retIva = tiva * 0.30;
                    saldoNuevoClie = saldoInicialClie + total;
                    String sql = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','F.E - COMPRA DE PRODUCTOS '," + total + ",0," + saldoNuevoClie + ")";
                    saldoNuevoClie = saldoNuevoClie - retFuente;
                    String sql1 = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','  Retencion a la Fuente 1% ',0," + retFuente + "," + saldoNuevoClie + ")";
                    saldoNuevoClie = saldoNuevoClie - retIva;
                    String sql2 = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','  Retencion IVA 30% ',0," + retIva + "," + saldoNuevoClie + ")";
                    con_conecta.agregarSql(sql);
                    con_conecta.agregarSql(sql1);
                    con_conecta.agregarSql(sql2);
                    break;
                }
                case "6": {
                    //ESPECIALES-EXPORTADORES
                    retFuente = subtotal * 0.01;
                    retIva = tiva * 1;
                    saldoNuevoClie = saldoInicialClie + total;
                    String sql = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','F.E - COMPRA DE PRODUCTOS '," + total + ",0," + saldoNuevoClie + ")";
                    saldoNuevoClie = saldoNuevoClie - retFuente;
                    String sql1 = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','  Retencion a la Fuente 1% ',0," + retFuente + "," + saldoNuevoClie + ")";
                    saldoNuevoClie = saldoNuevoClie - retIva;
                    String sql2 = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','  Retencion IVA 100% ',0," + retIva + "," + saldoNuevoClie + ")";
                    con_conecta.agregarSql(sql);
                    con_conecta.agregarSql(sql1);
                    con_conecta.agregarSql(sql2);
                    break;
                }
                default: {
                    //POR DEFECTO NO RETIENE NADA
                    retFuente = 0;
                    retIva = 0;
                    saldoNuevoClie = saldoInicialClie + total;
                    String sql = "INSERT INTO KARDEXCLIENTES VALUES((SELECT MAX(k.CODIGOKC )+1 FROM KARDEXCLIENTES k where  k.COD_CLIE='" + codClie + " '),'" + codClie + "','" + fechae + "','" + numFactura + "','F.E - COMPRA DE PRODUCTOS '," + total + ",0," + saldoNuevoClie + ")";
                    con_conecta.agregarSql(sql);
                }
            }
            String sql = "UPDATE CLIENTES SET EXISTENCIA=" + saldoNuevoClie + " where COD_CLIE='" + codClie + "'";
            con_conecta.agregarSql(sql);

            //Guarda Cabecera Factura electronica
            String sqlCabFactura = "INSERT INTO facturas_fe "
                    + "(NUM_FACTURA_FE,COD_CLIE,FECHA,SUBTOTAL,"
                    + "IVA,TOTAL,FORMA,ANULADA,"
                    + "VENTAS0,VENTAS12,RTE1,RTE30,"
                    + "NUM_GUIA,VENDEDOR,IDE_CCCFA)"
                    + "values "
                    + "(" + Integer.parseInt(tab_factura.getValor("secuencial_cccfa")) + ",'" + codClie + "','" + fechae + "'," + utilitario.getFormatoNumero(tab_factura.getValor("subtotal")) + ","
                    + utilitario.getFormatoNumero(tab_factura.getValor("valor_iva_cccfa")) + "," + utilitario.getFormatoNumero(tab_factura.getValor("total_cccfa")) + ",'" + tab_factura.getValor("nombre_cndfp") + "',false,"
                    + utilitario.getFormatoNumero(tab_factura.getValor("ventas0")) + "," + utilitario.getFormatoNumero(tab_factura.getValor("base_grabada_cccfa")) + "," + utilitario.getFormatoNumero(retFuente) + "," + utilitario.getFormatoNumero(retIva) + ","
                    + Integer.parseInt(tab_factura.getValor("secuencial_cccfa")) + ",'" + tab_factura.getValor("nombre_vgven") + "'," + ide_cccfa + ")";

            con_conecta.agregarSql(sqlCabFactura);

            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
                ///Kardex Productos
                String codProd = tab_factura.getValor(i, "codigo_inarti");
                if (codProd == null) {
                    codProd = "";
                }
                double vcant = Double.parseDouble(tab_factura.getValor(i, "cantidad_ccdfa"));
                double vpre = Double.parseDouble(tab_factura.getValor(i, "precio_ccdfa"));
                double exan = getExisteciaProducto_Escritorio(codProd);
                double exnue = exan - vcant;
                double vtotaldetalle = Double.parseDouble(tab_factura.getValor(i, "total_ccdfa"));
                String detallefactura = tab_factura.getValor(i, "nombre_inarti");
                detallefactura = detallefactura.trim();
                String nombreCliente = tab_factura.getValor("nom_geper");
                String sql_1 = "INSERT INTO  KARDEXPRODUCTOS VALUES((SELECT MAX(k.CODIGOKP )+1 FROM KARDEXPRODUCTOS k where  k.COD_PROD='" + codProd + " '),'" + codProd + "','" + fechae + "','" + numFactura + "','" + nombreCliente + "'," + vpre + ",0," + vcant + "," + exnue + ")";
                String sql_2 = "UPDATE PRODUCTOS SET EXISTENCIA=" + exnue + " where COD_PROD='" + codProd + "'";
                con_conecta.agregarSql(sql_1);
                con_conecta.agregarSql(sql_2);
                //Guarda detalle Factura electronica
                String sqlDetFactura = "INSERT INTO detalle_facturas_fe "
                        + "(NUM_FACTURA_FE,COD_PROD,CANTIDAD,DETALLE,"
                        + "PRECIO,PTOTAL)"
                        + "VALUES"
                        + "(" + Integer.parseInt(tab_factura.getValor("secuencial_cccfa")) + ",'" + codProd + "'," + vcant + ",'" + detallefactura + "',"
                        + vpre + "," + utilitario.getFormatoNumero(vtotaldetalle) + ")";
                con_conecta.agregarSql(sqlDetFactura);

            }

            if (con_conecta.ejecutarListaSql().isEmpty()) {
                System.out.println("OK guardo Kardex");
                //Actualiza los campos de retenciones en la factura 
                utilitario.getConexion().ejecutarSql("UPDATE cxc_cabece_factura set ret_fuente_cccfa =" + retFuente + ", ret_iva_cccfa=" + retIva + " where ide_cccfa=" + ide_cccfa);
            }
        }
    }

    /**
     * Guarda en kardex de proveedores y de productos
     *
     * @param ide_cpcfa
     */
    public void guardarKardexCompras(String ide_cpcfa) {

        TablaGenerica tab_factura = utilitario.consultar("select fecha_emisi_cpcfa,codigo_inarti,cantidad_cpdfa,precio_cpdfa,valor_cpdfa,"
                + "codigo_geper,nom_geper, numero_cpcfa as numfactura,autorizacio_cpcfa,\n"
                + "base_grabada_cpcfa+base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa as subtotal,valor_iva_cpcfa,total_cpcfa,"
                + "a.ide_cpcfa,base_grabada_cpcfa,base_tarifa0_cpcfa+base_no_objeto_iva_cpcfa as ventas0,"
                + "CASE WHEN dias_credito_cpcfa=0 THEN 'CONTADO'\n" //Crear tabla o campo  dias_credito_cpcfa 
                + "             ELSE 'CRÉDITO '||dias_credito_cpcfa||' DÍAS'\n"
                + "       END as nombre_cndfp,COALESCE(nombre_inuni,'')  ||' '|| nombre_inarti as nombre_inarti "
                + "from cxp_detall_factur a\n"
                + "inner join cxp_cabece_factur b on a.ide_cpcfa=b.ide_cpcfa\n"
                + "inner join gen_persona d on b.ide_geper=d.ide_geper\n"
                + "inner join inv_articulo e on a.ide_inarti= e.ide_inarti\n"
                + "left join  inv_unidad g on a.ide_inuni =g.ide_inuni\n"
                + "where a.ide_cpcfa=" + ide_cpcfa);
        //Guarda Kardex de productos de todos detalles de la factura

        if (tab_factura.isEmpty() == false) {
            Conexion con_conecta = getConexionEscritorio();
            String fechae = tab_factura.getValor("fecha_emisi_cpcfa");
            String numFactura = tab_factura.getValor("numfactura");

            //Guarda Kardex de proveedores
            String codProve = tab_factura.getValor("codigo_geper");
            if (codProve == null) {
                codProve = "";
            }
            double saldoInicialClie = getSaldoProveedor_Escritorio(codProve);
            double saldoNuevoClie = 0;
            double subtotal = Double.parseDouble(tab_factura.getValor("subtotal"));
            double tiva = Double.parseDouble(tab_factura.getValor("valor_iva_cpcfa"));
            double total = Double.parseDouble(tab_factura.getValor("total_cpcfa"));

            saldoNuevoClie = saldoInicialClie + total;
            String sql = "INSERT INTO kardexproveedores VALUES((SELECT MAX(k.CODIGOKPV )+1 FROM kardexproveedores k where  k.COD_PROVE='" + codProve + " '),'" + codProve + "','" + fechae + "','" + numFactura + "','COMPRA DE PRODUCTOS (WEB)'," + total + ",0," + saldoNuevoClie + ")";
            con_conecta.agregarSql(sql);

            String sql1 = "UPDATE proveedores SET EXISTENCIA=" + saldoNuevoClie + " where COD_PROVE='" + codProve + "'";
            con_conecta.agregarSql(sql1);
            int codCompra = getCodigoCompra();
            //Guarda Cabecera Factura compra
            String sqlCabFactura = "INSERT INTO compras "
                    + "(COD_COMPRA,FACTURA,COD_PROVE,FECHA_COMPRA,"
                    + "SUBTOTAL,IVA,TOTAL,FORMA,"
                    + "DESCUENTO,AUTORIZACION,"
                    + "TARIFA0,TARIFA12)"
                    + "values "
                    + "( " + codCompra + ", '" + tab_factura.getValor("numfactura") + "','" + codProve + "','" + fechae + "'," + utilitario.getFormatoNumero(subtotal) + ","
                    + utilitario.getFormatoNumero(tiva) + "," + utilitario.getFormatoNumero(tab_factura.getValor("total_cpcfa")) + ",'" + tab_factura.getValor("nombre_cndfp") + "',0,'" + tab_factura.getValor("autorizacio_cpcfa") + "',"
                    + utilitario.getFormatoNumero(tab_factura.getValor("ventas0")) + "," + utilitario.getFormatoNumero(tab_factura.getValor("base_grabada_cpcfa")) + ")";

            con_conecta.agregarSql(sqlCabFactura);

            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
                ///Kardex Productos
                String codProd = tab_factura.getValor(i, "codigo_inarti");
                if (codProd == null) {
                    codProd = "";
                }
                double vcant = Double.parseDouble(tab_factura.getValor(i, "cantidad_cpdfa"));
                double vpre = Double.parseDouble(tab_factura.getValor(i, "precio_cpdfa"));
                double exan = getExisteciaProducto_Escritorio(codProd);
                double exnue = exan - vcant;
                double vtotaldetalle = Double.parseDouble(tab_factura.getValor(i, "valor_cpdfa"));
                String detallefactura = tab_factura.getValor(i, "nombre_inarti");
                detallefactura = detallefactura.trim();
                String nombreCliente = tab_factura.getValor("nom_geper");
                String sql_1 = "INSERT INTO  KARDEXPRODUCTOS VALUES((SELECT MAX(k.CODIGOKP )+1 FROM KARDEXPRODUCTOS k where  k.COD_PROD='" + codProd + " '),'" + codProd + "','" + fechae + "','" + numFactura + "','" + nombreCliente + "'," + vpre + ",0," + vcant + "," + exnue + ")";
                String sql_2 = "UPDATE PRODUCTOS SET EXISTENCIA=" + exnue + " where COD_PROD='" + codProd + "'";
                con_conecta.agregarSql(sql_1);
                con_conecta.agregarSql(sql_2);

                //Guarda detalle Factura compra
                String sqlDetFactura = "INSERT INTO detalle_compras "
                        + "(COD_COMPRA,COD_PROD,CANT,DETALLE,"
                        + "PRECIO,TOTAL)"
                        + "VALUES"
                        + "(" + codCompra + ",'" + codProd + "'," + vcant + ",'" + detallefactura + "',"
                        + vpre + "," + utilitario.getFormatoNumero(vtotaldetalle) + ")";
                con_conecta.agregarSql(sqlDetFactura);

            }

            if (con_conecta.ejecutarListaSql().isEmpty()) {
                System.out.println("OK guardo Kardex");
            }
        }

    }

    /**
     * Guarda en kardex de proveedores y de productos
     *
     * @param ide_cpcfa
     */
    public void guardarKardexRetencionCompras(String ide_cpcfa) {

        TablaGenerica tab_factura = utilitario.consultar("select codigo_geper,fecha_emisi_cncre,numero_cpcfa,nombre_cncim,valor_cndre from con_detall_retenc a \n"
                + "inner join con_cabece_retenc b on a.ide_cncre=b.ide_cncre\n"
                + "inner join cxp_cabece_factur c on b.ide_cncre=c.ide_cncre\n"
                + "inner join con_cabece_impues d on a.ide_cncim=d.ide_cncim\n"
                + "inner join gen_persona e on c.ide_geper=e.ide_geper\n"
                + "where ide_cpcfa=" + ide_cpcfa);
        //Guarda Kardex de productos de todos detalles de la factura

        if (tab_factura.isEmpty() == false) {
            Conexion con_conecta = getConexionEscritorio();
            String fechae = tab_factura.getValor("fecha_emisi_cncre");
            String numFactura = tab_factura.getValor("numero_cpcfa");
            String codProve = tab_factura.getValor("codigo_geper");
            if (codProve == null) {
                codProve = "";
            }
            double saldoNuevoClie = getSaldoProveedor_Escritorio(codProve);

            //Guarda Kardex de proveedores
            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
                double valor = Double.parseDouble(tab_factura.getValor(i, "valor_cndre"));
                String descripcion = tab_factura.getValor(i, "nombre_cncim");
                saldoNuevoClie -= valor;
                String sql = "INSERT INTO kardexproveedores VALUES((SELECT MAX(k.CODIGOKPV )+1 FROM kardexproveedores k where  k.COD_PROVE='" + codProve + " '),'" + codProve + "','" + fechae + "','" + numFactura + "','" + descripcion + "',0," + valor + "," + saldoNuevoClie + ")";
                con_conecta.agregarSql(sql);
            }

            if (con_conecta.ejecutarListaSql().isEmpty()) {
                System.out.println("OK guardo Retencion Kardex");
            }
        }

    }

    /**
     * Elimina los movimientos en kardex de clientes y productos
     *
     * @param ide_cccfa
     */
    public void anularFactura_Escritorio(String ide_cccfa) {
        TablaGenerica tab_factura = utilitario.consultar("select codigo_inarti,cantidad_ccdfa,precio_ccdfa,"
                + "codigo_geper,nom_geper,ide_cntco,serie_ccdaf || secuencial_cccfa as numfactura,\n"
                + "base_grabada_cccfa+base_tarifa0_cccfa+base_no_objeto_iva_cccfa as subtotal,valor_iva_cccfa,total_cccfa,secuencial_cccfa from cxc_deta_factura a\n"
                + "inner join cxc_cabece_factura b on a.ide_cccfa=b.ide_cccfa\n"
                + "inner join cxc_datos_fac c on b.ide_ccdaf=c.ide_ccdaf\n"
                + "inner join gen_persona d on b.ide_geper=d.ide_geper\n"
                + "inner join inv_articulo e on a.ide_inarti= e.ide_inarti\n"
                + "where a.ide_cccfa=" + ide_cccfa);
        if (tab_factura.isEmpty() == false) {
            Conexion con_conecta = getConexionEscritorio();
            String numFactura = tab_factura.getValor("numfactura");
            for (int i = 0; i < tab_factura.getTotalFilas(); i++) {
                ///Kardex Productos
                String codProd = tab_factura.getValor(i, "codigo_inarti");
                String sql_1 = "DELETE FROM KARDEXPRODUCTOS  WHERE  FACTURA ='" + numFactura + "' AND COD_PROD='" + codProd + "'";
                String sql_2 = "call calcula('" + codProd + "');";
                con_conecta.agregarSql(sql_1);
                con_conecta.agregarSql(sql_2);
            }
            String codClie = tab_factura.getValor("codigo_geper");
            String sql_1 = "DELETE FROM KARDEXCLIENTES  WHERE  FACTURA ='" + numFactura + "' AND COD_CLIE='" + codClie + "'";
            String sql_2 = "call calculaclie('" + codClie + "')";
            con_conecta.agregarSql(sql_1);
            con_conecta.agregarSql(sql_2);
            //Factura Electronica
            String sqlCabFact = "DELETE FROM facturas_fe WHERE NUM_FACTURA_FE=" + Integer.parseInt(tab_factura.getValor("secuencial_cccfa"));
            String sqlDetFact = "DELETE FROM detalle_facturas_fe WHERE NUM_FACTURA_FE=" + Integer.parseInt(tab_factura.getValor("secuencial_cccfa"));
            con_conecta.agregarSql(sqlCabFact);
            con_conecta.agregarSql(sqlDetFact);
            if (con_conecta.ejecutarListaSql().isEmpty()) {
                System.out.println("OK Anulo Kardex");
            }
        }
    }

    /**
     * Retorna la existencia de un producto
     *
     * @param codigoProducto
     *
     * @return
     */
    public Double getExisteciaProducto_Escritorio(String codigoProducto) {
        String sql = "SELECT COD_PROD,EXISTENCIA from productos where COD_PROD='" + codigoProducto + "'";
        TablaGenerica tab = new TablaGenerica();
        Conexion con_conecta = getConexionEscritorio();
        tab.setConexion(con_conecta);
        tab.setSql(sql);
        tab.ejecutarSql();
        double dou_existencia = 0;
        if (tab.isEmpty() == false) {
            try {
                dou_existencia = Double.parseDouble(tab.getValor("EXISTENCIA"));
            } catch (Exception e) {
            }
        }
        return dou_existencia;
    }

    /**
     * Retorna el saldo del cliente
     *
     * @param codigoCliente
     *
     * @return
     */
    public Double getSaldoCliente_Escritorio(String codigoCliente) {
        String sql = "SELECT COD_CLIE,EXISTENCIA from CLIENTES where COD_CLIE='" + codigoCliente + "'";
        TablaGenerica tab = new TablaGenerica();
        Conexion con_conecta = getConexionEscritorio();
        tab.setConexion(con_conecta);
        tab.setSql(sql);
        tab.ejecutarSql();
        double dou_existencia = 0;
        if (tab.isEmpty() == false) {
            try {
                dou_existencia = Double.parseDouble(tab.getValor("EXISTENCIA"));
            } catch (Exception e) {
            }
        }
        return dou_existencia;
    }

    /**
     * Retorna el saldo del proveedor
     *
     * @param codigoProveedor
     *
     * @return
     */
    public Double getSaldoProveedor_Escritorio(String codigoProveedor) {
        String sql = "SELECT COD_PROVE,EXISTENCIA from PROVEEDORES where COD_PROVE='" + codigoProveedor + "'";
        TablaGenerica tab = new TablaGenerica();
        Conexion con_conecta = getConexionEscritorio();
        tab.setConexion(con_conecta);
        tab.setSql(sql);
        tab.ejecutarSql();
        double dou_existencia = 0;
        if (tab.isEmpty() == false) {
            try {
                dou_existencia = Double.parseDouble(tab.getValor("EXISTENCIA"));
            } catch (Exception e) {
            }
        }
        return dou_existencia;
    }

    public void anularFacturaporNotadeCredito(String secuencialFa) {
        String sql = "UPDATE facturas_fe SET ANULADA=true where NUM_FACTURA_FE=" + Integer.parseInt(secuencialFa) + "";
        Conexion con_conecta = getConexionEscritorio();
        con_conecta.ejecutarSql(sql);
    }

    public String getTipoCliente(String CODIGO_CLIE) {
        TablaGenerica tab = new TablaGenerica();
        String sql = "SELECT COD_CLIE,TIPO FROM clientes where COD_CLIE='" + CODIGO_CLIE + "'";
        Conexion con_conecta = getConexionEscritorio();
        tab.setConexion(con_conecta);
        tab.setSql(sql);
        tab.ejecutarSql();
        String ide_cntco = tab.getValor("TIPO");

        if (ide_cntco != null) {
            ide_cntco = ide_cntco.toUpperCase();
            if (ide_cntco.startsWith("OBLIGADO A LLEVAR CONTABILIDAD")) {
                ide_cntco = "3";
            } else if (ide_cntco.startsWith("PERSONA NATURAL")) {
                ide_cntco = "2";
            } else if (ide_cntco.startsWith("CONTRIBUYENTE ESPECIAL")) {
                ide_cntco = "1";
            } else if (ide_cntco.startsWith("ESPECIALES-EXPORTADORES")) {
                ide_cntco = "6";
            } else {
                ide_cntco = "2";
            }
        }
        return ide_cntco;
    }

    /**
     * Retorna utilidad en ventas de todas las facturas
     *
     * @param vendedor
     * @param anio
     * @param mes
     * @return
     */
    public String getSqlUtilidadVentas(String vendedor, String anio, String mes) {
        if (vendedor == null) {
            vendedor = "";
        }
        vendedor = vendedor.replace("null", "").trim();
        if (vendedor.isEmpty() == false) {
            vendedor = " and VENDEDOR like '%" + vendedor + "%' ";
        }

        return "select DATE_FORMAT(fecha,'%d/%m/%Y') as FECHA_EMISION,NOM_CLIE as CLIENTE,facturas.NUM_FACTURA,NOM_PROD as PRODUCTO,CANTIDAD,PRECIO,PRECIO_COMPRA,PRECIO-PRECIO_COMPRA as UTILIDAD,(PRECIO-PRECIO_COMPRA)*CANTIDAD as UTILIDAD_NETA,CANTIDAD * PRECIO_COMPRA as TOTAL_COMPRA,CANTIDAD * PRECIO as TOTAL_VENTA ,'FN' as TIPO \n"
                + "from facturas,detalle_facturas,productos,clientes \n"
                + "where facturas.NUM_FACTURA =detalle_facturas.NUM_FACTURA \n"
                + "and facturas.COD_CLIE=clientes.COD_CLIE \n"
                + "and detalle_facturas.COD_PROD=productos.COD_PROD \n"
                + "and DATE_FORMAT(fecha,'%m')=" + mes + " \n"
                + "and DATE_FORMAT(fecha,'%Y')=" + anio + " \n"
                + vendedor
                + "and anulada=false \n"
                + "UNION\n"
                + "select DATE_FORMAT(fecha,'%d/%m/%Y')as FECHA_EMISION,NOM_CLIE as CLIENTE,facturas_fe.NUM_FACTURA_FE,NOM_PROD as PRODUCTO,CANTIDAD,PRECIO,PRECIO_COMPRA,PRECIO-PRECIO_COMPRA as UTILIDAD,(PRECIO-PRECIO_COMPRA)*CANTIDAD as UTILIDAD_NETA,CANTIDAD * PRECIO_COMPRA as TOTAL_COMPRA,CANTIDAD * PRECIO as TOTAL_VENTA ,'FE' as TIPO\n"
                + "from facturas_fe,detalle_facturas_fe,productos,clientes \n"
                + "where facturas_fe.NUM_FACTURA_FE =detalle_facturas_fe.NUM_FACTURA_FE\n"
                + "and facturas_fe.COD_CLIE=clientes.COD_CLIE \n"
                + "and detalle_facturas_fe.COD_PROD=productos.COD_PROD \n"
                + "and DATE_FORMAT(fecha,'%m')=" + mes + " \n"
                + "and DATE_FORMAT(fecha,'%Y')=" + anio + " \n"
                + vendedor
                + "and anulada=false \n"
                + "order by 1,3";
    }

    /**
     * Retorna Listado de clientes
     *
     * @return
     */
    public String getSqlListaClientes() {
        return "";
    }

    /**
     * Retorna Listado de proveedores
     *
     * @return
     */
    public String getSqlListaProveedores() {
        return "";
    }

    /**
     * Retorna Listado de productos
     *
     * @return
     */
    public String getSqlListaProductos() {
        return "";
    }

    /**
     * Retorna los mejores clientes segun parámetros
     *
     * @param limite numero de clientes
     * @param anio
     * @return
     */
    public String getSqlMejoresClientes(Integer limite, String anio) {
        String lim = "";
        String conAnio = "";
        if (limite == null) {
            limite = 0;
        }
        if (limite > 0) {
            lim = "LIMIT " + limite;
        }
        if (anio == null) {
            anio = "";
        }
        if (anio.isEmpty() == false) {
            conAnio = " and YEAR(fecha) = " + anio + " \n";
        }

        return "SELECT facturas.COD_CLIE ,NOM_CLIE AS CLIENTE,COUNT(facturas.COD_CLIE) AS NUM_FACTURAS, SUM(TOTAL) AS TOTAL \n"
                + "from facturas,clientes \n"
                + "where  facturas.COD_CLIE=clientes.COD_CLIE \n"
                + "and anulada=false\n"
                + conAnio
                + "group by facturas.COD_CLIE ,NOM_CLIE HAVING COUNT(facturas.COD_CLIE)>0 AND  SUM(TOTAL) IS NOT NULL\n"
                + "ORDER BY 4 DESC  " + lim;
    }

    /**
     * Retorna el total de clientes creados
     *
     * @return
     */
    public int getTotalClientesEscritorio() {
        TablaGenerica tab = new TablaGenerica();
        String sql = "select count(1) as NUMERO, 'total' as TOTAL from clientes";
        Conexion con_conecta = getConexionEscritorio();
        tab.setConexion(con_conecta);
        tab.setSql(sql);
        tab.ejecutarSql();
        int total = 0;
        try {
            total = Integer.parseInt(tab.getValor("NUMERO"));
        } catch (Exception e) {
        }
        return total;
    }

    /**
     * Retorna el total de productos creados
     *
     * @return
     */
    public int getTotalProductosEscritorio() {
        TablaGenerica tab = new TablaGenerica();
        String sql = "select count(1) as NUMERO, 'total' as TOTAL from productos";
        Conexion con_conecta = getConexionEscritorio();
        tab.setConexion(con_conecta);
        tab.setSql(sql);
        tab.ejecutarSql();
        int total = 0;
        try {
            total = Integer.parseInt(tab.getValor("NUMERO"));
        } catch (Exception e) {
        }
        return total;
    }

    /**
     * Retorna los productos mas vendidos
     *
     * @param limite numero de clientes
     * @param anio
     * @return
     */
    public String getSqlProductosMasVendidos(Integer limite, String anio) {
        String lim = "";
        String conAnio = "";
        if (limite == null) {
            limite = 0;
        }
        if (limite > 0) {
            lim = "LIMIT " + limite;
        }
        if (anio == null) {
            anio = "";
        }
        if (anio.isEmpty() == false) {
            conAnio = " and YEAR(fecha) = " + anio + " \n";
        }
        return "SELECT productos.COD_PROD,nom_prod,sum(CANTIDAD) as CANTIDAD,presentacion from  detalle_facturas,productos,facturas "
                + "where detalle_facturas.COD_PROD=productos.COD_PROD "
                + "and facturas.NUM_FACTURA =detalle_facturas.NUM_FACTURA "
                + "and anulada=false "
                + conAnio
                + "GROUP BY  productos.COD_PROD,nom_prod,presentacion HAVING sum(CANTIDAD) IS NOT NULL "
                + "order by 3 DESC,2   " + lim;
    }

    /**
     * Retorna los productos mas comprados
     *
     * @param limite numero de clientes
     * @param anio
     * @return
     */
    public String getSqlProductosMasComprados(Integer limite, String anio) {
        String lim = "";
        String conAnio = "";
        if (limite == null) {
            limite = 0;
        }
        if (limite > 0) {
            lim = "LIMIT " + limite;
        }
        if (anio == null) {
            anio = "";
        }
        if (anio.isEmpty() == false) {
            conAnio = " and YEAR(fecha) = " + anio + " \n";
        }
        return "SELECT productos.COD_PROD,nom_prod,sum(CANT) as CANTIDAD,presentacion from  detalle_compras,productos,compras "
                + "where detalle_compras.COD_PROD=productos.COD_PROD "
                + "and compras.COD_COMPRA =detalle_compras.COD_COMPRA "
                + conAnio
                + "GROUP BY  productos.COD_PROD,nom_prod,presentacion HAVING sum(CANT) IS NOT NULL "
                + "order by 3 DESC,2  " + lim;
    }

    /**
     * Retorna el total de proveedores creados
     *
     * @return
     */
    public int getTotalProveedoresEscritorio() {
        TablaGenerica tab = new TablaGenerica();
        String sql = "select count(1) as NUMERO, 'total' as TOTAL from proveedores";
        Conexion con_conecta = getConexionEscritorio();
        tab.setConexion(con_conecta);
        tab.setSql(sql);
        tab.ejecutarSql();
        int total = 0;
        try {
            total = Integer.parseInt(tab.getValor("NUMERO"));
        } catch (Exception e) {
        }
        return total;
    }

    /**
     * Retorna los mejores proveedores segun parámetros
     *
     * @param limite numero de clientes
     * @param anio
     * @return
     */
    public String getSqlMejoresProveedores(Integer limite, String anio) {
        String lim = "";
        String conAnio = "";
        if (limite == null) {
            limite = 0;
        }
        if (limite > 0) {
            lim = "LIMIT " + limite;
        }
        if (anio == null) {
            anio = "";
        }
        if (anio.isEmpty() == false) {
            conAnio = " and YEAR(fecha_compra) = " + anio + " \n";
        }

        return "SELECT compras.COD_PROVE ,NOM_PROVE AS PROVEEDOR,COUNT(compras.COD_PROVE) AS NUM_FACTURAS, SUM(TOTAL) AS TOTAL \n"
                + "from compras,proveedores \n"
                + "where  compras.COD_PROVE=proveedores.COD_PROVE \n"
                + conAnio
                + "group by compras.COD_PROVE ,NOM_PROVE HAVING COUNT(compras.COD_PROVE)>0 AND  SUM(TOTAL) IS NOT NULL\n"
                + "ORDER BY 4 DESC  " + lim;
    }

}
