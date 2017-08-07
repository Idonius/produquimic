<?php

session_start();

class Util {

    /**
     * Valida que una petición sea Autorizada
     * @param type $_response
     * @return boolean
     */
    public static function isAutorizado($_response) {
        if (Util::isToken($_response)) {
            $respuesta = array();
            if (isset($_SESSION['CODIGO_USUARIO'])) {
                $_response->header('Authorization', 'AccesToken ');
                return true;
            }
            $_response->header('Content-Type', 'application/json;charset=UTF-8');
            $_response->setStatus(401);
            $respuesta['error'] = true;
            $respuesta['status'] = "error";
            $respuesta['mensaje'] = 'Usuario no autorizado';
            echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
        }
        return false;
    }

    /**
     * Valida si una petición contenga token en la cabecera
     * @param type $_response
     * @return boolean
     */
    public static function isToken($_response) {
        $request = \Slim\Slim::getInstance()->request();
        if ($request->headers("Authorization")) {
            if (Util::startsWith($request->headers("Authorization"), "AccesToken")) {
                return true;
            }
        }
        $_response->header('Content-Type', 'application/json;charset=UTF-8');
        $_response->setStatus(401);
        $respuesta = array();
        $respuesta['error'] = true;
        $respuesta['status'] = "error";
        $respuesta['mensaje'] = 'Token no válido';
        echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
        return false;
    }

    /**
     * Valida el resultado de una peticion en formato JSON
     * @param type $_response
     * @param type $_resultado
     */
    public static function validarResultado($_response, $_resultado) {
        $respuesta = array();
        $_response->header('Content-Type', 'application/json;charset=UTF-8');
        $respuesta['error'] = $_resultado->isError();
        if ($_resultado->isError() == true) {
            $_response->setStatus(400);
            $respuesta['status'] = "error";
        }
        $_response->header('Authorization', 'AccesToken ');
        $respuesta['mensaje'] = $_resultado->getMensaje();
        if ($_resultado->getDatos() != null) {
            $respuesta['datos'] = $_resultado->getDatos();
        }
        echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
    }

    /**
     * Funcion que retorna si una cadena comienza con un  texto determinado
     * @param String $_cadena
     * @param String $_busca
     * @return type
     */
    public static function startsWith($_cadena, $_busca) {
        $length = strlen($_busca);
        return (substr($_cadena, 0, $length) === $_busca);
    }

}

?>
