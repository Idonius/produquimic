  <?php
  class Util
  {
  	public static function isAutorizado($response){  
      /**
  		session_start();
  		if (isset($_SESSION['USUARIO'])) {
  			return true;
  		}
  		else{
  			$response->setStatus(401);
  			$response->header('Content-Type', 'text/html;charset=utf-8');
  			echo 'Usuario no autorizado';  		
  			return false;
  		}*/
      return true;
  	}

  	public static function validarResultado($response,$resultado){
  		if($resultado->isError() == true){  			
  			$response->setStatus(400);	
  		}


  		if ($resultado->getDatos() != null) {
  			$response->header('Content-Type', 'application/json;charset=utf-8');
  			echo $resultado->getDatos();
  		}
  		else{
  			$response->header('Content-Type', 'text/html;charset=utf-8');
  			echo $resultado->getMensaje();
  		}				
  	}	
  }
?>
