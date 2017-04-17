var app = angular.module('jdframework', ['ngMaterial', 'ngMessages', 'ngRoute', 'ngAnimate', 'ngCookies', 'ui.grid',
    'ui.grid.cellNav',
    'ui.grid.selection',
    'ui.grid.pagination'
]);

app.config(['$routeProvider',
        function($routeProvider) {
            $routeProvider.
            when('/login', {
                    title: 'Login',
                    templateUrl: 'views/login.html',
                    controller: 'SeguridadCtrl'
                })
                .when('/', {
                    title: 'Login',
                    templateUrl: 'views/login.html',
                    controller: 'SeguridadCtrl',
                    role: '0'
                })
                .when('/registro', {
                    title: 'Registro',
                    templateUrl: 'views/registro.html',
                    controller: 'RegistroCtrl'
                })
                .when('/principal', {
                    title: 'Principal',
                    templateUrl: 'views/principal.html',
                    controller: 'PrincipalCtrl'

                })
                .when('/cambiarClave', {
                    title: 'cambiarClave',
                    templateUrl: 'views/cambiarClave.html',
                    controller: 'CambiarClaveCtrl'

                })
                .when('/preguntas', {
                    title: 'Preguntas',
                    templateUrl: 'views/preguntas.html'
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