/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import dj.comprobantes.offline.enums.EstadoComprobanteEnum;
import framework.aplicacion.TablaGenerica;
import framework.componentes.Barra;
import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Confirmar;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.Mensaje;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import framework.componentes.VisualizarPDF;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import servicios.ceo.ServicioNotaCreditoElectronica;
import servicios.contabilidad.ServicioConfiguracion;
import servicios.cuentas_x_cobrar.ServicioCuentasCxC;
import servicios.inventario.ServicioProducto;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_nota_credito extends Pantalla {

    @EJB
    private final ServicioCuentasCxC ser_factura = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);
    @EJB
    private final ServicioProducto ser_producto = (ServicioProducto) utilitario.instanciarEJB(ServicioProducto.class);
    @EJB
    private final ServicioConfiguracion ser_configuracion = (ServicioConfiguracion) utilitario.instanciarEJB(ServicioConfiguracion.class);
    @EJB
    private final ServicioNotaCreditoElectronica ser_nocCreditoElectronica = (ServicioNotaCreditoElectronica) utilitario.instanciarEJB(ServicioNotaCreditoElectronica.class);

    private final Combo com_pto_emision = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private final MenuPanel mep_menu = new MenuPanel();

    private Tabla tab_tabla1;
    private Tabla tab_tabla2;
    private double tarifaIVA = 0;

    private final VisualizarPDF vpd_nota_ride = new VisualizarPDF();
    private final Mensaje men_factura = new Mensaje();
    private Confirmar con_confirma = new Confirmar();

    private String ide_cpcno = null;

    public pre_nota_credito() {
        tarifaIVA = ser_configuracion.getPorcentajeIva();
        bar_botones.quitarBotonsNavegacion();

        com_pto_emision.setId("com_pto_emision");
        com_pto_emision.setCombo(ser_factura.getSqlPuntosEmision());
        com_pto_emision.setMetodo("actualizarNotas");
        com_pto_emision.eliminarVacio();
        bar_botones.agregarComponente(new Etiqueta("PUNTO DE EMISIÓN:"));
        bar_botones.agregarComponente(com_pto_emision);
        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(new Etiqueta("FECHA DESDE :"));
        cal_fecha_inicio.setValue(utilitario.getFecha(utilitario.getAnio(utilitario.getFechaActual()) + "-01-01"));
        bar_botones.agregarComponente(cal_fecha_inicio);

        bar_botones.agregarComponente(new Etiqueta("FECHA HASTA :"));
        cal_fecha_fin.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_fin);

        Boton bot_consultar = new Boton();
        bot_consultar.setTitle("Buscar");
        bot_consultar.setMetodo("actualizarNotas");
        bot_consultar.setIcon("ui-icon-search");
        bar_botones.agregarComponente(bot_consultar);

        mep_menu.setMenuPanel("OPCIONES NOTAS DE CRÉDITO", "20%");
        mep_menu.agregarItem("Crear Nota de Crédito", "dibujarInsertar", "ui-icon-contact"); // 1
        mep_menu.agregarItem("Listado de Notas de Crédito", "dibujarNotaCredito", "ui-icon-note");  //2
        //  mep_menu.agregarItem("Generar Asiento Contable", "dibujarNotaCreditoNoContabilizadas", "ui-icon-notice"); //3
        //mep_menu.agregarItem("Notas de Crédito Anuladas", "dibujarNotaCreditoAnuladas", "ui-icon-cancel"); //4

        agregarComponente(mep_menu);

        vpd_nota_ride.setId("vpd_nota_ride");
        vpd_nota_ride.setTitle("RIDE");
        utilitario.getPantalla().getChildren().add(vpd_nota_ride);

        men_factura.setId("men_factura");
        utilitario.getPantalla().getChildren().add(men_factura);

        con_confirma.setId("con_confirma");
        con_confirma.setMessage("Está seguro de Anular la Nota de Crédito ?");
        con_confirma.setTitle("ANULAR NOTA DE CRÉDITO");
        con_confirma.getBot_aceptar().setValue("Si");
        con_confirma.getBot_cancelar().setValue("No");
        agregarComponente(con_confirma);

        dibujarNotaCredito();
    }

    public void dibujarInsertar() {
        ide_cpcno = null;
        dibujarNuevaNotaCredito();
    }

    public void actualizarNotas() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla1.setValor("ide_ccdaf", String.valueOf(com_pto_emision.getValue()));
        }
    }

    public void dibujarNuevaNotaCredito() {

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla2 = new Tabla();
        tab_tabla2.setId("tab_tabla2");
        tab_tabla1.setTabla("cxp_cabecera_nota", "ide_cpcno", 1);
        tab_tabla1.setCondicion("ide_cpcno=" + ide_cpcno);
        tab_tabla1.getGrid().setColumns(6);
        tab_tabla1.setMostrarNumeroRegistros(false);
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_cpmno").setCombo("cxp_motivo_nota", "ide_cpmno", "nombre_cpmno", "");
        tab_tabla1.getColumna("ide_cpmno").setRequerida(true);
        tab_tabla1.getColumna("ide_cpeno").setValorDefecto("1");//Estado normal por defecto         
        tab_tabla1.getColumna("ide_cpeno").setVisible(false);
        tab_tabla1.getColumna("ide_cpcno").setVisible(false);
        tab_tabla1.getColumna("ide_srcom").setVisible(false);
        if (ser_factura.isFacturaElectronica()) {
            tab_tabla1.getColumna("NUMERO_CPCNO").setLectura(true);
        }
        tab_tabla1.getColumna("ide_cntdo").setVisible(false);
        tab_tabla1.getColumna("ide_cntdo").setValorDefecto("0"); //nota de credito
        tab_tabla1.getColumna("ide_cndfp").setCombo("con_deta_forma_pago", "ide_cndfp", "nombre_cndfp", "");
        tab_tabla1.getColumna("ide_cndfp").setRequerida(true);
        tab_tabla1.getColumna("fecha_trans_cpcno").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_trans_cpcno").setVisible(false);
        tab_tabla1.getColumna("valor_ice_cpcno").setVisible(false);
        tab_tabla1.getColumna("ide_cnccc").setVisible(false);
        tab_tabla1.getColumna("total_cpcno").setLectura(true);
        tab_tabla1.getColumna("base_no_objeto_iva_cpcno").setLectura(true);
        tab_tabla1.getColumna("base_tarifa0_cpcno").setLectura(true);
        tab_tabla1.getColumna("base_grabada_cpcno").setLectura(true);
        tab_tabla1.getColumna("valor_iva_cpcno").setLectura(true);
        tab_tabla1.getColumna("total_cpcno").setLectura(true);
        tab_tabla1.getColumna("ide_ccdaf").setVisible(false);
        tab_tabla1.getColumna("fecha_emisi_cpcno").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_emisi_cpcno").setRequerida(true);
        tab_tabla1.getColumna("num_doc_mod_cpcno").setMascara("999-999-999999999");
        tab_tabla1.getColumna("num_doc_mod_cpcno").setMetodoChange("buscaFactura");
        tab_tabla1.getColumna("num_doc_mod_cpcno").setRequerida(true);
        tab_tabla1.getColumna("valor_mod_cpcno").setRequerida(true);
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
        if (tab_tabla1.isEmpty()) {
            tab_tabla1.insertar();
            tab_tabla1.setValor("ide_ccdaf", String.valueOf(com_pto_emision.getValue()));
        }

        if (ser_factura.isFacturaElectronica() == false) {
            tab_tabla1.setValor("NUMERO_CPCNO", String.valueOf(ser_factura.getSecuencialFactura(String.valueOf(com_pto_emision.getValue()))));
        }
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_buscar().setRendered(false);

        tab_tabla2.setTabla("cxp_detalle_nota", "ide_cpdno", 2);
        if (ide_cpcno != null) {
            tab_tabla2.setCondicion("ide_cpcno=" + ide_cpcno);
        } else {
            tab_tabla2.setCondicionForanea("ide_cpdno=-1");
        }

        tab_tabla2.getColumna("ide_cpdno").setVisible(false);
        tab_tabla2.getColumna("ide_cpcno").setVisible(false);
        tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("credi_tribu_cpdno").setVisible(false);
        tab_tabla2.getColumna("devolucion_cpdno").setVisible(false);
        tab_tabla2.getColumna("alter_tribu_cpdno").setVisible(false);
        tab_tabla2.getColumna("alter_tribu_cpdno").setValorDefecto("00");
        tab_tabla2.getColumna("cantidad_cpdno").setMetodoChange("cambioPrecioCantidadIva");
        tab_tabla2.getColumna("precio_cpdno").setMetodoChange("cambioPrecioCantidadIva");

        tab_tabla2.getColumna("valor_cpdno").setEtiqueta();
        tab_tabla2.getColumna("valor_cpdno").setEstilo("font-size:14px;font-weight: bold;");
        tab_tabla2.getColumna("valor_cpdno").alinearDerecha();

        tab_tabla2.setScrollable(true);
        tab_tabla2.getColumna("iva_inarti_cpdno").setCombo(ser_producto.getListaTipoIVA());
        tab_tabla2.getColumna("iva_inarti_cpdno").setPermitirNullCombo(false);
        tab_tabla2.getColumna("ide_inarti").setRequerida(true);
        tab_tabla2.getColumna("ide_inuni").setCombo("inv_unidad", "ide_inuni", "nombre_inuni", "");
        tab_tabla2.getColumna("ide_inuni").setLongitud(-1);
        tab_tabla2.getColumna("iva_inarti_cpdno").setMetodoChange("cambioPrecioCantidadIva");
        tab_tabla2.getColumna("iva_inarti_cpdno").setLongitud(-1);
        tab_tabla2.setScrollHeight(utilitario.getAltoPantalla() - 350);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        pat_panel2.getMenuTabla().getItem_actualizar().setRendered(false);        
        pat_panel2.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel1);
        gru.getChildren().add(pat_panel2);
        mep_menu.dibujar(1, "CONFIGURACIÓN FACTURAS ELECTRÓNICAS", gru);

    }

    public void buscaFactura() {
        tab_tabla2.limpiar();
        if (tab_tabla1.getValor("num_doc_mod_cpcno") != null) {
            TablaGenerica tab_fac = ser_factura.getFacturaPorSecuencial(tab_tabla1.getValor("num_doc_mod_cpcno"));
            tab_tabla1.setValor("ide_geper", tab_fac.getValor("ide_geper"));
            tab_tabla1.setValor("fecha_emision_mod_cpcno", tab_fac.getValor("fecha_emisi_cccfa"));
            tab_tabla1.setValor("valor_mod_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("total_cccfa")));
            tab_tabla1.setValor("total_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("total_cccfa")));
            tab_tabla1.setValor("base_grabada_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("base_grabada_cccfa")));
            tab_tabla1.setValor("base_no_objeto_iva_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("base_no_objeto_iva_cccfa")));
            tab_tabla1.setValor("base_tarifa0_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("base_tarifa0_cccfa")));
            tab_tabla1.setValor("valor_iva_cpcno", utilitario.getFormatoNumero(tab_fac.getValor("valor_iva_cccfa")));
            for (int i = 0; i < tab_fac.getTotalFilas(); i++) {
                tab_tabla2.insertar();
                tab_tabla2.setValor("ide_inarti", tab_fac.getValor(i, "ide_inarti"));
                tab_tabla2.setValor("ide_inuni", tab_fac.getValor(i, "ide_inuni"));
                tab_tabla2.setValor("cantidad_cpdno", tab_fac.getValor(i, "cantidad_ccdfa"));
                tab_tabla2.setValor("precio_cpdno", utilitario.getFormatoNumero(tab_fac.getValor(i, "precio_ccdfa")));
                tab_tabla2.setValor("valor_cpdno", utilitario.getFormatoNumero(tab_fac.getValor(i, "total_ccdfa")));
                tab_tabla2.setValor("iva_inarti_cpdno", tab_fac.getValor(i, "iva_inarti_ccdfa"));
            }
        }
        calcularTotalFactura();
    }

    /**
     * Se ejecuta cuando cambia el Precio o la Cantidad de un detalle de la
     * factura
     *
     * @param evt
     */
    public void cambioPrecioCantidadIva(AjaxBehaviorEvent evt) {
        tab_tabla2.modificar(evt);
        calcularTotalDetalleFactura();
    }

    /**
     * Calcula el valor total de cada detalle de la factura
     */
    private void calcularTotalDetalleFactura() {
        double cantidad = 0;
        double precio = 0;
        double total = 0;
        try {
            cantidad = Double.parseDouble(tab_tabla2.getValor("cantidad_cpdno"));
        } catch (Exception e) {
            cantidad = 0;
        }
        try {
            precio = Double.parseDouble(tab_tabla2.getValor("precio_cpdno"));
        } catch (Exception e) {
            precio = 0;
        }
        total = cantidad * precio;
        tab_tabla2.setValor("valor_cpdno", utilitario.getFormatoNumero(total));
        utilitario.addUpdateTabla(tab_tabla2, "valor_cpdno", "");
        calcularTotalFactura();
    }

    /**
     * Calcula totales de la factura
     */
    public void calcularTotalFactura() {
        double base_grabada = 0;
        double base_no_objeto = 0;
        double base_tarifa0 = 0;
        double valor_iva = 0;
        double porcentaje_iva = 0;

        for (int i = 0; i < tab_tabla2.getTotalFilas(); i++) {
            String iva = tab_tabla2.getValor(i, "iva_inarti_cpdno");
            if (iva.equals("1")) { //SI IVA
                base_grabada = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdno")) + base_grabada;
                porcentaje_iva = tarifaIVA;
                valor_iva = base_grabada * porcentaje_iva; //0.12
            } else if (iva.equals("-1")) { // NO IVA
                base_tarifa0 = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdno")) + base_tarifa0;
            } else if (iva.equals("0")) { // NO OBJETO
                base_no_objeto = Double.parseDouble(tab_tabla2.getValor(i, "valor_cpdno")) + base_no_objeto;
            }
        }
        tab_tabla1.setValor("base_grabada_cpcno", utilitario.getFormatoNumero(base_grabada));
        tab_tabla1.setValor("base_no_objeto_iva_cpcno", utilitario.getFormatoNumero(base_no_objeto));
        tab_tabla1.setValor("valor_iva_cpcno", utilitario.getFormatoNumero(valor_iva));
        tab_tabla1.setValor("base_tarifa0_cpcno", utilitario.getFormatoNumero(base_tarifa0));
        tab_tabla1.setValor("total_cpcno", utilitario.getFormatoNumero(base_grabada + base_no_objeto + base_tarifa0 + valor_iva));
        utilitario.addUpdateTabla(tab_tabla1, "ide_geper,VALOR_MOD_CPCNO,FECHA_EMISION_MOD_CPCNO,base_grabada_cpcno,base_no_objeto_iva_cpcno,valor_iva_cpcno,base_tarifa0_cpcno,total_cpcno", "");
    }

    public void dibujarNotaCredito() {
        Barra bar_menu = new Barra();
        bar_menu.setId("bar_menu");
        bar_menu.limpiar();
        Boton bot_ver = new Boton();
        bot_ver.setValue("Ver Nota de Crédito");
        bot_ver.setMetodo("abrirVerNota");
        bot_ver.setIcon("ui-icon-search");
        bar_menu.agregarComponente(bot_ver);
        bar_menu.agregarSeparador();

        Boton bot_anular = new Boton();
        bot_anular.setValue("Anular Nota de Crédito");
        bot_anular.setMetodo("abrirAnularNotas");
        bot_anular.setIcon("ui-icon-cancel");
        bar_menu.agregarComponente(bot_anular);
        bar_menu.agregarSeparador();

        if (ser_factura.isFacturaElectronica()) {
            bar_menu.agregarSeparador();

            Boton bot_ride = new Boton();
            bot_ride.setValue("Imprimir RIDE");
            bot_ride.setMetodo("abrirRIDE");
            bot_ride.setIcon("ui-icon-print");
            bar_menu.agregarBoton(bot_ride);

            Boton bot_enviar = new Boton();
            bot_enviar.setValue("Enviar al SRI");
            bot_enviar.setMetodo("enviarSRI");
            bot_enviar.setIcon("ui-icon-signal-diag");
            bar_menu.agregarBoton(bot_enviar);

        }

        tab_tabla1 = new Tabla();
        tab_tabla1.setId("tab_tabla1");
        tab_tabla1.setSql(ser_factura.getSqlNotas(com_pto_emision.getValue() + "", cal_fecha_inicio.getFecha(), cal_fecha_fin.getFecha()));
        tab_tabla1.setCampoPrimaria("ide_cpcno");
        tab_tabla1.getColumna("ide_cpcno").setVisible(false);
        tab_tabla1.getColumna("fecha_trans_cpcno").setVisible(false);
        tab_tabla1.getColumna("ide_cpeno").setVisible(false);
        tab_tabla1.getColumna("nombre_cpeno").setFiltroContenido();
        tab_tabla1.getColumna("fecha_emisi_cpcno").setNombreVisual("FECHA");
        tab_tabla1.getColumna("nombre_cpeno").setVisible(true);
        tab_tabla1.getColumna("nombre_cpeno").setNombreVisual("ESTADO");
        tab_tabla1.getColumna("nombre_cpeno").setFiltroContenido();
        tab_tabla1.getColumna("numero_cpcno").setNombreVisual("SECUENCIAL");
        tab_tabla1.getColumna("nom_geper").setFiltroContenido();
        tab_tabla1.getColumna("nom_geper").setNombreVisual("CLIENTE");
        tab_tabla1.getColumna("identificac_geper").setFiltroContenido();
        tab_tabla1.getColumna("identificac_geper").setNombreVisual("IDENTIFICACIÓN");
        tab_tabla1.getColumna("ide_cnccc").setFiltroContenido();
        tab_tabla1.getColumna("ide_cnccc").setNombreVisual("N. ASIENTO");
        tab_tabla1.getColumna("IDE_CNCCC").setLink();
        tab_tabla1.getColumna("IDE_CNCCC").setMetodoChange("abrirAsiento");
        tab_tabla1.getColumna("IDE_CNCCC").alinearCentro();
        tab_tabla1.getColumna("ventas0").alinearDerecha();
        tab_tabla1.getColumna("ventas0").setNombreVisual("VENTAS IVA 0");
        tab_tabla1.getColumna("ventas12").alinearDerecha();
        tab_tabla1.getColumna("ventas12").setNombreVisual("VENTAS IVA");
        tab_tabla1.getColumna("valor_iva_cpcno").alinearDerecha();
        tab_tabla1.getColumna("valor_iva_cpcno").setNombreVisual("IVA");
        tab_tabla1.getColumna("total_cpcno").alinearDerecha();
        tab_tabla1.getColumna("total_cpcno").setEstilo("font-size: 12px;font-weight: bold;");
        tab_tabla1.getColumna("total_cpcno").setNombreVisual("TOTAL");
        if (ser_factura.isFacturaElectronica()) {
            tab_tabla1.getColumna("ide_srcom").setVisible(false);
        }
        tab_tabla1.setRows(15);
        tab_tabla1.setLectura(true);
        //COLOR VERDE FACTURAS NO CONTABILIZADAS
        //COLOR ROJO FACTURAS ANULADAS
        tab_tabla1.setValueExpression("rowStyleClass", "fila.campos[3] eq '0' ? 'text-red' : fila.campos[2] eq null  ? 'text-green' : null");
        tab_tabla1.dibujar();
        PanelTabla pat_panel = new PanelTabla();
        pat_panel.setPanelTabla(tab_tabla1);
        Grupo gru = new Grupo();
        gru.getChildren().add(bar_menu);
        gru.getChildren().add(pat_panel);
        mep_menu.dibujar(2, "LISTADO DE NOTAS DE CRÉDITO", gru);
    }

    public void abrirAnularNotas() {
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            if (ser_factura.isFacturaElectronica()) {
                //valida que este en estado PENDIENTE
                if (tab_tabla1.getValor("nombre_cpeno") != null && !tab_tabla1.getValor("nombre_cpeno").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) {
                    utilitario.agregarMensajeError("No se puede anular la Nota de Crédito Electrónica seleccionada", "Solo se pueden anular Notas de Crédito en estado PENDIENTE");
                    return;
                }
            }
            con_confirma.getBot_aceptar().setMetodo("anularNota");
            con_confirma.dibujar();
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Nota de Crédito", "");
        }

    }

    public void abrirVerNota() {
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            ide_cpcno = tab_tabla1.getValor("ide_cpcno");
            dibujarNuevaNotaCredito();
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Nota de Crédito", "");
        }
    }

    public void anularNota() {
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            ser_factura.anularNotaCredito(tab_tabla1.getValor("ide_cpcno"));
            if (guardarPantalla().isEmpty()) {
                con_confirma.cerrar();
                String aux = tab_tabla1.getValorSeleccionado();
                tab_tabla1.actualizar();
                tab_tabla1.setFilaActual(aux);
                tab_tabla1.calcularPaginaActual();
            }
        } else {
            utilitario.agregarMensajeError("Debe seleccionar una Nota de Crédito", "");
        }
    }

    public void abrirRIDE() {
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            if (tab_tabla1.getValor("ide_srcom") != null) {
                try {
                    ser_nocCreditoElectronica.getRIDE(tab_tabla1.getValor("ide_srcom"));
                    vpd_nota_ride.setVisualizarPDFUsuario();
                    vpd_nota_ride.dibujar();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                utilitario.agregarMensajeInfo("La Nota de Crédito seleccionada no es electrónica", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una factura", "");
        }
    }

    public void enviarSRI() {
        if (tab_tabla1.getValor("ide_cpcno") != null) {
            //Valida que se encuentre en estado PENDIENTE o RECIBIDA
            if ((tab_tabla1.getValor("nombre_cpeno")) != null && (tab_tabla1.getValor("nombre_cpeno").equals(EstadoComprobanteEnum.PENDIENTE.getDescripcion())) || tab_tabla1.getValor("nombre_cpeno").equals(EstadoComprobanteEnum.RECIBIDA.getDescripcion())) {
                String mensaje = ser_nocCreditoElectronica.enviarComprobante(tab_tabla1.getValor("clave_acceso"));

                String aux = tab_tabla1.getValorSeleccionado();
                tab_tabla1.actualizar();
                tab_tabla1.setFilaActual(aux);
                tab_tabla1.calcularPaginaActual();
                if (mensaje.isEmpty()) {
                    String mensje = "<p> NOTA DE CRÉDITO NRO. " + tab_tabla1.getValor("numero_cpcno") + " ";
                    mensje += "</br>AMBIENTE : <strong>" + (utilitario.getVariable("p_sri_ambiente_comp_elect").equals("1") ? "PRUEBAS" : "PRODUCCIÓN") + "</strong></p>";  //********variable ambiente facturacion electronica                    
                    mensje += "<p>ESTADO : <strong>" + tab_tabla1.getValor("nombre_cpeno") + "</strong></p>";
                    mensje += "<p>NÚMERO DE AUTORIZACION : <span style='font-size:12px;font-weight: bold;'>" + tab_tabla1.getValor("CLAVE_ACCESO") + "</span> </p>";
                    men_factura.setMensajeExito("NOTA DE CRÉDITO ELECTRÓNICA AUTORIZADA", mensje);
                    men_factura.dibujar();
                } else {
                    utilitario.agregarMensajeError(mensaje, "");
                }

            } else {
                utilitario.agregarMensajeInfo("La Nota de Crédito seleccionada no se encuentra en estado PENDIENTE o RECIBIDA", "");
            }
        } else {
            utilitario.agregarMensajeInfo("Seleccione una Nota de Crédito", "");
        }
    }

    public void dibujarFacturasNoContabilizadas() {

    }

    public void dibujarFacturasAnuladas() {

    }

    @Override
    public void insertar() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla2.insertar();
        } else {
            ide_cpcno = null;
            dibujarNuevaNotaCredito();
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            if (tab_tabla1.guardar()) {
                if (tab_tabla2.guardar()) {
                    if (guardarPantalla().isEmpty()) {
                        if (ser_factura.isFacturaElectronica()) {
                            ser_nocCreditoElectronica.generarNotaCreditoElectronica(tab_tabla1.getValor("ide_cpcno"));
                            String aux = tab_tabla1.getValor("ide_cpcno");
                            dibujarNotaCredito();
                            tab_tabla1.setFilaActual(aux);
                            tab_tabla1.calcularPaginaActual();
                            abrirRIDE();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla2.eliminar();
        }
    }

    public Tabla getTab_tabla1() {
        return tab_tabla1;
    }

    public void setTab_tabla1(Tabla tab_tabla1) {
        this.tab_tabla1 = tab_tabla1;
    }

    public Tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(Tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }

    public Confirmar getCon_confirma() {
        return con_confirma;
    }

    public void setCon_confirma(Confirmar con_confirma) {
        this.con_confirma = con_confirma;
    }

}
