(function (window, angular, $) {
    'use strict';
    app.controller('FlowCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", '$modal', 'item', 'fileNavigator', 'fileManagerConfig',
		function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $modal, Item, FileNavigator, fileManagerConfig) {
            $rootScope.flowScope = $scope;
            //实例化参数
            $scope.item = new Item();
            $scope.fileNavigator = new FileNavigator();
            //共享知识的日期控件初始化
            $scope.StartTimeStartOption = {
                dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.StartTimeStart = e.el.value;
                }
            }
            $scope.StartTimeEndtOption = {
                dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.StartTimeEnd = e.el.value;
                }
            }
            $scope.EndTimeStartOption = {
                dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.EndTimeStart = e.el.value;
                }
            }
            $scope.EndTimeEndtOption = {
                dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.EndTimeEnd = e.el.value;
                }
            }
            //我的知识的日期控件初始化
            $scope.MyStartTimeStartOption = {
                dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.MyStartTimeStart = e.el.value;
                }
            }
            $scope.MyStartTimeEndtOption = {
                dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.MyStartTimeEnd = e.el.value;
                }
            }
            $scope.MyEndTimeStartOption = {
                dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.MyEndTimeStart = e.el.value;
                }
            }
            $scope.MyEndTimeEndtOption = {
                dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
                onpicked: function (e) {
                    $scope.MyEndTimeEnd = e.el.value;
                }
            }

            $scope.$on('$viewContentLoaded', function (event) {
                $scope.init();
                $scope.myScroll = null
            });

            $scope.init = function () {
                $scope.name = $translate.instant("WorkItemController.FinishedWorkitem");
                $scope.myName = $translate.instant("WorkItemController.FinishedWorkitem");
                $scope.StartTimeStart = datecalculation.redDays(new Date(), 30);
                $scope.StartTimeEnd = datecalculation.addDays(new Date(), 30);
                $scope.EndTimeStart = datecalculation.redDays(new Date(), 30);
                $scope.EndTimeEnd = datecalculation.addDays(new Date(), 30);

                $scope.MyStartTimeStart = datecalculation.redDays(new Date(), 30);
                $scope.MyStartTimeEnd = datecalculation.addDays(new Date(), 30);
                $scope.MyEndTimeStart = datecalculation.redDays(new Date(), 30);
                $scope.MyEndTimeEnd = datecalculation.addDays(new Date(), 30);
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
            //初始化流程模板
            $scope.WorkflowOptions = {
                Editable: true,
                Visiable: true,
                Mode: "WorkflowTemplate",
                IsMultiple: true,
                OnChange: "",
                PlaceHolder: $scope.LanJson.WorkFlow,
                IsSearch:true
            }

            $scope.getColumns = function () {
                var columns = [];
                columns.push({
                    "mData": "name",//返回数据的键
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);
                        if(data == 'null' || data == null){
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return `<a style='cursor: pointer;' ng-click='toDetailFlow(${full})' title='${data}'>${data}</a>`;
                    }
                });
                columns.push({
                    "mData": "tagName",
                    "mRender": function (data, type, full) {
                        //打开流程状态
                        data = data != "" ? data : full.ActivityCode;
                        data = $scope.htmlEncode(data);
                        if(data == 'null' || data == null){
                            data = '';
                        }
                        // return "<a title=\""+data+"\" href='index.html#/InstanceDetail/" + full.InstanceId + "/" + full.flowId + "/" + "/' target='_blank'>" + data + "</a>";
                        return "<span title=\""+data+"\">" + data + "</span>";
                    }
                });
                columns.push({
                    "mData": "flowCodeDesc",
                    "sClass": "center hide1024" ,
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);
                        if(data == 'null' || data == null){
                            data = '';
                        }
                        return "<span title=\""+data+"\">"+data+"<span>";
                    }
                });
                columns.push({
                    "mData": "startTime",
                    "sClass": "center hide414",
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);
                        data = data.trim();
                        if(data == 'null' || data == null){
                            data = '';
                        }else{
                            data = data.substring(0,10) + ' ' + data.substring(11,19);
                        }
                        return "<span title=\""+data+"\">"+data+"</span>";
                    }
                });
                columns.push({
                    "mData": "endTime",
                    "sClass": "center hide414",
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);//full.Originator应该是选人控件的ID,
                        data = data.trim();
                        if(data == 'null' || data == null){
                            data = '';
                        }else{
                            data = data.substring(0,10) + ' ' + data.substring(11,19);
                        }
                        // return "<a title=\""+data+"\" ng-click=\"showUserInfoModal('" + full.Originator + "');\" new-Bindcompiledhtml>" + data + "</a>";
                        return "<span title=\""+data+"\">" + data + "</span>";
                    }
                });
                // columns.push({
                //     "mData": "DisplayName",//是返回的数据的一个属性
                //     "sClass": "center hide1024",
                //     "mRender": function (data, type, full) {
                //         data = $scope.htmlEncode(data);
                //         return data;
                //     }
                // });
                columns.push({
                    "mRender": function (data, type, full) {
                        var temp = full;
                        full = JSON.stringify(full);
                        if(temp.createUserId == $rootScope.loginUser.User.ObjectID){//显示
                            return `
                                <button class='btn btn-sm btn-default' ng-click='toUpdateFlow(${full})' title='编辑'>
                                    <i class='glyphicon glyphicon-edit'></i>
                                </button>
                                <button class='btn btn-sm btn-default' ng-click='toCollectFlow(${full})' title='收藏到我的知识'>
                                    <!--<i class='glyphicon glyphicon-cloud-download'></i>-->
                                    <i class="glyphicon glyphicon-star-empty"></i>
                                </button>
                                <button class='btn btn-sm btn-danger' ng-click='toDeleteFlow(${full})' title='删除'>
                                    <i class='glyphicon glyphicon-trash'></i>
                                </button>`;
                        }else{
                            return `
                                <button class='btn btn-sm btn-default' ng-click='toCollectFlow(${full})' title='收藏到我的知识'>
                                    <!--<i class='glyphicon glyphicon-cloud-download'></i>-->
                                    <i class="glyphicon glyphicon-star-empty"></i>
                                </button>`;
                        }

                    }
                });
                return columns;
            }

            $scope.getMyColumns = function () {
                var columns = [];
                columns.push({
                    "mData": "name",//返回数据的键
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);
                        if(data == 'null' || data == null){
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return `<a style='cursor: pointer;' ng-click='toDetailMyFlow(${full})' title='${data}'>${data}</a>`;
                    }
                });
                columns.push({
                    "mData": "tagName",
                    "mRender": function (data, type, full) {
                        //打开流程状态
                        data = data != "" ? data : full.ActivityCode;
                        data = $scope.htmlEncode(data);
                        if(data == 'null' || data == null){
                            data = '';
                        }
                        return "<span title=\""+data+"\">" + data + "</span>";
                    }
                });
                columns.push({
                    "mData": "flowCodeDesc",
                    "sClass": "center hide1024" ,
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);
                        if(data == 'null' || data == null){
                            data = '';
                        }
                        return "<span title=\""+data+"\">"+data+"<span>";
                    }
                });
                columns.push({
                    "mData": "startTime",
                    "sClass": "center hide414",
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);
                        data = data.trim();
                        if(data == 'null' || data == null){
                            data = '';
                        }else{
                            data = data.substring(0,10) + ' ' + data.substring(11,19);
                        }
                        return "<span title=\""+data+"\">"+data+"</span>";
                    }
                });
                columns.push({
                    "mData": "endTime",
                    "sClass": "center hide414",
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);//full.Originator应该是选人控件的ID,
                        data = data.trim();
                        if(data == 'null' || data == null){
                            data = '';
                        }else{
                            data = data.substring(0,10) + ' ' + data.substring(11,19);
                        }
                        return "<span title=\""+data+"\">" + data + "</span>";
                    }
                });
                columns.push({
                    "mRender": function (data, type, full) {
                        full = JSON.stringify(full);
                        return `
                                <button class='btn btn-sm btn-default' ng-click='toUpdateMyFlow(${full})' title='编辑'>
                                    <i class='glyphicon glyphicon-edit'></i>
                                </button>
                                <button class='btn btn-sm btn-default' ng-click='toShareFlow(${full})' title='分享到共享知识'>
                                    <i class='glyphicon glyphicon-share'></i>
                                </button>
                                <button class='btn btn-sm btn-danger' ng-click='toDeleteMyFlow(${full})' title='删除'>
                                    <i class='glyphicon glyphicon-trash'></i>
                                </button>`;
                    }
                });
                return columns;
            }

            $scope.loadScroll = function() {
                $scope.myScroll = new IScroll('.dataTables_scrollBody', {
                    scrollbars: true,
                    bounce: false,
                    mouseWheel: true,
                    interactiveScrollbars: true,
                    shrinkScrollbars: 'scale',
                    fadeScrollbars: true
                });
            };
            $scope.dtFlowOptions = {
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
                "sAjaxSource": fileManagerConfig.listKnowledgeByPageUrl,
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
                    if ($("#StartTime").attr("placeholder") == $("#StartTime").val()) {
                        $scope.StartTimeStart = "";
                        $scope.EndTimeStart = "";
                    } else {
                        $scope.StartTimeStart = $("#StartTime").val();
                        $scope.EndTimeStart = $("#EndTimeStart").val();
                    }
                    if ($("#EndTime").attr("placeholder") == $("#EndTime").val()) {
                        $scope.StartTimeEnd = "";
                        $scope.EndTimeEnd = "";
                    } else {
                        $scope.StartTimeEnd = $("#EndTime").val();
                        $scope.EndTimeEnd = $("#EndTimeEnd").val();
                    }
                    //将时间转化为时间戳
                    var startTimes = new Date($scope.StartTimeStart.replace(/-/g, "/")).getTime();
                    var EndTimes = new Date($scope.StartTimeEnd.replace(/-/g, "/")).getTime();
                    var EndTimeStart = new Date($scope.EndTimeStart.replace(/-/g, "/")).getTime();
                    var EndTimeEnd = new Date($scope.EndTimeEnd.replace(/-/g, "/")).getTime();
                    if (startTimes > EndTimes || EndTimeStart > EndTimeEnd) {
                        $.notify({message: "时间区间错误", status: "danger"});
                        $("#EndTime").css("color", "red");
                        $("#EndTimeEnd").css("color", "red");
                        return false;
                    };
                    aoData.push(//name的值是传输数据的key，value的值是传输数据的value
                        {"name": "startTimeStart", "value": $filter("date")($scope.StartTimeStart, "yyyy-MM-dd HH:mm:ss")},
                        {"name": "startTimeEnd", "value": $filter("date")($scope.StartTimeEnd, "yyyy-MM-dd HH:mm:ss")},
                        {"name": "flowCodes", "value": $scope.WorkflowCodes},
                        {"name": "name", "value": $scope.name },
                        {"name": "tagName", "value": $scope.tagName },
                        {"name": "endTimeStart", "value": $filter("date")($scope.EndTimeStart, "yyyy-MM-dd HH:mm:ss")},
                        {"name": "endTimeEnd", "value": $filter("date")($scope.EndTimeEnd, "yyyy-MM-dd HH:mm:ss")},
                    );
                },
                "aoColumns": $scope.getColumns(), // 字段定义
                // 初始化完成事件,这里需要用到 JQuery ，因为当前表格是 JQuery 的插件
                "initComplete": function (settings, json) {
                    var filter = $(".searchContainer");
                    filter.find("button").unbind("click.DT").bind("click.DT", function () {
                        $scope.WorkflowCodes = $("#sheetWorkflow").SheetUIManager().GetValue();
                        $("#tabfinishWorkitem").dataTable().fnDraw();
                    });
                    $scope.loadScroll();
                },
                // 创建行，未绘画到屏幕上时调用
                "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {//其中aData存放的是每一行的数据
                    //将添加的angular事件添加到作用域中
                    $compile(nRow)($scope);
                    setTimeout(function(){
                        $scope.myScroll.refresh();
                    },300);
                },
                "fnDrawCallback": function () {
                    jqdatables.trcss();
                }
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
                "sAjaxSource": fileManagerConfig.listMyKnowledgeByPageUrl,
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
                    if ($("#MyStartTimeStart").attr("placeholder") == $("#MyStartTimeStart").val() || $("#MyEndTimeStart").attr("placeholder") == $("#MyEndTimeStart").val() ) {
                        $scope.MyStartTimeStart = "";
                        $scope.MyEndTimeStart = "";
                    } else {
                        $scope.MyStartTimeStart = $("#MyStartTimeStart").val();
                        $scope.MyEndTimeStart = $("#MyEndTimeStart").val();
                    }
                    if ($("#MyStartTimeEnd").attr("placeholder") == $("#MyStartTimeEnd").val() || $("#MyEndTimeEnd").attr("placeholder") == $("#MyEndTimeEnd").val()) {
                        $scope.MyStartTimeEnd = "";
                        $scope.MyEndTimeEnd = "";
                    } else {
                        $scope.MyStartTimeEnd = $("#MyStartTimeEnd").val();
                        $scope.MyEndTimeEnd = $("#MyEndTimeEnd").val();
                    }
                    //将时间转化为时间戳
                    var myStartTimes = new Date($scope.MyStartTimeStart.replace(/-/g, "/")).getTime();
                    var myEndTimes = new Date($scope.MyStartTimeEnd.replace(/-/g, "/")).getTime();
                    var myEndTimeStart = new Date($scope.MyEndTimeStart.replace(/-/g, "/")).getTime();
                    var myEndTimeEnd = new Date($scope.MyEndTimeEnd.replace(/-/g, "/")).getTime();
                    if (myStartTimes > myEndTimes || myEndTimeStart > myEndTimeEnd) {
                        $.notify({message: "时间区间错误", status: "danger"});
                        $("#MyStartTimeEnd").css("color", "red");
                        $("#MyEndTimeEnd").css("color", "red");
                        return false;
                    };
                    aoData.push(//name的值是传输数据的key，value的值是传输数据的value
                        {"name": "startTimeStart", "value": $filter("date")($scope.MyStartTimeStart, "yyyy-MM-dd HH:mm:ss")},
                        {"name": "startTimeEnd", "value": $filter("date")($scope.MyStartTimeEnd, "yyyy-MM-dd HH:mm:ss")},
                        {"name": "flowCodes", "value": $scope.WorkflowCodes},
                        {"name": "name", "value": $scope.myName },
                        {"name": "tagName", "value": $scope.myTagName },
                        {"name": "endTimeStart", "value": $filter("date")($scope.MyEndTimeStart, "yyyy-MM-dd HH:mm:ss")},
                        {"name": "endTimeEnd", "value": $filter("date")($scope.MyEndTimeEnd, "yyyy-MM-dd HH:mm:ss")},
                    );
                },
                "aoColumns": $scope.getMyColumns(), // 字段定义
                // 初始化完成事件,这里需要用到 JQuery ，因为当前表格是 JQuery 的插件
                "initComplete": function (settings, json) {
                    var filter = $(".searchContainer");
                    filter.find("button").unbind("click.DT").bind("click.DT", function () {
                        $scope.WorkflowCodes = $("#sheetMyWorkflow").SheetUIManager().GetValue();
                        $("#tabMyFlow").dataTable().fnDraw();
                    });
                    $scope.loadScroll();
                },
                // 创建行，未绘画到屏幕上时调用
                "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {//其中aData存放的是每一行的数据
                    //将添加的angular事件添加到作用域中
                    $compile(nRow)($scope);
                    setTimeout(function(){
                        $scope.myScroll.refresh();
                    },300);
                },
                "fnDrawCallback": function () {
                    jqdatables.trcss();
                }
            };

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

            //将标签进行后加载
            setTimeout(function () {
                $scope.renderTag('tagName');
                $scope.renderTag('myTagName');
            },500);

            //点击标签的时候重置当前的宽度
            $("#tagName").focus(function () {
                var inputWidth = $(this).outerWidth();
                $("div.l-box-select").css({"width": inputWidth - 2 + 'px'});
            })
            $("#myTagName").focus(function () {
                var inputWidth = $(this).outerWidth();
                $("div.l-box-select").css({"width": inputWidth - 2 + 'px'});
            })
						
            /*************共享知识新建知识模态框*****************/
            $scope.newFlow = function (data) {
                // 清空原始输入数据
                $rootScope.temp.tempModel.name = "";
                $rootScope.temp.tempModel.desc = "";
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
                            templateUrl: 'newFlow.html',    // 指向上面创建的视图
                            controller: 'ModalsController',// 初始化模态范围
                            size: "lg",
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

                var times = setInterval(function () {
                    if ($("#WorkflowCodes").length > 0) {//id没有改过来，因为没有用到
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //console.log($("#WorkflowCodes"));
                        $("#WorkflowCodes").css("width", "246px");
                    }
                }, 50);

                //此处使用jQuery插件，需要等到其他js执行完毕，才能执行此函数
                setTimeout(function () {
                    $scope.renderTag('newTag');
                },500);
            }

            /*************共享知识编辑模态框********************/
            $scope.toUpdateFlow = function (data) {
                for(var key in data){
                    if(data[key] == null){
                        data[key] = "";
                    }
                }
                $scope.temp.flow = data;
                $scope.temp.model.name = data.name;
                $scope.temp.model.startTime = data.startTime.substring(0,10)+' '+data.startTime.substring(11,19);
                $scope.temp.model.endTime = data.endTime.substring(0,10)+' '+data.endTime.substring(11,19);
                $scope.temp.model.tag = data.tagName;
                $scope.temp.model.descList=data.descList;
                if($scope.temp.model.descList==''){
                    $scope.temp.model.descList=[{key:new Date().getTime(),desc:'',detail:''}];
                }
                $rootScope.flowCodeDesc = data.flowCodeDesc;
                $rootScope.flowId = data.flowId;

                
                var arrOrgList = data.permission.orgs;

                var tmpData;
                var AgencyID;
                if (tmpData == undefined) AgencyID = "";
                else AgencyID = tmpData;
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
                        templateUrl: 'updateFlow.html',    // 指向上面创建的视图
                        controller: 'ModalsController',// 初始化模态范围
                        size: "lg",
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

                // var arrOrgList = [];//显示权限
                // for(var i = 0; i < lenOrgList; i++){
                //     arrOrgList.push('429a78b-2d75-594f-91c6-567fab7c5e5f');
                // }
                var times = setInterval(function() {
                    if($("#editFlowPer").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        var control = $("#editFlowPer").SheetUIManager();
                        control.SetValue(arrOrgList);
                    }
                }, 800);

                //此处使用jQuery插件，需要等到其他js执行完毕，才能执行此函数
                setTimeout(function () {
                    $scope.renderTag('editTag');
                },500);
            }

            /*************共享知识收藏到我的知识模态框**********/
            $scope.toCollectFlow = function (data) {
                $scope.temp.flow = data;
                $scope.temp.model.name = data.name;
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
                            templateUrl: 'collectFlow.html',    // 指向上面创建的视图
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

            /*************共享知识删除模态框*****************/
            $scope.toDeleteFlow = function (data) {
                // $scope.fileNavigator = $rootScope.scope.fileNavigator;//参数
                // $rootScope.temp = data;
                // var lenOrgList = data.model.filePermission.orgList.length;//组织的长度
                // data = "";
                $scope.temp.flow = data;
                $scope.temp.model.name = data.name;
                var AgencyID,tmpData;
                if (tmpData == undefined) AgencyID = "";
                else AgencyID = tmpData;
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
                        templateUrl: 'deleteFlow.html',    // 指向上面创建的视图
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

                // var arrOrgList = [];
                // for(var i = 0; i < lenOrgList; i++){
                //     //strOrgList += '<li class="select2-search-choice" id="'+ $rootScope.temp.model.filePermission.orgList[i].id + '" data-code="18f923a7-5a5e-426d-94ae-a55ad1a4b240" style="cursor: pointer; margin-top: 2px; background-color: rgb(250, 250, 250);"><div>'+ $rootScope.temp.model.filePermission.orgList[i].name +'</div><a href="javascript:void(0)" class="select2-search-choice-close"></a></li>';
                //     arrOrgList.push($rootScope.temp.model.filePermission.orgList[i].id);
                // }
                var times = setInterval(function() {
                    if($("#editPer").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //$("#editPer").find("ul").prepend(strOrgList);
                        // var control = $("#editPer").SheetUIManager();
                        // control.SetValue(arrOrgList);
                    }
                }, 800);
            }

            // ***********共享知识的详情页*******************
            $scope.toDetailFlow = function (data) {
                for(var key in data){
                    if(data[key] == null){
                        data[key] = "";
                    }
                }
                $scope.temp.flow = data;
                $scope.temp.model.name = data.name;
                $scope.temp.model.startTime = data.startTime.substring(0,10)+' '+data.startTime.substring(11,19);
                $scope.temp.model.endTime = data.endTime.substring(0,10)+' '+data.endTime.substring(11,19);
                $scope.temp.model.tag = data.tagName;
                $scope.temp.model.descList = data.descList;
                if($scope.temp.model.descList==''){
                    $scope.temp.model.descList=[{key:new Date().getTime(),desc:'',detail:''}];
                }
                $rootScope.flowCodeDesc = data.flowCodeDesc;
                $rootScope.flowId = data.flowId;
                var arrOrgList = data.permission.orgs;

                var tmpData;
                var AgencyID;
                if (tmpData == undefined) AgencyID = "";
                else AgencyID = tmpData;
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
                        templateUrl: 'detail.html',    // 指向上面创建的视图
                        controller: 'ModalsController',// 初始化模态范围
                        size: "lg",
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

                // var arrOrgList = [];//显示权限
                // for(var i = 0; i < lenOrgList; i++){
                //     arrOrgList.push('429a78b-2d75-594f-91c6-567fab7c5e5f');
                // }
                var times = setInterval(function() {
                    if($("#detailFlow").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        var control = $("#detailFlow").SheetUIManager();
                        control.SetValue(arrOrgList);
                    }
                }, 800);

                //此处使用jQuery插件，需要等到其他js执行完毕，才能执行此函数
                setTimeout(function () {
                    $scope.renderTag('editTag');
                },500);
            }

            // *********我的知识新建知识模态框****************
            $scope.newMyFlow = function (data) {
                // 清空原始输入数据
                $rootScope.temp.tempModel.name = "";
                $rootScope.temp.tempModel.desc = "";
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
                        templateUrl: 'newMyFlow.html',    // 指向上面创建的视图
                        controller: 'ModalsController',// 初始化模态范围
                        size: "lg",
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

                var times = setInterval(function () {
                    if ($("#WorkflowCodes").length > 0) {//id没有改过来，因为没有用到
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //console.log($("#WorkflowCodes"));
                        $("#WorkflowCodes").css("width", "246px");
                    }
                }, 50);

                //此处使用jQuery插件，需要等到其他js执行完毕，才能执行此函数
                setTimeout(function () {
                    $scope.renderTag('newMyTag');
                },500);
            }

            /*************我的知识编辑模态框********************/
            $scope.toUpdateMyFlow = function (data) {
                for(var key in data){
                    if(data[key] == null){
                        data[key] = "";
                    }
                }
                $scope.temp.flow = data;
                $scope.temp.model.name = data.name;
                $scope.temp.model.startTime = data.startTime.substring(0,10)+' '+data.startTime.substring(11,19);
                $scope.temp.model.endTime = data.endTime.substring(0,10)+' '+data.endTime.substring(11,19);
                $scope.temp.model.tag = data.tagName;
                $rootScope.flowCodeDesc = data.flowCodeDesc;
                $rootScope.flowId = data.flowId;
                $scope.temp.model.descList=data.descList;
                if($scope.temp.model.descList==''){
                    $scope.temp.model.descList=[{key:new Date().getTime(),desc:'',detail:''}];
                }
                // var arrOrgList = data.permission.orgs;

                var tmpData;
                var AgencyID;
                if (tmpData == undefined) AgencyID = "";
                else AgencyID = tmpData;
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
                        templateUrl: 'updateMyFlow.html',    // 指向上面创建的视图
                        controller: 'ModalsController',// 初始化模态范围
                        size: "lg",
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

                // var arrOrgList = [];//显示权限
                // for(var i = 0; i < lenOrgList; i++){
                //     arrOrgList.push('429a78b-2d75-594f-91c6-567fab7c5e5f');
                // }
                var times = setInterval(function() {
                    if($("#editFlowPer").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        // var control = $("#editFlowPer").SheetUIManager();
                        // control.SetValue(arrOrgList);
                    }
                }, 800);

                //此处使用jQuery插件，需要等到其他js执行完毕，才能执行此函数
                setTimeout(function () {
                    $scope.renderTag('editMyTag');
                },500);
            }

            /*************我的知识分享到共享知识模态框**********/
            $scope.toShareFlow = function (data) {
                $scope.temp.flow = data;
                $scope.temp.model.name = data.name;

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
                            templateUrl: 'shareFlow.html',    // 指向上面创建的视图
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

            /*************我的知识删除模态框*****************/
            $scope.toDeleteMyFlow = function (data) {
                // $scope.fileNavigator = $rootScope.scope.fileNavigator;//参数
                // $rootScope.temp = data;
                // var lenOrgList = data.model.filePermission.orgList.length;//组织的长度
                // data = "";
                $scope.temp.flow = data;
                $scope.temp.model.name = data.name;
                var AgencyID,tmpData;
                if (tmpData == undefined) AgencyID = "";
                else AgencyID = tmpData;
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
                        templateUrl: 'deleteMyFlow.html',    // 指向上面创建的视图
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

                // var arrOrgList = [];
                // for(var i = 0; i < lenOrgList; i++){
                //     //strOrgList += '<li class="select2-search-choice" id="'+ $rootScope.temp.model.filePermission.orgList[i].id + '" data-code="18f923a7-5a5e-426d-94ae-a55ad1a4b240" style="cursor: pointer; margin-top: 2px; background-color: rgb(250, 250, 250);"><div>'+ $rootScope.temp.model.filePermission.orgList[i].name +'</div><a href="javascript:void(0)" class="select2-search-choice-close"></a></li>';
                //     arrOrgList.push($rootScope.temp.model.filePermission.orgList[i].id);
                // }
                var times = setInterval(function() {
                    if($("#editPer").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        //$("#editPer").find("ul").prepend(strOrgList);
                        // var control = $("#editPer").SheetUIManager();
                        // control.SetValue(arrOrgList);
                    }
                }, 800);
            }

            // ***********我的知识的详情页*******************
            $scope.toDetailMyFlow = function (data) {
                for(var key in data){
                    if(data[key] == null){
                        data[key] = "";
                    }
                }
                $scope.temp.flow = data;
                $scope.temp.model.name = data.name;
                $scope.temp.model.startTime = data.startTime.substring(0,10)+' '+data.startTime.substring(11,19);
                $scope.temp.model.endTime = data.endTime.substring(0,10)+' '+data.endTime.substring(11,19);
                $scope.temp.model.tag = data.tagName;
                $rootScope.flowCodeDesc = data.flowCodeDesc;
                $rootScope.flowId = data.flowId;
                $scope.temp.model.descList = data.descList;
                if($scope.temp.model.descList==''){
                    $scope.temp.model.descList=[{key:new Date().getTime(),desc:'',detail:''}];
                }
                // var arrOrgList = data.permission.orgs;

                var tmpData;
                var AgencyID;
                if (tmpData == undefined) AgencyID = "";
                else AgencyID = tmpData;
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
                        templateUrl: 'detailMyFlow.html',    // 指向上面创建的视图
                        controller: 'ModalsController',// 初始化模态范围
                        size: "lg",
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

                // var arrOrgList = [];//显示权限
                // for(var i = 0; i < lenOrgList; i++){
                //     arrOrgList.push('429a78b-2d75-594f-91c6-567fab7c5e5f');
                // }
                var times = setInterval(function() {
                    if($("#detailFlow").length>0){
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);
                        // var control = $("#detailFlow").SheetUIManager();
                        // control.SetValue(arrOrgList);
                    }
                }, 800);

                //此处使用jQuery插件，需要等到其他js执行完毕，才能执行此函数
                setTimeout(function () {
                    $scope.renderTag('editTag');
                },500);
            }

        }]);
})(window, angular, jQuery);