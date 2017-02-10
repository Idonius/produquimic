<?php
$conexion=mysqli_connect("localhost", "root", "DIEGO", "ceo");
$sql = "select ARCHIVO_PDF FROM RIDE_COMPROBANTE WHERE PK_CODIGO_COMP=10";
$resultado = mysqli_query($conexion,$sql );
	
$row=mysqli_fetch_row($resultado);		
$file = $row[0];
//echo $file;

header('Content-disposition: attachment; filename=advertise.pdf');
header ("Content-Type:application/pdf"); 
//output the XML data
echo  $file; 
		

?>