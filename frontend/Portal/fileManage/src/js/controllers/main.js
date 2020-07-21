(function (window, angular, $) {
    'use strict';
    app.controller('FileManagerCtrl', [
        '$scope', '$translate', '$cookies', 'fileManagerConfig', 'item', 'fileNavigator', 'fileUploader','$http', 'ControllerConfig', '$modal',
        "$rootScope", "$state", "$filter",  "$compile", "jq.datables",
        function ($scope, $translate, $cookies, fileManagerConfig, Item, FileNavigator, FileUploader, $http, ControllerConfig, $modal, $rootScope, $state, $filter, $compile, jqdatables) {
					$scope.config = fileManagerConfig;
					$rootScope.initRootDir = function () {
						$scope.fileMemuTile = $scope.config.fileMemuTitle[$scope.fileMemuTile];
						$rootScope.rootdir = $scope.fileMemuTile;
					}
					$rootScope.initRootDir();

          $scope.reverse = false;
          $scope.query = '';
          //回收站按照删除时间默认排序
            if ($scope.fileMemuTile == $scope.config.fileMemuTitle['recycle']) {
                $scope.predicate = ['model.deleteTime'];
                $scope.reverse = true;
            } else {
                $scope.predicate = ['model.type', 'model.name'];
            }
            $scope.order = function (predicate) {
                $scope.reverse = ($scope.predicate[1] === predicate) ? !$scope.reverse : false;
                $scope.predicate[1] = predicate;
            };
            
            // $scope.temp = new Item();

            $rootScope.temp = new Item();
            $rootScope.rootdir = $scope.fileMemuTile;
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
									$scope.modal('updateFile', true);
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

            //我的搜索
            $scope.mySearch = function (searchId){
                $scope.fileNavigator.position = true;
                var keyword = $("#"+searchId).val();
                var path = $scope.fileNavigator.currentPath.join('/');
                $scope.fileNavigator.mySearch(keyword).then(function (data) {
                    $scope.fileNavigator.currentParentId = data.parentId;
                    $scope.fileNavigator.fileList = (data.result || []).map(function(file) {
                        return new Item(file, $scope.fileNavigator.currentPath);
                    });
                    $scope.fileNavigator.buildTree(path);
                });

            };

            //搜索,先拿到数据，先发送请求http，在渲染refresh
            $scope.search = function(searchId){
                $scope.fileNavigator.position = true;
                var keyword = $("#"+searchId).val();
                var path = $scope.fileNavigator.currentPath.join('/');
                $scope.fileNavigator.search(keyword).then(function (data) {
                    $scope.fileNavigator.currentParentId = data.parentId;
                    $scope.fileNavigator.fileList = (data.result || []).map(function(file) {
                        return new Item(file, $scope.fileNavigator.currentPath);
                    });
                    $scope.fileNavigator.buildTree(path);
                });
            }

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


// ***************************共享文件新建文件夹模态框********************************
            //进入视图触发
            $scope.$on('$viewContentLoaded', function (event) {
                $scope.myScroll = null
            });
            $scope.newFolder = function (data) {
                $rootScope.temp.tempModel.name = "";
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
                            templateUrl: 'newFolder.html',    // 指向上面创建的视图
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
                    })
                var times = setInterval(function() {
                    if($("#WorkflowCodes").length>0){//id没有改过来，因为没有用到
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //console.log($("#WorkflowCodes"));
                        $("#WorkflowCodes").css("width","246px");
                    }
                }, 50);
            }

// *************************共享文件上传文件选人模态框********************************
            $scope.uploadFile = function (data) {
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
                            templateUrl: 'uploadFile.html',    // 指向上面创建的视图
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

// *************************共享文件编辑路径和名称模态框******************************
            $scope.toUpdateFile = function (data) {
                $scope.fileNavigator = $rootScope.scope.fileNavigator;//参数
                $rootScope.temp = data;
                var lenOrgList = data.model.filePermission.orgList.length;//组织的长度
                data = "";
                var AgencyID;
                if (data == undefined) AgencyID = "";
                else AgencyID = data;
                $http({
                    url: ControllerConfig.Agents.GetAgency,
                    params: {
                        agentID: AgencyID,
                        random: new Date().getTime()
                    }
                }).success(function (result, header, config, status) {
                        var Agency = result.Rows[0];
                        // 弹出模态框
                        var modalInstance = $modal.open({
                            templateUrl: 'updateFile.html',    // 指向上面创建的视图
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

                var arrOrgList = [];
                for(var i = 0; i < lenOrgList; i++){
                    //strOrgList += '<li class="select2-search-choice" id="'+ $rootScope.temp.model.filePermission.orgList[i].id + '" data-code="18f923a7-5a5e-426d-94ae-a55ad1a4b240" style="cursor: pointer; margin-top: 2px; background-color: rgb(250, 250, 250);"><div>'+ $rootScope.temp.model.filePermission.orgList[i].name +'</div><a href="javascript:void(0)" class="select2-search-choice-close"></a></li>';
                    arrOrgList.push($rootScope.temp.model.filePermission.orgList[i].id);
                }
                var times = setInterval(function() {
                    if($("#editPer").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //$("#editPer").find("ul").prepend(strOrgList);
                        var control = $("#editPer").SheetUIManager();
                        control.SetValue(arrOrgList);
                    }
                }, 800);
            }

// ************************共享文件删除模态框*****************************************
            $scope.toDeleteFile = function (data) {
                $scope.fileNavigator = $rootScope.scope.fileNavigator;//参数
                $rootScope.temp = data;
                var lenOrgList = data.model.filePermission.orgList.length;//组织的长度
                data = "";
                var AgencyID;
                if (data == undefined) AgencyID = "";
                else AgencyID = data;
                $http({
                    url: ControllerConfig.Agents.GetAgency,
                    params: {
                        agentID: AgencyID,
                        random: new Date().getTime()
                    }
                }).success(function (result, header, config, status) {
                    var Agency = result.Rows[0];
                    // 弹出模态框
                    var modalInstance = $modal.open({
                        templateUrl: 'deleteFile.html',    // 指向上面创建的视图
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

                var arrOrgList = [];
                for(var i = 0; i < lenOrgList; i++){
                    //strOrgList += '<li class="select2-search-choice" id="'+ $rootScope.temp.model.filePermission.orgList[i].id + '" data-code="18f923a7-5a5e-426d-94ae-a55ad1a4b240" style="cursor: pointer; margin-top: 2px; background-color: rgb(250, 250, 250);"><div>'+ $rootScope.temp.model.filePermission.orgList[i].name +'</div><a href="javascript:void(0)" class="select2-search-choice-close"></a></li>';
                    arrOrgList.push($rootScope.temp.model.filePermission.orgList[i].id);
                }
                var times = setInterval(function() {
                    if($("#editPer").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //$("#editPer").find("ul").prepend(strOrgList);
                        var control = $("#editPer").SheetUIManager();
                        control.SetValue(arrOrgList);
                    }
                }, 800);
            }

// ***************************我的文件新建文件夹模态框********************************
            $scope.myNewFolder = function (data) {
                $rootScope.temp.tempModel.name = "";
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
                            templateUrl: 'myNewFolder.html',    // 指向上面创建的视图
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

// *************************我的文件上传文件选人模态框********************************
            $scope.myUploadFile = function (data) {
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
                            templateUrl: 'myUploadfile.html',    // 指向上面创建的视图
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

// *************************我的文件编辑路径和名称模态框******************************
            $scope.myToUpdateFile = function (data) {
                $scope.fileNavigator = $rootScope.scope.fileNavigator;//参数
                $rootScope.temp = data;
                data = "";
                var AgencyID;
                if (data == undefined) AgencyID = "";
                else AgencyID = data;
                $http({
                    url: ControllerConfig.Agents.GetAgency,
                    params: {
                        agentID: AgencyID,
                        random: new Date().getTime()
                    }
                }).success(function (result, header, config, status) {
                    var Agency = result.Rows[0];
                    // 弹出模态框

                    var modalInstance = $modal.open({
                        templateUrl: 'myUpdateFile.html',    // 指向上面创建的视图
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
                var times = setInterval(function() {
                    if($("#WorkflowCodes").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //console.log($("#WorkflowCodes"));
                        $("#WorkflowCodes").css("width","246px");
                    }
                }, 50);
            }
// ************************我的文件删除模态框*****************************************
            $scope.toMyDeleteFile = function (data) {
                $scope.fileNavigator = $rootScope.scope.fileNavigator;//参数
                $rootScope.temp = data;
                data = "";
                var AgencyID;
                if (data == undefined) AgencyID = "";
                else AgencyID = data;
                $http({
                    url: ControllerConfig.Agents.GetAgency,
                    params: {
                        agentID: AgencyID,
                        random: new Date().getTime()
                    }
                }).success(function (result, header, config, status) {
                    var Agency = result.Rows[0];
                    // 弹出模态框
                    var modalInstance = $modal.open({
                        templateUrl: 'myDeleteFile.html',    // 指向上面创建的视图
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
                var times = setInterval(function() {
                    if($("#editPer").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                    }
                }, 800);
            }


// ************************收藏到我的文件模态框***************************************
            $scope.toCollectFile = function (data) {
                $rootScope.temp = data;
                $scope.fileNavigator.currentModalFileId = '';//收藏直接弹出根路径
                $scope.fileNavigator.currentModalPath = [];//每次收藏都跳到根路径
                $scope.fileNavigator.isPrivate = true;//收藏进来
                $scope.fileNavigator.refreshCollectFile();

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
                            templateUrl: 'collectFile.html',    // 指向上面创建的视图
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
            }

// *************************分享到共享文件模态框**************************************
            $scope.toShareFile = function (data) {
                $rootScope.temp = data;
                $scope.fileNavigator.currentModalFileId = '';//分享直接弹出根路径
                $scope.fileNavigator.currentModalPath = [];//每次分享都跳到根路径
                $scope.fileNavigator.isPrivate = false;//分享出去
                $scope.fileNavigator.refreshShareFile();

                var tempData;
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
                            templateUrl: 'shareFile.html',    // 指向上面创建的视图
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
            }

            }]);


    app.controller("ModalsController", ["$scope", "$rootScope", "$http", "$translate", "$state", "$filter", "$modal", "$modalInstance", "$interval", "$timeout", "ControllerConfig", "notify", "datecalculation", "params",
        function ($scope, $rootScope, $http, $translate, $state, $filter, $modal, $modalInstance, $interval, $timeout, ControllerConfig, notify, datecalculation, params) {
            $scope.config = $rootScope.scope.config;
            var _newscope = $rootScope.scope;
            $scope.fileNavigator = _newscope.fileNavigator;
            $scope.temp = $rootScope.temp;
            $scope.fileMemuTile = $rootScope.rootdir;
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
                Editable: true, Visiable: true, PlaceHolder: $scope.LanJson.Originator, IsMultiple: true //全选属性
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

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel'); // 退出
            }
            $rootScope.cancel = $scope.cancel;
        }]);
})(window, angular, jQuery);
