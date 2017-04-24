app.controller('SeguridadCtrl', function($scope, $location, ServicioAutenticacion, Utilitario, $mdDialog) {
  $scope.procesa =false;
  $scope.usuario = {
    identificacion: null,
    clave: null
  };

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
          $location.path('/principal');
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
                CLAVE_USUARIO: Encripta.encode($scope.cambia.claveNueva),
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
        '  <form ng-cloak>' +
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
        '  </form>' +
        '</md-dialog>',
      parent: angular.element(document.body),
      targetEvent: ev,
      escapeToClose: false,
      clickOutsideToClose: false,
      fullscreen: false
    });
  };

});