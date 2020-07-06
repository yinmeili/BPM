(function(angular, $) {
    'use strict';
    app.controller('ModalFileManagerCtrl',
        ['$scope', '$rootScope', 'fileNavigator', 
        function($scope, $rootScope, FileNavigator) {

        $scope.reverse = false;
        $scope.predicate = ['model.type', 'model.name'];
        $scope.order = function(predicate) {
            $scope.reverse = ($scope.predicate[1] === predicate) ? !$scope.reverse : false;
            $scope.predicate[1] = predicate;
        };

        $scope.fileNavigator = new FileNavigator();
        $rootScope.select = function(item, temp) {
            temp.tempModel.id = item.model.id;
            temp.tempModel.path = item.model.fullPath().split('/');
            $('#selector').modal('hide');
        };
        
        $rootScope.openNavigator = function(item) {
            // debugger;
            $scope.fileNavigator.currentFileId = item.model.id;
            $scope.fileNavigator.currentParentId = item.model.parentId;
            $scope.fileNavigator.currentFileId = $scope.fileNavigator.currentParentId;
            $scope.fileNavigator.currentPath = item.model.path.slice();
            $scope.fileNavigator.refresh();
            $('#selector').modal('show');
        };

    }]);
})(angular, jQuery);