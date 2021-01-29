app.controller("testManageController", ["$scope", '$q', "$rootScope", "$http", "$translate", "$state", "$filter", "$modal", "$modalInstance", "$interval", "$timeout", "ControllerConfig", "notify", "datecalculation", "params",
    function ($scope, $q, $rootScope, $http, $translate, $state, $filter, $modal, $modalInstance, $interval, $timeout, ControllerConfig, notify, datecalculation, params) {
        $.ajax({
            "url": "/Portal/system/findDictionaryData",
            "type": "post",
            "data": { categoryCode: "交易系统"},
            "dataType": "json",
            "success": function (data) {
                $scope.testEnvData = data.data;
                var testEnvData= [];
                testEnvData= $scope.testEnvData;
                //定义一个对象数组，用于储存所需选项
                for (var i = 0; i < testEnvData.length; i++) {
                    $("#testEnvSystemName").append($("<option value=\"" + testEnvData[i].code + "\">" + testEnvData[i].code + "</option>"));
                    $("#editTestEnvSystemName").append($("<option value=\"" + testEnvData[i].code + "\">" + testEnvData[i].code + "</option>"));

                }
                $('#testEnvSystemName').selectpicker({
                    noneResultsText: '无搜索结果',//用于设置无搜索结果的提示
                    noneSelectedText: '没有选中内容'
                })
                $('#editTestEnvSystemName').selectpicker({
                    noneResultsText: '无搜索结果',//用于设置无搜索结果的提示
                    noneSelectedText: '没有选中内容'
                })

                var editTestEnvSystemName = $("#editTestEnvSystemName").find("option"); //获取select下拉框的所有值
                for (var j = 1; j < editTestEnvSystemName.length; j++) {
                    if ($(editTestEnvSystemName[j]).val() == $rootScope.editTestEnvSystemName) {
                        $(editTestEnvSystemName[j]).attr("selected", "selected");
                        $('#editTestEnvSystemName').selectpicker('refresh');
                        $('#editTestEnvSystemName').selectpicker('render');
                    }
                }

          
              },
            "error": function () {
            }
          })
          $.ajax({
            "url": "/Portal/system/findDictionaryData",
            "type": "post",
            "data": { categoryCode: "交易环境类别"},
            "dataType": "json",
            "success": function (data) {
                $scope.testEnvData = data.data;
                var testEnvData= [];
                testEnvData= $scope.testEnvData;
                //定义一个对象数组，用于储存所需选项
                for (var i = 0; i < testEnvData.length; i++) {
                    $("#testEnvEnvironmentalCategory").append($("<option value=\"" + testEnvData[i].code +"\">" + testEnvData[i].code + "</option>"));
                    $("#editTestEnvEnvironmentalCategory").append($("<option value=\"" + testEnvData[i].code +"\">" + testEnvData[i].code + "</option>"));
                  
                }
                var editTestEnvEnvironmentalCategory = $("#editTestEnvEnvironmentalCategory").find("option"); //获取select下拉框的所有值
                for (var j = 1; j < editTestEnvEnvironmentalCategory.length; j++) {
                    if ($(editTestEnvEnvironmentalCategory[j]).val() == $rootScope.editTestEnvEnvironmentalCategory) {
                        $(editTestEnvEnvironmentalCategory[j]).attr("selected", "selected");
                    }
                }
              },
            "error": function () {
            }
          })
        $.ajax({
            "url":'/Portal/testManage/listAllTestEnv',
            "type":'post',
            "data":"",
            "datatype":'json',
            "success":function(data){
                $scope.testEnvEnvData = data.data;
                var testEnvEnvData= [];
                testEnvEnvData= $scope.testEnvEnvData;
                for(var i=0;i<testEnvEnvData.length;i++){
                    $('#testEnvDockingEnvironment0').append("<option value=\"" + testEnvEnvData[i].id+"\">" + testEnvEnvData[i].text + "</option>")
                    if(typeof($scope.editTestEnvDockingEnvironmentList)!='undefined'){
                        for(var j=0;j<$scope.editTestEnvDockingEnvironmentList.length;j++){
                            $('#editTestEnvDockingEnvironment'+j).append("<option value=\"" + testEnvEnvData[i].id+"\">" + testEnvEnvData[i].text + "</option>")
                        }
                    }
                    
                }
                if(typeof($rootScope.editTestEnvDockingEnvironmentList)!='undefined'){
                    for(var i=0;i<$rootScope.editTestEnvDockingEnvironmentList.length;i++){
                        var editTestEnvDockingEnvironment = $("#editTestEnvDockingEnvironment"+i).find("option"); //获取select下拉框的所有值
                        for (var j = 0; j < editTestEnvDockingEnvironment.length; j++) {
                            if (editTestEnvDockingEnvironment[j].value == $rootScope.editTestEnvDockingEnvironmentList[i].id) {
                                $(editTestEnvDockingEnvironment[j]).attr("selected", "selected");
    
                            }
                        }
                    }
                }
                
            },
            "error":function(){
            }
        })
        $scope.testEnvList=[{name:"",ip:"",os:"",cpu:"",ram:"",diskSize:""}];
        $scope.testEnvDockingEnvironmentList=[{id:'',text:''}];
          //新建虚机增加输入框
        $scope.increate=function($index){
            $scope.testEnvList.splice($index+1,0,
                {name:"",ip:"",os:"",cpu:"",ram:"",diskSize:""})
        }
        //新建虚机减少输入框
        $scope.delete = function ($index) {
            if ($scope.testEnvList.length > 1) {
                $scope.testEnvList.splice($index, 1);
            }
        }
        //新建对接环境增加输入框
        $scope.increate2 = function ($index) {
            $scope.testEnvDockingEnvironmentList.splice($index + 1, 0,
                { id:'',text:'' })
                $.ajax({
                    "url":'/Portal/testManage/listAllTestEnv',
                    "type":'post',
                    "data":"",
                    "datatype":'json',
                    "success":function(data){
                        $scope.testEnvEnvData = data.data;
                        var testEnvEnvData= [];
                        testEnvEnvData= $scope.testEnvEnvData;
                        for(var i=0;i<testEnvEnvData.length;i++){
                            var indexs=$index+1;
                            $('#testEnvDockingEnvironment'+indexs).append("<option value=\"" + testEnvEnvData[i].id+"\">" + testEnvEnvData[i].text + "</option>")
                        }
                    },
                    "error":function(){
                    }
                })
        }
        //新建对接环境减少输入框
        $scope.delete2 = function ($index) {
            if ($scope.testEnvDockingEnvironmentList.length > 1) {
                $scope.testEnvDockingEnvironmentList.splice($index, 1);
            }
        }
        //编辑虚机增加输入框
        $scope.increate1 = function ($index) {
            $scope.editTestEnvList.splice($index + 1, 0,
                {name:"",ip:"",os:"",cpu:"",ram:"",diskSize:""})
        }
        //编辑虚机减少输入框
        $scope.delete1 = function ($index) {
            if ($scope.editTestEnvList.length > 1) {
                $scope.editTestEnvList.splice($index, 1);
            }
        }
         //编辑对接环境增加输入框
         $scope.increate3 = function ($index) {
            $scope.editTestEnvDockingEnvironmentList.splice($index + 1, 0,
                { id:"",text:"" })
                $.ajax({
                    "url":'/Portal/testManage/listAllTestEnv',
                    "type":'post',
                    "data":"",
                    "datatype":'json',
                    "success":function(data){
                        $scope.testEnvEnvData = data.data;
                        var testEnvEnvData= [];
                        testEnvEnvData= $scope.testEnvEnvData;
                        for(var i=0;i<testEnvEnvData.length;i++){
                            var editindexs=$index+1;
                            $('#editTestEnvDockingEnvironment'+editindexs).append("<option value=\"" + testEnvEnvData[i].id+"\">" + testEnvEnvData[i].text + "</option>")
                        }
                    },
                    "error":function(){
                    }
                })
        }
        //编辑对接环境减少输入框
        $scope.delete3 = function ($index) {
            if ($scope.editTestEnvDockingEnvironmentList.length > 1) {
                $scope.editTestEnvDockingEnvironmentList.splice($index, 1);
            }
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
               
            }
        }

        // $scope.hiddenJumpEnv=function(){
        //    if(typeof($rootScope.detailTestEnvDockingEnvironmentList)!='undefined'){
        //     for(var i=0;i<$rootScope.detailTestEnvDockingEnvironmentList.length;i++){
        //         if($rootScope.detailTestEnvDockingEnvironmentList[i].text=="无"){
        //             $('#detailTestEnvBtn'+i).css({ "display": "none" })
        //         }
        //       }
        //    }
        // }
        // $scope.hiddenJumpDetailEnv=function(){
        //     if(typeof($rootScope.detailEnvTestManageDockingEnvironmentList)!='undefined'){
        //         for(var i=0;i<$rootScope.detailEnvTestManageDockingEnvironmentList.length;i++){
        //             if($rootScope.detailEnvTestManageDockingEnvironmentList[i].text=="无"){
        //                 $('#detailEnvTestManageBtn'+i).css({ "display": "none" })
        //             }
        //           }
        //        }
        // }
        // setTimeout(() => {
        //     $scope.hiddenJumpEnv();
        //     $scope.hiddenJumpDetailEnv();
        // }, 500);
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
        //编辑
        editModalsProjectManager = function () {
            var self = params.AgencyID;
            for (var i = 0; i < $scope.editTestEnvDockingEnvironmentList.length; i++) {
                $scope.editTestEnvDockingEnvironmentList[i].id = $('#editTestEnvDockingEnvironment' + i).val();
                $scope.editTestEnvDockingEnvironmentList[i].text = $("#editTestEnvDockingEnvironment" + i + " option:selected").text();
            }
            var deferred = $q.defer();
            var data = {
                id: self.id,
                name: $scope.editTestEnvModalName,//名称
                systemName: $('#editTestEnvSystemName').val(),//系统名
                envType: $('#editTestEnvEnvironmentalCategory').val(),//环境类别
                vmDatas:$scope.editTestEnvList,//虚机数据
                envs:$scope.editTestEnvDockingEnvironmentList,//对接环境
                desc:$scope.editTestEnvDescription,//描述
                joinAddress:$scope.editTestEnvAddress

            };
            $http.post(' /Portal/testManage/updateTestEnv', data).success(function (data) {
                deferredHandler(data, deferred);
                $.notify({ message: data.msg, status: "success" });
            }).error(function (data) {
                deferredHandler(data, deferred, $translate.instant('error_creating_folder'));
            })['finally'](function () {
                // self.inprocess = false;
            });
            return deferred.promise;
        }

        //编辑
        $scope.editTestEnv = function () {
            var projectName = $scope.editTestEnvModalName;
            var msg = "";
            if (!projectName) {
                msg += '请输入项目名称 | ';
            }
            var vmDatas=$scope.editTestEnvList;
            for(var i=0;i<vmDatas.length;i++){
                if(vmDatas[i].cpu!=''){
                    if(isNaN(vmDatas[i].cpu)){
                        msg += 'cpu输入需为数字类型 | ';
                    }
                }
                if(vmDatas[i].ram!=''){
                    if(isNaN(vmDatas[i].ram)){
                        msg += '内存大小输入需为数字类型 | ';
                    }
                }
                if(vmDatas[i].diskSize!=''){
                    if(isNaN(vmDatas[i].diskSize)){
                        msg += '硬盘大小输入需为数字类型 | ';
                    }
                }
            }
            msg = msg.substr(0, msg.length - 3);
            if (!msg) {
                editModalsProjectManager().then(function () {
                    $("#tabTestEnv").dataTable().fnDraw();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
        //新建
        createTestEnvModalsProject = function () {
            var self = params.AgencyID;
            var deferred = $q.defer();
            for (var i = 0; i < $scope.testEnvDockingEnvironmentList.length; i++) {
                $scope.testEnvDockingEnvironmentList[i].text = $("#testEnvDockingEnvironment"+i+ " option:selected").text();
                $scope.testEnvDockingEnvironmentList[i].id = $("#testEnvDockingEnvironment"+i).val();
            }
            var data = {
                name:$scope.createTestEnvModalName,//名称
                systemName: $('#testEnvSystemName').val(),//系统名
                envType:$('#testEnvEnvironmentalCategory').val(),//环境类别
                vmDatas:$scope.testEnvList,//虚机数据
                envs:$scope.testEnvDockingEnvironmentList,//对接环境
                desc:$scope.createTestEnvDescription,
                joinAddress:$scope.createTestEnvAddress
            };
            $http.post('/Portal/testManage/createTestEnv', data).success(function (data) {
                deferredHandler(data, deferred);
                $.notify({ message: data.msg, status: "success" });
            }).error(function (data) {
                deferredHandler(data, deferred, $translate.instant('error_creating_folder'));
            })['finally'](function () {
            });
            return deferred.promise;
        }
        $scope.createTestEnv = function () {
            var msg = "";
            var projectName = $scope.createTestEnvModalName;
            var vmDatas=$scope.testEnvList;
            for(var i=0;i<vmDatas.length;i++){
                if(vmDatas[i].cpu!=''){
                    if(isNaN(vmDatas[i].cpu)){
                        msg += 'cpu输入需为数字类型 | ';
                    }
                }
                if(vmDatas[i].ram!=''){
                    if(isNaN(vmDatas[i].ram)){
                        msg += '内存大小输入需为数字类型 | ';
                    }
                }
                if(vmDatas[i].diskSize!=''){
                    if(isNaN(vmDatas[i].diskSize)){
                        msg += '硬盘大小输入需为数字类型 | ';
                    }
                }
            }
            if (!projectName) {
                msg += '请输入项目名称 | ';
            }
            msg = msg.substr(0, msg.length - 3);
            if (!msg) {
                createTestEnvModalsProject().then(function () {
                    $("#tabTestEnv").dataTable().fnDraw();
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
                url: ' /Portal/testManage/deleteTestEnv?id=' + params.AgencyID.id,
            }).success(function (data) {
                deferredHandler(data, deferred);
                $.notify({ message: data.msg, status: "success" });
            }).error(function (data) {
                deferredHandler(data, deferred, $translate.instant('error_deleting'));
            })['finally'](function () {

            });
            return deferred.promise;
        }
        $scope.removeTestEnv = function () {
            $scope.name = "123"
            removeModalsProjectManage().then(function () {
                $("#tabTestEnv").dataTable().fnDraw();
            });
            $scope.cancel();
        }
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel'); // 退出
        }
        $rootScope.cancel = $scope.cancel;
        $scope.jumpEnv=function($index){
            $rootScope.loading=true;
           
                $.ajax({
                    url:"/Portal/testManage/getTestEnvById?id="+$rootScope.detailTestEnvDockingEnvironmentList[$index].id,
                    method:"GET",
                    success:function(data){
                        var data=data.data
                        $rootScope.detailTestEnvModalName=data.name;
                        $rootScope.detailTestEnvSystemName=data.systemName
                        $rootScope.detailTestEnvEnvironmentalCategory=data.envType
                        $rootScope.detailTestEnvList=data.vmDatas
                        $rootScope.detailTestEnvDescription=data.desc
                        $rootScope.detailTestEnvDockingEnvironmentList=data.envs
                        

                        $rootScope.loading=false;
                        $scope.$apply();
                    }
    
                })
               
                
                
        }
        $scope.jumpDetailEnv=function($index){
            $rootScope.loading=true;
                $.ajax({
                    url:"/Portal/testManage/getTestEnvById?id="+$rootScope.detailEnvTestManageDockingEnvironmentList[$index].id,
                    method:"GET",
                    success:function(data){
                        var data=data.data
                        $rootScope.detailEnvTestManageModalName=data.name;
                        $rootScope.detailEnvTestManageSystemName=data.systemName
                        $rootScope.detailEnvTestManageEnvironmentalCategory=data.envType
                        $rootScope.detailEnvTestManageList=data.vmDatas
                        $rootScope.detailEnvTestManageDescription=data.desc
                        $rootScope.detailEnvTestManageDockingEnvironmentList=data.envs
                        $rootScope.loading=false;
                        $scope.$apply();
                    }
    
                })
              
               
                
        }
    }]);



