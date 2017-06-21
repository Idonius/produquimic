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
    MAIL_PASSWORD("password"),
    MAIL_GENERIC("comprobantes@produquimic.com.ec"),
    CPANEL_WEB("http://comprobantes.produquimic.com.ec/framework/servicios/ServicioNube.php/subirComprobante"),
    CPANEL_WEB_REENVIAR("http://comprobantes.produquimic.com.ec/framework/servicios/ServicioNube.php/reenviarComprobante"),    
    CODIGO_EMPR("1"); //CEO PRODUQUIMIC MG 

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
