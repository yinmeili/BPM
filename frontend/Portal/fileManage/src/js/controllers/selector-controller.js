(function(angular, $) {
    'use strict';
    app.controller('ModalFileManagerCtrl',
        ['$scope', '$rootScope', 'fileNavigator', '$http','ControllerConfig','$modal',
        function($scope, $rootScope, FileNavigator,$http,ControllerConfig,$modal) {

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
					// temp.tempModel.path = item.model.fullPath().split('/');
					temp.tempModel.path = item.model.fullPath().split('/').slice(1);//删除第一个元素
            $rootScope.cancel();
        };
				
				/***************点击更改路径 弹出路径选择框 ***************/
        $rootScope.openNavigator = function(item) {
            $scope.fileNavigator = $rootScope.scope.fileNavigator;// 参数问题
            // 判断是根目录下文件路径还是子文件路径
            if (item.model.parentId==null) {
                // 根目录Id设置为空字符串，避免列表显示为空
                $scope.fileNavigator.currentFileId = '';

            } else {
                $scope.fileNavigator.currentFileId = item.model.parentId;
            }
						$scope.fileNavigator.currentPath = item.model.path.slice();
            $scope.fileNavigator.selectFolderRefresh(item);
            //弹出更改路径模态框
            (function (data) {
                var AgencyID;
                if (data == undefined) AgencyID = "";
                else AgencyID = data;
                $http({
                    url: ControllerConfig.Agents.GetAgency,
                    params: {
                        agentID: AgencyID,
                        random: new Date().getTime()
                    }
                })
                    .success(function (result, header, config, status) {
                        var Agency = result.Rows[0];
                        // 弹出模态框

                        var modalInstance = $modal.open({
                            templateUrl: 'editPath.html',    // 指向上面创建的视图
                            controller: 'ModalsController',// 初始化模态范围
                            size: "md",
                            resolve: {
                                params: function () {
                                    return {
                                        user: $scope.user,
                                        Agency: Agency,
                                        AgencyID: AgencyID
                                    };
                                },
                                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                                    return $ocLazyLoad.load([
                                        'WFRes/_Content/themes/ligerUI/Aqua/css/ligerui-all.min.css',
                                        'WFRes/assets/stylesheets/sheet.css',
                                        'WFRes/_Scripts/jquery/jquery.lang.js',
                                        'WFRes/_Scripts/ligerUI/ligerui.all.min.js',
                                        'WFRes/_Scripts/MvcSheet/SheetControls.js',
                                        'WFRes/_Scripts/MvcSheet/MvcSheetUI.js'
                                    ]).then(function () {
                                        return $ocLazyLoad.load([
                                            'WFRes/_Scripts/MvcSheet/Controls/SheetWorkflow.js',
                                            'WFRes/_Scripts/MvcSheet/Controls/SheetUser.js'
                                        ]);
                                    });
                                }]
                            }
                        });
                        modalInstance.opened.then(function() {
                            //TODO not work
                        });
                    });
            })();
        };
    }]);
})(angular, jQuery);