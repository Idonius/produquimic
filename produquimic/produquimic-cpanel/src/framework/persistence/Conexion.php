  <?php
  include_once ("ResultadoSQL.php");
  class Conexion
  {

    private $db;
    
    public function __construct() {        
        $this->db =  new PDO('mysql:host=localhost;dbname=ceo', 'root', 'DIEGO');
        /*$this->db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);   */     
        /*,  PDO::ERRMODE_WARNING*/
    }

    public function __destruct() {
     $this->db = null;
    }

    
    /**
     * Realiza una consulta SQL a la base de datos
     * @param  String  $_sql       [sentencia SQL]
     * @param  boolean $_json      [obtener resultado en formato JSON]     
     * @return resultadoSQL        [resultado de la consulta]
     */
    public function consultar($_sql, $_json = true) {
        $resultadoSQL = new resultadoSQL();
        try {
            $st = $this->db->query($_sql);
            if ($_json) {
                $resultadoSQL->setDatos(json_encode($st->fetchAll(PDO::FETCH_CLASS)));
            } 
            else {
                $resultadoSQL->setDatos($st->fetchAll(PDO::FETCH_CLASS));
            }
        }
        catch(PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al consultar: " . $_sql . " ==> " . $e->getMessage());
        }
        return $resultadoSQL;
    }
    /**
     * Ejecuta una sentencia SQL a la base de datos
     * @param  String $_sql     [sentencia SQL]
     * @return resultadoSQL     [resultado de la ejecución]
     */
    public function ejecutar($_sql) {
        $resultadoSQL = new resultadoSQL();
        try {
            $stmt = $this->db->exec($_sql);
        }
        catch(PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al ejecutar: " . $_sql . " ==> " . $e->getMessage());
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
        try{
            $a = array();
            $c = "";
            $v = "";
            foreach ($_columnas as $key => $value) {
                $c .= $key. ", ";
                $v .= ":".$key. ", ";
                $a[":".$key] = $value;
            }
            $c = rtrim($c,', ');
            $v = rtrim($v,', ');            
            $stmt = $this->db->prepare("INSERT INTO $_tabla($c) VALUES($v)");
            $stmt->execute($a);
            $resultadoSQL->setMensaje($this->db->lastInsertId()); /*Retorna el id generado*/
        }catch(PDOException $e){
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al insertar en la tabla " . $_tabla . ": ==> " . $e->getMessage());
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
    public function actualizar($_tabla, $_columnas,$_condiciones=array()) {
        $resultadoSQL = new resultadoSQL();
        try {
            $a = array();
            $w = "";
            $c = "";
            foreach ($_condiciones as $key => $value) {
                $w .= " and " .$key. " = :".$key;
                $a[":".$key] = $value;
            }
            foreach ($_columnas as $key => $value) {
                $c .= $key. " = :".$key.", ";
                $a[":".$key] = $value;
            }
            $c = rtrim($c,", ");

            $stmt = $this->db->prepare("UPDATE $_tabla SET $c WHERE 1=1 ".$w);
            $stmt->execute($a);            
            $resultadoSQL->setMensaje("Filas Actualizadas ".$stmt->rowCount());
        }
        catch(PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al actualizar en la tabla " . $_tabla . ": ==> " . $e->getMessage());
        }
        return $resultadoSQL;
    }


     /**
     * Elimina registros en la base de datos
     * @param  String $_tabla    [nombre de la tabla de la base de datos]     
     * @param  array  $_condiciones [condicion de la sentencia es un arreglo asociativo con nombre de la columna y valor]
     * @return resultadoSQL      [resultado de la ejecución]
     */
     public function eliminar($_tabla, $_condiciones=array()) {
        $resultadoSQL = new resultadoSQL();
        try {
            $a = array();
            $w = "";            
            foreach ($_condiciones as $key => $value) {
                $w .= " and " .$key. " = :".$key;
                $a[":".$key] = $value;
            }
            $stmt = $this->db->prepare("DELETE FROM $_tabla  WHERE 1=1 ".$w);
            $stmt->execute($a);            
            $resultadoSQL->setMensaje("Filas Eliminadas ".$stmt->rowCount());
        }
        catch(PDOException $e) {
            $resultadoSQL->setError(true);
            $resultadoSQL->setMensaje("Error al eliminar en la tabla " . $_tabla . ": ==> " . $e->getMessage());
        }
        return $resultadoSQL;
    }

}
?>