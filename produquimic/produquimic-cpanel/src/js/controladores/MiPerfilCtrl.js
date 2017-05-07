app.controller('MiPerfilCtrl', function($scope, $location, Utilitario) {

	$scope.confirmaEmail = "";
	$scope.usuario = {};

	if (Utilitario.isLogin() === "true") {
		$scope.identificacion = sessionStorage.getItem('identificacion');
		Utilitario.consumirWebService('framework/servicios/ServicioUsuario.php/getDatosUsuario',
			({
				'identificacion': $scope.identificacion
			})).then(function(data) {
			if (data.datos) {
				$scope.usuario = data.datos;
			}
		});
	}

	$scope.actualizarDatos = function() {
		if ($scope.usuario.CORREO_USUARIO == $scope.confirmaEmail) {
			if ($scope.usuario.CODIGO_USUARIO) {
				Utilitario.consumirWebService('framework/servicios/ServicioUsuario.php/actualizarUsuario',
					$scope.usuario).then(function(data) {
					Utilitario.agregarDialogoMensajeInfo("Mensaje", "Sus datos se actualizaron correctamente.");
				});
			}
		} else {
			Utilitario.agregarDialogoMensajeError("Error", "Verificar la confirmación del correo electrónico.");
			$scope.confirmaEmail = "";
		}
	};

	$scope.cambia = {
		claveNueva: null,
		confirmaClaveNueva: null
	};

	$scope.cambiarClave = function() {
		if ($scope.cambia.claveNueva == $scope.cambia.confirmaClaveNueva) {
			$scope.usuarioCambia = {
				IDENTIFICACION_USUARIO: $scope.identificacion,
				CLAVE_USUARIO: $scope.cambia.claveNueva,
				CODIGO_ESTADO: 1
			};
			Utilitario.consumirWebService('framework/servicios/ServicioSeguridad.php/cambiarClave',
				$scope.usuarioCambia).then(function(data) {
				Utilitario.agregarDialogoMensajeInfo("Mensaje", "La Contraseña se cambio correctamente.");
			});
		} else {
			Utilitario.agregarDialogoMensajeError("Error", "La Nueva Contraseña y la Confirmación de la Nueva Contraseña deben ser iguales.");

			$scope.cambia = {
				claveNueva: null,
				confirmaClaveNueva: null
			};
		}
	};

});