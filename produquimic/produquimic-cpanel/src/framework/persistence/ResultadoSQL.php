<?php

class ResultadoSQL {
    /* Boolean */

    private $error = false;

    /* String */
    private $mensaje = 'ok';

    /* Array */
    private $datos;

    public function isError() {
        return $this->error;
    }

    public function setError($_error) {
        $this->error = $_error;
    }

    public function getMensaje() {
        return $this->mensaje;
    }

    public function setMensaje($_mensaje) {
        $this->mensaje = $_mensaje;
    }

    public function getDatos() {
        return $this->datos;
    }

    public function setDatos($_datos) {
        $this->datos = $_datos;
    }

}

?>