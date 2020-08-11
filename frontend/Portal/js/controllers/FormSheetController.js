(function (window, angular, $) {
    'use strict';
    app.controller('formSheetServiceCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", "$location", '$modal', 'item', 'fileNavigator', 'fileManagerConfig',
        function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $location, $modal, Item, FileNavigator, fileManagerConfig) {

            //渲染标签的数据
            $scope.renderTag = function(idName) {
                var obj = $("#"+idName);
                //-----标签-start  为了下拉框有数据，所以此处引用了json的数据，也可挪到其他地方使用-----
                var sltCategoryComBox;//拿到所有数据，然后再重新加载页面数据
                var initId = "";
                var initValue = "";
                var tmpData = [];

                //渲染数据的地方
                sltCategoryComBox = obj.ligerComboBox({
                    initValue: initId,
                    initText: initValue,
                    data: tmpData,
                    valueFieldID: 'category',
                    url: '/Portal/tag/listKnowledgeTagByName',
                    ajaxType: 'GET',
                    valueField: 'id',
                    textField: 'text',
                    autocomplete: true,
                    setTextBySource: true,
                    keySupport: true
                });

                //设置想要的样式
                obj.parent().removeClass();
                var inputHeight = obj.outerHeight();
                var inputWidth = obj.outerWidth();
                obj.css({"border":"1px solid #d9d9d9","border-radius":"4px"});
                //下拉框的内容样式设置,模态框样式有点bug
                $("div.l-box-select-inner").parent().css({"margin-top": inputHeight + 'px'});
                $("div.l-box-select").css({"width": inputWidth - 2 + 'px'});
                obj.mouseleave(function (e) {
                    var x = e.pageX - obj.offset().left;
                    var y = e.pageY - obj.offset().top;
                    if(x < 0 || y < 0 || x - inputWidth > 0 ){
                        $("div.l-box-select").hide();
                    }
                });

                //update by zhangj
                obj.change(function () {//按回车键触发该函数
                    var id = $("#category").val();//获取的是数据中的，选中数据条的id
                });
            }

            /*************已办任务收藏到我的知识模态框**********/
            $scope.toCollectFinishedFlow = function (data) {
                var tempData = "";
                var AgencyID;
                if (tempData == undefined) AgencyID = "";
                else AgencyID = tempData;
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
                            templateUrl: 'collectFinishedFlow.html',    // 指向上面创建的视图
                            controller: 'FormSheetModalsController',// 初始化模态范围
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

                //加载标签
                setTimeout(function () {
                    $scope.renderTag('collectTag');
                },500);
            }

            /*************已办任务分享到共享知识模态框**********/
            $scope.toShareFinishedFlow = function (data) {
                var tempData = "";
                var AgencyID;
                if (tempData == undefined) AgencyID = "";
                else AgencyID = tempData;
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
                            templateUrl: 'shareFinishedFlow.html',    // 指向上面创建的视图
                            controller: 'FormSheetModalsController',// 初始化模态范围
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

                //加载标签
                setTimeout(function () {
                    $scope.renderTag('shareTag');
                },500);
            }

        }]);
    app.controller("FormSheetModalsController", ["$scope", "$rootScope", "$http", "$translate", "$state", "$filter", "$modal", "$modalInstance", "$interval", "$timeout", "ControllerConfig", "datecalculation", "params","notify",
        function ($scope, $rootScope, $http, $translate, $state, $filter, $modal, $modalInstance, $interval, $timeout, ControllerConfig, datecalculation, params, notify) {

            //控件初始化参数
            $scope.WasAgentOptions = {
                Editable: true, Visiable: true, IsMultiple: true //全选属性
            }

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

            $scope.name;
            $scope.tag;
            $scope.desc;

            var url = window.location.search;//absUrl()//获取flowId
            // var str = `http://localhost:8088/Portal/formSheet.html?SheetCode=Sbusiness_exception&Mode=View&WorkItemID=ef021fe4-6b83-44d9-a23a-401e6ac962c2&&T=090919&localLan=zh_CN#/platform/login"`
            var arr = url.split('&');
            var val = null;
            for(var item of arr){
                if(item.includes('WorkItemID')){
                    val = item.split('=')[1];
                    break;
                }
            }


            //收藏知识的数据交互
            $scope.collectFlow = function(){
                var data = {}
                var msg = '';
                if(!$scope.name){
                    msg +='请输入知识名称 | ';
                }
                if(!$scope.desc){
                    msg += '请输入描述 | ';
                }
                msg = msg.substr(0,msg.length-3);
                if (!msg) {
                    data = {
                        flowId: val,
                        name: $scope.name,
                        desc: $scope.desc,
                        tagName: $scope.tag,
                        startTime: $scope.StartTime,
                        endTime: $scope.EndTime,
                    };
                    $http.post('/Portal/knowledgeManage/collectFlowToMyKnowledge', data).success(function (data) {
                        alert(data.msg);
                    }).error(function (data) {
                        alert(data.msg);
                    })['finally'](function () {

                    });
                } else {
                    $.notify({ message: msg, status: "danger" });
                    alert(msg);
                    return false;
                }
                $scope.cancel();
            }

            //分享知识的数据交互
            $scope.shareFlow = function(){
                var data = {}
                var permission = $("#sharePermission").SheetUIManager();
                var orgs = permission.GetValue();

                var msg = '';
                if(!permission || !orgs){
                    msg += '请输入权限 | ';
                }
                if(!$scope.name){
                    msg +='请输入知识名称 | ';
                }
                // if($rootScope.flowScope.fileNavigator.fileNameExists(foldername)){// 这个方法还未写似乎？
                //     msg +='有重名知识，请重新命名 | ';
                // }
                if(!$scope.desc){
                    msg += '请输入描述 | ';
                }
                msg = msg.substr(0,msg.length-3);
                if (!msg) {
                    data = {
                        flowId: val,
                        name: $scope.name,
                        desc: $scope.desc,
                        tagName: $scope.tag,
                        startTime: $scope.StartTime,
                        endTime: $scope.EndTime,
                        permission : {
                            knowledgeId: null,
                            orgs: orgs,
                            userList: []
                        }
                    };
                    $http.post('/Portal/knowledgeManage/shareFlowToKnowledge', data).success(function (data) {
                        alert(data.msg);
                    }).error(function (data) {
                        alert(data.msg);
                    })['finally'](function () {

                    });
                } else {
                    $.notify({ message: msg, status: "danger" });
                    alert(msg);
                    return false;
                }
                $scope.cancel();
            }

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel'); // 退出
            }
            $rootScope.cancel = $scope.cancel;

        }]);
})(window, angular, jQuery);