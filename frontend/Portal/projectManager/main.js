(function (window, angular, $) {
  'use strict';
  app.controller('projectManagerCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", '$modal', 'item', 'fileNavigator',
    function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $modal, Item, FileNavigator) {
      //$rootScope.flowScope = $scope;
      //实例化参数
      $scope.$on('$viewContentLoaded', function (event) {
        $scope.init();
        $scope.myScroll = null
      });

      $scope.init = function () {
      }
      $scope.ProjectManagerLeader = {
        Editable: true,
        Visiable: true,
        //IsMultiple: true,
        CompanyVisible: false,
        CompanySelectable: false,
      }
      $scope.ProjectManagerStartTimeStart = {
        dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
        onpicked: function (e) {
          $rootScope.ProjectManagerStartTimeStart = e.el.value;
        }
      }
      $scope.ProjectManagerStartTimeEnd = {
        dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
        onpicked: function (e) {
          $rootScope.ProjectManagerStartTimeEnd = e.el.value;

        }
      }
      $scope.ProjectManagerEndTimeStart = {
        dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
        onpicked: function (e) {
          $rootScope.ProjectManagerEndTimeStart = e.el.value;
        }
      }
      $scope.ProjectManagerEndTimeEnd = {
        dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
        onpicked: function (e) {
          $rootScope.ProjectManagerEndTimeEnd = e.el.value;

        }
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
      $scope.getMyColumns = function () {
        var columns = [

        ];
        columns.push({
          "mData": "name",//返回数据的键
          "mRender": function (data, type, full) {
            data = $scope.htmlEncode(data);

            if (data == 'null' || data == null) {
              data = '';
            }
            full = JSON.stringify(full);
            return `<a style='cursor: pointer;' ng-click='toDetailProjectManager(${full})' title='${data}'>${data}</a>`;
          }
        });
        columns.push({
          "mData": "leaderName",
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
          "mData": "startTime",
          "mRender": function (data, type, full) {
            data = $scope.htmlEncode(data);
            if (data == 'null' || data == null) {
              data = '';
            }
            else{
              data=data.split(" ");
              data.pop();
            }
            full = JSON.stringify(full);
            return "<span title=\"" + data + "\">" + data + "</span>";
          }
        });
        columns.push({
          "mData": "endTime",
          "mRender": function (data, type, full) {
            data = $scope.htmlEncode(data);
            if (data == 'null' || data == null) {
              data = '';
            }
            else{
              data=data.split(" ");
              data.pop();
            }
            full = JSON.stringify(full);
            return "<span title=\"" + data + "\">" + data + "<span>";
          }
        });


        columns.push({
          "mRender": function (data, type, full) {
            full = JSON.stringify(full);
            return `
                            <button class='btn btn-sm btn-default' ng-click='updateProjectManager(${full})' title='编辑'>
                                <i class='glyphicon glyphicon-edit'></i>
                            </button>
                       
                            <button class='btn btn-sm btn-danger' ng-click='toDeleteProjectManager(${full})' title='删除'>
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
      $scope.ProjectManagerOptions = {
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
        "sAjaxSource": "/Portal/project/listProjectByPage",
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
            { "name": "keyword", "value": $scope.ProjectManagerKeyword },
            { "name": "leaderId", "value": $scope.LeaderID },
            { "name": "endTimeStart", "value": $filter("date")($rootScope.ProjectManagerEndTimeStart, "yyyy-MM-dd HH:mm:ss") },
            { "name": "endTimeEnd", "value": $filter("date")($rootScope.ProjectManagerEndTimeEnd, "yyyy-MM-dd HH:mm:ss") },
            { "name": "startTimeStart", "value": $filter("date")($rootScope.ProjectManagerStartTimeStart, "yyyy-MM-dd HH:mm:ss") },
            { "name": "startTimeEnd", "value": $filter("date")($rootScope.ProjectManagerStartTimeEnd, "yyyy-MM-dd HH:mm:ss") },
          );

        },
        "aoColumns": $scope.getMyColumns(), // 字段定义
        "initComplete": function (settings, json) {
          var filter = $("#ProjectManagerSearch");
          filter.unbind("click.DT").bind("click.DT", function () {
            $scope.LeaderID = $("#ProjectManagerLeader").SheetUIManager().GetValue();
            if ($rootScope.ProjectManagerStartTimeStart == undefined && $rootScope.ProjectManagerStartTimeEnd == undefined) {
              $rootScope.ProjectManagerStartTimeStart = "";
              $rootScope.ProjectManagerStartTimeEnd = "";
            }
            else if ($("#ProjectManagerStartTimeStart").val() == "" && $("#ProjectManagerStartTimeEnd").val() == "") {
              $rootScope.ProjectManagerStartTimeStart = "";
              $rootScope.ProjectManagerStartTimeEnd = "";
            }
            else if ($("#ProjectManagerStartTimeStart").val() == "" && $("#ProjectManagerStartTimeEnd").val() !== "") {
              $rootScope.ProjectManagerStartTimeStart = "";

            }
            else if ($("#ProjectManagerStartTimeStart").val() !== "" && $("#ProjectManagerStartTimeEnd").val() == "") {
              $rootScope.ProjectManagerStartTimeEnd = "";
            }
            else  if ($("#ProjectManagerStartTimeStart").val() !== "" && $("#ProjectManagerStartTimeEnd").val() !== ""){
              var StartTimesStart = new Date($rootScope.ProjectManagerStartTimeStart.replace(/-/g, "/")).getTime();
              var StartTimesEnd = new Date($rootScope.ProjectManagerStartTimeEnd.replace(/-/g, "/")).getTime();
                if (StartTimesStart > StartTimesEnd) {
                  $.notify({ message: "时间区间错误", status: "danger" });
                  $("#ProjectManagerStartTimeEnd").css("color", "red");
                  return false;
                };
              
            }
      
         if ( $rootScope.ProjectManagerEndTimeStart == undefined && $rootScope.ProjectManagerEndTimeEnd == undefined) {
              $rootScope.ProjectManagerEndTimeStart="";
              $rootScope.ProjectManagerEndTimeEnd="";
          }
          else if($("#ProjectManagerEndTimeStart").val()==""&&$("#ProjectManagerEndTimeEnd").val()=="")
          {
              $rootScope.ProjectManagerEndTimeStart="";
              $rootScope.ProjectManagerEndTimeEnd="";
          }
          else if($("#ProjectManagerEndTimeStart").val()==""&&$("#ProjectManagerEndTimeEnd").val()!=="")
          {
              $rootScope.ProjectManagerEndTimeStart="";
              
          }
          else if($("#ProjectManagerEndTimeStart").val()!==""&&$("#ProjectManagerEndTimeEnd").val()=="")
          {
              $rootScope.ProjectManagerEndTimeEnd="";
          }
          
          else if($("#ProjectManagerEndTimeStart").val()!==""&&$("#ProjectManagerEndTimeEnd").val()!==""){
            var EndTimesStart = new Date($rootScope.ProjectManagerEndTimeStart.replace(/-/g, "/")).getTime();
            var EndTimesEnd = new Date($rootScope.ProjectManagerEndTimeEnd.replace(/-/g, "/")).getTime();
            if (EndTimesStart > EndTimesEnd) {
              $.notify({ message: "时间区间错误", status: "danger" });
              $("#ProjectManagerEndTimeEnd").css("color", "red");
              return false;
            }
            }
          
         
              $("#tabProjectManager").dataTable().fnDraw();
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

      $('#ProjectManagerKeyword').bind('keypress', function (event) {
        if (event.keyCode == "13") {
          $("#ProjectManagerSearch").click();
        }
      })
      $('#ProjectManagerStartTimeStart').bind('keypress', function (event) {
        if (event.keyCode == "13") {
          $("#ProjectManagerSearch").click();
        }
      })
      $('#ProjectManagerStartTimeEnd').bind('keypress', function (event) {
        if (event.keyCode == "13") {
          $("#ProjectManagerSearch").click();
        }
      })
      $('#ProjectManagerEndTimeStart').bind('keypress', function (event) {
        if (event.keyCode == "13") {
          $("#ProjectManagerSearch").click();
        }
      })
      $('#ProjectManagerEndTimeEnd').bind('keypress', function (event) {
        if (event.keyCode == "13") {
          $("#ProjectManagerSearch").click();
        }
      })

    //新建
      $scope.createProjectManager = function (data) {
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
            templateUrl: 'createProjectManage.html',    // 指向上面创建的视图
            controller: 'ProjectManagerlController',// 初始化模态范围
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
        })

        


      }

      $scope.updateProjectManager = function (data) {

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
            templateUrl: 'editProjectManage.html',    // 指向上面创建的视图
            controller: 'ProjectManagerlController',// 初始化模态范围
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
          if ($("#editFlowPer").length > 0) {
            clearInterval(times);
            $(".select2-search-field").find("input").css("z-index", 0);

          }
        }, 800);
      }



      /*************删除模态框*****************/
      $scope.toDeleteProjectManager = function (data) {

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
            templateUrl: 'deleteProjectManage.html',    // 指向上面创建的视图
            controller: 'ProjectManagerlController',// 初始化模态范围
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

      // ***********详情页*******************
      $scope.toDetailProjectManager = function (data) {

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
            templateUrl: 'detailProjectManage.html',    // 指向上面创建的视图
            controller: 'ProjectManagerlController',// 初始化模态范围
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
          if ($("#detailFlow").length > 0) {
            clearInterval(times);
            $(".select2-search-field").find("input").css("z-index", 0);

          }
        }, 800);


      }

    }]);
})(window, angular, jQuery);