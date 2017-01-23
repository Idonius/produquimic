/*
 *********************************************************************
 Objetivo: Enum Tipos de Impuestos
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.enums;

/**
 *
 * @author diego.jacome
 */
public enum TipoImpuestoEnum {

    RENTA(1, "Impuesto a la Renta"),
    IVA(2, "I.V.A."),
    ICE(3, "I.C.E."),
    IRBPNR(5, "IRBPNR");

    private final int codigo;
    private final String descripcion;

    private TipoImpuestoEnum(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoImpuestoEnum obtenerTipoImpuesto(int codigoImpuesto) {
        for (TipoImpuestoEnum tipoImpuestoEnum : TipoImpuestoEnum.values()) {
            if (tipoImpuestoEnum.getCodigo() == codigoImpuesto) {
                return tipoImpuestoEnum;
            }
        }
        return null;
    }
}
