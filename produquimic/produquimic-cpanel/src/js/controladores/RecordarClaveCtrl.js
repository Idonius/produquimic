app.controller('RecordarClaveCtrl', function($scope, $location, Utilitario, Encripta) {

	$scope.identificacion = "";
	$scope.usuario = {};
	$scope.procesa = false;

	$scope.buscarCliente = function() {
		if ($scope.identificacion) {
			if ($scope.identificacion.length == 10 || $scope.identificacion.length == 13) {
				Utilitario.consumirWebService('framework/servicios/ServicioUsuario.php/getDatosUsuario',
					({
						'identificacion': $scope.identificacion
					})).then(function(data) {
					if (data.datos) {
						$scope.usuario = data.datos;
						/*Si el usuario no se a registrado */
						if ($scope.usuario.REGISTRADO_USUARIO == 0) {
							Utilitario.agregarDialogoMensajeAlerta("Mensaje", "El usuario " + $scope.usuario.NOMBRE_USUARIO + " no se encuentra registrado.");
							$location.path('/registro');
						}
					} else {
						Utilitario.agregarDialogoMensajeAlerta("Mensaje", "El usuario ingresado no existe.");
						$scope.limpiar();
					}
				}).catch(function(err) {
					$scope.limpiar();
				});
			} else {
				Utilitario.agregarDialogoMensajeError("Error", "Identificación no válida.");
				$scope.limpiar();
			}
		}
	};

	$scope.limpiar = function() {
		$scope.identificacion = "";
		$scope.usuario = {};
		$scope.procesa = false;
	};

	$scope.resetarClave = function() {
		$scope.procesa = true;
		if ($scope.usuario.CODIGO_ESTADO != 4) { //Ya se a reseteado
			$scope.usuario.CODIGO_ESTADO = 4;
			$scope.usuario.CLAVE_USUARIO = Encripta.encode($scope.usuario.IDENTIFICACION_USUARIO);
			Utilitario.consumirWebService('framework/servicios/ServicioSeguridad.php/cambiarClave',
				$scope.usuario).then(function(data) {
				Utilitario.agregarDialogoMensajeInfo("Mensaje", "La Contraseña se reseteo correctamente, por favor revizar su correo electrónico.");
				$location.path('/login');
			});
		} else {
			Utilitario.agregarDialogoMensajeAlerta("Mensaje", "Ya se realizo el reseto de contraseña de su usuario, por favor revizar su correo electrónico.");
			$scope.limpiar();
		}
	};

});