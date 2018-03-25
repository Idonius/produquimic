var app = angular.module('jdframework', ['ngMaterial', 'ngMessages', 'ngRoute', 'ngAnimate', 'ngCookies', 'angular-loading-bar', 'smart-table']);

app.config(['$routeProvider',
        function($routeProvider) {
            $routeProvider.
            when('/login', {
                    title: 'Login',
                    templateUrl: 'views/login.html'
                })
                .when('/', {
                    title: 'Login',
                    templateUrl: 'views/login.html'
                })
                .when('/registro', {
                    title: 'Registro',
                    templateUrl: 'views/registro.html',
                    controller: 'RegistroCtrl'
                })
                .when('/comprobantes/:TipoComprobante', {
                    title: 'Comprobantes',
                    templateUrl: 'views/comprobante.html',
                    controller: 'ComprobanteCtrl'
                })
                .when('/principal', {
                    title: 'Principal',
                    templateUrl: 'views/principal.html',
                    controller: 'PrincipalCtrl'

                })
                .when('/recordarClave', {
                    title: 'recordarClave',
                    templateUrl: 'views/recordar.html',
                    controller: 'RecordarClaveCtrl'

                })
                .when('/miPerfil', {
                    title: 'MiPerfil',
                    templateUrl: 'views/perfil.html',
                    controller: 'MiPerfilCtrl'
                })
                .when('/cambiaClave', {
                    title: 'cambiaClave',
                    templateUrl: 'views/cambiaClave.html',
                    controller: 'MiPerfilCtrl'
                })
                .when('/preguntas', {
                    title: 'Preguntas',
                    templateUrl: 'views/preguntas.html'
                })
                .when('/consultaPrecios', {
                    title: 'consultaPrecios',
                    templateUrl: 'views/consultaProducto.html',
                    controller: 'ConsultaProductoCtrl'
                })
                .when('/empresasMuni', {
                    title: 'empresasMuni',
                    templateUrl: 'views/empresas.html',
                    controller: 'ConsultaEmpresasCtrl'
                })
                .when('/acerca', {
                    title: 'Acerca',
                    templateUrl: 'views/acercaDe.html'
                })
                .otherwise({
                    redirectTo: '/login'
                });
        }
    ])
    .run(['$rootScope', '$location', '$cookieStore', '$http',
        function($rootScope, $location, $cookieStore, $http) {
            // Valida que exista el token 
            $rootScope.globals = $cookieStore.get('globals') || {};
            if ($rootScope.globals.currentUser) {
                $http.defaults.headers.common['Authorization'] = 'AccesToken ' + $rootScope.globals.currentUser.authdata;
            }

            //Redirecciona a la pagina de login
            $rootScope.$on('$locationChangeStart', function(event, next, current) {
                if ($location.path() == '/registro') {
                    $location.path('/registro');
                } else if ($location.path() == '/preguntas') {
                    $location.path('/preguntas');
                } else if ($location.path() == '/acerca') {
                    $location.path('/acerca');
                } else if ($location.path() == '/recordarClave') {
                    $location.path('/recordarClave');
                } else if ($location.path() == '/consultaPrecios') {
                    $location.path('/consultaPrecios');
                } else if ($location.path() == '/empresasMuni') {
                    $location.path('/empresasMuni');
                } else if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                    $location.path('/login');
                }
            });
        }
    ]);

app.config(function($mdThemingProvider) {

    var neonRedMap = $mdThemingProvider.extendPalette('orange', {
        '500': '#4A9140',
        'contrastDefaultColor': 'light'
    });

    // Register the new color palette map with the name <code>neonRed</code>
    $mdThemingProvider.definePalette('neonRed', neonRedMap);

    // Use that theme for the primary intentions
    $mdThemingProvider.theme('default')
        .primaryPalette('neonRed');
});


app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = false;
}]);