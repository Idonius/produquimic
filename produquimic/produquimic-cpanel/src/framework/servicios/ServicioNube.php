<?php

require '../lib/Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once("Util.php");
include_once("../correo/EnviarCorreo.php");
\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();


$app->post('/reenviarComprobante', 'reenviarComprobante');

$app->post('/subirComprobante', 'subirComprobante');

$app->post('/subirDetalleComprobante', 'subirDetalleComprobante');

$app->run();

function reenviarComprobante() {
    $response = \Slim\Slim::getInstance()->response();
    $request = \Slim\Slim::getInstance()->request();
    $parametros = json_decode($request->getBody());

    $sql = "SELECT a.PK_CODIGO_COMP,NOMBRE_DOCUMENTO,CONCAT(ESTABLECIM,'-',PTO_EMISION,'-',SECUENCIAL) as SECUENCIAL,CLAVE_ACCESO,NOMBRE_USUARIO,CORREO_USUARIO,ARCHIVO_XML,ARCHIVO_PDF from COMPROBANTE a inner join USUARIO b on a.IDENTIFICACION= b.IDENTIFICACION_USUARIO inner join TIPO_DOCUMENTO c on a.CODIGO_DOCUMENTO=c.CODIGO_DOCUMENTO INNER JOIN RIDE_COMPROBANTE d on a.PK_CODIGO_COMP = d.PK_CODIGO_COMP where a.PK_CODIGO_COMP = " . $parametros->PK_CODIGO_COMP;
    $db = new Conexion();
    $resultado = $db->consultar($sql);
    if ($resultado->getDatos()) {
        foreach ($resultado->getDatos() as $param) {
            $correo = new EnviarCorreo();
            $correo->enviarComprobante($param, $parametros->CORREO_USUARIO);
        }
    }
}

function subirComprobante() {

    $respuesta = array();
    $respuesta['error'] = false;
    $respuesta['mensaje'] = "ok";
    $response = \Slim\Slim::getInstance()->response();
    $request = \Slim\Slim::getInstance()->request();
    $PK_CODIGO_COMP = $_POST['PK_CODIGO_COMP'];
    /** BUSCA SI EXISTE EL CLIENTE */
    $sql = "SELECT 1 FROM USUARIO WHERE IDENTIFICACION_USUARIO= :identificacion";
    $valoresSql = array(":identificacion" => $_POST['IDENTIFICACION_USUARIO']);
    $db = new Conexion();
    $resultado = $db->consultarUnico($sql, $valoresSql);
    if (!$resultado->isError()) {
        if ($resultado->getDatos() == null) {
            $columnas = array(
                "IDENTIFICACION_USUARIO" => $_POST['IDENTIFICACION_USUARIO'],
                "CLAVE_USUARIO" => $_POST['IDENTIFICACION_USUARIO'],
                "NOMBRE_USUARIO" => $_POST['NOMBRE_USUARIO'],
                "CORREO_USUARIO" => $_POST['CORREO_USUARIO'],
                "REGISTRADO_USUARIO" => false,
                "CODIGO_ESTADO" => $_POST['CODIGO_ESTADO'],
                "DIRECCION_USUARIO" => $_POST['DIRECCION_USUARIO']
            );
            $resultado = $db->insertar("USUARIO", $columnas);
        }
        /** BUSCA SI EXISTE EL COMPROBANTE */
        $sql = "SELECT 1 FROM COMPROBANTE WHERE PK_CODIGO_COMP= :PK_CODIGO_COMP";
        $valoresSql = array(":PK_CODIGO_COMP" => $PK_CODIGO_COMP);
        $resultado = $db->consultarUnico($sql, $valoresSql);
        if ($resultado->getDatos() == null) {
            $columnas = array(
                "PK_CODIGO_COMP" => $PK_CODIGO_COMP,
                "ESTADO" => $_POST['ESTADO'],
                "CLAVE_ACCESO" => $_POST['CLAVE_ACCESO'],
                "SECUENCIAL" => $_POST['SECUENCIAL'],
                "CODIGO_DOCUMENTO" => $_POST['CODIGO_DOCUMENTO'],
                "CLIENTE" => $_POST['NOMBRE_USUARIO'],
                "IDENTIFICACION" => $_POST['IDENTIFICACION_USUARIO'],
                "ENVIADO" => false,
                "FECHA_EMISION" => $_POST['FECHA_EMISION'],
                "NUM_AUTORIZACION" => $_POST['NUM_AUTORIZACION'],
                "FECHA_AUTORIZACION" => $_POST['FECHA_AUTORIZACION'],
                "ESTABLECIM" => $_POST['ESTABLECIM'],
                "PTO_EMISION" => $_POST['PTO_EMISION'],
                "TOTAL" => $_POST['TOTAL'],
                "CORREO_DOCUMENTO" => $_POST['CORREO_DOCUMENTO'],
                "CODIGO_EMPR" => $_POST['CODIGO_EMPR']
            );
            $resultado = $db->insertar("COMPROBANTE", $columnas);
        }
        /** BUSCA SI EXISTE RIDE */
        $fp = fopen($_FILES['pdf']["tmp_name"], 'r');
        $fileContent = fread($fp, filesize($_FILES['pdf']["tmp_name"]));
        $fileContent = $fileContent;
        fclose($fp);
        $sql = "SELECT 1 FROM RIDE_COMPROBANTE WHERE PK_CODIGO_COMP= :PK_CODIGO_COMP";
        $valoresSql = array(":PK_CODIGO_COMP" => $PK_CODIGO_COMP);
        $resultado = $db->consultarUnico($sql, $valoresSql);
        if ($resultado->getDatos() == null) {
            $columnas = array(
                "PK_CODIGO_COMP" => $PK_CODIGO_COMP,
                "ARCHIVO_PDF" => $fileContent,
                "ARCHIVO_XML" => base64_decode($_POST['ARCHIVO_XML'])
            );
            $resultado = $db->insertar("RIDE_COMPROBANTE", $columnas);
        }
    } else {
        $respuesta['error'] = true;
        $respuesta['status'] = "error";
        $respuesta['mensaje'] = $resultado->getMensaje();
    }
    echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
}

function subirDetalleComprobante() {
    $response = \Slim\Slim::getInstance()->response();
    $request = \Slim\Slim::getInstance()->request();

    $parametros = json_decode($request->getBody());

    $PK_CODIGO_COMP = $parametros->PK_CODIGO_COMP;
    $columnas = array(
        "PK_CODIGO_COMP" => $PK_CODIGO_COMP,
        "CODIGO_AUXILIAR" => $parametros->CODIGO_AUXILIAR,
        "CODIGO_PRINCIPAL" => $parametros->CODIGO_PRINCIPAL,
        "DESCRIPCION" => $parametros->DESCRIPCION,
        "PORCENTAJE_IVA" => $parametros->PORCENTAJE_IVA,
        "CANTIDAD" => $parametros->CANTIDAD,
        "PRECIO" => $parametros->PRECIO,
        "DESCUENTO" => $parametros->DESCUENTO,
        "TOTAL" => $parametros->TOTAL
    );
    $db = new Conexion();
    $resultado = $db->insertar("DETALLE_COMPROBANTE", $columnas);
}

?>