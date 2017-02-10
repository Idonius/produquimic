'use strict';
/**
 * Route configuration for the jdframework module.
 */
angular.module('jdframework').config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
        // For unmatched routes
        $urlRouterProvider.otherwise('/');
        // Application routes
        $stateProvider.state('/', {
            url: '/',
            templateUrl: 'views/dashboard.html'
        }).state('eje', {
            url: '/eje',
            templateUrl: 'views/TablaView.html'
        }).state('facturas', {
            url: '/facturas',
            templateUrl: 'views/Facturas.html'
        });
    }
]);