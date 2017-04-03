<?php
require '../../Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once("Util.php");
\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

$app->post('/getDatosUsuario/:IDENTIFICACION', 'getDatosUsuario');

$app->post('/actualizarUsuario', 'actualizarUsuario');


$app->run();


function getDatosUsuario($IDENTIFICACION){
	$response = \Slim\Slim::getInstance()->response();	
		$sql = "SELECT CODIGO_USUARIO, REGISTRADO_USUARIO, NOMBRE_USUARIO, IDENTIFICACION_USUARIO, CORREO_USUARIO,  TELEFONO_USUARIO, CONTACTO_USUARIO, DIRECCION_USUARIO FROM USUARIO WHERE IDENTIFICACION_USUARIO='".$IDENTIFICACION."'";	
		$db = new Conexion();
		$resultado = $db->consultar($sql);
	    Util::validarResultado($response,$resultado);
}


function actualizarUsuario() {
	$response = \Slim\Slim::getInstance()->response();	
	$request = \Slim\Slim::getInstance()->request();		
	$usuario = json_decode($request->getBody());	
	$columnas=array("NOMBRE_USUARIO" =>  $usuario->NOMBRE_USUARIO,
		"CORREO_USUARIO" => $usuario->CORREO_USUARIO,
		"TELEFONO_USUARIO" => $usuario->TELEFONO_USUARIO,
		"CONTACTO_USUARIO" => $usuario->CONTACTO_USUARIO,
		"REGISTRADO_USUARIO" => $usuario->REGISTRADO_USUARIO,
		"DIRECCION_USUARIO" => $usuario->DIRECCION_USUARIO);	
	$condiciones=array('CODIGO_USUARIO' => $usuario->$CODIGO_USUARIO);
	$db = new Conexion();
	$resultado = $db->actualizar("USUARIO",$columnas,$condiciones);
	Util::validarResultado($response,$resultado);	
}

?>