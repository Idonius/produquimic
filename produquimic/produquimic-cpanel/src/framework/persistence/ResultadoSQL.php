<?php
class ResultadoSQL
{
    
    /*Boolean*/
    private $error = false;
    
    /*String*/
    private $mensaje;
    
    /*Array*/
    private $datos;

    public function isError() {
        return $this->error;
    }
    
    public function setError($error) {
        $this->error = $error;
    }
    
    public function getMensaje() {
        return $this->mensaje;
    }
    
    public function setMensaje($mensaje) {
        $this->mensaje = $mensaje;
    }
    
    public function getDatos() {
        return $this->datos;
    }
    
    public function setDatos($datos) {
        $this->datos = $datos;
    }    
   
}
?>