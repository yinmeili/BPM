app.controller("liquidationController", ["$scope", '$q', "$rootScope", "$http", "$translate", "$state", "$filter", "$modal", "$modalInstance", "$interval", "$timeout", "ControllerConfig", "notify", "datecalculation", "params",
    function ($scope, $q, $rootScope, $http, $translate, $state, $filter, $modal, $modalInstance, $interval, $timeout, ControllerConfig, notify, datecalculation, params) {

        $scope.type = [
            '禁用',
            '启用'
        ]
        $scope.liquidationHours = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15',
            '16', '17', '18', '19', '20', '21', '22', '23',];
        $scope.liquidationTimes = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15',
            '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35', '36',
            '37', '38', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '55', '56', '57', '58', '59',];

        $scope.params = params;
        params.AgencyID;

        $scope.getLanguage = function () {
            $scope.LanJson = {
                StartTime: $translate.instant("QueryTableColumn.StartTime"),
                EndTime: $translate.instant("QueryTableColumn.EndTime"),

                InvalidAgency: $translate.instant("Agent.WorkflowExists"),
                NoSelectWorkItem: $translate.instant("WarnOfNotMetCondition.NoSelectWorkItem"),
                NoSelectWasAgent: $translate.instant("WarnOfNotMetCondition.NoSelectWasAgent"),
                NoSelectWorkflows: $translate.instant("WarnOfNotMetCondition.NoSelectWorkflows"),
                InvalidOfTime: $translate.instant("WarnOfNotMetCondition.InvalidOfTime"),
                NoSelectOriginatorRange: $translate.instant("WarnOfNotMetCondition.NoSelectOriginatorRange")
            }
        }
        $scope.getLanguage();
        // 获取语言
        $rootScope.$on('$translateChangeEnd', function () {
            $scope.getLanguage();
            $state.go($state.$current.self.name, {}, { reload: true });
        });


        $scope.init = function () {

            if (params.AgencyID != "") {

                var dotimeStr = params.AgencyID.doTime;
                $scope.liquidationHour = dotimeStr.slice(0, 2);
                $scope.liquidationTime = dotimeStr.slice(3, 5);
                $scope.liquidationModalsName = params.AgencyID.name;
                $scope.liquidationModalsStatus = $scope.type[params.AgencyID.status];
                $scope.liquidationIP = params.AgencyID.ipAddress;
                $scope.liquidationOperationsteps = params.AgencyID.operateStep;
                $scope.liquidationDescription = params.AgencyID.comment;
                $scope.liquidationSort = params.AgencyID.index;


            }
        }

        $scope.init();



        deferredHandler = function (data, deferred, defaultMsg) {
            if (!data || typeof data !== 'object') {
                this.error = 'Bridge response error, please check the docs';
            }
            if (!this.error && data.result && data.result.error) {
                this.error = data.result.error;
            }
            if (!this.error && data.error) {
                this.error = data.error.message;
            }
            if (!this.error && defaultMsg) {
                this.error = defaultMsg;
            }
            if (this.error) {
                return deferred.reject(data);
            }
            return deferred.resolve(data);
        };




        updateLiquidation = function () {

            var self = params.AgencyID;
            var selecttype = document.getElementById('liquidationModalsStatus');
            $scope.index = selecttype.selectedIndex;

            var deferred = $q.defer();
            var data = {
                id: self.id,
                name: $scope.liquidationModalsName,
                ipAddress: $scope.liquidationIP,
                status: $scope.index,
                operateStep: $scope.liquidationOperationsteps,
                comment: $scope.liquidationDescription,
                doTime: $scope.liquidationHour + ":" + $scope.liquidationTime,
                index: $scope.liquidationSort

            };

            $http.post('/Portal/liquidation/updateLiquidation', data).success(function (data) {
                deferredHandler(data, deferred);
                $.notify({ message: data.msg, status: "success" });
            }).error(function (data) {

                deferredHandler(data, deferred, $translate.instant('error_creating_folder'));
            })['finally'](function () {

            });
            return deferred.promise;
        }

        //编辑
        $scope.editLiquidation = function () {

            var systemname = $scope.liquidationModalsName;
            var type = $scope.liquidationModalsStatus;
            var IP = $scope.liquidationIP
            var dotime = $scope.liquidationHour + ":" + $scope.liquidationTime;
            var index = $scope.liquidationSort;
            var operate = $scope.liquidationOperationsteps;
            var msg = "";
            if (!operate) {
                msg += '请输入操作步骤 | ';
            }
            if (!systemname) {
                msg += '请输入系统名称 | ';
            }
            if (!type) {
                msg += '请选择状态 | ';
            }
            if (!IP) {
                msg += '请输入IP地址 | ';
            }
            if (!dotime) {
                msg += '请选择操作时间 | ';
            }
            if (!index) {
                msg += '请选择序号 | ';
            }
            msg = msg.substr(0, msg.length - 3);
            if (!msg) {
                updateLiquidation().then(function () {
                    $("#tabLiquidationFlow").dataTable().fnDraw();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
        //新建
        createModalsLiquidation = function () {
            var self = params.AgencyID;


            var doTime = $scope.liquidationHour + ":" + $scope.liquidationTime;
            var deferred = $q.defer();
            var data = {

                name: $scope.liquidationModalsName,
                ipAddress: $scope.liquidationIP,
                status: $scope.index,
                operateStep: $scope.liquidationOperationsteps,
                comment: $scope.liquidationDescription,
                doTime: doTime,
                index: $scope.liquidationSort

            };


            $http.post('/Portal/liquidation/createLiquidation', data).success(function (data) {
                deferredHandler(data, deferred);
                $.notify({ message: data.msg, status: "success" });
            }).error(function (data) {

                deferredHandler(data, deferred, $translate.instant('error_creating_folder'));
            })['finally'](function () {
            });
            return deferred.promise;
        }


        $scope.ModalsCreateLiquidation = function () {

            var dotime = $scope.liquidationHour + ":" + $scope.liquidationTime;
            var systemname = $scope.liquidationModalsName;

            var IP = $scope.liquidationIP
            var selecttype = document.getElementById('liquidationModalsStatus');
            $scope.status = selecttype.selectedIndex;
            if ($scope.status == 0) {
                var type = "启用";
                $scope.index = 1;
            }
            else {
                var type = $scope.liquidationModalsStatus;
                $scope.index = $scope.status - 1;
            }

            var index = $scope.liquidationSort;
            var operate = $scope.liquidationOperationsteps;
            var msg = "";
            if (!operate) {
                msg += '请输入操作步骤 | ';
            }
            if (!systemname) {
                msg += '请输入系统名称 | ';
            }
            if (!type) {
                msg += '请选择状态 | ';
            }
            if (!IP) {
                msg += '请输入IP地址 | ';
            }
            if (!dotime || $scope.liquidationHour == undefined || $scope.liquidationTime == undefined) {
                msg += '请选择操作时间 | ';
            }
            if (!index) {
                msg += '请选择序号 | ';
            }
            msg = msg.substr(0, msg.length - 3);
            if (!msg) {

                createModalsLiquidation().then(function () {
                    $("#tabLiquidationFlow").dataTable().fnDraw();

                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };

        //删除操作

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel'); // 退出
        }
        $rootScope.cancel = $scope.cancel;


        //删除

        removeliquidation = function () {
            var self = params.AgencyID;
            var deferred = $q.defer();
            var data = {
                id: params.AgencyID.id
            };

            $http({
                method: "GET",
                url: '/Portal/liquidation/deleteLiquidation?id=' + params.AgencyID.id,
            }).success(function (data) {
                deferredHandler(data, deferred);
                $.notify({ message: data.msg, status: "success" });
            }).error(function (data) {
                deferredHandler(data, deferred, $translate.instant('error_deleting'));
            })['finally'](function () {

            });
            return deferred.promise;
        }
        $scope.removeLiquidation = function () {
            removeliquidation().then(function () {
                $("#tabLiquidationFlow").dataTable().fnDraw();
            });
            $scope.cancel();
        }
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel'); // 退出
        }
        $rootScope.cancel = $scope.cancel;

    }]);



