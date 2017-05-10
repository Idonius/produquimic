/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_cuentas_x_cobrar;

import framework.componentes.Boton;
import framework.componentes.Calendario;
import framework.componentes.Combo;
import framework.componentes.Etiqueta;
import framework.componentes.Grupo;
import framework.componentes.MenuPanel;
import framework.componentes.PanelTabla;
import framework.componentes.Tabla;
import javax.ejb.EJB;
import servicios.cuentas_x_cobrar.ServicioCuentasCxC;
import sistema.aplicacion.Pantalla;

/**
 *
 * @author djacome
 */
public class pre_nota_credito extends Pantalla {

    @EJB
    private final ServicioCuentasCxC ser_factura = (ServicioCuentasCxC) utilitario.instanciarEJB(ServicioCuentasCxC.class);

    private final Combo com_pto_emision = new Combo();
    private final Calendario cal_fecha_inicio = new Calendario();
    private final Calendario cal_fecha_fin = new Calendario();
    private final MenuPanel mep_menu = new MenuPanel();

    private Tabla tab_tabla1;
    private Tabla tab_tabla2;

    public pre_nota_credito() {
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
        mep_menu.agregarItem("Crear Nota de Crédito", "dibujarNuevaNotaCredito", "ui-icon-contact"); // 1
        mep_menu.agregarItem("Listado de Notas de Crédito", "dibujarNotaCredito", "ui-icon-note");  //2
        mep_menu.agregarItem("Generar Asiento Contable", "dibujarNotaCreditoNoContabilizadas", "ui-icon-notice"); //3
        mep_menu.agregarItem("Notas de Crédito Anuladas", "dibujarNotaCreditoAnuladas", "ui-icon-cancel"); //4

        agregarComponente(mep_menu);

    }

    public void actualizarNotas() {

    }

    public void dibujarNuevaNotaCredito() {

        tab_tabla1 = new Tabla();
        tab_tabla2 = new Tabla();
        tab_tabla1.setTabla("cxp_cabecera_nota", "ide_cpcno", 1);
        tab_tabla1.setCondicion("ide_cpcno=-1");
        tab_tabla1.getColumna("ide_geper").setCombo("gen_persona", "ide_geper", "nom_geper,identificac_geper", "es_cliente_geper=TRUE AND nivel_geper='HIJO'");
        tab_tabla1.getColumna("ide_geper").setAutoCompletar();
        tab_tabla1.getColumna("ide_cpmno").setCombo("cxp_motivo_nota", "ide_cpmno", "nombre_cpmno", "");
        tab_tabla1.getColumna("ide_cpeno").setValorDefecto("1");//Estado normal por defecto
        //!!! BORRAR ide_cpttr  autorizacio_cpcno  AUMENTAR  ide_ccdaf   total_cpcno
        tab_tabla1.getColumna("fecha_trans_cpcno").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.getColumna("fecha_trans_cpcno").setVisible(false);
        tab_tabla1.getColumna("valor_ice_cpcno").setVisible(false);
        tab_tabla1.getColumna("total_cpcno").setLectura(true);
        tab_tabla1.getColumna("base_no_objeto_iva_cpcno").setLectura(true);
        tab_tabla1.getColumna("base_tarifa0_cpcno").setLectura(true);
        tab_tabla1.getColumna("base_grabada_cpcno").setLectura(true);
        tab_tabla1.getColumna("valor_iva_cpcno").setLectura(true);
        tab_tabla1.getColumna("fecha_emisi_cpcno").setValorDefecto(utilitario.getFechaActual());
        tab_tabla1.setTipoFormulario(true);
        tab_tabla1.agregarRelacion(tab_tabla2);
        tab_tabla1.dibujar();
        tab_tabla1.insertar();
        PanelTabla pat_panel1 = new PanelTabla();
        pat_panel1.setPanelTabla(tab_tabla1);
        pat_panel1.getMenuTabla().getItem_insertar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_eliminar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel1.getMenuTabla().getItem_buscar().setRendered(false);
        pat_panel1.getMenuTabla().getSub_otros().setRendered(false);
        tab_tabla2.setTabla("cxp_detalle_nota", "ide_cpdno", 2);
        tab_tabla2.setCondicion("ide_cpdno=-1");
        tab_tabla2.getColumna("ide_inarti").setCombo("inv_articulo", "ide_inarti", "nombre_inarti", "nivel_inarti='HIJO'");
        tab_tabla2.getColumna("ide_inarti").setAutoCompletar();
        tab_tabla2.getColumna("credi_tribu_cpdno").setVisible(false);
        tab_tabla2.getColumna("devolucion_cpdno").setVisible(false);
        tab_tabla2.getColumna("alter_tribu_cpdno").setVisible(false);
        tab_tabla2.getColumna("cantidad_cpdno").setMetodoChange("cambioPrecioCantidadIva");
        tab_tabla2.getColumna("precio_cpdno").setMetodoChange("cambioPrecioCantidadIva");
        tab_tabla2.getColumna("valor_cpdno").setLectura(true);
        tab_tabla2.dibujar();
        PanelTabla pat_panel2 = new PanelTabla();
        pat_panel2.setPanelTabla(tab_tabla2);
        pat_panel2.getMenuTabla().getItem_actualizar().setRendered(false);
        pat_panel2.getMenuTabla().getSub_otros().setRendered(false);
        pat_panel2.getMenuTabla().getItem_buscar().setRendered(false);

        Grupo gru = new Grupo();
        gru.getChildren().add(pat_panel1);
        gru.getChildren().add(pat_panel2);
        mep_menu.dibujar(1, "CONFIGURACIÓN FACTURAS ELECTRÓNICAS", gru);

    }

    public void dibujarNotaCredito() {

    }

    public void dibujarFacturasNoContabilizadas() {

    }

    public void dibujarFacturasAnuladas() {

    }

    @Override
    public void insertar() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla2.insertar();
        }
    }

    @Override
    public void guardar() {
        if (mep_menu.getOpcion() == 1) {
            if (tab_tabla1.guardar()) {
                if (tab_tabla2.guardar()) {
                    guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        if (mep_menu.getOpcion() == 1) {
            tab_tabla2.insertar();
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

}
