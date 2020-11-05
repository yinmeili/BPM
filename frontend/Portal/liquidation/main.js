(function (window, angular, $) {
    'use strict';
    app.controller('liquidationCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", '$modal', 'item', 'fileNavigator',
        function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $modal, Item, FileNavigator) {
            //$rootScope.flowScope = $scope;
            //实例化参数

            $scope.types = [

                { name: '禁用', id: "1" },
                { name: '启用', id: "2" },];
            $scope.$on('$viewContentLoaded', function (event) {
                $scope.init();
                $scope.myScroll = null
            });

            $scope.init = function () {

            }


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
                    "mData": "index",
                    "mRender": function (data, type, full) {

                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return "<span title=\"" + data + "\">" + data + "<span>";
                    }
                });
                columns.push({
                    "mData": "name",//返回数据的键
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);

                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return `<a style='cursor: pointer;' ng-click='toDetailLiquidation(${full})' title='${data}'>${data}</a>`;
                    }
                });
                columns.push({
                    "mData": "ipAddress",
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
                    "mData": "statusStr",
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);
                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return "<span title=\"" + data + "\">" + data + "</span>";
                    }
                });
                columns.push({
                    "mData": "doTime",
                    "sClass": "center hide1024",
                    "mRender": function (data, type, full) {
                        data = $scope.htmlEncode(data);
                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return "<span title=\"" + data + "\">" + data + "<span>";
                    }
                });

                columns.push({
                    "mData": "operateStep",
                    "sClass": "center hide1024",
                    "mRender": function (data, type, full) {

                        if (data == 'null' || data == null) {
                            data = '';
                        }
                        full = JSON.stringify(full);
                        return "<span title=\"" + data + "\">" + data + "<span>";
                    }
                });



                columns.push({
                    "mRender": function (data, type, full) {
                        full = JSON.stringify(full);
                        return `
                              <button class='btn btn-sm btn-default' ng-click='updateLiquidation(${full})' title='编辑'>
                                  <i class='glyphicon glyphicon-edit'></i>
                              </button>
                         
                              <button class='btn btn-sm btn-danger' ng-click='toDeleteLiquidation(${full})' title='删除'>
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
                "sAjaxSource": "/Portal/liquidation/listLiquidationByPage",
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


                    aoData.push(//name的值是传输数据的key，value的值是传输数据的value
                        { "name": "keyword", "value": $scope.liquidationKeyword },
                        { "name": "status", "value": $scope.liquidationStatusIndex },

                    );

                },
                "aoColumns": $scope.getMyColumns(), // 字段定义
                "initComplete": function (settings, json) {
                    var filter = $("#search_liquidation");
                    filter.unbind("click.DT").bind("click.DT", function () {
                        var myselect = document.getElementById('liquidationStatus');
                        $scope.index = myselect.selectedIndex;
                        if ($scope.index === 0) {
                            $scope.liquidationStatusIndex = '';
                        }

                        else {

                            $scope.liquidationStatusIndex = $scope.index - 1
                        }


                        $("#tabLiquidationFlow").dataTable().fnDraw();
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




            $scope.createLiquidation = function (data) {
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
                        templateUrl: 'createLiquidation.html',    // 指向上面创建的视图
                        controller: 'liquidationController',// 初始化模态范围
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

                                ]).then(function () {
                                    return $ocLazyLoad.load([

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
            $scope.updateLiquidation = function (data) {

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
                        templateUrl: 'editLiquidation.html',    // 指向上面创建的视图
                        controller: 'liquidationController',// 初始化模态范围
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

                                ]).then(function () {
                                    return $ocLazyLoad.load([

                                    ]);
                                });
                            }]
                        }
                    });
                    modalInstance.opened.then(function () {

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
            $scope.toDeleteLiquidation = function (data) {

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
                        templateUrl: 'deleteLiquidation.html',    // 指向上面创建的视图
                        controller: 'liquidationController',// 初始化模态范围
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

                                ]).then(function () {
                                    return $ocLazyLoad.load([

                                    ]);
                                });
                            }]
                        }
                    });
                    modalInstance.opened.then(function () {

                    });
                });


                var times = setInterval(function () {
                    if ($("#editPer").length > 0) {
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);

                    }
                }, 800);
            }

            // ***********共享知识的详情页*******************
            $scope.toDetailLiquidation = function (data) {

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

                    var modalInstance = $modal.open({
                        templateUrl: 'detailLiquidation.html',    // 指向上面创建的视图
                        controller: 'liquidationController',// 初始化模态范围
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

                                ]).then(function () {
                                    return $ocLazyLoad.load([

                                    ]);
                                });
                            }]
                        }
                    });
                    modalInstance.opened.then(function () {

                    });
                });


                var times = setInterval(function () {
                    if ($("#detailFlow").length > 0) {
                        clearInterval(times);
                        $(".select2-search-field").find("input").css("z-index", 0);

                    }
                }, 800);


            }

        }]);
})(window, angular, jQuery);