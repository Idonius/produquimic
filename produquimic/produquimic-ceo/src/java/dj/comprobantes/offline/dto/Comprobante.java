/*
 *********************************************************************
 Objetivo: Clase Comprobante
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dto;

import dj.comprobantes.offline.conexion.ConexionCEO;
import dj.comprobantes.offline.exception.GenericException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author diego.jacome
 */
public final class Comprobante implements Serializable {

    private Long codigocomprobante;
    private String tipoemision;
    private String claveacceso;
    private String coddoc;
    private String estab;
    private String ptoemi;
    private String secuencial;
    private Date fechaemision;
    private String direstablecimiento; //Direccion de la oficina
    private String guiaremision;
    private String numAutorizacion;
    private BigDecimal totalsinimpuestos;
    private BigDecimal totaldescuento;
    private BigDecimal propina;
    private BigDecimal importetotal;
    private String moneda;
    private String periodofiscal;
    private String rise;
    private String coddocmodificado;
    private String numdocmodificado;
    private Date fechaemisiondocsustento;
    private BigDecimal valormodificacion;
    private Firma codigofirma;
    private Integer codigoestado;
    private String oficina;
    private Date fechaautoriza;
    private Cliente cliente;
    private BigDecimal subtotal0;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private List<DetalleComprobante> detalle;

    private String formaCobro = "01"; //por defecto efectivo

    public Comprobante() {
    }

    public Comprobante(ResultSet resultado) {
        try {
            this.codigocomprobante = resultado.getLong("ide_srcom");
            this.tipoemision = resultado.getString("tipoemision_srcom");
            this.claveacceso = resultado.getString("claveacceso_srcom");
            this.coddoc = resultado.getString("coddoc_srcom");
            this.estab = resultado.getString("estab_srcom");
            this.ptoemi = resultado.getString("ptoemi_srcom");
            this.secuencial = resultado.getString("secuencial_srcom");
            this.fechaemision = resultado.getDate("fechaemision_srcom");
            //this.direstablecimiento =   Direccion donde se factura
            //this.guiaremision = resultado.getString("va_guia_remision");
            this.totalsinimpuestos = resultado.getBigDecimal("base_grabada_srcom");
            this.totaldescuento = resultado.getBigDecimal("descuento_srcom");
            this.propina = new BigDecimal(0.00);
            this.importetotal = resultado.getBigDecimal("total_srcom");
            this.moneda = "DOLAR";
            //this.periodofiscal = resultado.getString("va_anio");
            // this.rise = resultado.getString("");
            this.coddocmodificado = resultado.getString("codigo_docu_mod_srcom");
            this.numdocmodificado = resultado.getString("num_doc_mod_srcom"); ///0010010000001
            this.fechaemisiondocsustento = resultado.getDate("fecha_emision_mod_srcom");
            this.valormodificacion = resultado.getBigDecimal("valor_mod_srcom");
            this.numAutorizacion = resultado.getString("autorizacion_srcomn");
            this.oficina = "1";// SUCURSAL 1 

            if (resultado.getString("ide_srfid") != null) {
                this.codigofirma = new Firma(resultado.getInt("ide_srfid"));
            }
            this.codigoestado = resultado.getInt("ide_sresc");
            if (resultado.getBigDecimal("subtotal0_srcom") == null) {
                this.subtotal0 = new BigDecimal("0");
            } else {
                this.subtotal0 = resultado.getBigDecimal("subtotal0_srcom");
            }
            if (resultado.getBigDecimal("subtotal_srcom") == null) {
                this.subtotal = this.totalsinimpuestos;
            } else {
                this.subtotal = resultado.getBigDecimal("subtotal_srcom");
            }
            if (resultado.getBigDecimal("iva_srcom ") == null) {
                this.iva = new BigDecimal("0");
            } else {
                this.iva = resultado.getBigDecimal("iva_srcom ");
            }
            this.formaCobro = resultado.getString("forma_cobre_srcom");

            this.fechaautoriza = resultado.getDate("fechaautoriza_srcom");
            cliente = new Cliente(resultado);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Busca los detalles del Comprobante
        try {
            detalle = new ArrayList<>();
            ConexionCEO con = new ConexionCEO();
            ResultSet res = con.consultar("SELECT * FROM sri_detalle_comprobante where ide_srcom=" + this.codigocomprobante);
            while (res.next()) {
                DetalleComprobante dt = new DetalleComprobante(res);
                dt.setComprobante(this);
                detalle.add(dt);
            }
            res.close();
            con.desconectar();
        } catch (GenericException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Long getCodigocomprobante() {
        return codigocomprobante;
    }

    public void setCodigocomprobante(Long codigocomprobante) {
        this.codigocomprobante = codigocomprobante;
    }

    public String getTipoemision() {
        return tipoemision;
    }

    public void setTipoemision(String tipoemision) {
        this.tipoemision = tipoemision;
    }

    public String getClaveacceso() {
        return claveacceso;
    }

    public void setClaveacceso(String claveacceso) {
        this.claveacceso = claveacceso;
    }

    public String getCoddoc() {
        return coddoc;
    }

    public void setCoddoc(String coddoc) {
        this.coddoc = coddoc;
    }

    public String getEstab() {
        return estab;
    }

    public void setEstab(String estab) {
        this.estab = estab;
    }

    public String getPtoemi() {
        return ptoemi;
    }

    public void setPtoemi(String ptoemi) {
        this.ptoemi = ptoemi;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public Date getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(Date fechaemision) {
        this.fechaemision = fechaemision;
    }

    public String getDirestablecimiento() {
        return direstablecimiento;
    }

    public void setDirestablecimiento(String direstablecimiento) {
        this.direstablecimiento = direstablecimiento;
    }

    public String getGuiaremision() {
        return guiaremision;
    }

    public void setGuiaremision(String guiaremision) {
        this.guiaremision = guiaremision;
    }

    public BigDecimal getTotalsinimpuestos() {
        return totalsinimpuestos;
    }

    public void setTotalsinimpuestos(BigDecimal totalsinimpuestos) {
        this.totalsinimpuestos = totalsinimpuestos;
    }

    public BigDecimal getTotaldescuento() {
        return totaldescuento;
    }

    public void setTotaldescuento(BigDecimal totaldescuento) {
        this.totaldescuento = totaldescuento;
    }

    public BigDecimal getPropina() {
        return propina;
    }

    public void setPropina(BigDecimal propina) {
        this.propina = propina;
    }

    public BigDecimal getImportetotal() {
        return importetotal;
    }

    public void setImportetotal(BigDecimal importetotal) {
        this.importetotal = importetotal;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getPeriodofiscal() {
        return periodofiscal;
    }

    public void setPeriodofiscal(String periodofiscal) {
        this.periodofiscal = periodofiscal;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getCoddocmodificado() {
        return coddocmodificado;
    }

    public void setCoddocmodificado(String coddocmodificado) {
        this.coddocmodificado = coddocmodificado;
    }

    public String getNumdocmodificado() {
        return numdocmodificado;
    }

    public void setNumdocmodificado(String numdocmodificado) {
        this.numdocmodificado = numdocmodificado;
    }

    public Date getFechaemisiondocsustento() {
        return fechaemisiondocsustento;
    }

    public void setFechaemisiondocsustento(Date fechaemisiondocsustento) {
        this.fechaemisiondocsustento = fechaemisiondocsustento;
    }

    public BigDecimal getValormodificacion() {
        return valormodificacion;
    }

    public void setValormodificacion(BigDecimal valormodificacion) {
        this.valormodificacion = valormodificacion;
    }

    public Firma getCodigofirma() {
        return codigofirma;
    }

    public void setCodigofirma(Firma codigofirma) {
        this.codigofirma = codigofirma;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public Date getFechaautoriza() {
        return fechaautoriza;
    }

    public void setFechaautoriza(Date fechaautoriza) {
        this.fechaautoriza = fechaautoriza;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getSubtotal0() {
        return subtotal0;
    }

    public void setSubtotal0(BigDecimal subtotal0) {
        this.subtotal0 = subtotal0;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public List<DetalleComprobante> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleComprobante> detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public Integer getCodigoestado() {
        return codigoestado;
    }

    public void setCodigoestado(Integer codigoestado) {
        this.codigoestado = codigoestado;
    }

    public String getFormaCobro() {
        return formaCobro;
    }

    public void setFormaCobro(String formaCobro) {
        this.formaCobro = formaCobro;
    }

}
