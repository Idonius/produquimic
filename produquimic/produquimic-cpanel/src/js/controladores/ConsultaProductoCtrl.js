app.controller('ConsultaProductoCtrl', function($scope, $routeParams, $location, $http, Utilitario) {
  $scope.producto = "";
  $scope.ultimosComprobantes = {};

  $scope.borrar = function() {
    $scope.ultimosComprobantes = {};
    $scope.producto = "";
  };


  $scope.buscar = function() {
    $scope.busqueda = {
      producto: $scope.producto
    };


    Utilitario.consumirWebService('framework/servicios/ServicioComprobante.php/getPreciosProductos',
      ($scope.busqueda)).then(function(data) {
      if (data.datos) {
        $scope.ultimosComprobantes = data.datos;
      } else {
        $scope.ultimosComprobantes = {};
      }

    });

  };



});