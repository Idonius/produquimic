app.controller('RegistroCtrl', function($scope, $location, Utilitario) {

	$scope.identificacion = "";
	$scope.confirmaEmail = "";
	$scope.procesa = false;

	$scope.usuario = {
		CODIGO_USUARIO: null,
		REGISTRADO_USUARIO: 0,
		NOMBRE_USUARIO: null,
		IDENTIFICACION_USUARIO: null,
		CORREO_USUARIO: null,
		TELEFONO_USUARIO: null,
		CONTACTO_USUARIO: null,
		DIRECCION_USUARIO: null
	};

	$scope.buscarCliente = function() {
		if ($scope.identificacion) {
			if ($scope.identificacion.length == 10 || $scope.identificacion.length == 13) {
				Utilitario.consumirWebService('framework/servicios/ServicioUsuario.php/getDatosUsuario',
					({
						'identificacion': $scope.identificacion
					})).then(function(data) {
					if (data.datos) {
						$scope.usuario = data.datos;
						/*Si el usuario ya se a registrado envia mensaje*/
						if ($scope.usuario.REGISTRADO_USUARIO == 1) {
							Utilitario.agregarDialogoMensajeAlerta("Mensaje", "El usuario " + $scope.usuario.NOMBRE_USUARIO + " ya se encuentra registrado.");
							$location.path('/login');
						}
					} else {
						Utilitario.agregarDialogoMensajeAlerta("Mensaje", "El usuario ingresado no existe.");
						$scope.identificacion = "";
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
		$scope.confirmaEmail = "";
		$scope.usuario = {
			CODIGO_USUARIO: null,
			REGISTRADO_USUARIO: 0,
			NOMBRE_USUARIO: null,
			IDENTIFICACION_USUARIO: null,
			CORREO_USUARIO: null,
			TELEFONO_USUARIO: null,
			CONTACTO_USUARIO: null,
			DIRECCION_USUARIO: null
		};
		$scope.procesa = false;
	};

	$scope.registrarUsuario = function() {
		$scope.procesa = true;
		if ($scope.usuario.CORREO_USUARIO == $scope.confirmaEmail) {
			if ($scope.usuario.CODIGO_USUARIO) {
				$scope.usuario.REGISTRADO_USUARIO = 1; //true
				$scope.usuario.CODIGO_ESTADO = 5; //REGISTRADO
				Utilitario.consumirWebService('framework/servicios/ServicioUsuario.php/actualizarUsuario',
					$scope.usuario).then(function(data) {
					Utilitario.agregarDialogoMensajeInfo("Mensaje", "Se registro correctamente en el sistema, por favor revizar su correo electrónico.");
					$location.path('/login');
				});
			}
		} else {
			Utilitario.agregarDialogoMensajeError("Error", "Verificar la confirmación del correo electrónico.");
			$scope.confirmaEmail = "";
		}
	};

});