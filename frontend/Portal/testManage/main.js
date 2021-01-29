(function (window, angular, $) {
  'use strict';
  app.controller('testManageCtrl', ['$scope', "$rootScope", "$translate", "$compile", "$http", "$timeout", "$state", "$interval", "$filter", "ControllerConfig", "datecalculation", "jq.datables", '$modal', 'item', 'fileNavigator',
    function ($scope, $rootScope, $translate, $compile, $http, $timeout, $state, $interval, $filter, ControllerConfig, datecalculation, jqdatables, $modal, Item, FileNavigator) {
      //$rootScope.flowScope = $scope;
      //实例化参数
      $scope.$on('$viewContentLoaded', function (event) {
        $scope.init();
        $scope.myScroll = null;
       
       
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
      $scope.getMyColumns = function () {
        var columns = [

        ];
        columns.push({
          "mData": "systemName",//返回数据的键
          "mRender": function (data, type, full) {
            data = $scope.htmlEncode(data);

            if (data == 'null' || data == null) {
              data = '';
            }
            full = JSON.stringify(full);
            return `<a style='cursor: pointer;' ng-click='toDetailTestEnv(${full})'title='${data}'>${data}</a>`;

          }
        });
        columns.push({
          "mData": "name",
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
          "mData": "envType",
          "mRender": function (data, type, full) {
            data = $scope.htmlEncode(data);
            if (data == 'null' || data == null) {
              data = '';
            }
           
            full = JSON.stringify(full);
            return "<span title=\"" + data + "\">" + data + "</span>";
          }
        });
        //关联环境
        columns.push({
          "mData": "envs",
          "mRender": function (data, type, full) {
            // data = $scope.htmlEncode(data);
            if (data == 'null' || data == null) {
              data = '';
            }
            var envTitle = "";
            for (var i = 0; i < data.length; i++) {
              envTitle += data[i].text;
              if (i != data.length - 1) {
                envTitle += ",";
              }
            }
            full = JSON.stringify(full);
            var resultHtml = '';
            for (var i = 0, len = data.length; i < len; i++) {
              var arr=[];
              arr.push(data[i].text);
              var envData='';
              envData=JSON.stringify(data[i]);
              if(data[i].text=="无"){
                resultHtml+= `<li style='list-style:none;readonly'readonlytitle='${envTitle}'>${data[i].text}</li>`;

              }else{
                resultHtml+= `<li style='list-style:none;'><a ng-click='toDetailEnvs(${envData})' title='${envTitle}'>${data[i].text}</a></li>`;

              }
              // resultHtml += '<a id='+data[i].id+' style="list-style:none"title='+envTitle+'\>' + data[i].text + '</a>';

            }
           
            return resultHtml;
            
          
          }
        });
        columns.push({
          "mRender": function (data, type, full) {
            full = JSON.stringify(full);
            return `
                            <button class='btn btn-sm btn-default' ng-click='updateTestEnv(${full})' title='编辑'>
                                <i class='glyphicon glyphicon-edit'></i>
                            </button>
                       
                            <button class='btn btn-sm btn-danger' ng-click='toDeleteTestEnv(${full})' title='删除'>
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
      $scope.testEnvOptions = {
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
        "sAjaxSource": "/Portal/testManage/listTestEnvByPage",
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
            },
            "error":function(){
            
            }
          });
        },
        "sAjaxDataProp": 'datas',
        "sDom": '<"top"f>rt<"row"ipl>',
        "sPaginationType": "full_numbers",
        "fnServerParams": function (aoData) {
          aoData.push(//name的值是传输数据的key，value的值是传输数据的value
            { "name": "keyword", "value": $scope.testEnvKeyword },
           
          );

        },
        "aoColumns": $scope.getMyColumns(), // 字段定义
        "initComplete": function (settings, json) {
          var filter = $("#testEnvSearch");
          filter.unbind("click.DT").bind("click.DT", function () {
      
        
          
         
         
              $("#tabTestEnv").dataTable().fnDraw();
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

      $('#testEnvKeyword').bind('keypress', function (event) {
        if (event.keyCode == "13") {
          $("#testEnvSearch").click();
        }
      })
      //详情
      $scope.toDetailTestEnv=function(data){
           for (var key in data) {
            if (data[key] == null) {
              data[key] = "";
            }
          }
          $rootScope.detailTestEnvModalName=data.name;
          $rootScope.detailTestEnvSystemName=data.systemName
          $rootScope.detailTestEnvEnvironmentalCategory=data.envType
          $rootScope.detailTestEnvList=data.vmDatas
          $rootScope.detailTestEnvDescription = data.desc
          $rootScope.detailTestEnvDockingEnvironmentList = data.envs
          $rootScope.detailTestEnvAddress=data.joinAddress;
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
              templateUrl: 'detailTestEnv.html',    // 指向上面创建的视图
              controller: 'testManageController',// 初始化模态范围
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
  
        
      }
    //新建
      $scope.createTestEnv = function (data) {
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
            templateUrl: 'createTestEnv.html',    // 指向上面创建的视图
            controller: 'testManageController',// 初始化模态范围
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

        


      }
      //编辑
      $scope.updateTestEnv = function (data) {

        for (var key in data) {

          if (data[key] == null) {
            data[key] = "";
          }
        }
        $rootScope.editTestEnvModalName=data.name;
        $rootScope.editTestEnvSystemName=data.systemName
        $rootScope.editTestEnvEnvironmentalCategory=data.envType
        $rootScope.editTestEnvList=data.vmDatas
        $rootScope.editTestEnvDescription=data.desc
        $rootScope.editTestEnvDockingEnvironmentList=data.envs
        $rootScope.editTestEnvAddress=data.joinAddress
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
            templateUrl: 'editTestEnv.html',    // 指向上面创建的视图
            controller: 'testManageController',// 初始化模态范围
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



      /*************删除模态框*****************/
      $scope.toDeleteTestEnv = function (data) {
        $rootScope.deleteSystemName=data.systemName
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
            templateUrl: 'deleteTestEnv.html',    // 指向上面创建的视图
            controller: 'testManageController',// 初始化模态范围
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
      //关联环境点击详情模态框
      $scope.toDetailEnvs=function(data){
        if(data.text=="无"){

        }else{
          $.ajax({
            url:"/Portal/testManage/getTestEnvById?id="+data.id,
            method:"GET",
            success:function(data){
                data=data.data
                $rootScope.detailEnvTestManageModalName=data.name;
                $rootScope.detailEnvTestManageSystemName=data.systemName
                $rootScope.detailEnvTestManageEnvironmentalCategory=data.envType
                $rootScope.detailEnvTestManageList=data.vmDatas
                $rootScope.detailEnvTestManageDescription=data.desc
                $rootScope.detailEnvTestManageDockingEnvironmentList=data.envs
                $rootScope.detailEnvTestManageDescription=data.joinAddress;
                var AgencyID =data;
                $http({
                  url: ControllerConfig.Agents.GetAgency,
                  params: {
                    agentID: AgencyID,
                    random: new Date().getTime()
                  }
                }).success(function (result, header, config, status) {
                  var Agency = result.Rows[0];
                
                  var modalInstance = $modal.open({
                    templateUrl: 'detailTestEnvTestManage.html',    // 指向上面创建的视图
                    controller: 'testManageController',// 初始化模态范围
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
                  }
        
              })
                
        
  
        
        }
  
      }
    }]);
    
})(window, angular, jQuery);