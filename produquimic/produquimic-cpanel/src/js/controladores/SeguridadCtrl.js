app.controller('SeguridadCtrl', function($scope, $location, ServicioAutenticacion, Utilitario, $mdDialog, $mdSidenav, $timeout) {
  $scope.procesa =false;
  $scope.usuario = {
    identificacion: null,
    clave: null
  };
  $scope.nombreUsuario=sessionStorage.getItem('nombre');
  $scope.validarAcceso = function(ev) {
    $scope.procesa =true;
    ServicioAutenticacion.login($scope.usuario, function(response) {
      if (response.error) {
        $scope.limpiar();
      } else {
        if (response.datos.CODIGO_ESTADO == 5 || response.datos.CODIGO_ESTADO == 4) {
          //Dialogo Cambiar clave          
          $scope.abrirDialogoCambiarClave(ev, $scope.usuario);
          $scope.limpiar();
        } else if (response.datos.CODIGO_ESTADO == 1) {
          ServicioAutenticacion.generarToken($scope.usuario.identificacion, response);
          $scope.getPaginas(); 
          $scope.nombreUsuario=sessionStorage.getItem('nombre');
          $location.path('/principal');
          $scope.limpiar();          
        }
      }
    });    
  };

  $scope.limpiar = function() {
    $scope.usuario = {};
    $scope.procesa =false;  
  }

  $scope.abrirDialogoCambiarClave = function(ev, usuario) {
    $mdDialog.show({
      controllerAs: 'DialogController',
      controller: function($scope, $mdDialog, Utilitario, Encripta) {

        $scope.cambia = {
          claveActual: null,
          claveNueva: null,
          confirmaClaveNueva: null
        };

        this.cancelar = function() {
          $mdDialog.hide();
        }
        this.aceptar = function() {
          if (usuario.clave == Encripta.encode($scope.cambia.claveActual)) {
            if ($scope.cambia.claveNueva == $scope.cambia.confirmaClaveNueva) {

              $scope.usuarioCambia = {
                IDENTIFICACION_USUARIO: usuario.identificacion,
                CLAVE_USUARIO: $scope.cambia.claveNueva,
                CODIGO_ESTADO: 1
              };

              Utilitario.consumirWebService('framework/servicios/ServicioSeguridad.php/cambiarClave',
                $scope.usuarioCambia).then(function(data) {
                $mdDialog.hide();
                Utilitario.agregarDialogoMensajeInfo("Mensaje","La Contraseña se cambio correctamente, por favor ingrese al sistema con su nueva contraseña.");
              });


            } else {
              Utilitario.agregarMensajeError("La Nueva Contraseña y la Confirmación de la Nueva Contraseña deben ser iguales.");
            }
          } else {
            Utilitario.agregarMensajeError("La Clave Actual es incorrecta.");
          }

        }
      },
      template: '<md-dialog aria-label="Cambiar Contraseña">' +
        '    <md-toolbar>' +
        '      <div class="md-toolbar-tools">' +
        '        <h2>Cambiar Contraseña</h2>' +
        '      </div>' +
        '    </md-toolbar>' +
        '    <md-dialog-content>' +
        '    <form name="frmCambia" class="form-horizontal" role="form">' +
        '      <div class="md-dialog-content">        ' +
        '        <p>' +
        '          Para cambiar la contraseña primero debe digitar su contraseña actual.' +
        '        </p>' +
        '     <md-input-container class="md-block">' +
        '       <label>Contraseña Actual:</label>     ' +
        '       <input ng-model="cambia.claveActual" type="password" name="txtClaveActual" ng-required="true" required >' +
        '       <div ng-messages="frmCambia.txtClaveActual.$error">' +
        '        <div ng-message="required">Contraseña Actual es un campo obligatorio.</div>' +
        '      </div>' +
        '    </md-input-container>' +
        '    <p>' +
        '       Ingrese su nueva contraseña y repita para confirmarla.' +
        '    </p>' +
        '     <md-input-container class="md-block"> ' +
        '       <label>Contraseña Nueva:</label>         ' +
        '       <input ng-model="cambia.claveNueva" type="password" name="txtClaveNueva" ng-required="true" required>' +
        '       <div ng-messages="frmCambia.txtClaveNueva.$error">' +
        '        <div ng-message="required">Contraseña Nueva es un campo obligatorio.</div>' +
        '      </div>' +
        '    </md-input-container>' +
        '     <md-input-container class="md-block">' +
        '       <label>Confirme Contraseña Nueva:</label>         ' +
        '       <input ng-model="cambia.confirmaClaveNueva" type="password" name="txtClaveActual"  ng-required="true" required>' +
        '       <div ng-messages="frmCambia.txtClaveActual.$error">' +
        '        <div ng-message="required">Confirme Contraseña Nueva es un campo obligatorio.</div>' +
        '      </div>' +
        '    </md-input-container>' +
        '      </div>' +
        '    </md-dialog-content>' +
        '    </form>' +
        '    <md-dialog-actions layout="row">      ' +
        '      <span flex></span>' +
        '      <md-button ng-click="DialogController.cancelar()">' +
        '       Cancelar' +
        '      </md-button>' +
        '      <md-button class="md-raised md-primary" ng-click="DialogController.aceptar()" >' +
        '        Aceptar' +
        '      </md-button>' +
        '    </md-dialog-actions>' +
        '</md-dialog>',
      parent: angular.element(document.body),
      targetEvent: ev,
      escapeToClose: false,
      clickOutsideToClose: false,
      fullscreen: false
    });
  };


   
    $scope.selectedIndex = 0;    
    $scope.getPaginas  = function() {       
       if(Utilitario.isLogin() === "true"){      
        $scope.isLogin=true; 
          $scope.paginas = [{
              nombre: 'Inicio',
              state: '/principal',
              icono: 'ion-ios-home s24',
              color: 'green'
          }, {
              nombre: 'Facturas',
              state: '/comprobantes/Facturas',
              icono: 'ion-ios-paper s24',
              color: 'orange'
          }, {
              nombre: 'Notas de Crédito',
              state:  '/comprobantes/Notas de Crédito',
              icono: 'ion-ios-paper s24',
              color: 'light-green'
          }, {
              nombre: 'Notas de Débito',
              state:  '/comprobantes/Notas de Débito',
              icono: 'ion-ios-paper s24',
              color: 'purple'
          }, {
              nombre: 'Comprobantes de Retención',
              state:  '/comprobantes/Comprobantes de Retención',
              icono: 'ion-ios-paper s24',
              color: 'light-blue'
          }];
         }else{
          $scope.isLogin=false; 
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
              state: '/acerca',
              icono: 'ion-android-bulb s24',
              color: 'amber'
          }];
         }         
     };

     $scope.getPaginas(); 

    $scope.abrirPagina = function(pagina, index) {
        $scope.selectedIndex = index;
        $location.path(pagina);
        $mdSidenav('right').close();
    };

    $scope.toggleRight = buildToggler('right');
    $scope.isOpenRight = function() {
        return $mdSidenav('right').isOpen();
    };

    $scope.cerrarSideNav  = function() {
        $mdSidenav('right').close();
    };

    function buildToggler(navID) {
        return function() {
            // Component lookup should always be available since we are not using `ng-if`
            $mdSidenav(navID).toggle();
        };
    }

    $scope.openMenu = function($mdOpenMenu, ev) {
        $mdOpenMenu(ev);
    };

    $scope.cerrarSesion = function () {
        ServicioAutenticacion.logout();        
        $scope.cerrarSideNav();
        $scope.getPaginas(); 
        $location.path("/login");
     };

});