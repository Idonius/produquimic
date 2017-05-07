

app.directive('fwNumeros', function () {
    return {
        require: 'ngModel',
        restrict: 'A',
        link: function (scope, element, attrs, ngModel,ctrl) {

            element.bind('keypress', function (e) {
                var charCode = (e.which) ? e.which : e.keyCode
        if (charCode > 31 && (charCode < 48 || charCode > 57))
            e.preventDefault();
            });
            
        }
    };
});

app.directive('fwMayusculas', function () {
    return {
        require: 'ngModel',
        restrict: 'A',
        link: function (scope, element, attrs, modelCtrl) {
            element.addClass('mayuscula');

            modelCtrl.$parsers.push(function (input) {
                return input ? input.toUpperCase() : "";
            });
        }
    };
});



app.directive("fwCargando", function ($rootScope) {
       return function($scope, element, attrs) {
           $scope.$on("loader_show", function () {
               if (element.hasClass("hidden")) {
                   element.removeClass("hidden")
               }
            });
            return $scope.$on("loader_hide", function () {
                if(!element.hasClass("hidden")){
                    element.addClass("hidden")
                }
            });
        };
});
