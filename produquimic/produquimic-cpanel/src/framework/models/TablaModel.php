<?php
require '../../Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once("Util.php");
\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

$app->get('/getTablas', 'getTablas');

$app->run();


function getTablas(){
	$response = \Slim\Slim::getInstance()->response();
	if(Util::isAutorizado($response)){
		$sql = "SELECT * FROM COMPROBANTE";	
		$db = new Conexion();
		$resultado = $db->consultar($sql);
		Util::validarResultado($response,$resultado);
	}
}



?>