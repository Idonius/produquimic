app.controller('ComprobanteCtrl', function($scope, $routeParams, $location, $http, Utilitario) {
    $scope.tipoComprobante = $routeParams.TipoComprobante;
    $scope.fechaMaxima = new Date();
    $scope.fechaHasta = $scope.fechaMaxima;
    $scope.fechaDesde = new Date();
    $scope.fechaDesde.setDate($scope.fechaDesde.getDate() - 365);


    $scope.getXML = function(row) {
        $http.get('framework/servicios/ServicioComprobante.php/getXML/' + row.PK_CODIGO_COMP, {
            responseType: 'arraybuffer'
        }).success(function(data) {

            var file = new Blob([data], {
                type: 'text/xml'
            });
            var fileURL = URL.createObjectURL(file);
            var fileName = row.NUM_AUTORIZACION + ".xml";
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.href = fileURL;
            a.download = fileName;
            a.click();
        });
    };


    $scope.getPDF = function(row) {
        $http.get('framework/servicios/ServicioComprobante.php/getPDF/' + row.PK_CODIGO_COMP, {
            responseType: 'arraybuffer'
        }).success(function(data) {
            var file = new Blob([data], {
                type: 'application/pdf'
            });
            var fileURL = URL.createObjectURL(file);
            var fileName = row.NUM_AUTORIZACION + ".pdf";
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.href = fileURL;
            a.download = fileName;
            a.click();
        });
    };

    $scope.buscar = function() {
        $scope.listaComprobantes = [];
        if ((Date.parse($scope.fechaDesde)) > (Date.parse($scope.fechaHasta))) {
            Utilitario.agregarMensajeError('La Fecha Desde no puede ser mayor que la Fecha Hasta');
        } else {
            var fechaD = $scope.fechaDesde.getFullYear() + '-' + ($scope.fechaDesde.getMonth() + 1) + '-' + $scope.fechaDesde.getDate();
            var fechaH = $scope.fechaHasta.getFullYear() + '-' + ($scope.fechaHasta.getMonth() + 1) + '-' + $scope.fechaHasta.getDate();
            if ($scope.tipoComprobante == "Facturas") {
                Utilitario.consumirWebService('framework/servicios/ServicioComprobante.php/getFacturas/' + fechaD + '/' + fechaH,
                    null).then(function(data) {
                    if (data.datos) {
                        $scope.listaComprobantes = data.datos;
                    }
                });
            } else if ($scope.tipoComprobante == "Notas de Crédito") {
                Utilitario.consumirWebService('framework/servicios/ServicioComprobante.php/getNotasCredito/' + fechaD + '/' + fechaH,
                    null).then(function(data) {
                    if (data.datos) {
                        $scope.listaComprobantes = data.datos;
                    }
                });
            } else if ($scope.tipoComprobante == "Notas de Débito") {
                Utilitario.consumirWebService('framework/servicios/ServicioComprobante.php/getNotasDebito/' + fechaD + '/' + fechaH,
                    null).then(function(data) {
                    if (data.datos) {
                        $scope.listaComprobantes = data.datos;
                    }
                });
            } else if ($scope.tipoComprobante == "Comprobantes de Retención") {
                Utilitario.consumirWebService('framework/servicios/ServicioComprobante.php/getRetenciones/' + fechaD + '/' + fechaH,
                    null).then(function(data) {
                    if (data.datos) {
                        $scope.listaComprobantes = data.datos;
                    }
                });
            }
        }
    };

});