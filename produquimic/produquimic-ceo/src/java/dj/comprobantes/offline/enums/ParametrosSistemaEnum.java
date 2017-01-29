/*
 *********************************************************************
 Objetivo: Enum Parámetros del Sistema Comprobantes Electrónicos Offline
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
public enum ParametrosSistemaEnum {

    RUTA_SISTEMA("D:\\ComprobantesElectronicosSRIOffline"), //Ruta del sistema
    PROXY_HOST(""),
    PROXY_PORT(""),
    MAIL_SMTP_HOST("mail.produquimic.com.ec"),
    MAIL_SMTP_PORT("465"),//25  //465
    MAIL_PASSWORD("sami2008"),
    MAIL_GENERIC("comprobanteselectronicos@produquimic.com.ec");

    private final String codigo;

    private ParametrosSistemaEnum(final String codigo) {
        this.codigo = codigo;
    }

    /**
     * obtiene el codigo de la numeracion
     *
     * @return
     */
    public String getCodigo() {
        return codigo;
    }
}
