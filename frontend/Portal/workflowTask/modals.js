app.controller("workflowTaskController", ["$scope", '$q', "$rootScope", "$http", "$translate", "$state", "$filter", "$modal", "$modalInstance", "$interval", "$timeout", "ControllerConfig", "notify", "datecalculation", "params",
    function ($scope, $q, $rootScope, $http, $translate, $state, $filter, $modal, $modalInstance, $interval, $timeout, ControllerConfig, notify, datecalculation, params) {
        $scope.params = params;
      
        $scope.workFlow = [
            { workFlowname: '清算', id: "liquidation" },
        ];
        params.AgencyID;

        $scope.init = function () {
            if (params.AgencyID!=undefined) {
            if (params.AgencyID!="") {
                $scope.editOperatorWorkFlow = params.AgencyID.workflowCodeStr;
                $scope.editOperatorName =params.AgencyID.userDisplayName;
                var time = new Date(params.AgencyID.startTime);
                
                var year = time.getFullYear();
                var month = time.getMonth() + 1
                var date = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
                var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
                var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
                params.AgencyID.startTime = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
                $scope.editOperatorCreateTime = params.AgencyID.startTime;
                if (params.AgencyID.executeType == 'null' || params.AgencyID.executeType == null) {
                    params.AgencyID.executeType = '';
                }
               else if(params.AgencyID.executeType==1){
                params.AgencyID.executeType="是"
               }
               else if(params.AgencyID.executeType==0){
                params.AgencyID.executeType="否"
               }
                $scope.editOperatorIfCreate = params.AgencyID.executeType;
            }
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


        $scope.cancel = function () {
            $modalInstance.dismiss('cancel'); // 退出
        }
        $rootScope.cancel = $scope.cancel;

        startUpload = function () {
            var deferred = $q.defer();
            var fd = new FormData();
            $rootScope.loading=true;
            fd.append("file", $('#upload')[0].files[0]);
            fd.append("workflowCode", $scope.workflowModal);
            $.ajax({
                url: '/Portal/workflowTask/importTask',
                type: 'post',
                data: fd,
                processData: false,
                contentType: false,
                success: function (data) {
                    deferredHandler(data, deferred);
                    $.notify({ message: data.msg, status: "success" });
                },
                error: function (data) {
                    deferredHandler(data, deferred, $translate.instant('error_creating_folder'));
                }

            }).then(function(){
                $rootScope.loading=false;
            })
            return deferred.promise;
           

        };




        $scope.myStartUploadFile = function () {
            var hasFile = $('#upload').get(0).files[0];
            var myselect = document.getElementById('workflowModal');
            $scope.index = myselect.selectedIndex;
            if ($scope.index === 0) {
                $scope.workflowModal=$scope.workFlow[0].id
            }
            else {
                $scope.workflowModal = $scope.workFlow[$scope.index - 1].id;
            }

            var msg = '';
            if (!hasFile) {
                msg += '请上传文件 | ';
            }
            if (!$scope.workflowModal) {
                msg += '请选择流程名| ';
            }
            msg = msg.substr(0, msg.length - 3);
            if (!msg) {
                startUpload().then(function () {
                    $("#tabWorkFlowTask").dataTable().fnDraw();

                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
       //下载模板文件
        $scope.modalsTaskdownload=function(){
            var msg = '';
            var myselect = document.getElementById('workflowModal');
            $scope.index = myselect.selectedIndex;
            if ($scope.index === 0) {
                $scope.workflowModal=$scope.workFlow[0].id
            }
            else {
                $scope.workflowModal = $scope.workFlow[$scope.index - 1].id;
            }
            
            if (!$scope.workflowModal) {
                msg += '请选择流程名| ';
            }
            msg = msg.substr(0, msg.length - 3);
            if(!msg){
                window.open('/Portal/workflowTask/downloadTemplate?workflowCode=' +$scope.workflowModal);
            }
            else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
                
          
               
        }


        editWorkFlow = function () {

           
            var deferred = $q.defer();
            var data = {
                id:params.AgencyID.id,
                userDisplayName: $scope.editOperatorName,
            };

            $http.post('/Portal/workflowTask/updateWorkFlowTaskUser', data).success(function (data) {
             deferredHandler(data, deferred); 
              $.notify({ message: data.msg, status: "success" });
              $scope.cancel();
              
            }).error(function (data) {
                // deferredHandler(data, deferred, $translate.instant('error_creating_folder'));
                $.notify({ message: data.msg, status: "danger" });
            })['finally'](function () {

            });
            return deferred.promise;
        }

        //编辑
        $scope.editOWorkflowOperator = function () {

            var OperatorName = $scope.editOperatorName;
            var msg = "";
            if (!OperatorName) {
                msg += '请输入执行人名字 | ';
            }
           
            msg = msg.substr(0, msg.length - 3);
            if (!msg) {
                editWorkFlow().then(function () {
                    $("#tabWorkFlowTask").dataTable().fnDraw();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            
        };


    }]);



