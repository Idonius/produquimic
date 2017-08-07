<?php

require("../lib/phpmailer/PHPMailerAutoload.php");

class EnviarCorreo {

    public function enviarRegistro($_usuario = array()) {
        $_mensaje = "<html>\n"
                . "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                . "<body style='font-family: sans-serif;margin: 0;padding: 0;font-size: 13px;overflow: hidden;'>\n"
                . "<div>\n"
                . "	<div align=\"right\" style=\"background: #4A9140;width: 98%;color: white;padding-right:35px;height:33px;font-size:20px;\"> <div style=\"height:33px;vertical-align:middle;display: table-cell;\"> Comprobantes Electrónicos</div></div>\n"
                . "	<br/>\n"
                . "	<div style=\"padding-left:15px\"> \n"
                . "	<p>Estimado cliente " . $_usuario->NOMBRE_USUARIO . ", </p>\n"
                . "	<p> Se registro exitosamente en el sistema. Por favor ingrese a <a href=\"http://comprobantes.produquimic.com.ec\">http://comprobantes.produquimic.com.ec</a> y digite la contraseña indicada a continuación:</p>\n"
                . "	<p><strong>Usuario:</strong> " . $_usuario->IDENTIFICACION_USUARIO . "\n"
                . "	<br/><strong>Contraseña temporal:</strong>  " . $_usuario->IDENTIFICACION_USUARIO . "</p>	\n"
                . "	\n"
                . "	<p>Gracias por preferirnos.</p>	\n"
                . "	<img src=\"http://produquimic.com.ec/images/logo_mail.gif\">\n"
                . "	<br/>\n"
                . "	Teléfonos: 2692634  -  3651120\n"
                . "	<br/>\n"
                . "	Celular: 0998224739\n"
                . "	<br/>\n"
                . "	<a href=\"http://produquimic.com.ec\">http://www.produquimic.com.ec</a>\n"
                . "	<br/>\n"
                . "	<br/>\n"
                . "	</div>\n"
                . "	<div align=\"left\" style=\"display:block;background: #4A9140;width: 100%;color: white;padding-left:15px;padding-right:15px;font-size:13px;padding-top: 7px;padding-bottom: 7px;\">\n"
                . "	NOTA. Esta es una notificación automática, por favor no responder este correo electrónico. Cualquier duda o inconveniente favor contactarnos al correo <strong><a style=\"color: white\" href=\"mailto:sistemas@produquimic.com.ec?Subject=Ayuda\" target=\"_top\">sistemas@produquimic.com.ec</a></strong>    \n"
                . "	</div>\n"
                . "</div>\n"
                . "</body>\n"
                . "</html>";
        $this->enviar("REGISTRO EN EL SISTEMA", $_mensaje, $_usuario);
    }

    public function enviarResetearClave($_usuario = array()) {
        $_mensaje = "<html>\n"
                . "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                . "<body style='font-family: sans-serif;margin: 0;padding: 0;font-size: 13px;overflow: hidden;'>\n"
                . "<div>\n"
                . " <div align=\"right\" style=\"background: #4A9140;width: 98%;color: white;padding-right:35px;height:33px;font-size:20px;\"> <div style=\"height:33px;vertical-align:middle;display: table-cell;\"> Comprobantes Electrónicos</div></div>\n"
                . " <br/>\n"
                . " <div style=\"padding-left:15px\"> \n"
                . " <p>Estimado cliente " . $_usuario->NOMBRE_USUARIO . ", </p>\n"
                . " <p> Su contraseña ha sido reseteada exitosamente. Por favor ingrese a <a href=\"http://comprobantes.produquimic.com.ec\">http://comprobantes.produquimic.com.ec</a> y digite la contraseña indicada a continuación:</p>\n"
                . " <p><strong>Usuario:</strong> " . $_usuario->IDENTIFICACION_USUARIO . "\n"
                . " <br/><strong>Contraseña temporal:</strong>  " . $_usuario->IDENTIFICACION_USUARIO . "</p>   \n"
                . " \n"
                . " <p>Gracias por preferirnos.</p> \n"
                . " <img src=\"http://produquimic.com.ec/images/logo_mail.gif\">\n"
                . " <br/>\n"
                . " Teléfonos: 2692634  -  3651120\n"
                . " <br/>\n"
                . " Celular: 0998224739\n"
                . " <br/>\n"
                . " <a href=\"http://produquimic.com.ec\">http://www.produquimic.com.ec</a>\n"
                . " <br/>\n"
                . " <br/>\n"
                . " </div>\n"
                . " <div align=\"left\" style=\"display:block;background: #4A9140;width: 100%;color: white;padding-left:15px;padding-right:15px;font-size:13px;padding-top: 7px;padding-bottom: 7px;\">\n"
                . " NOTA. Esta es una notificación automática, por favor no responder este correo electrónico. Cualquier duda o inconveniente favor contactarnos al correo <strong><a style=\"color: white\" href=\"mailto:sistemas@produquimic.com.ec?Subject=Ayuda\" target=\"_top\">sistemas@produquimic.com.ec</a></strong>    \n"
                . " </div>\n"
                . "</div>\n"
                . "</body>\n"
                . "</html>";
        $this->enviar("RESTAURACIÓN DE CONTRASEÑA", $_mensaje, $_usuario);
    }

    public function enviar($_titulo, $_mensaje, $_usuario = array()) {
        $mail = new PHPMailer();
        $mail->IsSMTP();
        $mail->CharSet = 'UTF-8';
        $mail->SMTPDebug = 0;    // set mailer to use SMTP
        $mail->SMTPAuth = true;     // turn on SMTP authentication
        $mail->SMTPSecure = "ssl";
        $mail->Host = "mail.produquimic.com.ec";  // specify main and backup server
        $mail->Port = 465;
        $mail->Username = "comprobantes@produquimic.com.ec";  // SMTP username
        $mail->Password = "sami2008"; // SMTP password
        $mail->From = 'comprobantes@produquimic.com.ec';
        $mail->FromName = "Produquimic (Comprobantes Electrónicos)";
        $mail->AddAddress($_usuario->CORREO_USUARIO, $_usuario->NOMBRE_USUARIO);
        $mail->isHTML(true);
        $mail->Subject = $_titulo;
        $mail->Body = $_mensaje;

        if (!$mail->Send()) {
            // echo "Message could not be sent. <p>";
            // echo "Mailer Error: " . $mail->ErrorInfo;
            exit;
        }
    }

    public function enviarComprobante($_usuario = array(), $_correo = NULL) {
        $_mensaje = "<html>\n"
                . "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                . "<body style='font-family: sans-serif;margin: 0;padding: 0;font-size: 13px;overflow: hidden;'>\n"
                . "<div>\n"
                . " <div align=\"right\" style=\"background: #4A9140;width: 98%;color: white;padding-right:35px;height:33px;font-size:20px;\"> <div style=\"height:33px;vertical-align:middle;display: table-cell;\"> Comprobantes Electrónicos</div></div>\n"
                . " <br/>\n"
                . " <div style=\"padding-left:15px\"> \n"
                . " <p>Estimado cliente " . $_usuario->NOMBRE_USUARIO . ", </p>\n"
                . " <p>Nos complace informale que su documento electrónico ha sido generado con el siguiente detalle:</p>\n"
                . " <p><strong>Tipo de documento :</strong> " . $_usuario->NOMBRE_DOCUMENTO . "\n"
                . " <br/><strong>Número de documento :</strong>  " . $_usuario->SECUENCIAL . "</p><br/>   \n"
                . " <p> También puede ingresar a <a href=\"http://comprobantes.produquimic.com.ec\">http://comprobantes.produquimic.com.ec</a> para consultar todos sus documentos emitidos.</p>\n"
                . " <p>Gracias por preferirnos.</p> \n"
                . " <img src=\"http://produquimic.com.ec/images/logo_mail.gif\">\n"
                . " <br/>\n"
                . " Teléfonos: 2692634  -  3651120\n"
                . " <br/>\n"
                . " Celular: 0998224739\n"
                . " <br/>\n"
                . " <a href=\"http://produquimic.com.ec\">http://www.produquimic.com.ec</a>\n"
                . " <br/>\n"
                . " <br/>\n"
                . " </div>\n"
                . " <div align=\"left\" style=\"display:block;background: #4A9140;width: 100%;color: white;padding-left:15px;padding-right:15px;font-size:13px;padding-top: 7px;padding-bottom: 7px;\">\n"
                . " NOTA. Esta es una notificación automática, por favor no responder este correo electrónico. Cualquier duda o inconveniente favor contactarnos al correo <strong><a style=\"color: white\" href=\"mailto:sistemas@produquimic.com.ec?Subject=Ayuda\" target=\"_top\">sistemas@produquimic.com.ec</a></strong>    \n"
                . " </div>\n"
                . "</div>\n"
                . "</body>\n"
                . "</html>";
        $this->enviarAdjunto("COMPROBANTE ELECTRÓNICO", $_mensaje, $_usuario, $_correo);
    }

    public function enviarAdjunto($_titulo, $_mensaje, $_usuario = array(), $_correo) {
        $mail = new PHPMailer();
        $mail->IsSMTP();
        $mail->CharSet = 'UTF-8';
        $mail->SMTPDebug = 0;    // set mailer to use SMTP
        $mail->SMTPAuth = true;     // turn on SMTP authentication
        $mail->SMTPSecure = "ssl";
        $mail->Host = "mail.produquimic.com.ec";  // specify main and backup server
        $mail->Port = 465;
        $mail->Username = "comprobantes@produquimic.com.ec";  // SMTP username
        $mail->Password = "sami2008"; // SMTP password
        $mail->From = 'comprobantes@produquimic.com.ec';
        $mail->FromName = "Produquimic (Comprobantes Electrónicos)";
        if (is_null($_correo)) {
            $mail->AddAddress($_usuario->CORREO_DOCUMENTO, $_usuario->NOMBRE_USUARIO);
        } else {
            $mail->AddAddress($_correo, $_usuario->NOMBRE_USUARIO);
        }

        $mail->AddStringAttachment($_usuario->ARCHIVO_PDF, $_usuario->CLAVE_ACCESO . ".pdf");
        $mail->AddStringAttachment($_usuario->ARCHIVO_XML, $_usuario->CLAVE_ACCESO . ".xml");
        $mail->isHTML(true);
        $mail->Subject = $_titulo;
        $mail->Body = $_mensaje;

        if (!$mail->Send()) {
            // echo "Message could not be sent. <p>";
            // echo "Mailer Error: " . $mail->ErrorInfo;
            exit;
        }
    }

}

?>