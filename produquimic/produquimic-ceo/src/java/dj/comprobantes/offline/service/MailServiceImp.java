/*
 *********************************************************************
 Objetivo: Servicio que implementa interface MailService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.enums.ParametrosSistemaEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.exception.GenericException;

import javax.ejb.Stateless;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import sistema.aplicacion.Utilitario;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class MailServiceImp implements MailService {

    private List<MimeMessage> listaMensajes;
    private Session session = null;
    @EJB
    private ArchivoService archivoService;

    private final Utilitario utilitario = new Utilitario();

    @Override
    public String agregarCorreo(Comprobante comprobanteMail) {
        if (comprobanteMail == null) {
            return null;
        }
        if (comprobanteMail.getCliente().getCorreo() == null) {
            return null;
        }

        if (utilitario.isCorreoValido(comprobanteMail.getCliente().getCorreo()) == false) {
            return null;
        }

        String str = "";
        if (listaMensajes == null) {
            listaMensajes = new ArrayList<>();
        }
        try {
            BodyPart texto = new MimeBodyPart();
            String stb_mensaje = "<html><head><title></title></head><body bgcolor='#EFF0F2' style='font-family: sans-serif'><div align=\"justify\">"
                    + "<span style='color:#939598;font-size:44px;'>Ban</span><span style='color:#6D6E71;font-size:44px;'>Ecuador</span>"
                    + "<div align='right' style='background-color:#43BEAC;color:white;white:100%;padding-right:15px;height:30px;font-size:24px;'>  COMPROBANTES ELECTRONICOS</div>"
                    + "<div style='padding-left:15px;font-size:14px;'>"
                    + "<p>Estimad@ " + comprobanteMail.getCliente().getNombreCliente().toUpperCase() + "</p>"
                    + "<p>" + "Usted a recibido un Comprobante Electr&oacute;nico :</p>"
                    + "<p> TIPO: " + TipoComprobanteEnum.getDescripcion(comprobanteMail.getCoddoc()) + " </p>"
                    + "<p>NÚMERO : " + comprobanteMail.getSecuencial() + "</p>"
                    + "<p>CLAVE DE ACCESO : " + comprobanteMail.getClaveacceso() + "</p>"
                    + "<br/>"
                    + "<i style='font-size:13px;'><b> No responda a este mensaje ya que ha sido generado automaticamente para su informacion.</b></i>"
                    + "</div>"
                    + "</div></body></html>";
            texto.setContent(stb_mensaje, "text/html");
            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();

            multiParte.addBodyPart(texto);

            byte[] pdf = archivoService.getPdf(comprobanteMail);
            if (pdf != null) {
                File archivo = File.createTempFile(comprobanteMail.getClaveacceso(), ".pdf", null);
                FileOutputStream fos = new FileOutputStream(archivo);
                fos.write(pdf);
                fos.close();
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo)));
                adjunto.setFileName(comprobanteMail.getClaveacceso() + ".pdf");
                multiParte.addBodyPart(adjunto);
            }
            byte[] xml = archivoService.getXml(comprobanteMail);
            if (xml != null) {
                File archivo = File.createTempFile(comprobanteMail.getClaveacceso(), ".xml", null);
                FileOutputStream fos = new FileOutputStream(archivo);
                fos.write(xml);
                fos.close();
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo)));
                adjunto.setFileName(comprobanteMail.getClaveacceso() + ".xml");
                multiParte.addBodyPart(adjunto);
            }

            // Se compone el correo, dando to, from, subject y el
            // contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ParametrosSistemaEnum.MAIL_GENERIC.getCodigo()));
            message.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(comprobanteMail.getCliente().getCorreo()));
            message.setSubject("COMPROBANTE ELECTRONICO");
            message.setContent(multiParte);
            listaMensajes.add(message);
        } catch (MessagingException | GenericException | IOException e) {
        }

        return str;
    }

    private void configurarMail() {
        if (session == null) {
            ///SSH
            Properties propiedades = new Properties();
            propiedades.setProperty("mail.smtp.starttls.enable", "false");
            propiedades.setProperty("mail.smtp.auth", "true");
            propiedades.setProperty("mail.transport.protocol", "smtp");
            //Propiedades smtp
            propiedades.put("mail.smtp.host", ParametrosSistemaEnum.MAIL_SMTP_HOST.getCodigo());
            propiedades.setProperty("mail.smtp.port", ParametrosSistemaEnum.MAIL_SMTP_PORT.getCodigo());
            propiedades.setProperty("mail.smtp.user", ParametrosSistemaEnum.MAIL_GENERIC.getCodigo());
            session = Session.getDefaultInstance(propiedades, null);
        }
    }

    @Override
    public String enviarTodos() {
        String str = "";
        if (listaMensajes != null) {
            try {
                configurarMail();
                Transport t = session.getTransport("smtp");
                t.connect(ParametrosSistemaEnum.MAIL_GENERIC.getCodigo(), ParametrosSistemaEnum.MAIL_PASSWORD.getCodigo());
                for (Message actual : listaMensajes) {
                    if (t.isConnected() == false) {
                        t.connect(ParametrosSistemaEnum.MAIL_GENERIC.getCodigo(), ParametrosSistemaEnum.MAIL_PASSWORD.getCodigo());
                    } else {
                        t.sendMessage(actual, actual.getRecipients(Message.RecipientType.TO));
                    }
                }
                t.close();
            } catch (Exception e) {
                str += "No se puede enviar el correo: " + e.getMessage();
            }
            listaMensajes.clear();
        }
        return str;
    }

    @Override
    public String enviar(Comprobante comprobante) {
        configurarMail();
        String msj = agregarCorreo(comprobante);
        if (msj.isEmpty()) {
            msj = enviarTodos();
        }
        return msj;
    }
}
