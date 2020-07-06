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
					// 修改过路径的id赋值给旧的parentId
          temp.tempModel.parentId = item.model.id;
          temp.tempModel.path = item.model.fullPath().split('/');
          $('#selector').modal('hide');
        };
        
        $rootScope.openNavigator = function(item) {
						// 判断是根目录下文件路径还是子文件路径
						if (item.model.parentId==null) {
							// 根目录Id设置为空字符串，避免列表显示为空
							$scope.fileNavigator.currentFileId = '';

						} else {
							$scope.fileNavigator.currentFileId = item.model.parentId;
						}

            $scope.fileNavigator.currentPath = item.model.path.slice();
            $scope.fileNavigator.refresh();
            $('#selector').modal('show');
        };

    }]);
})(angular, jQuery);