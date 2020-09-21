app.controller("ModalsController", ["$scope", "$rootScope", "$http", "$translate", "$state", "$filter", "$modal", "$modalInstance", "$interval", "$timeout", "ControllerConfig", "notify", "datecalculation", "params",
    function ($scope, $rootScope, $http, $translate, $state, $filter, $modal, $modalInstance, $interval, $timeout, ControllerConfig, notify, datecalculation, params) {
        $scope.config = $rootScope.scope.config;
        var _newscope = $rootScope.scope;
        $scope.fileNavigator = _newscope.fileNavigator;
        $scope.temp = $rootScope.temp;
        $scope.fileMemuTile = $rootScope.rootdir;
        $scope.descList=[{key:new Date().getTime(),desc:"",detail:""}];
        $scope.getLanguage = function () {
            $scope.LanJson = {
                StartTime: $translate.instant("QueryTableColumn.StartTime"),
                EndTime: $translate.instant("QueryTableColumn.EndTime"),
                //update by zhangj
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

        //控件初始化参数
        $scope.WorkflowOptions = {
            Editable: true, Visiable: true, Mode: "WorkflowTemplate", IsMultiple: true, PlaceHolder: $scope.LanJson.WorkFlow, IsSearch:true
        }
        $scope.WasAgentOptions = {
            Editable: true, Visiable: true, PlaceHolder: $scope.LanJson.Originator, IsMultiple: true //全选属性
        }

		//设置权限控件不可编辑
        $scope.DetailOptions = {
            Visiable: true, IsMultiple: true //全选属性
        }
        $scope.OriginatorRangeOptions = {
            Editable: true, Visiable: true, OrgUnitVisible: false, UserVisible: false, PlaceHolder: $scope.LanJson.Originator,GroupVisible:true,IsMultiple: true
        }

        $scope.init = function () {
            $scope.user = params.user;
            $scope.IsAllWorkflow = "true";
            //编辑初始化
            if (params.AgencyID != "") {
                $scope.IsEdit = true;
                var Agency = params.Agency;
                if (Agency.WorkflowCode == "") {
                    $scope.IsAllWorkflow = "true";
                }
                else {
                    $scope.IsAllWorkflow = "false";
                    $scope.WorkflowOptions.V = Agency.WorkflowCode;
                    $scope.WorkflowCodes = Agency.WorkflowCode;
                }
                $scope.StartTime = Agency.StartTime;
                $scope.EndTime = Agency.EndTime;

                $scope.WorkflowOptions.Editable = false;
                $scope.WasAgentOptions.V = Agency.WasAgentID;
                //$scope.OriginatorRangeOptions.V = Agency.OriginatorRange

            }
        }
        $scope.init();

        // 功能：获取权限，参数：jq对象，返回值：权限的对象
        $scope.getPermission = function(jqObj){
            //权限
            var fileId = null;//文件的id
            var orgList = [];//组织的对象数组
            var userList = [];//个人的对象数组

            var H3Obj = jqObj.SheetUIManager();
            if(H3Obj){
                if(H3Obj.GetValue()){
                    var idArray = H3Obj.GetValue();// 获取的是组织或者人员的id
                    var nameArray = H3Obj.GetText().split(',');// 可以获取组织或者人员的名称

                    var len = idArray.length;
                    for(var i = 0;i < len;i++){
                        orgList.push({
                            id:idArray[i],
                            name: nameArray[i]
                        });
                        // if(IsUser){userList.push({})}
                    }
                }else{
                    return;
                }
            }else{
                return;
            }
            return {
                "fileId": fileId,
                "orgList": orgList,
                "userList": userList
            };
        }

        //选择收藏数据交互
        $rootScope.selectCollect = function(item, temp) {//item目前的新数据，temp以前的旧数据
            item.tempModel.id = temp.model.id;
            item.selectCollect().then(function () {
                //收藏之后要刷新
                _newscope.fileNavigator.refreshCollectFile();
            });
        }

        //选择分享数据交互
        $rootScope.selectShare = function(item, temp) {//item目前的新数据，temp以前的旧数据没有权限
            item.tempModel.id = temp.model.id;
            var permission = $scope.getPermission($("#shareFile"))
            item.tempModel.filePermission = permission;
            if(!permission){
                $.notify({ message: "请选择权限！", status: "danger" });
                return false;
            }
            item.selectShare().then(function () {
                //没有进来
                _newscope.fileNavigator.refreshShareFile();
            });
        }

        //-----共享文件 start
        //新建文件夹数据交互
        $scope.createFolder = function (item) {
            var foldername = item.tempModel.name && item.tempModel.name.trim();//文件夹名称
            item.tempModel.filePermission =  $scope.getPermission($("#folderPer"));
            item.tempModel.type = 'dir';
            item.tempModel.path = _newscope.fileNavigator.currentPath;
            if(_newscope.fileNavigator.currentFileId == ""){
                item.tempModel.id = null;
            }else{
                item.tempModel.id = _newscope.fileNavigator.currentFileId;
            }
            item.tempModel.name = foldername;

            var msg = '';
            if(!foldername){
                msg +='请输入名称 | ';
            }
            if(_newscope.fileNavigator.fileNameExists(foldername)){
                msg +='有重名文件，请重新命名 | ';
            }

            if(!item.tempModel.filePermission){
                msg +='请选择权限 | ';
            }
            msg = msg.substr(0,msg.length-3);

            if (!msg) {
                item.createFolder().then(function () {
                    _newscope.fileNavigator.refresh();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
        //上传文件的数据交互
        $scope.startUploadFile = function (e) {
            $scope.fileNavigator = _newscope.fileNavigator;
            var permission = $("#uploadPer").SheetUIManager().GetValue();
            var hasFile = $("#selectFile").find(".ajax-file-upload-container").html();
            var msg = '';
            if(!hasFile){
                msg +='请上传文件 | ';
            }
            if(!permission){
                msg +='请选择权限 | ';
            }
            msg = msg.substr(0,msg.length-3);
            if(!msg){
                $scope.fileNavigator.currentFileId;
                $scope.fileNavigator.filePermission = permission.join(',');
                extraObj.startUpload();
            }else{
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
        //更改路径数据交互
        $scope.updateFile = function (item) {
            item.tempModel.filePermission = $scope.getPermission($("#editPer"));
            $scope.fileNavigator = _newscope.fileNavigator;
            var foldername = item.tempModel.name && item.tempModel.name.trim();//文件夹名称
            var samePath = item.tempModel.path.join() === item.model.path.join();
            var msg = '';
            if(!foldername){
                msg +='请输入名称 | ';
            }
            msg = msg.substr(0,msg.length-3);
            if (!msg) {
                item.updateFile().then(function () {
                    $scope.fileNavigator.refresh();
                });
            }else{
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
        //删除文件
        $scope.removeFile = function(item){
            item.removeFile().then(function () {
                $scope.fileNavigator.refresh();
            });
            $scope.cancel();
        }
        //-----共享文件 end


        //-----我的文件 start
        //新建文件夹数据交互
        $scope.myCreateFolder = function (item) {
            var foldername = item.tempModel.name && item.tempModel.name.trim();//文件夹名称
            item.tempModel.type = 'dir';
            item.tempModel.path = _newscope.fileNavigator.currentPath;
            if(_newscope.fileNavigator.currentFileId == ""){
                item.tempModel.id = null;
            }else{
                item.tempModel.id = _newscope.fileNavigator.currentFileId;
            }
            item.tempModel.name = foldername;

            var msg = '';
            if(!foldername){
                msg +='请输入名称 | ';
            }
            if(_newscope.fileNavigator.fileNameExists(foldername)){
                msg +='有重名文件，请重新命名 | ';
            }
            msg = msg.substr(0,msg.length-3);
            if (!msg) {
                item.myCreateFolder().then(function () {
                    _newscope.fileNavigator.refresh();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
        //上传文件的数据交互
        $scope.myStartUploadFile = function (e) {
            var hasFile = $("#selectMyFile").find(".ajax-file-upload-container").html();
            var msg = '';
            if(!hasFile){
                msg +='请上传文件 | ';
            }
            msg = msg.substr(0,msg.length-3);
            if(!msg){
                extraObj.startUpload();
            }else{
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
        //更改路径数据交互
        $scope.myUpdateFile = function (item) {
            var foldername = item.tempModel.name && item.tempModel.name.trim();//文件夹名称
            $scope.fileNavigator = _newscope.fileNavigator;
            var samePath = item.tempModel.path.join() === item.model.path.join();
            var msg = '';
            if(samePath && $scope.fileNavigator.fileNameExists(item.tempModel.name)){
                msg += "请编辑 | ";
            }
            if(!foldername){
                msg += "请输入文件夹名称 | ";
            }
            msg = msg.substr(0,msg.length-3);
            if (!msg) {
                item.myUpdateFile().then(function () {
                    $scope.fileNavigator.refresh();
                });
            }else{
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };
        //删除文件
        $scope.myRemoveFile = function(item){
            item.myRemoveFile().then(function () {
                $scope.fileNavigator.refresh();
            });
            $scope.cancel();
        }
        //------我的文件 end

        //控件初始化参数
        $scope.StartTimeOption = {
            dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
            onpicked: function (e) {
                $scope.StartTime = e.el.value;
            }
        }
        $scope.EndTimeOption = {
            dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
            onpicked: function (e) {
                $scope.EndTime = e.el.value;
            }
        }

        $scope.EditStartTime = {
            dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
            onpicked: function (e) {
                $rootScope.temp.model.startTime = e.el.value;
            }
        }
        $scope.EditEndTime = {
            dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
            onpicked: function (e) {
                $rootScope.temp.model.endTime = e.el.value;
            }
        }

        //-----共享知识 start ----------
        //共享知识新建知识增加输入框
        $scope.increate=function($index){
            $scope.descList.splice($index+1,0,
                {key:new Date().getTime(),desc:"",detail:""})
        }
        //共享知识减少输入框
        $scope.delete=function($index){
            if($scope.descList.length>1){
                $scope.descList.splice($index,1);
            }
        }
        //编辑新增输入框
        $scope.increate2=function($index){
            $scope.temp.model.descList.splice($index+1,0,
                {key:new Date().getTime(),desc:"",detail:""})
        }
        //编辑减少输入框
        $scope.delete2=function($index){
            if($scope.temp.model.descList.length>1){
                $scope.temp.model.descList.splice($index,1);
            }
        }
        //新建知识数据交互
        $scope.createFlow = function (item) {
            item.tempModel.startTime = $scope.StartTime;
            item.tempModel.endTime = $scope.EndTime;
            item.tempModel.descList=$scope.descList;
            var permission = $("#flowPermission").SheetUIManager();
            var orgs = permission.GetValue();
            var foldername = item.tempModel.name && item.tempModel.name.trim();//名称
            var msg = '';
            if(!permission || !orgs){
                msg += '请输入权限 | ';
            }else{
                item.tempModel.filePermission = {
                    knowledgeId: null,
                    orgs: orgs,
                    userList: []
                };
            }
            if(!foldername){
                msg +='请输入知识名称 | ';
            }
            if($rootScope.flowScope.fileNavigator.fileNameExists(foldername)){// 这个方法还未写似乎？
                msg +='有重名知识，请重新命名 | ';
            }
            msg = msg.substr(0,msg.length-3);
            if (!msg) {
                item.createFlow().then(function () {
                    $("#tabfinishWorkitem").dataTable().fnDraw();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };

        //更新知识数据交互
        $scope.updateFlow = function (item) {
            var foldername = item.model.name && item.model.name.trim();//name
            // var desc = item.model.desc && item.model.desc.trim();
            
            var permission = $("#editFlowPer").SheetUIManager();
            var orgs = permission.GetValue();
            var msg = '';
            if(!permission || !orgs){
                msg += '请输入权限 | ';
            }else{
                item.model.filePermission = {
                    knowledgeId: null,
                    orgs: orgs,
                    userList: []
                };
            }
            if(!foldername){
                msg +='请输入知识名称 | ';
            }
            // if(!desc){
            //     msg += '请输入描述 | ';
            // }
            msg = msg.substr(0,msg.length-3);
            if (!msg) {
                item.updateFlow().then(function () {
                    $("#tabfinishWorkitem").dataTable().fnDraw();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };

        //收藏知识的数据交互
        $scope.collectFlow = function(item){
            item.collectFlow().then(function () {
                $("#tabMyFlow").dataTable().fnDraw();//在共享知识中收藏到我的知识，刷新我的知识
            });
            $scope.cancel();
        }

        //删除知识的数据交互
        $scope.removeFlow = function(item){
            item.removeFlow().then(function () {
                $("#tabfinishWorkitem").dataTable().fnDraw();
            });
            $scope.cancel();
        }
        //-----共享知识 end ----------

        //-----我的知识 start ---------
        $scope.createMyFlow = function(item){
            item.tempModel.startTime = $scope.StartTime;
            item.tempModel.endTime = $scope.EndTime;
            item.tempModel.descList=$scope.descList;
            var foldername = item.tempModel.name && item.tempModel.name.trim();//名称
            var msg = '';
            if(!foldername){
                msg +='请输入知识名称 | ';
            }
            if($rootScope.flowScope.fileNavigator.fileNameExists(foldername)){// 这个方法还未写似乎？
                msg +='有重名知识，请重新命名 | ';
            }
            msg = msg.substr(0,msg.length-3);
            if (!msg) {
                item.createMyFlow().then(function () {
                    $("#tabMyFlow").dataTable().fnDraw();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };

        //更新知识数据交互
        $scope.updateMyFlow = function (item) {
            var foldername = item.model.name && item.model.name.trim();//name
            // var desc = item.model.desc && item.model.desc.trim();//desc,temp.model.tag,
            var descList=item.model.descList;
            
            var msg="";
            if(!foldername){
                msg +='请输入知识名称 | ';
            }
            // if(!desc){
            //     msg += '请输入描述 | ';
            // }
            msg = msg.substr(0,msg.length-3);
            if (!msg) {
                item.updateMyFlow().then(function () {
                    $("#tabMyFlow").dataTable().fnDraw();
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        };

        //分享知识的数据交互
        $scope.shareFlow = function(item){
            var permission = $("#editMyFlowPer").SheetUIManager();
            var orgs = permission.GetValue();
            var msg = '';
            if(!permission || !orgs){
                msg += '请输入权限 | ';
            }else{
                item.model.filePermission = {
                    knowledgeId: null,
                    orgs: orgs
                };
            }
            msg = msg.substr(0,msg.length-3);
            if (!msg) {
                item.shareFlow().then(function () {
                    $("#tabfinishWorkitem").dataTable().fnDraw();//在我的知识分享到共享知识，刷新共享知识，
                });
            } else {
                $.notify({ message: msg, status: "danger" });
                return false;
            }
            $scope.cancel();
        }

        //删除知识的数据交互
        $scope.removeMyFlow = function(item){
            item.removeMyFlow().then(function () {
                $("#tabMyFlow").dataTable().fnDraw();
            });
            $scope.cancel();
        }
        //-----我的知识 end ----------

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel'); // 退出
        }
        $rootScope.cancel = $scope.cancel;



    }]);