/*
 *********************************************************************
 Objetivo: Clase DetalleComprobante
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author diego.jacome
 */
public class DetalleComprobante implements Serializable {

    private String codigoprincipal;
    private String codigoauxiliar;
    private String descripciondet;
    private BigDecimal cantidad;
    private BigDecimal preciounitario;
    private BigDecimal descuento;
    private BigDecimal preciototalsinimpuesto;
    private BigDecimal porcentajeiva;
    private Comprobante comprobante;

    public DetalleComprobante(ResultSet resultado) {
        try {
            this.codigoprincipal = resultado.getString("codigo_principal_srdec");
            this.codigoauxiliar = resultado.getString("codigo_auxiliar_srdec");
            this.cantidad = resultado.getBigDecimal("cantidad_srdec");
            this.descripciondet = resultado.getString("descripcion_srdec");
            this.preciounitario = resultado.getBigDecimal("precio_srdec");
            this.descuento = resultado.getBigDecimal("descuento_detalle_srdec");
            this.preciototalsinimpuesto = resultado.getBigDecimal("total_detalle_srdec");
            this.porcentajeiva = resultado.getBigDecimal("porcentaje_iva_srdec");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getCodigoprincipal() {
        return codigoprincipal;
    }

    public void setCodigoprincipal(String codigoprincipal) {
        this.codigoprincipal = codigoprincipal;
    }

    public String getCodigoauxiliar() {
        return codigoauxiliar;
    }

    public void setCodigoauxiliar(String codigoauxiliar) {
        this.codigoauxiliar = codigoauxiliar;
    }

    public String getDescripciondet() {
        return descripciondet;
    }

    public void setDescripciondet(String descripciondet) {
        this.descripciondet = descripciondet;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPreciounitario() {
        return preciounitario;
    }

    public void setPreciounitario(BigDecimal preciounitario) {
        this.preciounitario = preciounitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPreciototalsinimpuesto() {
        return preciototalsinimpuesto;
    }

    public void setPreciototalsinimpuesto(BigDecimal preciototalsinimpuesto) {
        this.preciototalsinimpuesto = preciototalsinimpuesto;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public BigDecimal getPorcentajeiva() {
        return porcentajeiva;
    }

    public void setPorcentajeiva(BigDecimal porcentajeiva) {
        this.porcentajeiva = porcentajeiva;
    }

}
