
app.factory('httpInterceptor', function ($q, $rootScope, $log) 
{
    var numLoadings = 0;    // Es un contador del número de requerimientos que se ha enviado
    // Esta función sirve para controlar que se presione la tecla ESC cuando no haya recibido respuesta y pueda quitarse el reloj de la pantalla
    $rootScope.keyHandler = function (e) {
        if (e.keyCode == 27) {
            numLoadings = 0;
            angular.element(document).off('keydown', $rootScope.keyHandler);
            // Hide loader
            $rootScope.$broadcast("loader_hide");   // Si el valor es cero, oculta el reloj
        }
        //console.log('keyHandler: ' + e.keyCode);
    };
    return {
        request: function (config) {
            numLoadings++;  // Cuando hay un nuevo request, el contador se incrementa
            angular.element(document).on('keydown', $rootScope.keyHandler); // Registra el handler de la tecla ESC
            // Show loader
            $rootScope.$broadcast("loader_show");   // Y presenta el reloj
            return config || $q.when(config)
        },
        response: function (response) {
            if ((--numLoadings) <= 0) {    // Al recibir una respuesta, disminuye el contador
                numLoadings = 0;
                angular.element(document).off('keydown', $rootScope.keyHandler); // Elimina el handler de la tecla ESC
                // Hide loader
                $rootScope.$broadcast("loader_hide");   // Si el valor es cero, oculta el reloj
            }
            return response || $q.when(response);
        },
        responseError: function (response) {
            if ((--numLoadings) <= 0) {         // También disminuye el contador cuando recibe errores
                numLoadings = 0;
                angular.element(document).off('keydown', $rootScope.keyHandler); // Elimina el handler de la tecla ESC
                // Hide loader
                $rootScope.$broadcast("loader_hide");   // Y oculta el contador
            }
            return $q.reject(response);
        }
    };
})
.config(function ($httpProvider) {
    $httpProvider.interceptors.push('httpInterceptor');
});

