  <?php
  class Util
  {
  	public static function isAutorizado($response){        
      if (!isset($_SESSION)) {
        session_start();
      }
      $respuesta = array();      
      if (isset($_SESSION['CODIGO_USUARIO'])) {
       return true;
     }
     else{
       $response->header('Content-Type', 'text/html;charset=utf-8');
       $response->setStatus(401);  			
       $respuesta['error']=true;
       $respuesta['status'] = "error";
       $respuesta['mensaje'] = 'Usuario no autorizado';
       echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);  			
       return false;
     }
     return true;
   }

   public static function validarResultado($response,$resultado){
    $respuesta = array();
    $response->header('Content-Type', 'application/json;charset=utf-8');
    $respuesta['error']=$resultado->isError();
    if($resultado->isError() == true){  			
     $response->setStatus(400);
     $respuesta['status'] = "error";	
   }
   $respuesta['mensaje'] = $resultado->getMensaje();

   if ($resultado->getDatos() != null) {         
     $respuesta['datos'] =  $resultado->getDatos();
   }
   echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);  		
 }	
}
?>
