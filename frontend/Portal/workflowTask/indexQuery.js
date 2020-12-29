(function (window, angular, $) {
  'use strict';
  app.controller('WorkflowTaskIndexQueryCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", '$modal', 'item', 'fileNavigator',
      function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $modal, Item, FileNavigator) {
          $scope.dex=document.getElementById('WorkflowTaskIndexQueryIndex').selectedIndex 
          $scope.WorkFlowTaskIndexQueryIndex = [
              { WorkflowTaskIndexQueryIndexName: '清算', id: "liquidation" },
          ];
          
          //共享知识的日期控件初始化
          $scope.WorkFlowTaskIndexQuerysearchStartTimeStart = {
              dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
              onpicked: function (e) {
                  $rootScope.WorkFlowTaskIndexQuerysearchStartTimeStart = e.el.value;
              }
          }
          $scope.WorkFlowTaskIndexQuerysearchStartTimeEnd = {
              dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
              onpicked: function (e) {
                  $rootScope.WorkFlowTaskIndexQuerysearchStartTimeEnd = e.el.value;
              
              }
          }
       

          $scope.$on('$viewContentLoaded', function (event) {
              $scope.init();
              $scope.myScroll = null
          });

          $scope.init = function () {
              $scope.name = $translate.instant("WorkItemController.FinishedWorkitem");
              $scope.searchStartTimeStart = datecalculation.redDays(new Date(), 30);
              $scope.searchStartTimeEnd = datecalculation.addDays(new Date(), 30);
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
              var columns = [];
              columns.push({
                  "mData": "workflowCodeStr",//返回数据的键
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
                  "mData": "startTime",
                  "sClass": "center hide1024",
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
                  "mData": "userDisplayName",//返回数据的键
                  "sClass": "center hide414",
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
                  "mData": "executeType",//返回数据的键
                  "sClass": "center hide414",
                  "mRender": function (data, type, full) {
                      data = $scope.htmlEncode(data);

                      if (data == 'null' || data == null) {
                          data = '';
                      }
                     else if(data==1){
                         data="是"
                     }
                     else if(data==0){
                         data="否"
                     }
                      
                      full = JSON.stringify(full);
                      return "<span title=\"" + data + "\">" + data + "</span>";
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
          $scope.dtWorkFlowTaskIndexQueryOptions = {
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
              "sAjaxSource": "/Portal/workflowTask/listWorkFlowTaskByPage",
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
                  if($scope.WorkflowTaskIndexQueryIndex ==undefined){
                      $scope.WorkflowTaskIndexQueryIndex=$scope.WorkflowTaskIndexQueryIndex=$scope.WorkFlowTaskIndexQueryIndex[0].id ;
                  }
                  aoData.push(//name的值是传输数据的key，value的值是传输数据的value
                      { "name": "startTimeStart", "value": $filter("date")( $rootScope.WorkFlowTaskIndexQuerysearchStartTimeStart, "yyyy-MM-dd HH:mm:ss") },
                      { "name": "startTimeEnd", "value": $filter("date")( $rootScope.WorkFlowTaskIndexQuerysearchStartTimeEnd ,"yyyy-MM-dd HH:mm:ss") },
                      { "name": "userDisplayName", "value": $scope.WorkflowTaskIndexQueryUserName },
                      { "name": "flowCode", "value": $scope.WorkflowTaskIndexQueryIndex },
                  );
                 
              },
              "aoColumns": $scope.getMyColumns(), // 字段定义
              // 初始化完成事件,这里需要用到 JQuery ，因为当前表格是 JQuery 的插件
              "initComplete": function (settings, json) {
                  var filter = $("#WorkFlowTaskIndexQuerySearch");
                  filter.unbind("click.DT").bind("click.DT", function () {
                   
                     var myselect = document.getElementById('WorkflowTaskIndexQueryIndex');
                     $scope.index = myselect.selectedIndex;
                     if ($scope.index === 0) {
                      $scope.WorkflowTaskIndexQueryIndex=$scope.WorkFlowTaskIndexQueryIndex[0].id
                     }
                     else  {
                      $scope.WorkflowTaskIndexQueryIndex = $scope.WorkFlowTaskIndexQueryIndex[$scope.index-1].id ;
                  }
                  
                      if ( $rootScope.WorkFlowTaskIndexQuerysearchStartTimeStart == undefined && $rootScope.WorkFlowTaskIndexQuerysearchStartTimeEnd == undefined) {
                          $rootScope.WorkFlowTaskIndexQuerysearchStartTimeStart="";
                          $rootScope.WorkFlowTaskIndexQuerysearchStartTimeEnd="";
                      }
                      else if($("#WorkFlowTaskIndexQuerysearchStartTimeStart").val()==""&&$("#WorkFlowTaskIndexQuerysearchStartTimeEnd").val()=="")
                      {
                          $rootScope.WorkFlowTaskIndexQuerysearchStartTimeStart="";
                          $rootScope.WorkFlowTaskIndexQuerysearchStartTimeEnd="";
                      }
                      else if($("#WorkFlowTaskIndexQuerysearchStartTimeStart").val()==""&&$("#WorkFlowTaskIndexQuerysearchStartTimeEnd").val()!=="")
                      {
                          $rootScope.WorkFlowTaskIndexQuerysearchStartTimeStart="";
                          
                      }
                      else if($("#WorkFlowTaskIndexQuerysearchStartTimeStart").val()!==""&&$("#WorkFlowTaskIndexQuerysearchStartTimeEnd").val()=="")
                      {
                          $rootScope.WorkFlowTaskIndexQuerysearchStartTimeEnd="";
                      }
                      else{
                          
                      var myStartTimes = new Date( $rootScope.WorkFlowTaskIndexQuerysearchStartTimeStart.replace(/-/g, "/")).getTime();
                          var myEndTimes = new Date($rootScope.WorkFlowTaskIndexQuerysearchStartTimeEnd.replace(/-/g, "/")).getTime();
                          if (myStartTimes > myEndTimes) {
                              $.notify({ message: "时间区间错误", status: "danger" });
                              $("#MyStartTimeEnd").css("color", "red");
                              return false;
                          };
                      }
                      $("#tabWorkFlowTaskIndexQuery").dataTable().fnDraw();
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
          $('#WorkflowTaskIndexQueryUserName').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#WorkFlowTaskIndexQuerySearch").click();
              }
             })
             $('#WorkflowTaskIndexQueryIndex').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#WorkFlowTaskIndexQuerySearch").click();
              }
             })
             $('#WorkFlowTaskIndexQuerysearchStartTimeStart').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#WorkFlowTaskIndexQuerySearch").click();
              }
             })
             $('#WorkFlowTaskIndexQuerysearchStartTimeEnd').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#WorkFlowTaskIndexQuerySearch").click();
              }
             })

         
         

          
      }]);

})(window, angular, jQuery);