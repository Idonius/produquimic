angular.module('jdframework').controller('TablaController', ['$scope','$http', TablaController]);

function TablaController($scope,$http) {

/**TABLA USUARIOS**/
  $scope.gridTablas = {
   enableFiltering: true,
   flatEntityAccess: true,        
   fastWatch: true,
   enableRowSelection: true,
   enableRowHeaderSelection: false,
   enableColumnMenus: false
 };    
//Seleccion simple
$scope.gridTablas.multiSelect = false;
$scope.gridTablas.modifierKeysToMultiSelect = false;
$scope.gridTablas.noUnselect = true;
$scope.gridApi =null;
$scope.gridTablas.onRegisterApi = function(gridApi) {
  $scope.gridApi =gridApi;
};
//Columnas de la tabla
$scope.gridTablas.columnDefs = [{
  field: 'FECHA_EMISION',
  displayName: 'CODIGO',
  width: "10%" 
}, {
  field: 'ESTADO',
  displayName: 'NOMBRE DE LA TABLA',
  width: "30%" 
}, {
  field: 'NUM_AUTORIZACION',
  displayName: 'DESCRIPCION',
  width: "60%" 
}];
/** FIN TABLA USUARIOS**/

$scope.getTablas= function() {  
  $http.get('/framework/models/TablaModel.php/getTablas').success(function(data) {
    $scope.gridTablas.data =  data;
  });
};

//Carga los datos
$scope.getTablas();

}