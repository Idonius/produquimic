<?php
require '../../Slim/Slim.php';
include_once("../persistence/Conexion.php");
include_once ("../persistence/ResultadoSQL.php");
include_once("Util.php");

\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();

$app->post('/validarLogin/:usuario/:password', 'validarLogin');
$app->get('/cerrarSession', 'cerrarSession');

$app->run();

function validarLogin($usuario,$password) {	
	$response = \Slim\Slim::getInstance()->response();
	$resultadoSQL = new ResultadoSQL();
	try {

		$sql = "SELECT * FROM USUARIO WHERE IDENTIFICACION_USUARIO = :usuario";
		$db = new Conexion(); 
		$pdo = $db->getConnection();
		$query = $pdo->prepare($sql);
		$query->bindValue(':usuario', $usuario, PDO::PARAM_STR);		
		$query->execute();
		$result = $query->fetch(PDO::FETCH_OBJ);
		if($result){
			/**Valida password*/
			if($password != $result->CLAVE_USUARIO){
				$resultadoSQL->setError(true);
				$resultadoSQL->setMensaje("Contraseña incorrecta");
			}
			else{
				if (!isset($_SESSION)) {
					session_start();
					$_SESSION['CODIGO_USUARIO'] =  $result->CODIGO_USUARIO;
					$_SESSION['IDENTIFICACION'] = $result->IDENTIFICACION_USUARIO;
					$resultadoSQL->setMensaje("Acceso Correcto");
				}
				$resultadoSQL->setDatos(json_encode($result));	
			}			
		}
		else{
			$resultadoSQL->setError(true);
			$resultadoSQL->setMensaje("El Usuario no existe");
		}	
	}catch(Exception $ex){
		$resultadoSQL->setError(true);
		$resultadoSQL->setMensaje("Error al validarLogin ==> " . $ex->getMessage());
	}	
	Util::validarResultado($response,$resultadoSQL);
}

function cerrarSession(){
	$response = \Slim\Slim::getInstance()->response();
	$response->header('Content-Type', 'text/html;charset=utf-8');	
	if (!isset($_SESSION)) {
		session_start();
	}	
	session_destroy();	
	if(isSet($_SESSION['CODIGO_USUARIO']))   {
		unset($_SESSION['CODIGO_USUARIO']);
		unset($_SESSION['IDENTIFICACION_USUARIO']);        
	}	
}

?>
