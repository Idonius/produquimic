<?php

require '../lib/Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once("../correo/EnviarCorreo.php");
include_once("Util.php");
\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

$app->post('/getDatosUsuario', 'getDatosUsuario');
$app->post('/actualizarUsuario', 'actualizarUsuario');

$app->run();

function getDatosUsuario() {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $request = \Slim\Slim::getInstance()->request();
        $param = json_decode($request->getBody());
        $sql = "SELECT CODIGO_USUARIO, REGISTRADO_USUARIO, NOMBRE_USUARIO, IDENTIFICACION_USUARIO, CORREO_USUARIO,  TELEFONO_USUARIO, CONTACTO_USUARIO, DIRECCION_USUARIO,CODIGO_ESTADO FROM USUARIO WHERE IDENTIFICACION_USUARIO= :identificacion";
        $valoresSql = array(":identificacion" => $param->identificacion);
        $db = new Conexion();
        $resultado = $db->consultarUnico($sql, $valoresSql);
        Util::validarResultado($response, $resultado);
    }
}

function actualizarUsuario() {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $request = \Slim\Slim::getInstance()->request();
        $param = json_decode($request->getBody());
        $columnas = array("NOMBRE_USUARIO" => $param->NOMBRE_USUARIO,
            "CORREO_USUARIO" => $param->CORREO_USUARIO,
            "TELEFONO_USUARIO" => $param->TELEFONO_USUARIO,
            "CONTACTO_USUARIO" => $param->CONTACTO_USUARIO,
            "REGISTRADO_USUARIO" => $param->REGISTRADO_USUARIO,
            "CODIGO_ESTADO" => $param->CODIGO_ESTADO,
            "DIRECCION_USUARIO" => $param->DIRECCION_USUARIO);
        $condiciones = array("CODIGO_USUARIO" => $param->CODIGO_USUARIO);
        $db = new Conexion();
        $resultado = $db->actualizar("USUARIO", $columnas, $condiciones);
        //Envia correo electronico de registro o cambio de clave
        if ($param->CODIGO_ESTADO == "5") { //USUARIO REGISTRADO
            $correo = new EnviarCorreo();
            $correo->enviarRegistro($param);
        }

        Util::validarResultado($response, $resultado);
    }
}

?>