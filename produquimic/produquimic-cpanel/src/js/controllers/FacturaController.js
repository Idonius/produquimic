angular.module('jdframework').controller('FacturaController', ['$scope', '$http', FacturaController]);

function FacturaController($scope, $http) {    
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
    $http.get('framework/models/ComprobanteModel.php/getFacturas').success(function(data) {
        $scope.gridOptions.data = data;
    });
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
        width: "50%"          
    }, {
        field: 'PDF',
        displayName: 'PDF',
        width: "5%", 
        enableSorting: false,
        cellTemplate:  '<div  class="grid-action-cell" align="center">'+
                       '<a ng-click="grid.appScope.foo()" ><img src="img/im_pdf.png"/></a></div>'
    }, {
        field: 'XML',
        displayName: 'XML',
        width: "5%",
        enableSorting: false,
        cellTemplate:  '<div class="grid-action-cell" align="center">'+
                       '<a ng-click="grid.appScope.getXML(row)" ><img src="img/im_xml.png"/></a></div>'
    }];
    
    
    $scope.getXML = function(row) {  
    alert (row.entity.PK_CODIGO_COMP);
    
    $http.get('framework/models/ComprobanteModel.php/getXML').success(function(data) {
         angular.extend(_this, data);
         defer.resolve();
    });
    
    }; 
    
    $scope.foo = function() {

    alert('');
    };
    
    
}