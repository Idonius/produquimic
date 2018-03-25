<?php

require '../lib/Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once("Util.php");

\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

$app->post('/getEmpresas', 'getEmpresas');
$app->post('/getActividades', 'getActividades');

$app->post('/actualizarEmpresa', 'actualizarEmpresa');

$app->post('/getEmpresasActualizadas', 'getEmpresasActualizadas');

$app->run();

function getEmpresas() {	
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $request = \Slim\Slim::getInstance()->request();
        $param = json_decode($request->getBody());        
        $sql = "SELECT `CODIGO_EM`,`RUC_EM`,`RAZON_SOCIAL_EM`,`NOMBRE_COMERCIAL_EM`,`ACTIVIDAD_ECONOMICA_EM`,`PARROQUIA_EM`,`TELEFONO1_EM`,`TELEFONO2_EM`,`MAIL_EM`,`LLAMA_EM`,`CALLE_PRINCIPAL_EM`,`CALLE_SECUNDARIA_EM`,`NUMERO_EM`,`HORARIO_ATENCION_EM`,`REFERENCIA_EM`,`OBSERVACIONES_LLAMA_EM`,`FECHA_LLAMA_EM`,`COTIZA_EM`,`COMPRA_EM` FROM `EMPRESAS_MUNI` WHERE `ACTIVIDAD_ECONOMICA_EM` like :actividad
			 and (`RAZON_SOCIAL_EM` like :empresa1 or `NOMBRE_COMERCIAL_EM` like :empresa2)";
        $valoresSql = array(":actividad" => "%".$param->actividad."%",
        ":empresa1" => "%".$param->empresa."%",
        ":empresa2" => "%".$param->empresa."%");
        $db = new Conexion();
        $resultado = $db->consultar($sql, $valoresSql);
        Util::validarResultado($response, $resultado);
    }
}

function getActividades() {	
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $request = \Slim\Slim::getInstance()->request(); 
        $param = json_decode($request->getBody());          
        $sql = "select ACTIVIDAD_ECONOMICA_EM,count(1) as NUMERO from EMPRESAS_MUNI WHERE ACTIVIDAD_ECONOMICA_EM LIKE :actividad GROUP BY ACTIVIDAD_ECONOMICA_EM order by  ACTIVIDAD_ECONOMICA_EM "; 
        $valoresSql = array(":actividad" => "%".$param->actividad."%");       
        $db = new Conexion();
        $resultado = $db->consultar($sql, $valoresSql);
        Util::validarResultado($response, $resultado);
    }
}

function actualizarEmpresa() {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $request = \Slim\Slim::getInstance()->request();
        $param = json_decode($request->getBody());
        $columnas = array("LLAMA_EM" => $param->LLAMA_EM,
            "FECHA_LLAMA_EM" => $param->FECHA_LLAMA_EM,
            "COTIZA_EM" => $param->COTIZA_EM,
            "COMPRA_EM" => $param->COMPRA_EM,
            "OBSERVACIONES_LLAMA_EM" => $param->OBSERVACIONES_LLAMA_EM);
        $condiciones = array("CODIGO_EM" => $param->CODIGO_EM);
        $db = new Conexion();
        $resultado = $db->actualizar("EMPRESAS_MUNI", $columnas, $condiciones);
        Util::validarResultado($response, $resultado);
    }
}


function getEmpresasActualizadas() {	
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $request = \Slim\Slim::getInstance()->request();             
        $sql = "SELECT `CODIGO_EM`,`RUC_EM`,`RAZON_SOCIAL_EM`,`NOMBRE_COMERCIAL_EM`,`ACTIVIDAD_ECONOMICA_EM`,`PARROQUIA_EM`,`TELEFONO1_EM`,`TELEFONO2_EM`,`MAIL_EM`,`LLAMA_EM`,`CALLE_PRINCIPAL_EM`,`CALLE_SECUNDARIA_EM`,`NUMERO_EM`,`HORARIO_ATENCION_EM`,`REFERENCIA_EM`,`OBSERVACIONES_LLAMA_EM`,`FECHA_LLAMA_EM`,`COTIZA_EM`,`COMPRA_EM` FROM `EMPRESAS_MUNI` WHERE FECHA_LLAMA_EM is not null order by FECHA_LLAMA_EM desc";       
        $db = new Conexion();
        $resultado = $db->consultar($sql);
        Util::validarResultado($response, $resultado);
    }
}

?>