<?php

require '../lib/Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once ("../persistence/ResultadoSQL.php");
include_once("Util.php");

\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

$app->post('/validarLogin', 'validarLogin');
$app->post('/cambiarClave', 'cambiarClave');
$app->post('/cerrarSession', 'cerrarSession');

$app->run();

function validarLogin() {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $resultadoSQL = new ResultadoSQL();
        $request = \Slim\Slim::getInstance()->request();
        $usuario = json_decode($request->getBody());
        try {
            $sql = "SELECT CODIGO_USUARIO,IDENTIFICACION_USUARIO,CLAVE_USUARIO,CODIGO_ESTADO FROM USUARIO WHERE IDENTIFICACION_USUARIO = :usuario";
            $valoresSql = array(":usuario" => $usuario->identificacion);
            $db = new Conexion();
            $result = $db->consultarUnico($sql, $valoresSql);
            if ($result->getDatos()) {
                // Valida password
                $password = base64_decode($usuario->clave);
                if ($password != $result->getDatos()->CLAVE_USUARIO) {
                    $resultadoSQL->setError(true);
                    $resultadoSQL->setMensaje("Contraseña incorrecta");
                } else {
                    if (!isset($_SESSION)) {
                        session_start();
                    }
                    $_SESSION['CODIGO_USUARIO'] = $result->getDatos()->CODIGO_USUARIO;
                    $_SESSION['IDENTIFICACION'] = $result->getDatos()->IDENTIFICACION_USUARIO;
                    $resultadoSQL->setMensaje("Acceso Correcto");
                    //Respuesta del servicio
                    $datos = array("CODIGO_ESTADO" => $result->getDatos()->CODIGO_ESTADO,
                        "CODIGO_USUARIO" => $result->getDatos()->CODIGO_USUARIO);
                    $resultadoSQL->setDatos($datos);
                }
            } else {
                $resultadoSQL->setError(true);
                $resultadoSQL->setMensaje("El Usuario no existe");
            }
        } catch (Exception $ex) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al validarLogin ==> " . $ex->getMessage());
        }
        Util::validarResultado($response, $resultadoSQL);
    }
}

function cambiarClave() {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $resultadoSQL = new ResultadoSQL();
        $request = \Slim\Slim::getInstance()->request();
        $param = json_decode($request->getBody());
        try {
            $columnas = array("CLAVE_USUARIO" => base64_decode($param->clave),
                "CODIGO_ESTADO" => 1); //Cambia a ACTIVO
            $condiciones = array("IDENTIFICACION_USUARIO" => $param->identificacion);
            $db = new Conexion();
            $resultadoSQL = $db->actualizar("USUARIO", $columnas, $condiciones);
        } catch (Exception $ex) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al cambiarClave ==> " . $ex->getMessage());
        }
        Util::validarResultado($response, $resultadoSQL);
    }
}

function cerrarSession() {
    $response = \Slim\Slim::getInstance()->response();
    if (Util::isToken($response)) {
        $response->header('Content-Type', 'text/html;charset=utf-8');
        if (!isset($_SESSION)) {
            session_start();
        }
        session_destroy();
        if (isSet($_SESSION['CODIGO_USUARIO'])) {
            unset($_SESSION['CODIGO_USUARIO']);
            unset($_SESSION['IDENTIFICACION_USUARIO']);
        }
    }
}

?>
