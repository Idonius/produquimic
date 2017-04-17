<?php

include_once ("ResultadoSQL.php");

class Conexion {

    private $db;
    private $mensajeError;

    public function __construct() {
        try {
            $this->db = new PDO('mysql:host=localhost;dbname=ceo', 'root', 'DIEGO');
            $this->db->exec("SET CHARACTER SET utf8");
            $this->db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
        } catch (PDOException $e) {
            $this->mensajeError = "Error en la conexión a la base de datos: " . utf8_encode($e->getMessage());
        }
    }

    public function __destruct() {
        $this->db = null;
        $this->mensajeError = null;
    }

    /**
     * Realiza una consulta SQL a la base de datos con parametros para evitar SQL Injection
     * @param  String  $_sql       [sentencia SQL]
     * @param  Array  $_valoresSql [parametros de la consulta SQL]
     * @param  boolean $_json      [obtener resultado en formato JSON]     
     * @return resultadoSQL        [Array resultado de la consulta]
     */
    public function consultar($_sql, $_valoresSql = array(), $_json = false) {
        $resultadoSQL = new resultadoSQL();
        if ($this->isConnected() == false) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje($this->mensajeError);
            return $resultadoSQL;
        }
        try {
            $st = $this->db->prepare($_sql);
            if (isset($_valoresSql)) {
                foreach ($_valoresSql as $key => $value) {
                    $st->bindValue($key, $value);
                }
            }
            $st->execute();
            if ($_json) {
                $resultadoSQL->setDatos(json_encode($st->fetchAll(PDO::FETCH_OBJ), JSON_UNESCAPED_UNICODE));
            } else {
                $resultadoSQL->setDatos($st->fetchAll(PDO::FETCH_OBJ));
            }
        } catch (PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al consultar: " . utf8_encode($e->getMessage()));
        }
        return $resultadoSQL;
    }

    /**
     * Realiza una consulta SQL a la base de datos con parametros para evitar SQL Injection
     * @param  String  $_sql       [sentencia SQL]
     * @param  Array  $_valoresSql [parametros de la consulta SQL]
     * @param  boolean $_json      [obtener resultado en formato JSON]     
     * @return resultadoSQL        [Objeto resultado de la consulta]
     */
    public function consultarUnico($_sql, $_valoresSql = array(), $_json = false) {
        $resultadoSQL = new resultadoSQL();
        if ($this->isConnected() == false) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje($this->mensajeError);
            return $resultadoSQL;
        }
        try {
            $st = $this->db->prepare($_sql);
            if (isset($_valoresSql)) {
                foreach ($_valoresSql as $key => $value) {
                    $st->bindValue($key, $value);
                }
            }

            $st->execute();
            if ($_json) {
                $resultadoSQL->setDatos(json_encode($st->fetch(PDO::FETCH_OBJ), JSON_UNESCAPED_UNICODE));
            } else {
                $resultadoSQL->setDatos($st->fetch(PDO::FETCH_OBJ));
            }
        } catch (PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al consultar: " . utf8_encode($e->getMessage()));
        }
        return $resultadoSQL;
    }

    /**
     * Realiza una consulta SQL a la base de datos sin parametros 
     * @param  String  $_sql       [sentencia SQL]
     * @param  boolean $_json      [obtener resultado en formato JSON]     
     * @return resultadoSQL        [resultado de la consulta]
     */
    public function consultarSQL($_sql, $_json = false) {
        $resultadoSQL = new resultadoSQL();
        if ($this->isConnected() == false) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje($this->mensajeError);
            return $resultadoSQL;
        }
        try {
            $st = $this->db->query($_sql);
            if ($_json) {
                $resultadoSQL->setDatos(json_encode($st->fetchAll(PDO::FETCH_CLASS), JSON_UNESCAPED_UNICODE));
            } else {
                $resultadoSQL->setDatos($st->fetchAll(PDO::FETCH_CLASS));
            }
        } catch (PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al consultar: " . utf8_encode($e->getMessage()));
        }
        return $resultadoSQL;
    }

    /**
     * Ejecuta una sentencia SQL a la base de datos
     * @param  String $_sql     [sentencia SQL]
     * @return resultadoSQL     [resultado de la ejecución]
     */
    public function ejecutar($_sql, $_valoresSql = array()) {
        $resultadoSQL = new resultadoSQL();
        if ($this->isConnected() == false) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje($this->mensajeError);
            return $resultadoSQL;
        }
        try {
            $st = $this->db->prepare($_sql);
            if (isset($_valoresSql)) {
                foreach ($_valoresSql as $key => $value) {
                    $st->bindValue($key, $value);
                }
            }
            $st->execute();
            $resultadoSQL->setMensaje("Registros afectados " . $st->rowCount());
        } catch (PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al ejecutar: " . utf8_encode($e->getMessage()));
        }
        return $resultadoSQL;
    }

    /**
     * Inserta un registro en la base de datos
     * @param  String $_tabla    [nombre de la tabla de la base de datos]
     * @param  array $_columnas  [arreglo asociativo con nombre de la columna y valor]
     * @return resultadoSQL      [resultado de la ejecución]
     */
    public function insertar($_tabla, $_columnas) {
        $resultadoSQL = new resultadoSQL();
        if ($this->isConnected() == false) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje($this->mensajeError);
            return $resultadoSQL;
        }
        try {
            $a = array();
            $c = "";
            $v = "";
            foreach ($_columnas as $key => $value) {
                $c .= $key . ", ";
                $v .= ":" . $key . ", ";
                $a[":" . $key] = $value;
            }
            $c = rtrim($c, ', ');
            $v = rtrim($v, ', ');
            $stmt = $this->db->prepare("INSERT INTO $_tabla($c) VALUES($v)");
            $stmt->execute($a);
            //$resultadoSQL->setMensaje($this->db->lastInsertId()); /*Retorna el id generado*/
            $resultadoSQL->setMensaje("Registros insertados " . $stmt->rowCount());
        } catch (PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al insertar " . utf8_encode($e->getMessage()));
        }
        return $resultadoSQL;
    }

    /**
     * Actualiza registros en la base de datos
     * @param  String $_tabla    [nombre de la tabla de la base de datos]
     * @param  array $_columnas  [arreglo asociativo con nombre de la columna y valor]
     * @param  array  $_condiciones [condicion de la sentencia es un arreglo asociativo con nombre de la columna y valor]
     * @return resultadoSQL      [resultado de la ejecución]
     */
    public function actualizar($_tabla, $_columnas, $_condiciones = array()) {
        $resultadoSQL = new resultadoSQL();
        if ($this->isConnected() == false) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje($this->mensajeError);
            return $resultadoSQL;
        }
        try {
            $a = array();
            $w = "";
            $c = "";
            foreach ($_condiciones as $key => $value) {
                $w .= " and " . $key . " = :" . $key;
                $a[":" . $key] = $value;
            }
            foreach ($_columnas as $key => $value) {
                $c .= $key . " = :" . $key . ", ";
                $a[":" . $key] = $value;
            }
            $c = rtrim($c, ", ");

            $stmt = $this->db->prepare("UPDATE $_tabla SET $c WHERE 1=1 " . $w);
            $stmt->execute($a);
            $resultadoSQL->setMensaje("Registros actualizados " . $stmt->rowCount());
        } catch (PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al actualizar " . utf8_encode($e->getMessage()));
        }
        return $resultadoSQL;
    }

    /**
     * Elimina registros en la base de datos
     * @param  String $_tabla    [nombre de la tabla de la base de datos]     
     * @param  array  $_condiciones [condicion de la sentencia es un arreglo asociativo con nombre de la columna y valor]
     * @return resultadoSQL      [resultado de la ejecución]
     */
    public function eliminar($_tabla, $_condiciones = array()) {
        $resultadoSQL = new resultadoSQL();
        if ($this->isConnected() == false) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje($this->mensajeError);
            return $resultadoSQL;
        }
        try {
            $a = array();
            $w = "";
            foreach ($_condiciones as $key => $value) {
                $w .= " and " . $key . " = :" . $key;
                $a[":" . $key] = $value;
            }
            $stmt = $this->db->prepare("DELETE FROM $_tabla  WHERE 1=1 " . $w);
            $stmt->execute($a);
            $resultadoSQL->setMensaje("Registros eliminados " . $stmt->rowCount());
        } catch (PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al eliminar " . utf8_encode($e->getMessage()));
        }
        return $resultadoSQL;
    }

    /**
     * [getConnection description]
     * @return [type] [description]
     */
    public function getConnection() {
        return $this->db;
    }

    /**
     * [isConnected description]
     * @return boolean [description]
     */
    public function isConnected() {
        if ($this->mensajeError != null) {
            return false;
        } else {
            return true;
        }
    }

}

?>