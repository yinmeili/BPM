(function (window, angular, $) {
    'use strict';
    app.controller('FileManagerCtrl', [
        '$scope', '$translate', '$cookies', 'fileManagerConfig', 'item', 'fileNavigator', 'fileUploader','$http', 'ControllerConfig', '$modal',
        "$rootScope", "$state", "$filter",  "$compile", "jq.datables",
        function ($scope, $translate, $cookies, fileManagerConfig, Item, FileNavigator, FileUploader, $http, ControllerConfig, $modal, $rootScope, $state, $filter, $compile, jqdatables) {
            $scope.config = fileManagerConfig;
            $scope.reverse = false;
            $scope.predicate = ['model.type', 'model.name'];
            $scope.order = function (predicate) {
                $scope.reverse = ($scope.predicate[1] === predicate) ? !$scope.reverse : false;
                $scope.predicate[1] = predicate;
            };
            $scope.query = '';
            // $scope.temp = new Item();
            $rootScope.temp = new Item();
            $rootScope.rootdir = $scope.fileMemuTile
            $scope.fileNavigator = new FileNavigator();
            $scope.fileUploader = FileUploader;
            $scope.uploadFileList = [];
            $scope.viewTemplate = $cookies.viewTemplate || 'main-table.html';

            $scope.setTemplate = function (name) {
                $scope.viewTemplate = $cookies.viewTemplate = name;
            };

            $scope.changeLanguage = function (locale) {
                // if (locale) {
                //     return $translate.use($cookies.language = locale);
                // }
                // $translate.use($cookies.language || 'cn');
                $translate.use('cn');
            };

            $scope.touch = function (item) {
                item = item instanceof Item ? item : new Item();
                item.revert();
                $scope.temp = item;

                // $scope.WorkflowCode = $("#sheetWorkflow").SheetUIManager().GetValue();
                // $scope.Originator = $("#sheetUser").SheetUIManager().GetValue();
                // $("#tabQueryInstance").dataTable().fnDraw();
                // alert(2222222222222);
            };

            $scope.smartClick = function (item, e) {
                if (item.isFolder()) {
                    return $scope.fileNavigator.folderClick(item);
                }
                if (item.isImage()) {
                    return $scope.openImagePreview(item);
                }
                if (item.isEditable()) {
                    return $scope.openEditItem(item);
                }
            };

            $scope.openImagePreview = function (item) {
                item.inprocess = true;
                $scope.modal('imagepreview')
                    .find('#imagepreview-target')
                    .attr('src', item.getUrl(true))
                    .unbind('load error')
                    .on('load error', function () {
                        item.inprocess = false;
                        $scope.$apply();
                    });
                return $scope.touch(item);
            };

            $scope.startUploadFile = function (e) {
                extraObj.startUpload();
            };

            $scope.genQRcode = function () {
                var qrcode = $("#qrcode");
                $("#qrcode").html("");


                var ulObj = $("<ul />", {
                    "class": "nav nav-tabs"
                }).appendTo(qrcode);

                var liClass = "";
                for (var i = 0; i < serverAddresses.length; i++) {
                    if (i == 0) {
                        liClass = "active";
                    } else {
                        liClass = "";
                    }
                    var liObj = $("<li />", {
                        "class": liClass
                    }).appendTo(ulObj);

                    $("<a /> ", {
                        "href": "#qrcontent" + i,
                        "data-toggle": "tab",
                        "text": "地址" + (i + 1)
                    }).appendTo(liObj);
                }
                var contentClass = "";
                var contentObj = $("<div />", {
                    "class": "tab-content"
                }).appendTo(qrcode);
                for (var i = 0; i < serverAddresses.length; i++) {
                    if (i == 0) {
                        contentClass = "active";
                    } else {
                        contentClass = ""
                    }
                    var obj = $("<div />", {
                        "class": " qrcode tab-pane fade in " + contentClass,
                        "id": "qrcontent" + i
                    }).appendTo(contentObj);

                    $("<div />", {
                        "id": "qrcode" + i
                    }).appendTo(obj);

                    $('#qrcode' + i).qrcode(serverAddresses[i]);
                }
            }

            $scope.clearUploadFileList = function (e) {
                //console.log("clearUploadFileList");
                $(".ajax-file-upload-container").html("");
            };

            $scope.openEditItem = function (item) {
                item.getContent();
                $scope.modal('edit');
                return $scope.touch(item);
            };

            $scope.modal = function (id, hide) {
                return $('#' + id).modal(hide ? 'hide' : 'show');
            };

            $scope.isInThisPath = function (path) {
                var currentPath = $scope.fileNavigator.currentPath.join('/');
                return currentPath.indexOf(path) !== -1;
            };

            $scope.isMyFolderInThisPath = function (path) {
                var currentPath = $scope.fileNavigator.myFolderCurrentPath.join('/');
                return currentPath.indexOf(path) !== -1;
            };

            $scope.edit = function (item) {
                item.edit().then(function () {
                    $scope.modal('edit', true);
                });
            };

            $scope.changePermissions = function (item) {
                item.changePermissions().then(function () {
                    $scope.modal('changepermissions', true);
                });
            };

            $scope.copy = function (item) {
                var samePath = item.tempModel.path.join() === item.model.path.join();
                if (samePath && $scope.fileNavigator.fileNameExists(item.tempModel.name)) {
                    item.error = $translate.instant('error_invalid_filename');
                    return false;
                }
                item.copy().then(function () {
                    $scope.fileNavigator.refresh();
                    $scope.modal('copy', true);
                });
            };

            $scope.compress = function (item) {
                item.compress().then(function () {
                    $scope.fileNavigator.refresh();
                    if (!$scope.config.compressAsync) {
                        return $scope.modal('compress', true);
                    }
                    item.asyncSuccess = true;
                }, function () {
                    item.asyncSuccess = false;
                });
            };

            $scope.extract = function (item) {
                item.extract().then(function () {
                    $scope.fileNavigator.refresh();
                    if (!$scope.config.extractAsync) {
                        return $scope.modal('extract', true);
                    }
                    item.asyncSuccess = true;
                }, function () {
                    item.asyncSuccess = false;
                });
            };

            $scope.remove = function (item) {
                item.remove().then(function () {
                    $scope.fileNavigator.refresh();
                    $scope.modal('delete', true);
                });
            };

            $scope.rename = function (item) {
                var samePath = item.tempModel.path.join() === item.model.path.join();
                if (samePath && $scope.fileNavigator.fileNameExists(item.tempModel.name)) {
                    item.error = $translate.instant('error_invalid_filename');
                    return false;
                }
                item.rename().then(function () {
                    $scope.fileNavigator.refresh();
                    $scope.modal('rename', true);
                });
            };

            $scope.updateFile = function (item) {
                var samePath = item.tempModel.path.join() === item.model.path.join();
                if (samePath && $scope.fileNavigator.fileNameExists(item.tempModel.name)) {
                    item.error = $translate.instant('error_invalid_filename');
                    return false;
                }
                item.updateFile().then(function () {
                    $scope.fileNavigator.refresh();
                    $scope.modal('rename', true);
                });
            };

            $scope.createFolder = function (item) {
                var name = item.tempModel.name && item.tempModel.name.trim();
                item.tempModel.type = 'dir';
                item.tempModel.path = $scope.fileNavigator.currentPath;
                item.tempModel.id = $scope.fileNavigator.currentFileId;
                if (name && !$scope.fileNavigator.fileNameExists(name)) {
                    item.createFolder($rootScope.rootdir).then(function () {
                        $scope.fileNavigator.refresh();
                        $scope.modal('newfolder', true);
                    });
                } else {
                    item.error = $translate.instant('error_invalid_filename');
                    return false;
                }
            };
            $rootScope.scope = $scope;

            $scope.uploadFiles = function () {
                $scope.fileUploader.upload($scope.uploadFileList, $scope.fileNavigator.currentPath).then(function () {
                    $scope.fileNavigator.refresh();
                    $scope.modal('uploadfile', true);
                }, function (data) {
                    var errorMsg = data.result && data.result.error || $translate.instant('error_uploading_files');
                    $scope.temp.error = errorMsg;
                });
            };

            $scope.getQueryParam = function (param) {
                var found;
                window.location.search.substr(1).split('&').forEach(function (item) {
                    if (param === item.split('=')[0]) {
                        found = item.split('=')[1];
                        return false;
                    }
                });
                return found;
            };

            // $scope.changeLanguage($scope.getQueryParam('lang'));
            $scope.isWindows = $scope.getQueryParam('server') === 'Windows';
            $scope.fileNavigator.refresh();
            $translate.refresh('cn');
            //$translate.use('cn');


            /*新建文件夹弹出框*/
            // $scope.loadScroll = function() {
            //     $scope.myScroll = new IScroll('.dataTables_scrollBody', {
            //         scrollbars: true,
            //         bounce: false,
            //         mouseWheel: true,
            //         interactiveScrollbars: true,
            //         shrinkScrollbars: 'scale',
            //         fadeScrollbars: true
            //     });
            // };
            //
            // $scope.UserOptions = {
            //     ResignVisible:true,
            //     Editable: true,
            //     Visiable: true,
            //     OrgUnitVisible: true,
            //     V: $scope.user == undefined ? "": $scope.user.ObjectID,
            //     PlaceHolder: '',
            //     IsMultiple : true
            // };
            //
            //
            // var filter = $(".searchContainer");
            // filter.find("button").unbind("click.DT").bind("click.DT", function () {
            //     $scope.Originator = $("#sheetUser").SheetUIManager().GetValue();
            //     $("#tabQueryInstance").dataTable().fnDraw();
            // });
            // filter.find("select").unbind("change.Load").bind("change.Load", function () {
            //     $scope.Originator = $("#sheetUser").SheetUIManager().GetValue();
            //     $("#tabQueryInstance").dataTable().fnDraw();
            // });
            // $scope.loadScroll();

            // $(".H3Panel").BuildPanel({ excludeIDs: ["FuntionAclList"] });
            // var code = "";
            // CreateLigerGrid(
            //     $("#FuntionAclList"),
            //     GetCategoryColumns(),
            //     $.Controller.FunctionNode.GetFunctionAclList,
            //     true,
            //     "98%",
            //     null, null, null,
            //     { code: code }
            // );
            //
            // $("#divUser").SheetUser({ Editable: true, Visiable: true, Originate: true, OrgUnitVisible: true });

            $(".H3Panel").BuildPanel();
            //构造SheetUser
            // $("#divUser").SheetUser({ Editable: true, Visiable: true, Originate: true, OrgUnitVisible: true });
            $("#divUser").SheetUser({
                ResignVisible:true,
                    Editable: true,
                Visiable: true,
                OrgUnitVisible: true,
                IsMultiple : true,
                UserVisible: true
            });
            // function GetCategoryColumns() {
            //     var userName = '姓名';
            //     var view = '展示';
            //     return [
            //         { display: "ObjectID", name: "ObjectID", hide: true },
            //         { display: userName, name: "UserID", width: '30%' },
            //         {
            //             display: view, name: "Run", width: 250, align: 'center', render: function (row) {
            //             if (row.Run != "0") {
            //                 return "<img src='"+_PORTALROOT_GLOBAL+ "/WFRes/images/checked.gif' />";
            //             }
            //             else
            //                 return "<img src='"+_PORTALROOT_GLOBAL+ "/WFRes/images/unChecked.gif' />";
            //         }
            //         }
            //     ];
            // }


// ***************************新建文件夹模态框********************************
            //进入视图触发
            $scope.$on('$viewContentLoaded', function (event) {
                $scope.myScroll = null
            });
            $scope.newfolder = function (data) {
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
                            templateUrl: 'newfolder.html',    // 指向上面创建的视图
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

                    })
                var times = setInterval(function() {
                    if($("#WorkflowCodes").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //console.log($("#WorkflowCodes"));
                        $("#WorkflowCodes").css("width","246px");
                    }
                }, 50);
            }

// *************************上传文件选人模态框********************************
            $scope.uploadfile = function (data) {
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
                            templateUrl: 'uploadfile.html',    // 指向上面创建的视图
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
                    })
                var times = setInterval(function() {
                    if($("#WorkflowCodes").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //console.log($("#WorkflowCodes"));
                        $("#WorkflowCodes").css("width","246px");
                    }
                }, 50);
            }

            }]);


    app.controller("ModalsController", ["$scope", "$rootScope", "$http", "$translate", "$state", "$filter", "$modal", "$modalInstance", "$interval", "$timeout", "ControllerConfig", "notify", "datecalculation", "params",
        function ($scope, $rootScope, $http, $translate, $state, $filter, $modal, $modalInstance, $interval, $timeout, ControllerConfig, notify, datecalculation, params) {
            var $newscope = $rootScope.scope;
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
            $scope.EtartTimeOption = {
                dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.StartTime = e.el.value;
                }
            }
            $scope.EndTimeOption = {
                dateFmt: 'yyyy-MM-dd',
                realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.EndTime = e.el.value;
                }
            }
            $scope.WorkflowOptions = {
                Editable: true, Visiable: true, Mode: "WorkflowTemplate", IsMultiple: true, PlaceHolder: $scope.LanJson.WorkFlow, IsSearch:true
            }
            $scope.WasAgentOptions = {
                Editable: true, Visiable: true, PlaceHolder: $scope.LanJson.Originator
            }
            $scope.OriginatorRangeOptions = {
               // Editable: true, Visiable: true, OrgUnitVisible: false, UserVisible: false, PlaceHolder: $scope.LanJson.Originator,GroupVisible:true,IsMultiple: true
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

            //新建的文件夹是否公开
            $scope.WasShow = function(){
                var WasView = $("input[name='open']:checked").val();//选中的单选框的值
                if(WasView == 1){//不公开
                    $("#permission").show();
                    return 1;
                }else if(WasView == 0){
                    $("#permission").hide();
                    return 0;
                }
            }
            $scope.createFolder = function (item) {
                var foldername = item.tempModel.name && item.tempModel.name.trim();//文件夹名称

                //权限
                var fileId = null;//文件的id
                var orgList = [];//组织的对象数组
                var userList = [];//个人的对象数组

                var orgListObj = {};// 组织的对象
                var userListObj = {};// 个人的对象
                var WasView = $scope.WasShow()//选中的单选框的值
                if(WasView){//如果公开的话，就取出权限的人员和组织
                    var id = $('#WasView').SheetUIManager().GetValue();// 获取的是组织或者人员的id
                    var name = $('#WasView').SheetUIManager().GetText();// 可以获取组织或者人员的名称
                    orgListObj = {
                        "id": id,
                        "name": name
                    }
                    // if(IsUser){
                    //     var id = $('#WasView').SheetUIManager().GetValue();// 获取的是组织或者人员的id
                    //     var name = $('#WasView').SheetUIManager().GetText();// 可以获取组织或者人员的名称
                    //     userListObj = {
                    //         "id": id,
                    //         "name": name
                    //     }
                    // }
                }
                else{
                    orgListObj = {};
                    userListObj = {};
                }
                orgList.push(orgListObj);
                userList.push(userListObj);
                //协办管理员
                // unitID:82237383-a18e-4055-8006-8c873e84e087
                // unitID:82237383-a18e-4055-8006-8c873e84e087
                item.tempModel.type = 'dir';
                item.tempModel.path = $newscope.fileNavigator.currentPath;
                if($newscope.fileNavigator.currentFileId == ""){
                    item.tempModel.id = null;
                }else{
                    item.tempModel.id = $newscope.fileNavigator.currentFileId;
                }
                item.tempModel.filePermission =  {
                    "fileId": fileId,
                    "orgList": orgList,
                    "userList": userList
                };
                item.tempModel.name = foldername;

                if (foldername && ! $newscope.fileNavigator.fileNameExists(foldername)) {
                    item.createFolder($rootScope.rootdir).then(function () {
                        $newscope.fileNavigator.refresh();
                        $newscope.modal('newfolder', true);
                    });
                } else {
                    item.error = $translate.instant('error_invalid_filename');
                    return false;
                }
                $scope.cancel();
            };
            $scope.startUploadFile = function (e) {
                extraObj.startUpload();
                $scope.cancel();
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel'); // 退出
            }

        }]);
})(window, angular, jQuery);
