/*
 *********************************************************************
 Objetivo: Enum Tipos de Impuestos y Porcentajes del IVA
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
public enum TipoImpuestoIvaEnum {

    IVA_VENTA_0("0", "0"),
    IVA_VENTA_12("2", "12"),
    IVA_VENTA_14("3", "14"),
    IVA_NO_OBJETO("6", "NO OBJETO"),
    IVA_EXCENTO("7", "EXCENTO");

    private final String codigo;
    private final String porcentaje;

    private TipoImpuestoIvaEnum(String codigo, String porcentaje) {
        this.codigo = codigo;
        this.porcentaje = porcentaje;
    }

    /**
     * obtiene el codigo del impuesto
     *
     * @return
     */
    public String getCodigo() {
        return codigo;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    /**
     * Dado el porcentaje retorna el codigo correspondiente
     *
     * @param porcentaje1
     * @return
     */
    public static String getCodigo(String porcentaje1) {

        String codigo = null;
        if (porcentaje1.equals(IVA_VENTA_0.getPorcentaje())) {
            codigo = IVA_VENTA_0.getCodigo();
        } else if (porcentaje1.equals(IVA_VENTA_12.getPorcentaje())) {
            codigo = IVA_VENTA_12.getCodigo();
        } else if (porcentaje1.equals(IVA_VENTA_14.getPorcentaje())) {
            codigo = IVA_VENTA_14.getCodigo();
        }
        return codigo;
    }

    public static String getCodigo(Double valor) {
        String codigo = IVA_VENTA_14.getCodigo(); //por defecto
        if (String.valueOf(valor.intValue()).equals(IVA_VENTA_12.getPorcentaje())) {
            codigo = IVA_VENTA_12.getCodigo();
        } else if (String.valueOf(valor.intValue()).equals(IVA_VENTA_0.getPorcentaje())) {
            codigo = IVA_VENTA_0.getCodigo();
        }
        return codigo;
    }

    /**
     * Dado el codigo retorna el porcentaje correspondiente
     *
     * @param codigo1
     * @return
     */
    public static String getPorcentaje(String codigo1) {

        String porcentaje = null;
        if (codigo1.equals(IVA_VENTA_0.getCodigo())) {
            porcentaje = IVA_VENTA_0.getPorcentaje();
        } else if (codigo1.equals(IVA_VENTA_12.getCodigo())) {
            porcentaje = IVA_VENTA_12.getPorcentaje();
        } else if (codigo1.equals(IVA_VENTA_14.getCodigo())) {
            porcentaje = IVA_VENTA_14.getPorcentaje();
        }
        return porcentaje;
    }

}
