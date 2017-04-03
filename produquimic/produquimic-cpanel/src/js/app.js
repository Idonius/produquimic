var app = angular.module('jdframework', ['ngMaterial', 'ngMessages', 'ngRoute', 'ngAnimate', 'toaster','ngCookies','ui.grid', 
'ui.grid.cellNav', 
'ui.grid.selection',
'ui.grid.pagination']);

app.config(['$routeProvider',
  	function ($routeProvider) {
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
        .otherwise({
            redirectTo: '/login'
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
