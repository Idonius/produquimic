app.controller('SeguridadCtrl', function ($scope, $rootScope, $routeParams, $location, $http, toaster,ServicioAutenticacion) {
  $scope.doLogin = function () {
    ServicioAutenticacion.login($scope.usuario, $scope.password, function(response) {                    
      if(response.error) {        
        toaster.pop(response.status,"", response.mensaje, 10000, 'trustedHtml');
        $location.path('login');
      } else {
        ServicioAutenticacion.generarToken($scope.usuario, $scope.password, response);
        $location.path('/principal');          
      }
    });
  };
});