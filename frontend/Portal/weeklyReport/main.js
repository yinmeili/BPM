(function (window, angular, $) {
  'use strict';
  app.controller('weeklyReportCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", '$modal', 'item', 'fileNavigator',
      function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $modal, Item, FileNavigator) {
          //$rootScope.flowScope = $scope;
          //实例化参数
         $scope.$on('$viewContentLoaded', function (event) {
            $scope.init();
            $.ajax({
                "url": "/Portal/user/listChildrenOrg",
                "type": "get",
                "dataType": "json",
                "success": function (data) {
                    $scope.WeeklySelectDatas=data;
                    // $('#WeeklySelect').selectpicker('refresh');
                    // //render方法强制重新渲染引导程序 - 选择ui。
                    // $('#WeeklySelect').selectpicker('render');
                    // $(".selectpicker").selectpicker({
                    //     noneSelectedText : '请选择'//默认显示内容
                    // });
                    var WeeklyData = [];
                    WeeklyData = $scope.WeeklySelectDatas;
                    //定义一个对象数组，用于储存所需选项
                    for (var i = 0; i <  WeeklyData.length; i++) {
                        $("#WeeklySelect").append($("<option value=\"" + WeeklyData[i].id +"\">" + WeeklyData[i].text + "</option>"));
                    }
    
                },
                "error": function () {
                }
            })  
         })
          $scope.searchWeeklyReportWasAgentOptions = {
              Editable: true,
              Visiable: true,
              //IsMultiple: true,
              UserVisible: true,//是否显示用户
             
          }
               //共享知识的日期控件初始化
          $scope.searchWeeklyReportStartTimeStart = {
              dateFmt: 'yyyy-MM-dd', realDateFmt: "yyyy-MM-dd", minDate: '2012-1-1', maxDate: '2099-12-31',
              skin: 'whyGreen',//皮肤
              firstDayOfWeek: 1, errDealMode: 3, isShowClear: "true",
              disabledDays: [1, 2, 3, 4, 5, 6],//只选周一 
              onpicked: function (e) {
                  $rootScope.searchWeeklyStartTimeStart = e.el.value;
                  var date = new Date($rootScope.searchWeeklyStartTimeStart);
                  var weekFirstDay = new Date(date - (date.getDay() - 1) * 86400000);
                  var firstMonth = Number(weekFirstDay.getMonth()) + 1;
                  var weekFirstDays = weekFirstDay.getDate();
                  if (firstMonth <= 9) {
                    firstMonth="0"+firstMonth

                  }
                  if (weekFirstDays  <= 9) {
                    weekFirstDays="0"+weekFirstDays 
                  }
                  var WeekFirstDate = weekFirstDay.getFullYear() + '-' + firstMonth + '-' + weekFirstDays;
                  //这周最后一天
                  var weekLastDay = new Date((weekFirstDay / 1000 + 6 * 86400) * 1000);
                  var lastMonth = Number(weekLastDay.getMonth()) + 1;
                  var weekLastDays = weekLastDay.getDate();
                  if (lastMonth <= 9) {
                      lastMonth = "0" + lastMonth

                  }
                  if (weekLastDays <= 9) {
                      weekLastDays = "0" + weekLastDays
                  }
                  if (firstMonth > lastMonth) {
                      var WeekLastDate = weekFirstDay.getFullYear() + 1 + '-' + lastMonth + '-' + weekLastDays;
                  } else {
                      var WeekLastDate = weekFirstDay.getFullYear() + '-' + lastMonth + '-' + weekLastDays;
                  }
                  $rootScope.searchWeeklyStartTimeEnd = WeekLastDate;
                  $scope.$apply();
                 

              }
          }

          $scope.$on('$viewContentLoaded', function (event) {
              $scope.init();
              $scope.myScroll = null
          });

          $scope.init = function () {
              $scope.name = $translate.instant("WorkItemController.FinishedWorkitem");
              //   $scope.searchWeeklyStartTimeStart = datecalculation.redDays(new Date(), 30);
              //   $scope.searchWeeklyStartTimeEnd = datecalculation.addDays(new Date(), 30);
              var date = new Date();
              //这周第一天
              var weekFirstDay = new Date(date - (date.getDay() - 1) * 86400000);
              var firstMonth = Number(weekFirstDay.getMonth()) + 1;
              var weekFirstDays = weekFirstDay.getDate();
              if (firstMonth <= 9) {
                firstMonth="0"+firstMonth

              }
              if (weekFirstDays  <= 9) {
                weekFirstDays="0"+weekFirstDays 
              }
              var WeekFirstDate = weekFirstDay.getFullYear() + '-' + firstMonth + '-' + weekFirstDays;
              $rootScope.searchWeeklyStartTimeStart = WeekFirstDate;
              //这周最后一天
              var weekLastDay = new Date((weekFirstDay / 1000 + 6 * 86400) * 1000);
              var lastMonth = Number(weekLastDay.getMonth()) + 1;
              var weekLastDays = weekLastDay.getDate();
              if (lastMonth <= 9) {
                  lastMonth = "0" + lastMonth
              }
              if (weekLastDays <= 9) {
                  weekLastDays = "0" + weekLastDays
              }
              if (firstMonth > lastMonth) {
                  var WeekLastDate = weekFirstDay.getFullYear() + 1 + '-' + lastMonth + '-' + weekLastDays;
              } else {
                  var WeekLastDate = weekFirstDay.getFullYear() + '-' + lastMonth + '-' + weekLastDays;
              }
              $rootScope.searchWeeklyStartTimeEnd = WeekLastDate;
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
                  "mData": "title",//返回数据的键
                  "mRender": function (data, type, full) {
                      data = $scope.htmlEncode(data);
                      if (data == 'null' || data == null) {
                          data = '';
                      }
                    //   full = JSON.stringify(full);
                    return `<a class="like"target="_blank"href="WorkItemSheets.html?WorkItemID=${full.workItemId}"style='cursor: pointer;' title='${data}'>${data}</a>`;
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
                        //   var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
                        //   var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                        //   var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
                        //   data = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
                          data = year + '-' + (month) + '-' + (date) 

                      }
                      return  "<span title=\"" + data + "\">" + data + "<span>";
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
                        //   var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
                        //   var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                        //   var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
                        //   data = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
                          data = year + '-' + (month) + '-' + (date) 

                      }


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
              "sAjaxSource": "/Portal/queryWorkflowList/listOrgWeeklyReportByPage",
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
                  //将时间转化为时间戳
                  aoData.push(//name的值是传输数据的key，value的值是传输数据的value
                    { "name": "orgId", "value":$('#WeeklySelect').val() },
                    { "name": "startTime", "value": $filter("date")( $rootScope.searchWeeklyStartTimeStart,"yyyy-MM-dd 00:00:00") },
                    { "name": "endTime", "value":$filter("date")( $scope.searchWeeklyStartTimeEnd,"yyyy-MM-dd 00:00:00")},
                  );
               
              },
              "aoColumns": $scope.getMyColumns(), // 字段定义
              // 初始化完成事件,这里需要用到 JQuery ，因为当前表格是 JQuery 的插件
              "initComplete": function (settings, json) {
                  var filter = $("#searchWeeklyReportOrgName");
                  filter.unbind("click.DT").bind("click.DT", function () {
                      var myWeeklyReportselect = document.getElementById('WeeklySelect');
                      $scope.index = myWeeklyReportselect.selectedIndex;
                      if ($scope.index === 0) {
                          $scope.index = '';
                      }
                      if ($rootScope.searchWeeklyStartTimeStart == undefined && $rootScope.searchWeeklyStartTimeEnd == undefined) {
                          $rootScope.searchWeeklyStartTimeStart = "";
                          $rootScope.searchWeeklyStartTimeEnd = "";
                      }

                      else if($("#searchWeeklyStartTimeStart").val()==""&&$("#searchWeeklyStartTimeEnd").val()=="")
                      {
                          $rootScope.searchWeeklyStartTimeStart="";
                          $rootScope.searchWeeklyStartTimeEnd="";
                      }
                      else if($("#searchWeeklyStartTimeStart").val()==""&&$("#searchWeeklyStartTimeEnd").val()!=="")
                      {
                          $rootScope.searchWeeklyStartTimeStart="";
                          
                      }
                      else if($("#searchWeeklyStartTimeStart").val()!==""&&$("#searchWeeklyStartTimeEnd").val()=="")
                      {
                          $rootScope.searchWeeklyStartTimeEnd="";
                      }
                      $("#tabWeeklyReportFlow").dataTable().fnDraw();
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
          $('#weeklyReportName').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#searchWeeklyReportOrgName").click();
              }
             })
          $('#searchWeeklyStartTimeStart').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#searchWeeklyReportOrgName").click();
              }
          })
          $('#searchWeeklyStartTimeEnd').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#searchWeeklyReportOrgName").click();
              }
          })
          $('#WeeklySelect').bind('keypress', function (event) { 
            if (event.keyCode == "13") { 
             $("#searchWeeklyReportOrgName").click();
            }
        })
         
          
          
         
       

      }]);
})(window, angular, jQuery);