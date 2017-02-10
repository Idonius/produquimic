/**
 * Master Controller
 */
angular.module('jdframework').controller('MasterCtrl', ['$scope', MasterCtrl]);

function MasterCtrl($scope) {
    $scope.templates = [{
        nombre: 'Principal',
        url: '#/dashboard',
        icono: 'menu-icon fa fa-home'
    }, {
        nombre: 'Facturas',
        url: '#/facturas',
        icono: 'menu-icon fa fa-file-text-o'
    }, {
        nombre: 'Notas de Crédito',
        url: '#/notasCredito',
        icono: 'menu-icon fa fa-file-text-o'
    }, {
        nombre: 'Guias de Remisión',
        url: '#/guias',
        icono: 'menu-icon fa fa-file-text-o'
    }, {
        nombre: 'Notas de Débito',
        url: '#/notasDedito',
        icono: 'menu-icon fa fa-file-text-o'
    }, {
        nombre: 'Preguntas Frecuentes',
        url: '#/preguntas',
        icono: 'menu-icon fa fa-question'
    }, {
        nombre: 'Perfil',
        url: '#/perfil',
        icono: 'menu-icon fa fa-user'
    }, {
        nombre: 'Cambiar Contraseña',
        url: '#/cambiarClave',
        icono: 'menu-icon fa fa-key'
    }, {
        nombre: 'Salir',
        url: '#/salir',
        icono: 'menu-icon fa fa-power-off'
    }];
    $scope.linkClicked = function(op) {
        // your code here
        $scope.template = op;
    };
}
angular.module('jdframework').controller('Mycontroller', ['$scope', '$http', Mycontroller]);

function Mycontroller($scope, $http) {
    $scope.message = "FACTURAS ELECTRÓNICAS";
    $scope.gridOptions = {
        enableFiltering: false,
        flatEntityAccess: true,
        showGridFooter: true,
        paginationPageSizes: [25, 50, 75],
        paginationPageSize: 5,
        fastWatch: true,
        enableRowSelection: true,
        enableRowHeaderSelection: false
    };    
    //Seleccion simple
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.modifierKeysToMultiSelect = false;
    $scope.gridOptions.noUnselect = true;
    $scope.gridOptions.onRegisterApi = function(gridApi) {
        $scope.gridApi = gridApi;
    };
    /*  
 $scope.gridOptions.data = [{name: "Moroni", age: 50},
                     {name: "Tiancum", age: 43},
                     {name: "Jacob", age: 27},
                     {name: "Nephi", age: 29},
                     {name: "Enos", age: 34}];
*/
    $http.get('framework/models/TablaModel.php/getTablas').success(function(data) {
        $scope.gridOptions.data = data;
    });
    $scope.gridOptions.columnDefs = [{
        field: 'FECHA_EMISION',
        displayName: 'FECHA'
        
    }, {
        field: 'NUM_AUTORIZACION',
        displayName: 'NUM'
    }];
}
angular.module('jdframework').controller('MyFormCtrl', function MyFormCtrl() {
    //https://scotch.io/tutorials/easy-angularjs-forms-with-angular-formly

    var vm = this; // vm stands for "View Model" --> see https://github.com/johnpapa/angular-styleguide#controlleras-with-vm
    vm.user = {};
    vm.userFields = [{
        // the key to be used in the model values
        // so this will be bound to vm.user.username
        key: 'username',
        type: 'input',
        templateOptions: {
            label: 'Username',
            placeholder: 'johndoe',
            required: true,
            description: 'Descriptive text'
        }
    }, {
        key: 'password',
        type: 'input',
        templateOptions: {
            type: 'password',
            label: 'Password',
            required: true
        },
        expressionProperties: {
            'templateOptions.disabled': '!model.username' // disabled when username is blank
        }
    }];
    vm.onSubmit = onSubmit;

    function onSubmit() {
        console.log('form submitted:', vm.user);
    }
});