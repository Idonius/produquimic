<?php

require '../lib/Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once("Util.php");
include_once("../correo/EnviarCorreo.php");

\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

$app->post('/getPreciosProductos', 'getPreciosProductos');

$app->post('/getFacturas/:FECHA_DESDE/:FECHA_HASTA', 'getFacturas');

$app->post('/getNotasCredito/:FECHA_DESDE/:FECHA_HASTA', 'getNotasCredito');

$app->post('/getNotasDebito/:FECHA_DESDE/:FECHA_HASTA', 'getNotasDebito');

$app->post('/getGuias/:FECHA_DESDE/:FECHA_HASTA', 'getGuias');

$app->post('/getRetenciones/:FECHA_DESDE/:FECHA_HASTA', 'getRetenciones');

$app->post('/getTotalComprobantes', 'getTotalComprobantes');

$app->post('/getUltimosComprobantes', 'getUltimosComprobantes');

$app->get('/getXML/:CLAVE_ACCESO', 'getXML');

$app->get('/getPDF/:CLAVE_ACCESO', 'getPDF');

$app->get('/getComprobantesNoEviadosMail', 'getComprobantesNoEviadosMail');

$app->run();

function getPreciosProductos() {    
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $request = \Slim\Slim::getInstance()->request();
        $param = json_decode($request->getBody()); 
        $sql = "SELECT DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') AS FECHA_EMISION, SECUENCIAL, CLIENTE, IDENTIFICACION,
                CODIGO_PRINCIPAL,CODIGO_AUXILIAR,CANTIDAD,DESCRIPCION,PRECIO,DC.TOTAL,PORCENTAJE_IVA
                FROM DETALLE_COMPROBANTE DC
                INNER JOIN COMPROBANTE  CO ON DC.PK_CODIGO_COMP=CO.PK_CODIGO_COMP
                WHERE UPPER(DESCRIPCION) LIKE  UPPER(:producto) ORDER BY DC.PK_CODIGO_COMP DESC";    
        $db = new Conexion();
        $valoresSql = array(":producto" => "%".$param->producto."%");    
        $resultado = $db->consultar($sql, $valoresSql);
        Util::validarResultado($response, $resultado);
    }
}

function getFacturas($FECHA_DESDE, $FECHA_HASTA) {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='01' AND IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' AND FECHA_EMISION BETWEEN '" . $FECHA_DESDE . "' AND '" . $FECHA_HASTA . "' ORDER BY FECHA_EMISION desc";
        $db = new Conexion();
        $resultado = $db->consultar($sql);
        Util::validarResultado($response, $resultado);
    }
}

function getNotasCredito($FECHA_DESDE, $FECHA_HASTA) {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='04' AND IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' AND FECHA_EMISION BETWEEN '" . $FECHA_DESDE . "' AND '" . $FECHA_HASTA . "' ORDER BY FECHA_EMISION desc";
        $db = new Conexion();
        $resultado = $db->consultar($sql);
        Util::validarResultado($response, $resultado);
    }
}

function getNotasDebito($FECHA_DESDE, $FECHA_HASTA) {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='05' AND IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' AND FECHA_EMISION BETWEEN '" . $FECHA_DESDE . "' AND '" . $FECHA_HASTA . "' ORDER BY FECHA_EMISION desc";
        $db = new Conexion();
        $resultado = $db->consultar($sql);
        Util::validarResultado($response, $resultado);
    }
}

function getGuias($FECHA_DESDE, $FECHA_HASTA) {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='06' AND IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' AND FECHA_EMISION BETWEEN '" . $FECHA_DESDE . "' AND '" . $FECHA_HASTA . "' ORDER BY FECHA_EMISION desc";
        $db = new Conexion();
        $resultado = $db->consultar($sql);
        Util::validarResultado($response, $resultado);
    }
}

function getTotalComprobantes() {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "SELECT LABEL_DOCUMENTO,(SELECT COUNT(1)  FROM COMPROBANTE WHERE CODIGO_DOCUMENTO=a.CODIGO_DOCUMENTO AND IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' )AS NUM_FACTURAS,COLOR_DOCUMENTO FROM TIPO_DOCUMENTO a WHERE ACTIVO_DOCUMENTO=1";
        $db = new Conexion();
        $resultado = $db->consultar($sql);
        Util::validarResultado($response, $resultado);
    }
}

function getRetenciones($FECHA_DESDE, $FECHA_HASTA) {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='07' AND IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' AND FECHA_EMISION BETWEEN '" . $FECHA_DESDE . "' AND '" . $FECHA_HASTA . "' ORDER BY FECHA_EMISION desc";
        $db = new Conexion();
        $resultado = $db->consultar($sql);
        Util::validarResultado($response, $resultado);
    }
}

function getUltimosComprobantes() {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "SELECT PK_CODIGO_COMP,NOMBRE_DOCUMENTO,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE a INNER JOIN TIPO_DOCUMENTO b on a.CODIGO_DOCUMENTO=b.CODIGO_DOCUMENTO WHERE IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' ORDER BY PK_CODIGO_COMP desc LIMIT 10";
        $db = new Conexion();
        $resultado = $db->consultar($sql);
        Util::validarResultado($response, $resultado);
    }
}

function getXML($CLAVE_ACCESO) {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "select ARCHIVO_XML FROM COMPROBANTE a INNER JOIN RIDE_COMPROBANTE b on a.PK_CODIGO_COMP = b.PK_CODIGO_COMP  WHERE a.CLAVE_ACCESO='" . $CLAVE_ACCESO . "'";
        $db = new Conexion();
        $resultado = $db->consultarSQL($sql, false);
        $file = $resultado->getDatos()[0]->ARCHIVO_XML;
        $response->header('Content-Type', 'text/xml');
        echo $file;
    }
}

function getPDF($CLAVE_ACCESO) {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "select ARCHIVO_PDF FROM COMPROBANTE a INNER JOIN RIDE_COMPROBANTE b on a.PK_CODIGO_COMP = b.PK_CODIGO_COMP  WHERE  a.CLAVE_ACCESO='" . $CLAVE_ACCESO . "'";
        $db = new Conexion();
        $resultado = $db->consultarSQL($sql, false);
        $file = $resultado->getDatos()[0]->ARCHIVO_PDF;
        $response->header('Content-Type', 'application/pdf;charset=utf-8');
        echo $file;
    }
}

function getComprobantesNoEviadosMail() {
    $response = \Slim\Slim::getInstance()->response();
    $sql = "SELECT a.PK_CODIGO_COMP,NOMBRE_DOCUMENTO,CONCAT(ESTABLECIM,'-',PTO_EMISION,'-',SECUENCIAL) as SECUENCIAL,CLAVE_ACCESO,NOMBRE_USUARIO,CORREO_DOCUMENTO,ARCHIVO_XML,ARCHIVO_PDF from COMPROBANTE a inner join USUARIO b on a.IDENTIFICACION= b.IDENTIFICACION_USUARIO inner join TIPO_DOCUMENTO c on a.CODIGO_DOCUMENTO=c.CODIGO_DOCUMENTO INNER JOIN RIDE_COMPROBANTE d on a.PK_CODIGO_COMP = d.PK_CODIGO_COMP where ENVIADO = false ORDER BY FECHA_EMISION desc";
    $db = new Conexion();
    $resultado = $db->consultar($sql);

    if ($resultado->getDatos()) {
        foreach ($resultado->getDatos() as $param) {
            $correo = new EnviarCorreo();
            $correo->enviarComprobante($param);
            $columnas = array("ENVIADO" => true);
            $condiciones = array("PK_CODIGO_COMP" => $param->PK_CODIGO_COMP);
            $db = new Conexion();
            $resultado = $db->actualizar("COMPROBANTE", $columnas, $condiciones);
        }
    }
}
   

?>