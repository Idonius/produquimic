app.factory('Utilitario',['$http', '$rootScope', '$mdToast' ,'$mdDialog','$q',
function ( $http, $rootScope, $mdToast, $mdDialog, $q) {
	var service = {};

	service.agregarMensajeError = function (mensaje) {
    $mdToast.show({
        template:'<md-toast class="errorToast"> <i class="iconoToast ion-alert-circled"></i>' +	mensaje  +	'</md-toast>',
        hideDelay: 6000,
        position: 'top right'
    });
	};

	service.agregarMensajeInfo = function (mensaje) {
    $mdToast.show({
        template:'<md-toast class="infoToast"> <i class="iconoToast ion-information-circled"></i>' +	mensaje  +	'</md-toast>',
        hideDelay: 6000,
        position: 'top right'
    });
	};

	service.agregarMensajeAlerta = function (mensaje) {
    $mdToast.show({
        template:'<md-toast class="warnToast"> <i class="iconoToast ion-alert-circled"></i>' +	mensaje  +	'</md-toast>',
        hideDelay: 6000,
        position: 'top right'
    });
	};

	service.agregarMensajeExito = function (mensaje) {
    $mdToast.show({
        template:'<md-toast class="successToast"> <i class="iconoToast ion-checkmark-round"></i>' +	mensaje  +	'</md-toast>',
        hideDelay: 6000,
        position: 'top right'
    });
	};

    service.agregarMensajeSistema = function (mensaje) {
    $mdToast.show({
        template:'<md-toast> <i class="iconoToast ion-android-bulb"></i>' + mensaje  +  '</md-toast>',
        hideDelay: 6000,
        position: 'bottom left'
    });
    };

     service.agregarDialogoMensajeError= function (titulo,mensaje) {   
        $mdDialog.show({                     
          preserveScope: true,  
          controllerAs: 'dialogCtrl',
                controller: function($mdDialog){
                  this.click = function(){
                    $mdDialog.hide();
                  }
                },
          template: '<md-dialog aria-label="List dialog">' +  
           '<md-subheader class="errorFondo"><i class="ion-alert-circled s18"></i> '+ titulo + '</md-subheader>'+
           '<md-divider ></md-divider>' +           
           '  <md-dialog-content> <div class="md-dialog-content">'+ mensaje + '</div> </md-dialog-content>' +
           '  <md-dialog-actions layout="row">' +
           '    <md-button ng-click="dialogCtrl.click()" md-autofocus>' +
           '      ACEPTAR' +
           '    </md-button>' +
           '  </md-dialog-actions>' +
           
           '</md-dialog>'
       });
    };   

    service.agregarDialogoMensajeInfo= function (titulo,mensaje) {   
        $mdDialog.show({                     
          preserveScope: true,  
          controllerAs: 'dialogCtrl',
                controller: function($mdDialog){
                  this.click = function(){
                    $mdDialog.hide();
                  }
                },
          template: '<md-dialog aria-label="List dialog">' +  
           '<md-subheader class="infoFondo"><i class="ion-information-circled s18"></i> '+ titulo + '</md-subheader>'+
           '<md-divider ></md-divider>' +           
           '  <md-dialog-content> <div class="md-dialog-content">'+ mensaje + '</div> </md-dialog-content>' +
           '  <md-dialog-actions layout="row">' +
           '    <md-button ng-click="dialogCtrl.click()" md-autofocus>' +
           '      ACEPTAR' +
           '    </md-button>' +
           '  </md-dialog-actions>' +
           
           '</md-dialog>'
       });
    };  


    service.agregarDialogoMensajeAlerta= function (titulo,mensaje) {            
        $mdDialog.show({                     
          preserveScope: true,  
          controllerAs: 'dialogCtrl',
                controller: function($mdDialog){
                  this.click = function(){
                    $mdDialog.hide();
                  }
                },
          template: '<md-dialog aria-label="List dialog">' +  
           '<md-subheader class="alertFondo"><i class="ion-alert-circled s18"></i> '+ titulo + '</md-subheader>'+
           '<md-divider ></md-divider>' +           
           '  <md-dialog-content> <div class="md-dialog-content">'+ mensaje + '</div> </md-dialog-content>' +
           '  <md-dialog-actions layout="row">' +
           '    <md-button ng-click="dialogCtrl.click()" md-autofocus>' +
           '      ACEPTAR' +
           '    </md-button>' +
           '  </md-dialog-actions>' +
           
           '</md-dialog>'
       });
    }; 


    service.consumirWebService = function(metodo,jsonParametro) {
     deferred = $q.defer();    
     $http({
         method: 'post',
         url: metodo,
         headers: {'Authorization': 'AccesToken ',
                   'Accept': 'application/json;odata=verbose', 
                   'Content-Type': 'application/x-www-form-urlencoded'},
         data:    jsonParametro     
     }).success(function(data){
         // With the data succesfully returned, we can resolve promise and we can access it in controller
         deferred.resolve(data);         
     }).error(function(data){
         service.agregarDialogoMensajeError("Error",data.mensaje);
         deferred.reject();
     });
     return deferred.promise;
  };



return service;
}]);
