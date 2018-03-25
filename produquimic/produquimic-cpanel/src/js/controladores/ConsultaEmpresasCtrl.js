app.controller('ConsultaEmpresasCtrl', function($scope, $routeParams, $location, $http, Utilitario, $timeout, $mdDialog) {
  $scope.empresa = "";
  $scope.actividad = "";
  $scope.listaEmpresas = {};
  $scope.empresaSeleccionada = null;

  $scope.listaEmpresasActualizadas = {};

  $scope.borrar = function() {
    $scope.empresa = "";
    $scope.actividad = "";
    $scope.listaEmpresas = {};
    $scope.empresaSeleccionada = null;
  };


  $scope.buscar = function() {
    $scope.busqueda = {
      actividad: $scope.actividad,
      empresa: $scope.empresa
    };


    Utilitario.consumirWebService('framework/servicios/ServicioEmpresas.php/getEmpresas',
      ($scope.busqueda)).then(function(data) {
      if (data.datos) {
        $scope.listaEmpresas = data.datos;
      } else {
        $scope.listaEmpresas = {};
      }

    });

  };


  $scope.actividad1 = "";
  $scope.listaActividades = {};
  $scope.buscarActividad = function() {

 
    $scope.busqueda = {
      actividad: $scope.actividad1
    };


    Utilitario.consumirWebService('framework/servicios/ServicioEmpresas.php/getActividades',
      ($scope.busqueda)).then(function(data) {
      if (data.datos) {
        $scope.listaActividades = data.datos;
      } else {
        $scope.listaActividades = {};
      }

    });
  };


  $scope.buscarEmpresasActualizadas = function() {
    Utilitario.consumirWebService('framework/servicios/ServicioEmpresas.php/getEmpresasActualizadas',
      null).then(function(data) {
      if (data.datos) {
        $scope.listaEmpresasActualizadas = data.datos;
      } else {
        $scope.listaEmpresasActualizadas = {};
      }

    });
  };

  $scope.borrarActividad = function() {
    $scope.actividad1 = "";
    $scope.listaActividades = {};
  };



  $scope.abrirDialogoLlamar = function(row) {
    $scope.empresaSeleccionada = row;

    $mdDialog.show({
      parent: angular.element(document.body),
      template: '<md-dialog aria-label="EMPRESA"  >' +
        '<form ng-cloak>' +
        '<md-subheader class="md-primary">{{empresaSeleccionada.RAZON_SOCIAL_EM}}</md-subheader>' +
        '<md-dialog-content > ' +
        '<div class="md-dialog-content">' +
        '<div layout="row" > ' +
        '<md-input-container style="width: 20%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Ruc">Ruc:</label>' +
        ' <input ng-model="empresaSeleccionada.RUC_EM"  type="text"  ng-disabled="true">' +
        ' </md-input-container>' +
        '<md-input-container style="width: 80%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Nombre Comercial">Nombre Comercial:</label>' +
        ' <input ng-model="empresaSeleccionada.NOMBRE_COMERCIAL_EM" size="120" type="text" ng-disabled="true">' +
        ' </md-input-container>' +
        '</div>' +

        '<div layout="row" > ' +
        '<md-input-container style="width: 50%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Parroquia">Parroquia:</label>' +
        ' <input ng-model="empresaSeleccionada.PARROQUIA_EM"  type="text"  ng-disabled="true">' +
        ' </md-input-container>' +
        '<md-input-container style="width: 50%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Calle Principal">Calle Principal:</label>' +
        ' <input ng-model="empresaSeleccionada.CALLE_PRINCIPAL_EM" type="text" ng-disabled="true">' +
        ' </md-input-container>' +
        '</div>' +

        '<div layout="row" > ' +
        '<md-input-container style="width: 45%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Calle Secundaria">Calle Secundaria:</label>' +
        ' <input ng-model="empresaSeleccionada.CALLE_SECUNDARIA_EM"  type="text"  ng-disabled="true">' +
        ' </md-input-container>' +
        '<md-input-container style="width: 15%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Numero">Número:</label>' +
        ' <input ng-model="empresaSeleccionada.NUMERO_EM" type="text" ng-disabled="true">' +
        ' </md-input-container>' +
        '<md-input-container style="width: 40%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Referencia">Referencia:</label>' +
        ' <input ng-model="empresaSeleccionada.REFERENCIA_EM" type="text" ng-disabled="true">' +
        ' </md-input-container>' +
        '</div>' +
        '<div layout="row" > ' +
        '<md-input-container style="width: 20%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Telefono 1">Teléfono 1:</label>' +
        ' <input ng-model="empresaSeleccionada.TELEFONO1_EM"  type="text"  ng-disabled="true">' +
        ' </md-input-container>' +
        '<md-input-container style="width: 20%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Telefono 2">Teléfono 2:</label>' +
        ' <input ng-model="empresaSeleccionada.TELEFONO2_EM" type="text" ng-disabled="true">' +
        ' </md-input-container>' +
        '<md-input-container style="width: 40%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Correo">Correo Electrónico:</label>' +
        ' <input ng-model="empresaSeleccionada.MAIL_EM" type="text" ng-disabled="true">' +
        ' </md-input-container>' +
        '<md-input-container style="width: 20%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Horario">Horario de Atención:</label>' +
        ' <input ng-model="empresaSeleccionada.HORARIO_ATENCION_EM" type="text" ng-disabled="true">' +
        ' </md-input-container>' +
        '</div>' +

        '<div layout="row" > ' +

        '<md-input-container style="width: 33%;">' +
        '<md-switch md-invert ng-model="empresaSeleccionada.LLAMA_EM"  aria-label="Seleccionar">' +
        '<i class="ion-social-whatsapp-outline s28"></i>&nbsp;&nbsp; Se Llamó ? : {{ empresaSeleccionada.LLAMA_EM == true ?"SI" : "NO" }}' +
        '</md-switch>' +
        ' </md-input-container>' +


        '<md-input-container style="width: 33%;">' +
        '<md-switch  ng-model="empresaSeleccionada.COTIZA_EM"  aria-label="Seleccionar">' +
        '<i class="ion-android-mail s28"></i>&nbsp;&nbsp; Se Cotizó ? : {{ empresaSeleccionada.COTIZA_EM == true ?"SI" : "NO" }}' +
        '</md-switch>' +
        ' </md-input-container>' +

        '<md-input-container style="width: 33%;">' +
        '<md-switch  ng-model="empresaSeleccionada.COMPRA_EM"  aria-label="Seleccionar">' +
        '<i class="ion-thumbsup s28"></i>&nbsp;&nbsp; Se Vendió ? : {{ empresaSeleccionada.COMPRA_EM == true ?"SI" : "NO" }}' +
        '</md-switch>' +
        ' </md-input-container>' +

        '</div>' +

        '<div layout="row"> ' +
        '<md-input-container style="width: 100%;padding: 0px;margin: 0">' +
        '<label style="color: black;" aria-label="Observaciones">Observaciones:</label>' +
        ' <input ng-model="empresaSeleccionada.OBSERVACIONES_LLAMA_EM"  type="text" placeholder="DIGITAR OBSERVACIONES" ng-required="true" fw-mayusculas >' +
        ' </md-input-container>' +
        '</div>' +
        '</div>' +
        ' </md-dialog-content>' +
        '<md-dialog-actions layout="row" >' +
        '<span flex></span> ' +
        ' <md-button class="md-raised md-primary"  ng-click="aceptar()" >' +
        '  ACEPTAR' +
        ' </md-button>' +
        ' <md-button class="md-select"  ng-click="cancelar()" >' +
        '  CANCELAR' +
        ' </md-button>' +
        '</md-dialog-actions>  <div style="display:none" ui-grid-selection  ui-grid="gridSolicitudes">  ' +
        '</form>' +
        '</md-dialog>',
      locals: {
        empresaSeleccionada: $scope.empresaSeleccionada
      },
      controller: DialogoAprobarController
    });
  };


});


function DialogoAprobarController(Utilitario, $scope, $mdDialog, empresaSeleccionada) {
  $scope.empresaSeleccionada = empresaSeleccionada;

  /**
   * Oculta el dialogo cuando presiona Cancelar
   */
  $scope.cancelar = function() {
    $mdDialog.hide();
  };

  /**
   * Acepta los tramites seleccionados y los aprueba
   * @return {[type]} [description]
   */
  $scope.aceptar = function() {
    if ($scope.empresaSeleccionada.OBSERVACIONES_LLAMA_EM) {
      $scope.empresaSeleccionada.FECHA_LLAMA_EM = new Date();
      Utilitario.consumirWebService('framework/servicios/ServicioEmpresas.php/actualizarEmpresa',
        $scope.empresaSeleccionada).then(function(data) {
        Utilitario.agregarDialogoMensajeInfo("Mensaje", "Se guardo correctamente.");
        $mdDialog.hide();
      });
    } else {
      Utilitario.agregarDialogoMensajeError("Error", "Debe ingresar una Observación.");
    }
  };

}