(function (window, angular, $) {
  'use strict';
  app.controller('businessExceptionCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", '$modal', 'item', 'fileNavigator',
      function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $modal, Item, FileNavigator) {
          //$rootScope.flowScope = $scope;
          //实例化参数
          $scope.searchExceptionWasAgentOptions = {
              Editable: true,
              Visiable: true,
              //IsMultiple: true,
              UserVisible: true,//是否显示用户
             
          }
          $.ajax({
            "url": "/Portal/system/findDictionaryData",
            "type": "post",
            "data": { categoryCode: "交易系统"},
            "dataType": "json",
            "success": function (data) {
                $scope.businessExceptionOptionData = data.data;
                //使用refresh方法更新UI以匹配新状态
                $('#usertype').selectpicker('refresh');
                //render方法强制重新渲染引导程序 - 选择ui。
                $('#usertype').selectpicker('render');
                var optionMulti = [];
                optionMulti = $scope.businessExceptionOptionData;
                //定义一个对象数组，用于储存所需选项
                for (var i = 0; i < optionMulti.length; i++) {
                    $("#usertype").append($("<option value=\"" + optionMulti[i].code + "\">" + optionMulti[i].code + "</option>"));
                }
                
              },
            "error": function () {
            }
          })
          $('#usertype').on('changed.bs.select',
              function () {
                  if ($('#usertype').val() != null) {
                      $scope.businessExceptionOptionData.forEach(el => {
                          if ($('#usertype').val().indexOf(el.code) != "-1") {
                              $('#businessExceptionAll').removeAttr("selected");
                              $('#usertype').selectpicker('refresh');
                              $('#usertype').selectpicker('render');
                          }
                      })
                  }

          });
          //共享知识的日期控件初始化
          $scope.searchExceptionStartTimeStart = {
              dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
              onpicked: function (e) {
                  $rootScope.searchExceptionStartTimeStart = e.el.value;
              }
          }
          $scope.searchExceptionStartTimeEnd = {
              dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
              onpicked: function (e) {
                  $rootScope.searchExceptionStartTimeEnd = e.el.value;
              
              }
          }
          $scope.searchExceptionEndTimeStart = {
              dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
              onpicked: function (e) {
                  $rootScope.searchExceptionEndTimeStart = e.el.value;
              }
          }
          $scope.searchExceptionEndTimeEnd = {
              dateFmt: 'yyyy-MM-dd HH:mm:ss', realDateFmt: "yyyy-MM-dd HH:mm:ss", minDate: '2012-1-1', maxDate: '2099-12-31',
              onpicked: function (e) {
                  $rootScope.searchExceptionEndTimeEnd = e.el.value;
              }
          }
          $scope.$on('$viewContentLoaded', function (event) {
              $scope.init();
              $scope.myScroll = null
          });

          $scope.init = function () {
              $scope.name = $translate.instant("WorkItemController.FinishedWorkitem");
              $scope.searchExceptionStartTimeStart = datecalculation.redDays(new Date(), 30);
              $scope.searchExceptionStartTimeEnd = datecalculation.addDays(new Date(), 30);
              $scope.searchExceptionEndTimeStart = datecalculation.redDays(new Date(), 30);
              $scope.searchExceptionEndTimeEnd = datecalculation.addDays(new Date(), 30);
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
                      return `<a class="like"target="_blank"href="InstanceSheets.html?InstanceId=${full.instanceId}"style='cursor: pointer;' title='${data}'>${data}</a>`;
                  }
              });
              columns.push({
                  "mData": "businessSystem",
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
                  "mData": "createUserName",
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
                          var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
                          var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                          var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
                          data = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
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
              "sAjaxSource": " /Portal/queryWorkflowList/listBusinessExceptionByPage",
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
                      { "name": "startTimeStart", "value": $filter("date")( $rootScope.searchExceptionStartTimeStart, "yyyy-MM-dd HH:mm:ss") },
                      { "name": "startTimeEnd", "value": $filter("date")( $rootScope.searchExceptionStartTimeEnd ,"yyyy-MM-dd HH:mm:ss") },
                      { "name": "keyword", "value":$scope.businessExceptionKeyword },
                      { "name": "userId", "value": $scope.Originator },
                      { "name": "businessSystem","value":$("#usertype").val()},
                      { "name": "endTimeStart", "value": $filter("date")( $rootScope.searchExceptionEndTimeStart, "yyyy-MM-dd HH:mm:ss") },
                      { "name": "endTimeEnd", "value": $filter("date")( $rootScope.searchExceptionEndTimeEnd ,"yyyy-MM-dd HH:mm:ss") },
                  );               
              },
              "aoColumns": $scope.getMyColumns(), // 字段定义
              // 初始化完成事件,这里需要用到 JQuery ，因为当前表格是 JQuery 的插件
              "initComplete": function (settings, json) {
                  var filter = $("#searchBusinessExceptionOrgName");
                  filter.unbind("click.DT").bind("click.DT", function () {
                      
                      $scope.Originator = $("#searchExceptionCompany").SheetUIManager().GetValue();
                      if ( $rootScope.searchExceptionStartTimeStart == undefined && $rootScope.searchExceptionStartTimeEnd == undefined) {
                          $rootScope.searchExceptionStartTimeStart="";
                          $rootScope.searchExceptionStartTimeEnd="";
                      }
                      if ($rootScope.searchExceptionEndTimeStart == undefined && $rootScope.searchExceptionEndTimeEnd == undefined) {
                          $rootScope.searchExceptionEndTimeStart = "";
                          $rootScope.searchExceptionEndTimeEnd = "";
                      }
                      if($("#searchExceptionStartTimeStart").val()==""&&$("#searchExceptionStartTimeEnd").val()=="")
                      {
                          $rootScope.searchExceptionStartTimeStart="";
                          $rootScope.searchExceptionStartTimeEnd="";
                      }
                      if($("#searchExceptionStartTimeStart").val()==""&&$("#searchExceptionStartTimeEnd").val()!=="")
                      {
                          $rootScope.searchExceptionStartTimeStart="";
                          
                      }
                      if($("#searchExceptionStartTimeStart").val()!==""&&$("#searchExceptionStartTimeEnd").val()=="")
                      {
                          $rootScope.searchExceptionStartTimeEnd="";
                      }
                      if($("#searchExceptionEndTimeStart").val()==""&&$("#searchExceptionEndTimeEnd").val()=="")
                      {
                          $rootScope.searchExceptionEndTimeStart="";
                          $rootScope.searchExceptionEndTimeEnd="";
                      }
                      if($("#searchExceptionEndTimeStart").val()==""&&$("#searchExceptionEndTimeEnd").val()!=="")
                      {
                          $rootScope.searchExceptionEndTimeStart="";
                          
                      }
                      if($("#searchExceptionEndTimeStart").val()!==""&&$("#searchExceptionEndTimeEnd").val()=="")
                      {
                          $rootScope.searchExceptionEndTimeEnd="";
                      }
                      else {
                          
                          var myStartTimes = new Date( $rootScope.searchExceptionStartTimeStart.replace(/-/g, "/")).getTime();
                          var myEndTimes = new Date($rootScope.searchExceptionStartTimeEnd.replace(/-/g, "/")).getTime();
                          var myEndStartTimes=new Date($rootScope.searchExceptionEndTimeStart.replace(/-/g, "/")).getTime();
                          var myEndEndTimes=new Date($rootScope.searchExceptionEndTimeEnd.replace(/-/g, "/")).getTime();
                          if (myStartTimes > myEndTimes) {
                              $.notify({ message: "时间区间错误", status: "danger" });
                              $("#MyStartTimeEnd").css("color", "red");
                              return false;
                          }
                          if(myEndStartTimes>myEndEndTimes){
                            $.notify({ message: "时间区间错误", status: "danger" });
                            $("#MyEndTimeEnd").css("color", "red");
                            return false;
                          }
                          
                          
                      }
                      //截止时间
                     
                      
                       
                     
                      
                      $("#tabBusinessxceptionFlow").dataTable().fnDraw();
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
          $('#businessExceptionName').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#searchBusinessExceptionOrgName").click();
              }
             })
         
          $('#searchExceptionCompany').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#searchBusinessExceptionOrgName").click();
              }
          })
          $('#searchExceptionStartTimeStart').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#searchBusinessExceptionOrgName").click();
              }
          })
          $('#searchExceptionStartTimeEnd').bind('keypress', function (event) { 
              if (event.keyCode == "13") { 
               $("#searchBusinessExceptionOrgName").click();
              }
          })
          $('#searchExceptionEndTimeStart').bind('keypress', function (event) {
              if (event.keyCode == "13") {
                  $("#searchBusinessExceptionOrgName").click();
              }
          })
          $('#searchExceptionEndTimeEnd').bind('keypress', function (event) {
              if (event.keyCode == "13") {
                  $("#searchBusinessExceptionOrgName").click();
              }
          })
          
          
         
       

      }]);
})(window, angular, jQuery);