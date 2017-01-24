/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dj.comprobantes.offline.util;

import framework.aplicacion.Framework;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author djacome
 */
public class UtilitarioCeo extends Framework {

    /**
     * Retorna una fecha en formato especificado
     *
     * @param fecha
     * @param formato
     * @return
     */
    @Override
    public String getFormatoFecha(Object fecha, String formato) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            String fechaStr = formatoFecha.format(fecha);
            fechaStr = fechaStr.toUpperCase();
            if (fechaStr != null) {
                fechaStr = fechaStr.replace("JANUARY ", "ENERO ");
                fechaStr = fechaStr.replace("FEBRUARY ", "FEBRERO ");
                fechaStr = fechaStr.replace("MARCH ", "MARZO ");
                fechaStr = fechaStr.replace("APRIL ", "ABRIL ");
                fechaStr = fechaStr.replace("MAY ", "MAYO ");
                fechaStr = fechaStr.replace("JUNE ", "JUNIO ");
                fechaStr = fechaStr.replace("JULY ", "JULIO ");
                fechaStr = fechaStr.replace("AUGUST ", "AGOSTO ");
                fechaStr = fechaStr.replace("SEPTEMBER ", "SEPTIEMBRE ");
                fechaStr = fechaStr.replace("OCTOBER ", "OCTUBRE ");
                fechaStr = fechaStr.replace("NOVEMBER ", "NOVIEMBRE ");
                fechaStr = fechaStr.replace("DECEMBER ", "DICIEMBRE ");
            }
            return fechaStr;
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    /**
     * Transforma a Date de una fecha en un formato especifico
     *
     * @param fecha
     * @param formato
     * @return
     */
    public Date toDate(String fecha, String formato) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
            Date dat_fecha = formatoFecha.parse(fecha);
            return dat_fecha;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Valida una direccion de correo
     *
     * @param email
     * @return
     */
    public boolean isCorreoValido(String email) {
        Pattern pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        Matcher mat = pat.matcher(email);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Permite hacer una istalacion a certificados SSL para webservice
     *
     * @return
     */
    public String instalarCertificados() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            return e.getMessage();
        }
        return null;
    }

    /**
     * Retorna un valor dentro de una estructura XML
     *
     * @param cadenaXML
     * @param etiqueta
     * @return
     */
    public String getValorEtiqueta(String cadenaXML, String etiqueta) {
        String str_valor = "";
        try {
            String str_etiqueta1 = "<" + etiqueta + ">";
            String str_etiqueta2 = "</" + etiqueta + ">";
            str_valor = cadenaXML.substring((cadenaXML.indexOf(str_etiqueta1) + str_etiqueta1.length()), (cadenaXML.indexOf(str_etiqueta2)));
            str_valor = str_valor.trim();
        } catch (Exception e) {
        }
        return str_valor;
    }
}
