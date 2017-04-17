app.controller('PrincipalCtrl', ['$scope', '$http', 'Utilitario', PrincipalCtrl]);

function PrincipalCtrl($scope, $http, Utilitario) {

    $scope.tipoComprobante = "Factura";
    $scope.fechaMaxima = new Date();
    $scope.fechaHasta = $scope.fechaMaxima;
    $scope.fechaDesde = new Date();
    $scope.fechaDesde.setDate($scope.fechaDesde.getDate() - 365);

    $scope.listaTipoComprobante = [
        "Factura",
        "Nota de Crédito",
        "Retención"
    ];

    $scope.gridOptions = {
        enableFiltering: false,
        flatEntityAccess: true,
        showGridFooter: true,
        paginationPageSizes: [25, 50, 75],
        paginationPageSize: 25,
        fastWatch: true,
        enableColumnMenus: false,
        enableRowSelection: false,
        enableRowHeaderSelection: false
    };
    //Seleccion simple
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.modifierKeysToMultiSelect = false;
    $scope.gridOptions.noUnselect = true;
    $scope.gridOptions.onRegisterApi = function(gridApi) {
        $scope.gridApi = gridApi;
    };

    $scope.gridOptions.columnDefs = [{
        field: 'FECHA_EMISION',
        displayName: 'FECHA EMISION',
        width: "15%"
    }, {
        field: 'SECUENCIAL',
        displayName: 'NUM. FACTURA',
        width: "25%"
    }, {
        field: 'NUM_AUTORIZACION',
        displayName: 'NUM. AUTORIZACION',
        width: "40%"
    }, {
        field: 'TOTAL',
        displayName: 'TOTAL',
        cellFilter: 'currency',
        width: "10%"
    }, {
        field: 'PDF',
        displayName: 'PDF',
        width: "5%",
        enableSorting: false,
        cellTemplate: '<div  class="grid-action-cell" align="center">' +
            '<a ng-click="grid.appScope.getPDF(row)"  target="_blank"><img src="img/im_pdf.png"/></a></div>'
    }, {
        field: 'XML',
        displayName: 'XML',
        width: "5%",
        enableSorting: false,
        cellTemplate: '<div class="grid-action-cell" align="center">' +
            '<a ng-click="grid.appScope.getXML(row)"  target="_blank"><img src="img/im_xml.png"/></a></div>'
    }];


    $scope.getXML = function(row) {
        $http.get('framework/servicios/ServicioComprobante.php/getXML/' + row.entity.PK_CODIGO_COMP, {
            responseType: 'arraybuffer'
        }).success(function(data) {
            var file = new Blob([data], {
                type: 'text/xml'
            });
            var fileURL = URL.createObjectURL(file);
            var fileName = row.entity.NUM_AUTORIZACION + ".xml";
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.href = fileURL;
            a.download = fileName;
            a.click();
        });
    };


    $scope.getPDF = function(row) {
        $http.get('framework/servicios/ServicioComprobante.php/getPDF/' + row.entity.PK_CODIGO_COMP, {
            responseType: 'arraybuffer'
        }).success(function(data) {
            var file = new Blob([data], {
                type: 'application/pdf'
            });
            var fileURL = URL.createObjectURL(file);
            var fileName = row.entity.NUM_AUTORIZACION + ".pdf";
            var a = document.createElement("a");
            document.body.appendChild(a);
            a.href = fileURL;
            a.download = fileName;
            a.click();
        });
    };

    $scope.buscar = function() {
        if ((Date.parse($scope.fechaDesde)) > (Date.parse($scope.fechaHasta))) {
            Utilitario.agregarMensajeError('La Fecha Desde no puede ser mayor que la Fecha Hasta');
        } else {
            var fechaD = $scope.fechaDesde.getFullYear() + '-' + ($scope.fechaDesde.getMonth() + 1) + '-' + $scope.fechaDesde.getDate();
            var fechaH = $scope.fechaHasta.getFullYear() + '-' + ($scope.fechaHasta.getMonth() + 1) + '-' + $scope.fechaHasta.getDate();
            if ($scope.tipoComprobante == "Factura") {

                Utilitario.consumirWebService('framework/servicios/ServicioComprobante.php/getFacturas/' + fechaD + '/' + fechaH,
                    null).then(function(data) {
                    if (data.datos) {
                        $scope.gridOptions.data = data.datos;
                    }
                });


            } else if ($scope.tipoComprobante == "Nota de Crédito") {
                $http.get('framework/servicios/ServicioComprobante.php/getNotasCredito/' + fechaD + '/' + fechaH).success(function(data) {
                    $scope.gridOptions.data = data.datos;
                });
            } else if ($scope.tipoComprobante == "Retención") {
                $http.get('framework/servicios/ServicioComprobante.php/getRetenciones/' + fechaD + '/' + fechaH).success(function(data) {
                    $scope.gridOptions.data = data.datos;
                });
            }

        }
    };


}