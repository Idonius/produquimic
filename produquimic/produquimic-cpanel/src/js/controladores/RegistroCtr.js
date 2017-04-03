app.controller('RegistroCtrl', function ($scope, $rootScope, $routeParams, $location, $http, toaster) {

	$scope.identificacion="";
	$scope.confirmaEmail="";
	
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

	$scope.buscarCliente = function () {	
		if($scope.identificacion)	{
			if ($scope.identificacion.length==10 || $scope.identificacion.length==13)	{
				$http.post('framework/models/UsuarioModel.php/getDatosUsuario/'+ $scope.identificacion ).success(function(data) {	
				    if (data.datos) {
					     $scope.usuario=data.datos[0];					     			 
						 /*Si el usuario ya se a registrado envia mensaje*/
						 if( $scope.usuario.REGISTRADO_USUARIO == 1){
						 	toaster.pop("warning","","El usuario "+$scope.usuario.NOMBRE_USUARIO +" ya se encuentra registrado", 10000, 'trustedHtml');
						 	$scope.limpiar();
						 }
				    }
				    else{
				    	toaster.pop("warning","","No se han generado comprobantes electrónicos para el usuario ingresado", 10000, 'trustedHtml');
				    	$scope.identificacion="";
				    }
				});
			}
			else{
				toaster.pop("error","","Identificación no válida", 10000, 'trustedHtml');
				$scope.limpiar();
			}
		} 	
	};

	$scope.limpiar = function () {
		$scope.identificacion="";
		$scope.confirmaEmail="";		
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
	};	


	$scope.registrarUsuario = function() { 
		if($scope.usuario.CORREO_USUARIO==$scope.confirmaEmail){
			if($scope.usuario.CODIGO_USUARIO){
			  $scope.usuario.REGISTRADO_USUARIO=1;
			  $http.post('framework/models/UsuarioModel.php/actualizarUsuario', $scope.usuario).success(function(data){                   		    
			  	toaster.pop("succes","", "Se guardo Correctamente", 10000, 'trustedHtml');     
			  	$scope.limpiar();             
			  }).error(function(data){   		  	
			    toaster.pop("error","", data.mensaje, 10000, 'trustedHtml');                  
			  });
			}			
		}
		else{
			toaster.pop("error","", "Verificar la confirmación del correo electrónico", 10000, 'trustedHtml');                  
			$scope.confirmaEmail="";
		}


	};	
});