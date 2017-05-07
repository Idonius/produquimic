app.controller('PrincipalCtrl', ['$scope', 'Utilitario', PrincipalCtrl]);

function PrincipalCtrl($scope, Utilitario) {

    Utilitario.consumirWebService('framework/servicios/ServicioComprobante.php/getUltimosComprobantes',
        null).then(function(data) {
        if (data.datos) {
            $scope.ultimosComprobantes = data.datos;
        }
    });

    $scope.totalComprobantes = {};
    Utilitario.consumirWebService('framework/servicios/ServicioComprobante.php/getTotalComprobantes',
        null).then(function(data) {
        if (data.datos) {
            $scope.totalComprobantes = data.datos;
        }
    });

};