<?php
require '../../Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once("Util.php");
\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

$app->get('/getFacturas', 'getFacturas');

$app->get('/getNotasCredito', 'getNotasCredito');

$app->get('/getNotasDebito', 'getNotasDebito');

$app->get('/getGuias', 'getGuias');

$app->get('/getRetenciones', 'getRetenciones');

$app->get('/getXML', 'getXML');

$app->get('/getPDF/', 'getPDF');

$app->run();


function getFacturas(){
	$response = \Slim\Slim::getInstance()->response();
	if(Util::isAutorizado($response)){
		$sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='01' ORDER BY FECHA_EMISION desc";	
		$db = new Conexion();
		$resultado = $db->consultar($sql);
		Util::validarResultado($response,$resultado);
	}
}

function getNotasCredito(){
	$response = \Slim\Slim::getInstance()->response();
	if(Util::isAutorizado($response)){
		$sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='01' ORDER BY FECHA_EMISION desc";	
		$db = new Conexion();
		$resultado = $db->consultar($sql);
		Util::validarResultado($response,$resultado);
	}
}

function getNotasDebito(){
	$response = \Slim\Slim::getInstance()->response();
	if(Util::isAutorizado($response)){
		$sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='01' ORDER BY FECHA_EMISION desc";	
		$db = new Conexion();
		$resultado = $db->consultar($sql);
		Util::validarResultado($response,$resultado);
	}
}

function getGuias(){
	$response = \Slim\Slim::getInstance()->response();
	if(Util::isAutorizado($response)){
		$sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='01' ORDER BY FECHA_EMISION desc";	
		$db = new Conexion();
		$resultado = $db->consultar($sql);
		Util::validarResultado($response,$resultado);
	}
}

function getRetenciones(){
	$response = \Slim\Slim::getInstance()->response();
	if(Util::isAutorizado($response)){
		$sql = "SELECT PK_CODIGO_COMP,DATE_FORMAT(FECHA_EMISION,'%d/%m/%Y') as FECHA_EMISION,concat(ESTABLECIM , '-' , PTO_EMISION , '-' , SECUENCIAL) as SECUENCIAL,NUM_AUTORIZACION,TOTAL FROM COMPROBANTE WHERE CODIGO_DOCUMENTO='01' ORDER BY FECHA_EMISION desc";	
		$db = new Conexion();
		$resultado = $db->consultar($sql);
		Util::validarResultado($response,$resultado);
	}
}

function getXML() {
		$response = \Slim\Slim::getInstance()->response();
	if(Util::isAutorizado($response)){	    
     	$sql = "select ARCHIVO_XML FROM RIDE_COMPROBANTE WHERE PK_CODIGO_COMP=10";
		$db = new Conexion();
		$resultado = $db->consultar($sql,false);
		$file = $resultado->getDatos()[0]->ARCHIVO_XML;		
		$response->header('Content-Type', 'text/xml');		
		echo  $file; 	
	}
}

function getPDF() {
	$response = \Slim\Slim::getInstance()->response();
	if(Util::isAutorizado($response)){		
		$conexion=mysqli_connect("localhost", "root", "DIEGO", "ceo");			
     	$sql = "select ARCHIVO_PDF FROM RIDE_COMPROBANTE WHERE PK_CODIGO_COMP=10";
		$db = new Conexion();
		$resultado = $db->consultar($sql,false);
		$file = $resultado->getDatos()[0]->ARCHIVO_PDF;			
		$response->header('Content-Type', 'application/pdf;charset=utf-8');		
		echo  $file; 
	}
}

?>