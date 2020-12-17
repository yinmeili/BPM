app.controller("ProjectManagerlController", ["$scope", '$q', "$rootScope", "$http", "$translate", "$state", "$filter", "$modal", "$modalInstance", "$interval", "$timeout", "ControllerConfig", "notify", "datecalculation", "params",
    function ($scope, $q, $rootScope, $http, $translate, $state, $filter, $modal, $modalInstance, $interval, $timeout, ControllerConfig, notify, datecalculation, params) {

        $scope.CraeteProjectModalLeader = {
            Editable: true,
            Visiable: true,
            CompanyVisible: false,
            CompanySelectable: false,

        }

        $scope.editProjectLearder = {
            Editable: true,
            Visiable: true,
            CompanyVisible: false,
            CompanySelectable: false,

        }

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

            //编辑初始化
            if (params.AgencyID != "") {
                $scope.editProjectModalname = params.AgencyID.name;
                var cutStarttime = params.AgencyID.startTime.indexOf(" ");
                var cutEndtime = params.AgencyID.endTime.indexOf(" ");
                $scope.EditProjectStartTimeShow = params.AgencyID.startTime.substring(0, cutStarttime);
                $scope.EditProjectEndTimeShow = params.AgencyID.endTime.substring(0, cutEndtime);
                $scope.editProjectdescription = params.AgencyID.desc;
                $scope.detailProjectLearder = params.AgencyID.leaderName

                setTimeout(function () {

                    $("#editModalsProjectLearder").SheetUIManager().SetValue(params.AgencyID.leaderId);
                }, 100)


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


        $scope.EditProjectStartTime = {
            dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
            onpicked: function (e) {
                $rootScope.modalsStartTimeEdit = e.el.value;

            }
        }
        $scope.EditProjectEndTime = {
            dateFmt: 'yyyy-MM-dd ', realDateFmt: "yyyy-MM-dd ", minDate: '2012-1-1', maxDate: '2099-12-31',
            onpicked: function (e) {
                $rootScope.modalsEndTimeEdit = e.el.value;
            }
        }

        $scope.createProjectStartTime = {
            dateFmt: 'yyyy-MM-dd  ', realDateFmt: "yyyy-MM-dd ", minDate: '2012-1-1', maxDate: '2099-12-31',
            onpicked: function (e) {
                $rootScope.modalsStartTimeCreate = e.el.value;

            }
        }
        $scope.createProjectEndTime = {
            dateFmt: 'yyyy-MM-dd ', realDateFmt: "yyyy-MM-dd ", minDate: '2012-1-1', maxDate: '2099-12-31',
            onpicked: function (e) {
                $rootScope.modalsEndTimeCreate = e.el.value;
            }
        }


        editModalsProjectManager = function () {


            if ($rootScope.modalsStartTimeEdit == undefined && $rootScope.modalsEndTimeEdit == undefined) {
                var StartTimes = new Date($scope.EditProjectStartTimeShow.replace(/-/g, "/")).getTime();
                var EndTimes = new Date($scope.EditProjectEndTimeShow.replace(/-/g, "/")).getTime();
                if (StartTimes > EndTimes) {
                    $.notify({ message: "时间区间错误", status: "danger" });
                    $("#EditProjectStartTimeShow").css("color", "red");
                    $("#EditProjectEndTimeShow").css("color", "red");
                    return false;
                };
            }
            else if ($rootScope.modalsStartTimeEdit != undefined && $rootScope.modalsEndTimeEdit != undefined) {
                $scope.EditProjectStartTimeShow = $rootScope.modalsStartTimeEdit;
                $scope.EditProjectEndTimeShow = $rootScope.modalsEndTimeEdit;
                var StartTimes = new Date($scope.EditProjectStartTimeShow.replace(/-/g, "/")).getTime();
                var EndTimes = new Date($scope.EditProjectEndTimeShow.replace(/-/g, "/")).getTime();
                if (StartTimes > EndTimes) {
                    $.notify({ message: "时间区间错误", status: "danger" });
                    $("#EditProjectStartTimeShow").css("color", "red");
                    $("#EditProjectEndTimeShow").css("color", "red");
                    return false;
                };
            }
            else if ($rootScope.modalsStartTimeEdit != undefined && $rootScope.modalsEndTimeEdit == undefined) {
                $scope.EditProjectStartTimeShow = $rootScope.modalsStartTimeEdit;
                var StartTimes = new Date($scope.EditProjectStartTimeShow.replace(/-/g, "/")).getTime();
                var EndTimes = new Date($scope.EditProjectEndTimeShow.replace(/-/g, "/")).getTime();
                if (StartTimes > EndTimes) {
                    $.notify({ message: "时间区间错误", status: "danger" });
                    $("#EditProjectStartTimeShow").css("color", "red");
                    $("#EditProjectEndTimeShow").css("color", "red");
                    return false;
                };
            }

            else if ($rootScope.modalsStartTimeEdit == undefined && $rootScope.modalsEndTimeEdit != undefined) {

                $scope.EditProjectEndTimeShow = $rootScope.modalsEndTimeEdit;
                var StartTimes = new Date($scope.EditProjectStartTimeShow.replace(/-/g, "/")).getTime();
                var EndTimes = new Date($scope.EditProjectEndTimeShow.replace(/-/g, "/")).getTime();
                if (StartTimes > EndTimes) {
                    $.notify({ message: "时间区间错误", status: "danger" });
                    $("#EditProjectStartTimeShow").css("color", "red");
                    $("#EditProjectEndTimeShow").css("color", "red");
                    return false;
                };
            }


            var self = params.AgencyID;

            if ($scope.OriginatorRange === "") {
                $scope.OriginatorRange = params.AgencyID.leaderId;
            }
          
            var deferred = $q.defer();
            var data = {
                id: self.id,
                name: $scope.editProjectModalname,
                desc: $scope.editProjectdescription,
                startTime: $scope.EditProjectStartTimeShow,
                endTime: $scope.EditProjectEndTimeShow,
                leaderId: $scope.OriginatorRange,

            };

            $http.post('/Portal/project/updateProject', data).success(function (data) {
               
                deferredHandler(data, deferred);
                $.notify({ message: data.msg, status: "success" });
            }).error(function (data) {

                deferredHandler(data, deferred, $translate.instant('error_creating_folder'));
            })['finally'](function () {
                // self.inprocess = false;
            });
            return deferred.promise;
        }


        $scope.editProjectManager = function () {
            var projectName = $scope.editProjectModalname;
            var editStartTime = $scope.EditProjectStartTimeShow;
            var editEndTime = $scope.EditProjectEndTimeShow;
            $scope.OriginatorRange = $("#editModalsProjectLearder").SheetUIManager().GetValue();
            var leaderid = $scope.OriginatorRange;



            var msg = "";
            if (!projectName) {
                msg += '请输入项目名称 | ';
            }
            if (!leaderid) {
                msg += '请选择负责人 | ';
            }
            if (!editStartTime) {
                msg += '请选择开始时间 | ';
            }
            if (!editEndTime) {
                msg += '请选择结束时间 | ';
            }

            msg = msg.substr(0, msg.length - 3);
            if (!msg) {
                editModalsProjectManager().then(function () {
                    $("#tabProjectManager").dataTable().fnDraw();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
        //新建
        createModalsProject = function () {
            var self = params.AgencyID;
            var deferred = $q.defer();
            var StartTimes = new Date($rootScope.modalsStartTimeCreate.replace(/-/g, "/")).getTime();
            var EndTimes = new Date($rootScope.modalsEndTimeCreate.replace(/-/g, "/")).getTime();
            if (StartTimes > EndTimes) {
                $.notify({ message: "时间区间错误", status: "danger" });
                $("#createProjectEndTime").css("color", "red");
                $("#createProjectStartTime").css("color", "red");
                return false;
            };

            var data = {
                id: self.id,
                name: $scope.CreateProjectModalName,
                desc: $scope.createDescription,
                startTime: $rootScope.modalsStartTimeCreate,
                endTime: $rootScope.modalsEndTimeCreate,
                leaderId: $scope.OriginatorRange,
            };

            $http.post('/Portal/project/createProject', data).success(function (data) {
                deferredHandler(data, deferred);
                $.notify({ message: data.msg, status: "success" });
            }).error(function (data) {

                deferredHandler(data, deferred, $translate.instant('error_creating_folder'));
            })['finally'](function () {
            });
            return deferred.promise;
        }


        $scope.createProject = function () {
            var msg = "";
            var projectName = $scope.CreateProjectModalName;
            var createStartTime = $rootScope.modalsStartTimeCreate;
            var createEndTime = $rootScope.modalsEndTimeCreate;
            $scope.OriginatorRange = $("#CraeteProjectModalLeader").SheetUIManager().GetValue();
            var createleaderid = $scope.OriginatorRange;
            if (!projectName) {
                msg += '请输入项目名称 | ';
            }

            if (!createStartTime) {
                msg += '请选择开始时间 | ';
            }
            if (!createEndTime) {
                msg += '请选择结束时间 | ';
            }
            if (!createleaderid) {
                msg += '请选择负责人 | ';
            }
            msg = msg.substr(0, msg.length - 3);
            if (!msg) {

                createModalsProject().then(function () {
                    $("#tabProjectManager").dataTable().fnDraw();

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

        removeModalsProjectManage = function () {
            var self = params.AgencyID;
            var deferred = $q.defer();
            var data = {
                id: params.AgencyID.id
            };

            $http({
                method: "GET",
                url: '/Portal/project/deleteProject?id=' + params.AgencyID.id,
            }).success(function (data) {
                deferredHandler(data, deferred);
                $.notify({ message: data.msg, status: "success" });
            }).error(function (data) {
                deferredHandler(data, deferred, $translate.instant('error_deleting'));
            })['finally'](function () {

            });
            return deferred.promise;
        }
        $scope.removeProjectManager = function () {
            $scope.name = "123"
            removeModalsProjectManage().then(function () {
                $("#tabProjectManager").dataTable().fnDraw();
            });
            $scope.cancel();
        }
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel'); // 退出
        }
        $rootScope.cancel = $scope.cancel;

    }]);



