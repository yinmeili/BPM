(function (window, angular, $) {
  'use strict';
  app.controller('orgWeeklyReportCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", '$modal', 'item', 'fileNavigator',
    function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $modal, Item, FileNavigator) {
      $scope.$on('$viewContentLoaded', function (event) {
        //此方法内所有方法页面只加载一次原因未知
        $rootScope.loading = true;
        $scope.listChildrenOrg();
          $scope.init();
      });

      $scope.level = [
        { "id": "1", "text": "一级及以上" },
        { "id": "2", "text": "二级及以上" },
        { "id": "3", "text": "三级及以上" },

      ]
      $scope.downloadTable = function () {
        var str1 = document.getElementById("org_weekly_report_job").outerHTML;
        var str2 = document.getElementById("org_weekly_report_pj").outerHTML;
        var str3 = document.getElementById("org_weekly_job_plan").outerHTML;
        var str4 = document.getElementById("org_weekly_pj_plan").outerHTML;
        var str5 = $('#orgWeeklyReportCompany option:selected').text();
        var str6 = $('#orgWeeklyReportLevel option:selected').text();
        var str8 = $scope.orgWeeklyReportStartTimeEnd;
        var str7 = $scope.orgWeeklyReportStartTime;
        var uri = 'data:application/vnd.ms-excel;base64,';
        const worksheet = 'sheet1';

        const template = `<html xmlns:o="urn:schemas-microsoft-com:office:office" 
          xmlns:x="urn:schemas-microsoft-com:office:excel" 
          xmlns="http://www.w3.org/TR/REC-html40">
          <head><meta charset="utf-8">
          <!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>
          <x:Name>${worksheet}</x:Name>
          <x:WorksheetOptions><x:DisplayGridlines><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>
          </x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]-->
         <style type="text/css">
         table{
           border:solid 1px #000; 
           }   
           .header{
          height:40px; 
          color:black;
          background-color: #A6A6A6;
          text-align:center;
         }
         #titlecol td{
          height:40px;
         }
          #titlecol{ 
          color:black;
          background-color: #538DD5 ;
          border:solid 1px #000; 
          }
          #title td{
            height:70px;
            text-align:center;
            font-size:20px
          }
          #title{ 
          color:black;
          background-color: #E26B0A ;
          }
          #newtitle td{
            height:40px;
            text-align:center;
          }
          </style>
          </head> <body>
          <div><table id="title"><tr><td></td><td></td><td></td><td><b>部门工作周报</b></td><td></td><td></td><td></td><td></td></tr></table></div>
          <div><table id="newtitle"><tr><td><b style="color:red">部门:</b>${str5}</td>
          <td><b style="color:red;">等级:</b>${str6}</td>
          <td><b style="color:red">开始时间:</b>${str7}</td>
          <td><b style="color:red">结束时间:</b>${str8}</td></tr></table></div>
          <table id="titlecol"><tr><td>常规工作</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></table><div>${str1}</div><br>
          <table id="titlecol"><tr><td>项目工作</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></table><div>${str2}</div><br>
          <table id="titlecol"><tr><td>下周常规工作计划</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></table><div>${str3}</div><br>
          <table id="titlecol"><tr><td>下周项目工作计划</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></table><div>${str4} </div><br></body>
          </html>`;

        const base64 = s => window.btoa(unescape(encodeURIComponent(s)));
        // window.location.href = uri + base64(template);
        // window.location.download = "部门工作周报.xls";
        document.getElementById("exportExcel").href = uri + base64(template);
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
        var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        var timestamp = year + month + day + hour + minute + second ;
        document.getElementById("exportExcel").download = "部门工作周报"+timestamp + ".xls";//自定义文件名
        document.getElementById("exportExcel").click();


      };





      $scope.orgWeeklyReportStartTimeStart = {
        dateFmt: 'yyyy-MM-dd ', realDateFmt: "yyyy-MM-dd ", minDate: '2012-1-1', maxDate: '2099-12-31', skin: 'whyGreen',
        firstDayOfWeek: 1, errDealMode: 3, isShowClear: "true",
        disabledDays: [1, 2, 3, 4, 5, 6],//只选周一 readOnly:true,
        onpicked: function (e) {
          $rootScope.orgWeeklyReportStartTimeStart = e.el.value;
          var date = new Date($rootScope.orgWeeklyReportStartTimeStart);
          var weekFirstDay = new Date(date - (date.getDay() - 1) * 86400000);
          var firstMonth = Number(weekFirstDay.getMonth()) + 1;
          var weekFirstDays = weekFirstDay.getDate();
          var WeekFirstDate = weekFirstDay.getFullYear() + '-' + firstMonth + '-' + weekFirstDays;
          //这周最后一天
          var weekLastDay = new Date((weekFirstDay / 1000 + 6 * 86400) * 1000);
          var lastMonth = Number(weekLastDay.getMonth()) + 1;
          var weekLastDays = weekLastDay.getDate();
          if (firstMonth > lastMonth) {
            var WeekLastDate = weekFirstDay.getFullYear() + 1 + '-' + lastMonth + '-' + weekLastDays;
          } else {
            var WeekLastDate = weekFirstDay.getFullYear() + '-' + lastMonth + '-' + weekLastDays;
          }


          $scope.orgWeeklyReportStartTimeEnd = WeekLastDate;
          $scope.$apply();
        }

      }

      $scope.init = function () {
        var date = new Date();
        //这周第一天
        var weekFirstDay = new Date(date - (date.getDay() - 1) * 86400000);
        var firstMonth = Number(weekFirstDay.getMonth()) + 1;
        var weekFirstDays = weekFirstDay.getDate();
        var WeekFirstDate = weekFirstDay.getFullYear() + '-' + firstMonth + '-' + weekFirstDays;
        //这周最后一天
        var weekLastDay = new Date((weekFirstDay / 1000 + 6 * 86400) * 1000);
        var lastMonth = Number(weekLastDay.getMonth()) + 1;
        var weekLastDays = weekLastDay.getDate();
        if (firstMonth > lastMonth) {
          var WeekLastDate = weekFirstDay.getFullYear() + 1 + '-' + lastMonth + '-' + weekLastDays;
        } else {
          var WeekLastDate = weekFirstDay.getFullYear() + '-' + lastMonth + '-' + weekLastDays;
        }
        $scope.orgWeeklyReportStartTime = WeekFirstDate;
        $scope.orgWeeklyReportStartTimeEnd = WeekLastDate;
      }
      $scope.searchOrgWeeklyReport = function () {
        $rootScope.loading = true;
        var searchorgId = $("#orgWeeklyReportCompany").val()
        if ($rootScope.orgWeeklyReportStartTimeStart != undefined) {
          var startTime = $rootScope.orgWeeklyReportStartTimeStart;
          var WeekLastDate = $scope.orgWeeklyReportStartTimeEnd
          var date = new Date($rootScope.orgWeeklyReportStartTimeStart);
          var weekFirstDay = new Date(date - (date.getDay() - 1) * 86400000);
          var firstMonth = Number(weekFirstDay.getMonth()) + 1;
          var weekFirstDays = weekFirstDay.getDate();
          var WeekFirstDate = weekFirstDay.getFullYear() + '-' + firstMonth + '-' + weekFirstDays;
          //这周最后一天
          var weekLastDay = new Date((weekFirstDay / 1000 + 6 * 86400) * 1000);
          var lastMonth = Number(weekLastDay.getMonth()) + 1;
          var weekLastDays = weekLastDay.getDate();
          if (firstMonth > lastMonth) {
            var WeekLastDate = weekFirstDay.getFullYear() + 1 + '-' + lastMonth + '-' + weekLastDays;
          } else {
            var WeekLastDate = weekFirstDay.getFullYear() + '-' + lastMonth + '-' + weekLastDays;
          }

        }
        else {
          var date = new Date();
          var weekFirstDay = new Date(date - (date.getDay() - 1) * 86400000);
          var firstMonth = Number(weekFirstDay.getMonth()) + 1;
          var weekFirstDays = weekFirstDay.getDate();
          var WeekFirstDate = weekFirstDay.getFullYear() + '-' + firstMonth + '-' + weekFirstDays;

          var weekLastDay = new Date((weekFirstDay / 1000 + 6 * 86400) * 1000);
          var lastMonth = Number(weekLastDay.getMonth()) + 1;
          var weekLastDays = weekLastDay.getDate();
          if (firstMonth > lastMonth) {
            var WeekLastDate = weekFirstDay.getFullYear() + 1 + '-' + lastMonth + '-' + weekLastDays;
          } else {
            var WeekLastDate = weekFirstDay.getFullYear() + '-' + lastMonth + '-' + weekLastDays;
          }
          startTime = WeekFirstDate;
          WeekLastDate = WeekLastDate
        }
        var selectLEVEL = document.getElementById('orgWeeklyReportLevel');
        $scope.index = selectLEVEL.selectedIndex;

        if ($scope.index === 0) {
          $scope.orgWeeklyReportLevel = $scope.level[1].id
        }
        else {
          $scope.orgWeeklyReportLevel = $scope.level[$scope.index - 1].id;
        }
        var data = {
          orgId: searchorgId,
          startTime: startTime,
          endTime: WeekLastDate,
          jobLevel: $scope.orgWeeklyReportLevel,
        };
        var searchListOrgProjectData = {
          orgId: searchorgId,
          startTime: startTime,
          endTime: WeekLastDate,
        };

        var loadingfinishCount = 0
        $http.post('/Portal/weeklyReport/listOrgJob', data).success(function (data) {
          var result = data.data;
          $("#org_weekly_report_job tr:not(:first)").remove("");//删除除表头外的所有行
          if (result.length > 0) {
            for (var i = 0; i < result.length; i++) {
              var item = '<tr><td><center>' + parseInt(i + 1) + '</center></td><td style="white-space:pre-wrap">' + result[i].content + "</td><td style='white-space:pre-wrap'>" + result[i].evolve + "</td><td><center>" +
                result[i].ratio + "</center></td><td style='white-space:pre-wrap'>" + result[i].problem + "</td><td><center>" + result[i].type +
                "</center></td><td><center>" + result[i].jobLevelStr + "</center></td><td><center>" + result[i].userName + "</center></td></tr>";
              $('#org_weekly_report_job').append(item);//动态加行
            }
            $('#DashedBlank11').html("");

          }
          else {
            $('#DashedBlank11').html("暂无数据");
          }
          loadingfinishCount++;
          if (loadingfinishCount == 4) {
            $rootScope.loading = false;
          }
        }).error(function (data) {
          alert("请求失败")
        })

        $http.post('/Portal/weeklyReport/listOrgProject', searchListOrgProjectData).success(function (data) {
          var listOrgProjectResult = data.data;
          $("#org_weekly_report_pj tr:not(:first)").remove();//删除除表头外的所有行
          if (listOrgProjectResult.length > 0) {
            for (var i = 0; i < listOrgProjectResult.length; i++) {
              var item = '<tr><td ><center>' + parseInt(i + 1) + '</center></td><td style="white-space:pre-wrap">' + listOrgProjectResult[i].name + "</td><td style='white-space:pre-wrap'>" + listOrgProjectResult[i].info + "</td><td style='white-space:pre-wrap'>" +
                listOrgProjectResult[i].evolve + "</td><td><center>" + listOrgProjectResult[i].ratio + "</center></td><td><center>" + listOrgProjectResult[i].org +
                "</center></td><td style='white-space:pre-wrap'>" + listOrgProjectResult[i].remark + "</td><td><center>" + listOrgProjectResult[i].userName + "</center></td></tr>";
              $('#org_weekly_report_pj').append(item);//动态加行
            }
            $('#DashedBlank12').html("");
          }
          else {
            $('#DashedBlank12').html("暂无数据");
          }
          loadingfinishCount++;
          if (loadingfinishCount == 4) {
            $rootScope.loading = false;
          }
        }).error(function (data) {
          alert("请求失败")
        })
        $http.post('/Portal/weeklyReport/listOrgJobPlan', data).success(function (data) {
          var listOrgJobPlanResult = data.data;
          $("#org_weekly_job_plan tr:not(:first)").remove();//删除除表头外的所有行
          if (listOrgJobPlanResult.length > 0) {
            for (var i = 0; i < listOrgJobPlanResult.length; i++) {
              var item = '<tr><td><center>' + parseInt(i + 1) + '</center></td><td style="white-space:pre-wrap">' + listOrgJobPlanResult[i].content + "</td><td style='white-space:pre-wrap'>" + listOrgJobPlanResult[i].evolve + "</td><td><center>" +
                listOrgJobPlanResult[i].ratio + "</center></td><td style='white-space:pre-wrap'>" + listOrgJobPlanResult[i].problem + "</td><td><center>" + listOrgJobPlanResult[i].type +
                "</center></td><td><center>" + listOrgJobPlanResult[i].jobLevelStr + "</center></td><td><center>" + listOrgJobPlanResult[i].userName + "</center></td></tr>";
              $('#org_weekly_job_plan').append(item);//动态加行
            }
            $('#DashedBlank13').html("");
          }
          else {
            $('#DashedBlank13').html("暂无数据");
          }
          loadingfinishCount++;
          if (loadingfinishCount == 4) {
            $rootScope.loading = false;
          }
        }).error(function (data) {
          alert("请求失败")
        })
        $http.post('/Portal/weeklyReport/listOrgProjectPlan', searchListOrgProjectData).success(function (data) {
          var listOrgProjectResult = data.data;
          $("#org_weekly_pj_plan tr:not(:first)").remove();//删除除表头外的所有行
          if (listOrgProjectResult.length > 0) {
            for (var i = 0; i < listOrgProjectResult.length; i++) {
              var item = '<tr><td ><center>' + parseInt(i + 1) + '</center></td><td style="white-space:pre-wrap">' + listOrgProjectResult[i].name + "</td><td style='white-space:pre-wrap'>" + listOrgProjectResult[i].info + "</td><td style='white-space:pre-wrap'>" +
                listOrgProjectResult[i].evolve + "</td><td><center>" + listOrgProjectResult[i].ratio + "</center></td><td><center>" + listOrgProjectResult[i].org +
                "</center></td><td style='white-space:pre-wrap'>" + listOrgProjectResult[i].remark + "</td><td><center>" + listOrgProjectResult[i].userName + "</center></td></tr>";
              $('#org_weekly_pj_plan').append(item);//动态加行

            }

            $('#DashedBlank14').html("");
          }
          else {
            $('#DashedBlank14').html("暂无数据");

          }
          loadingfinishCount++;
          if (loadingfinishCount == 4) {
            $rootScope.loading = false;
          }
        }).error(function (data) {
          alert("请求失败")
        })
      }
   
       

     
        
      //部门下拉框请求
      $scope.listChildrenOrg = function () { 
      
        $('#orgWeeklyReportCompany').selectpicker({
          noneResultsText: '无搜索结果',
          noneSelectedText:'无组织部门'
      }); 
        $.ajax({
          dataType: 'json',
          type: 'GET',
          url: '/Portal/user/listChildrenOrg',
          success: function (data) { 
            $scope.companyOptionData = data;
              var optionMulti = [];
              optionMulti = $scope.companyOptionData;
              //定义一个对象数组，用于储存所需选项
              for (var i = 0; i < optionMulti.length; i++) {
                  $("#orgWeeklyReportCompany").append($("<option value=\"" + optionMulti[i].id +"\">" + optionMulti[i].text + "</option>"));
              }
              //使用refresh方法更新UI以匹配新状态
              $('#orgWeeklyReportCompany').selectpicker('refresh');
              //render方法强制重新渲染引导程序 - 选择ui。
              $('#orgWeeklyReportCompany').selectpicker('render');
          },
          error: function () {
            alert("Failed");
          },
        }).then(function(data) {
          var searchselect = $("#orgWeeklyReportCompany").val()
          $rootScope.loading = true;
          if(searchselect==null)
          {
            $('#DashedBlank11').html("暂无数据");
            $('#DashedBlank14').html("暂无数据");
            $('#DashedBlank13').html("暂无数据");
            $('#DashedBlank12').html("暂无数据");
            $rootScope.loading = false;
            $scope.$apply();
          }
          else{
             $scope.searchOrgWeeklyReport();
          }
      })
      $('#orgWeeklyReportCompany').selectpicker('refresh');
      //render方法强制重新渲染引导程序 - 选择ui。
      $('#orgWeeklyReportCompany').selectpicker('render');
      }
    }]);
})(window, angular, jQuery);