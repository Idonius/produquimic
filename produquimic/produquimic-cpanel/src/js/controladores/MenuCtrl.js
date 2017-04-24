app.controller('MenuCtrl', function($scope, $timeout, $mdSidenav, $location) {
    $scope.selectedIndex = 0;
    $scope.paginas = [{
        nombre: 'Inicio',
        state: '/login',
        icono: 'ion-ios-home s24',
        color: 'green'
    }, {
        nombre: 'Registrarse',
        state: '/registro',
        icono: 'ion-android-person-add s24',
        color: 'indigo'
    }, {
        nombre: 'Recordar Contraseña',
        state: '/recordarClave',
        icono: 'ion-android-unlock s24',
        color: 'deep-orange'
    }, {
        nombre: 'Preguntas Frecuentes',
        state: '/preguntas',
        icono: 'ion-help-circled s24',
        color: 'blue'
    }, {
        nombre: 'Acerca del Sistema ',
        state: '/sistema',
        icono: 'ion-android-bulb s24',
        color: 'amber'
    }];


    $scope.abrirPagina = function(pagina, index) {
        $scope.selectedIndex = index;
        $location.path(pagina);
        $mdSidenav('right').close();
    };

    $scope.toggleRight = buildToggler('right');
    $scope.isOpenRight = function() {
        return $mdSidenav('right').isOpen();
    };


    function buildToggler(navID) {
        return function() {
            // Component lookup should always be available since we are not using `ng-if`
            $mdSidenav(navID)
                .toggle();
        };
    }
});