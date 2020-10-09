(function (window, angular, $) {
    'use strict';
    app.controller('AnnouncementManageCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", '$modal', 'item', 'fileNavigator',
        function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $modal, Item, FileNavigator) {
            //$rootScope.flowScope = $scope;
            //实例化参数
            
            $scope.types = [
                // {name:'全部公告',id:"0"}, 
                { name: '部门公告', id: "1" },
                { name: '外部公告', id: "2" },];

            $scope.searchWasAgentOptions = {
                Editable: true,
                Visiable: true,
                //IsMultiple: true,
                UserVisible: false,//是否显示用户
            }
          
            //共享知识的日期控件初始化
            $scope.searchStartTimeStart = {
                dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $rootScope.searchStartTimeStart = e.el.value;
                }
            }
            $scope.searchStartTimeEnd = {
                dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $rootScope.searchStartTimeEnd = e.el.value;
                
                }
            }
            // $scope.EndTimeStartOption = {
            //     dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
            //     onpicked: function (e) {
            //         $scope.EndTimeStart = e.el.value;
            //     }
            // }
            // $scope.EndTimeEndtOption = {
            //     dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
            //     onpicked: function (e) {
            //         $scope.EndTimeEnd = e.el.value;
            //     }
            // }


            $scope.$on('$viewContentLoaded', function (event) {
                $scope.init();
                $scope.myScroll = null
            });

            $scope.init = function () {
                $scope.name = $translate.instant("WorkItemController.FinishedWorkitem");
                //  $scope.myName = $translate.instant("WorkItemController.FinishedWorkitem");
                // $scope.StartTimeStart = datecalculation.redDays(new Date(), 30);
                // $scope.StartTimeEnd = datecalculation.addDays(new Date(), 30);
                // $scope.EndTimeStart = datecalculation.redDays(new Date(), 30);
                // $scope.EndTimeEnd = datecalculation.addDays(new Date(), 30);

                $scope.searchStartTimeStart = datecalculation.redDays(new Date(), 30);
                $scope.searchStartTimeEnd = datecalculation.addDays(new Date(), 30);
                // $scope.MyEndTimeStart = datecalculation.redDays(new Date(), 30);
                // $scope.MyEndTimeEnd = datecalculation.addDays(new Date(), 30);
            }
            // 获取语言
            // $rootScope.$on('$translateChangeEnd', function () {
            //     $scope.getLanguage();
            //     $state.go($state.$current.self.name, {}, { reload: true });
            // });

            $scope.getLanguage = function () {
                $scope.LanJson = {
                    search: $translate.instant("uidataTable.search"),
                    ProcessName: $translate.instant("QueryTableColumn.Name"),
                    WorkFlow: $translate.instant("QueryTableColumn.WorkFlow"),
                    StartTime: $translate.instant("QueryTableColumn.StartTime"),
                    FinishTime: $translate.instant("QueryTableColumn.FinishTime"),
                    EndTime: $translate.instant("QueryTableColumn.EndTime"),
                    sLengthMenu: $translate.instant("uidataTable.sLengthMenu"),
                    sZeroRecords: $translate.instant("uidataTable.sZeroRecords"),
                    sInfo: $translate.instant("uidataTable.sInfo"),
                    sProcessing: $translate.instant("uidataTable.sProcessing")
                };
            }
            $scope.getLanguage();
            //页面最初渲染的请求
            $scope.getMyColumns = function () {
                var columns = [
                   
                ];
                columns.push({
                    "mData": "title",//返回数据的键
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);

                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return `<a style='cursor: pointer;' ng-click='toDetailMyAnnouncement(${full})' title='${data}'>${data}</a>`;
                    }
                });
                columns.push({
                    "mData": "orgName",
                    "mRender": function (data, type, full) {
                        //打开流程状态

                        data = $scope.htmlEncode(data);
                        // data=$scope.htmlEncode[data];
                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return "<span title=\"" + data + "\">" + data + "</span>";
                    }
                });
                columns.push({
                    "mData": "typeStr",
                    "mRender": function (data, type, full) {
                        //打开流程状态

                        data = $scope.htmlEncode(data);
                        // data=$scope.htmlEncode[data];
                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return "<span title=\"" + data + "\">" + data + "</span>";
                    }
                });
                columns.push({
                    "mData": "createTime",
                    "sClass": "center hide1024",
                    "mRender": function (data, type, full) {
                        // data = $scope.htmlEncode(data);
                        //将long时间转为date类型
                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        else {
                            var time = new Date(data);
                            var year = time.getFullYear();
                            var month = time.getMonth() + 1 
                            var date = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
                            var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
                            var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                            var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
                            data = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
                        }
                        return "<span title=\"" + data + "\">" + data + "<span>";
                    }
                });
                columns.push({
                    "mData": "updateTime",
                    "sClass": "center hide414",
                    "mRender": function (data, type, full) {
                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        else {
                            var time = new Date(data);
                            var year = time.getFullYear();
                            var month = time.getMonth() + 1
                            var date = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
                            var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
                            var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                            var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
                            data = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
                        }


                        return "<span title=\"" + data + "\">" + data + "</span>";
                    }
                });
                columns.push({
                    "mData": "startTime",
                    "sClass": "center hide414",
                    "mRender": function (data, type, full) {
                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        else {
                            var time = new Date(data);
                            var year = time.getFullYear();
                            var month = time.getMonth() + 1
                            var date = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
                            var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
                            var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                            var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
                            data = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
                        }
                        return "<span title=\"" + data + "\">" + data + "</span>";
                    }
                });
                columns.push({
                    "mData": "endTime",
                    "sClass": "center hide414",
                    "mRender": function (data, type, full) {
                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        else {
                            var time = new Date(data);
                            var year = time.getFullYear();
                            var month = time.getMonth() + 1 
                            var date = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
                            var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
                            var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                            var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
                            data = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
                        }
                        return "<span title=\"" + data + "\">" + data + "</span>";
                    }
                });

                columns.push({
                    "mRender": function (data, type, full) {
                        full = JSON.stringify(full);
                        return `
                              <button class='btn btn-sm btn-default' ng-click='updateMyAnnouncement(${full})' title='编辑'>
                                  <i class='glyphicon glyphicon-edit'></i>
                              </button>
                         
                              <button class='btn btn-sm btn-danger' ng-click='toDeleteAnnouncement(${full})' title='删除'>
                                  <i class='glyphicon glyphicon-trash'></i>
                              </button>`;
                    }
                });
                return columns;
            }

            $scope.loadScroll = function () {
                $scope.myScroll = new IScroll('.dataTables_scrollBody', {
                    scrollbars: true,
                    bounce: false,
                    mouseWheel: true,
                    interactiveScrollbars: true,
                    shrinkScrollbars: 'scale',
                    fadeScrollbars: true
                });
            };
            $scope.dtMyFlowOptions = {
                "bProcessing": true,
                "bServerSide": true,    // 是否读取服务器分页
                "paging": true,         // 是否启用分页
                "bPaginate": true,      // 分页按钮
                "bFilter": false,        // 是否显示搜索栏
                "searchDelay": 1000,    // 延迟搜索
                "iDisplayLength": 20,   // 每页显示行数
                "bSort": false,         // 排序
                "singleSelect": true,
                "bInfo": true,          // Showing 1 to 10 of 23 entries 总记录数没也显示多少等信息
                "bLengthChange": true, // 每页显示多少数据
                "aLengthMenu": [[10, 20, 50, 100], [10, 20, 50, 100]],//设置每页显示数据条数的下拉选项
                "pagingType": "full_numbers",  // 设置分页样式，这个是默认的值
                "sScrollY": "330px",
                // "responsive": false,
                //     "sScrollX": true,
                //   "sScrollXInner": "150%",
                //    "bAutoWidth":true,
                "bScrollCollapse": true,
                "iScrollLoadGap": 50,
                "language": {           // 语言设置
                    "sLengthMenu": $scope.LanJson.sLengthMenu,
                    "sZeroRecords": "<div class='no-data'><p class='no-data-img'></p><p>" + $scope.LanJson.sZeroRecords + "</p></div>",
                    "sInfo": $scope.LanJson.sInfo,
                    "infoEmpty": "",
                    "sProcessing": '<div class="loading-box"><i class="icon-loading"></i><p>' + $scope.LanJson.sProcessing + '</p></div> ',
                    "paginate": {
                        "first": "<<",
                        "last": ">>",
                        "previous": "<",
                        "next": ">"
                    }
                },
                "sAjaxSource": "/Portal/announcement/listAnnouncementByPage",
                "fnServerData": function (sSource, aDataSet, fnCallback) {//sSource的值与sAjaxSource同步，WorkItem/GetFinishWorkItems,点击查询
                    $.ajax({
                        "dataType": 'json',
                        "type": 'POST',
                        "url": sSource,
                        "data": aDataSet,
                        "success": function (json) {//返回数据列表
                            if (json.ExceptionCode == 1 && json.Success == false) {
                                json.datas = [];
                                json.sEcho = 1;
                                json.Total = 0;
                                json.iTotalDisplayRecords = 0;
                                json.iTotalRecords = 0;
                                $state.go("platform.login");
                            }
                            fnCallback(json);
                            //每次搜索成功后清空搜索关键字
                            $(".input-text").val("");
                        }
                    });
                },
                "sAjaxDataProp": 'datas',
                "sDom": '<"top"f>rt<"row"ipl>',
                "sPaginationType": "full_numbers",
                "fnServerParams": function (aoData) {
                    // 增加自定义查询条件
                    //ie9不兼容placeholder属性 ie9下当value为空时，其value取placeholder值
                    // if ($("#MyStartTimeStart").attr("placeholder") == $("#MyStartTimeStart").val()) {
                    //     $scope.MyStartTimeStart = "";
                       

                    // } else {
                    //     $scope.MyStartTimeStart = $("#MyStartTimeStart").val();
                    //     debugger;
                    // }
                    // if ($("#MyStartTimeEnd").attr("placeholder") == $("#MyStartTimeEnd").val()) {
                    //     $scope.MyStartTimeEnd = "";

                    // } else {
                    //     $scope.MyStartTimeEnd = $("#MyStartTimeEnd").val();

                    // }
                    //将时间转化为时间戳
                    aoData.push(//name的值是传输数据的key，value的值是传输数据的value
                        { "name": "createTimeStart", "value": $filter("date")( $rootScope.searchStartTimeStart, "yyyy-MM-dd HH:mm:ss") },
                        { "name": "createTimeEnd", "value": $filter("date")( $rootScope.searchStartTimeEnd ,"yyyy-MM-dd HH:mm:ss") },
                        { "name": "title", "value": $scope.myName },
                        { "name": "type", "value": $scope.index },
                        { "name": "orgId", "value": $scope.Originator },
                    );
                 
                },
                "aoColumns": $scope.getMyColumns(), // 字段定义
                // 初始化完成事件,这里需要用到 JQuery ，因为当前表格是 JQuery 的插件
                "initComplete": function (settings, json) {
                    var filter = $("#searchAnnounceOrgName");
                    filter.unbind("click.DT").bind("click.DT", function () {
                        var myselect = document.getElementById('myTagName');
                        $scope.index = myselect.selectedIndex;
                        if ($scope.index === 0) {
                            $scope.index = '';
                        }
                        $scope.Originator = $("#searchcompany").SheetUIManager().GetValue();
                        if ( $rootScope.searchStartTimeStart == undefined && $rootScope.searchStartTimeEnd == undefined) {
                            $rootScope.searchStartTimeStart="";
                            $rootScope.searchStartTimeEnd="";
                        }
                        else if($("#searchStartTimeStart").val()==""&&$("#searchStartTimeEnd").val()=="")
                        {
                            $rootScope.searchStartTimeStart="";
                            $rootScope.searchStartTimeEnd="";
                        }
                        else if($("#searchStartTimeStart").val()==""&&$("#searchStartTimeEnd").val()!=="")
                        {
                            $rootScope.searchStartTimeStart="";
                            
                        }
                        else if($("#searchStartTimeStart").val()!==""&&$("#searchStartTimeEnd").val()=="")
                        {
                            $rootScope.searchStartTimeEnd="";
                        }
                        else{
                            
                        var myStartTimes = new Date( $rootScope.searchStartTimeStart.replace(/-/g, "/")).getTime();
                            var myEndTimes = new Date($rootScope.searchStartTimeEnd.replace(/-/g, "/")).getTime();
                            if (myStartTimes > myEndTimes) {
                                $.notify({ message: "时间区间错误", status: "danger" });
                                $("#MyStartTimeEnd").css("color", "red");
                                return false;
                            };
                        }
                        $("#tabMyFlow").dataTable().fnDraw();
                    });
                    $scope.loadScroll();
                },
                // 创建行，未绘画到屏幕上时调用
                "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {//其中aData存放的是每一行的数据
                    //将添加的angular事件添加到作用域中
                    $compile(nRow)($scope);
                    setTimeout(function () {
                        $scope.myScroll.refresh();
                    }, 300);
                },
                "fnDrawCallback": function () {
                    jqdatables.trcss();
                }
            };




            $scope.createAnnouncement = function (data) {
                var AgencyID = "";
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
                        templateUrl: 'createAnnouncement.html',    // 指向上面创建的视图
                        controller: 'AnnouncementModalsController',// 初始化模态范围
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
                                    // 'WFRes/_Content/themes/ligerUI/Aqua/css/ligerui-all.min.css',
                                    // 'WFRes/assets/stylesheets/sheet.css',
                                    // 'WFRes/_Scripts/jquery/jquery.lang.js',
                                    // 'WFRes/_Scripts/ligerUI/ligerui.all.min.js',
                                    // 'WFRes/_Scripts/MvcSheet/SheetControls.js',
                                    // 'WFRes/_Scripts/MvcSheet/MvcSheetUI.js'
                                ]).then(function () {
                                    return $ocLazyLoad.load([
                                        // 'WFRes/_Scripts/MvcSheet/Controls/SheetWorkflow.js',
                                        // 'WFRes/_Scripts/MvcSheet/Controls/SheetUser.js'
                                    ]);
                                });
                            }]
                        }
                    });
                })

                var times = setInterval(function () {
                    if ($("#WorkflowCodes").length > 0) {//id没有改过来，因为没有用到
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //console.log($("#WorkflowCodes"));
                        $("#WorkflowCodes").css("width", "246px");
                    }
                }, 50);


            }

            /*************公告管理编辑模态框********************/
            $scope.updateMyAnnouncement = function (data) {
                //  console.log(data)
                for (var key in data) {

                    if (data[key] == null) {
                        data[key] = "";
                    }
                }
                var AgencyID = data;

                $http({
                    url: ControllerConfig.Agents.GetAgency,
                    // url:"/Portal/announcement/listAnnouncementByPage",
                    params: {
                        agentID: AgencyID,

                        random: new Date().getTime()
                    }
                }).success(function (result, header, config, status) {
                    var Agency = result.Rows[0];
                    // 弹出模态框
                    var modalInstance = $modal.open({
                        templateUrl: 'editAnnouncement.html',    // 指向上面创建的视图
                        controller: 'AnnouncementModalsController',// 初始化模态范围
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
                                    // 'WFRes/_Content/themes/ligerUI/Aqua/css/ligerui-all.min.css',
                                    // 'WFRes/assets/stylesheets/sheet.css',
                                    // 'WFRes/_Scripts/jquery/jquery.lang.js',
                                    // 'WFRes/_Scripts/ligerUI/ligerui.all.min.js',
                                    // 'WFRes/_Scripts/MvcSheet/SheetControls.js',
                                    // 'WFRes/_Scripts/MvcSheet/MvcSheetUI.js'
                                ]).then(function () {
                                    return $ocLazyLoad.load([
                                        // 'WFRes/_Scripts/MvcSheet/Controls/SheetWorkflow.js',
                                        // 'WFRes/_Scripts/MvcSheet/Controls/SheetUser.js'
                                    ]);
                                });
                            }]
                        }
                    });
                    modalInstance.opened.then(function () {
                        //TODO not work
                    });
                });

            
                var times = setInterval(function () {
                    if ($("#editFlowPer").length > 0) {
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);

                    }
                }, 800);
            }



            /*************共享知识删除模态框*****************/
            $scope.toDeleteAnnouncement = function (data) {

                var AgencyID = data;
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
                        templateUrl: 'deleteMyAnnouncement.html',    // 指向上面创建的视图
                        controller: 'AnnouncementModalsController',// 初始化模态范围
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
                                    // 'WFRes/_Content/themes/ligerUI/Aqua/css/ligerui-all.min.css',
                                    // 'WFRes/assets/stylesheets/sheet.css',
                                    // 'WFRes/_Scripts/jquery/jquery.lang.js',
                                    // 'WFRes/_Scripts/ligerUI/ligerui.all.min.js',
                                    // 'WFRes/_Scripts/MvcSheet/SheetControls.js',
                                    // 'WFRes/_Scripts/MvcSheet/MvcSheetUI.js'
                                ]).then(function () {
                                    return $ocLazyLoad.load([
                                        // 'WFRes/_Scripts/MvcSheet/Controls/SheetWorkflow.js',
                                        // 'WFRes/_Scripts/MvcSheet/Controls/SheetUser.js'
                                    ]);
                                });
                            }]
                        }
                    });
                    modalInstance.opened.then(function () {
                        //TODO not work
                    });
                });

                // var arrOrgList = [];
                // for(var i = 0; i < lenOrgList; i++){
                //     //strOrgList += '<li class="select2-search-choice" id="'+ $rootScope.temp.model.filePermission.orgList[i].id + '" data-code="18f923a7-5a5e-426d-94ae-a55ad1a4b240" style="cursor: pointer; margin-top: 2px; background-color: rgb(250, 250, 250);"><div>'+ $rootScope.temp.model.filePermission.orgList[i].name +'</div><a href="javascript:void(0)" class="select2-search-choice-close"></a></li>';
                //     arrOrgList.push($rootScope.temp.model.filePermission.orgList[i].id);
                // }
                var times = setInterval(function () {
                    if ($("#editPer").length > 0) {
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //$("#editPer").find("ul").prepend(strOrgList);
                        // var control = $("#editPer").SheetUIManager();
                        // control.SetValue(arrOrgList);
                    }
                }, 800);
            }

            // ***********共享知识的详情页*******************
            $scope.toDetailMyAnnouncement = function (data) {

                for (var key in data) {
                    if (data[key] == null) {
                        data[key] = "";
                    }
                }

                var AgencyID = data;

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
                        templateUrl: 'detailAnnouncement.html',    // 指向上面创建的视图
                        controller: 'AnnouncementModalsController',// 初始化模态范围
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
                                    // 'WFRes/_Content/themes/ligerUI/Aqua/css/ligerui-all.min.css',
                                    // 'WFRes/assets/stylesheets/sheet.css',
                                    // 'WFRes/_Scripts/jquery/jquery.lang.js',
                                    // 'WFRes/_Scripts/ligerUI/ligerui.all.min.js',
                                    // 'WFRes/_Scripts/MvcSheet/SheetControls.js',
                                    // 'WFRes/_Scripts/MvcSheet/MvcSheetUI.js'
                                ]).then(function () {
                                    return $ocLazyLoad.load([
                                        // 'WFRes/_Scripts/MvcSheet/Controls/SheetWorkflow.js',
                                        // 'WFRes/_Scripts/MvcSheet/Controls/SheetUser.js'
                                    ]);
                                });
                            }]
                        }
                    });
                    modalInstance.opened.then(function () {
                        //TODO not work
                    });
                });

                // var arrOrgList = [];//显示权限
                // for(var i = 0; i < lenOrgList; i++){
                //     arrOrgList.push('429a78b-2d75-594f-91c6-567fab7c5e5f');
                // }
                var times = setInterval(function () {
                    if ($("#detailFlow").length > 0) {
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //     var control = $("#detailFlow").SheetUIManager();
                        //     control.SetValue(arrOrgList);
                    }
                }, 800);


            }

        }]);
})(window, angular, jQuery);