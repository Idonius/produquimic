<?php

require '../lib/Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once("Util.php");
\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

$app->post('/getFacturas/:FECHA_DESDE/:FECHA_HASTA', 'getFacturas');

$app->post('/getNotasCredito/:FECHA_DESDE/:FECHA_HASTA', 'getNotasCredito');

$app->post('/getNotasDebito/:FECHA_DESDE/:FECHA_HASTA', 'getNotasDebito');

$app->post('/getGuias/:FECHA_DESDE/:FECHA_HASTA', 'getGuias');

$app->post('/getRetenciones/:FECHA_DESDE/:FECHA_HASTA', 'getRetenciones');

$app->post('/getTotalComprobantes', 'getTotalComprobantes');

$app->post('/getUltimosComprobantes', 'getUltimosComprobantes');

$app->get('/getXML/:PK_CODIGO_COMP', 'getXML');

$app->get('/getPDF/:PK_CODIGO_COMP', 'getPDF');

$app->run();

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
        $sql = "SELECT PK_CODIGO_COMP,NOMBRE_DOCUMENTO,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE a INNER JOIN TIPO_DOCUMENTO b on a.CODIGO_DOCUMENTO=b.CODIGO_DOCUMENTO WHERE IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' ORDER BY FECHA_EMISION desc LIMIT 10";
        $db = new Conexion();
        $resultado = $db->consultar($sql);
        Util::validarResultado($response, $resultado);
    }
}
function getXML($PK_CODIGO_COMP) {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "select ARCHIVO_XML FROM COMPROBANTE a INNER JOIN RIDE_COMPROBANTE b on a.PK_CODIGO_COMP = b.PK_CODIGO_COMP  WHERE IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' AND  b.PK_CODIGO_COMP=" . $PK_CODIGO_COMP;
        $db = new Conexion();
        $resultado = $db->consultarSQL($sql, false);
        $file = $resultado->getDatos()[0]->ARCHIVO_XML;
        $response->header('Content-Type', 'text/xml');
        echo $file;
    }
}

function getPDF($PK_CODIGO_COMP) {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isAutorizado($response)) {
        $sql = "select ARCHIVO_PDF FROM COMPROBANTE a INNER JOIN RIDE_COMPROBANTE b on a.PK_CODIGO_COMP = b.PK_CODIGO_COMP  WHERE IDENTIFICACION='" . $_SESSION['IDENTIFICACION'] . "' AND  b.PK_CODIGO_COMP=" . $PK_CODIGO_COMP;
        var_dump($sql);
        $db = new Conexion();
        $resultado = $db->consultarSQL($sql, false);
        $file = $resultado->getDatos()[0]->ARCHIVO_PDF;
        $response->header('Content-Type', 'application/pdf;charset=utf-8');
        echo $file;
    }
}

?>