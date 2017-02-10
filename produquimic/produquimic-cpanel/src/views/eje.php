<?php
echo "<p>Thanks for using gulp-connect-php :) DFJG</p>";

include ("../framework/conexion/Conexion.php");
$sql = 'SELECT *,IDE_OPCI as alias from SIS_OPCION';
$db1 = new Conexion();
$consulta = $db1->consultar($sql);
echo $consulta->getDatos();

echo "<p>TABLA</p>";
include ("../framework/componentes/Tabla.php");
$tab = new Tabla("tabla1");
$tab->setTabla("SIS_OPCION", "IDE_OPCI", "");
$tab->ejecutar();
echo $tab->getFilas();
echo $tab->getColumna("PATH_OPCI")->getTipo();
echo $tab->getColumna("PATH_OPCI")->getOrden();

/*
$app = new \Slim\Slim();
$app = \Slim\Slim::getInstance();
$db = new Conexion();
 
// Products
$app->get('/products', function() {
global $db;
$rows = $db->consultar('SELECT * from SIS_OPCION');
echoResponse(200, $rows);
});
 


*/
?>

