<?php
$conexion=mysqli_connect("localhost", "root", "DIEGO", "ceo");
$sql = "select ARCHIVO_XML FROM RIDE_COMPROBANTE WHERE PK_CODIGO_COMP=10";
$resultado = mysqli_query($conexion,$sql );
$row=mysqli_fetch_row($resultado);		
$file = $row[0];
header('Content-disposition: attachment; filename=advertise.xml');
header ("Content-Type:text/xml"); 
//output the XML data
echo  $file;
?>