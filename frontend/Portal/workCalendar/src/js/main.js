app.controller('workCalendarCtrl', ['$scope', '$rootScope', '$http', '$compile', '$timeout', 'uiCalendarConfig', function ($scope, $rootScope, $http, $compile, $timeout, uiCalendarConfig) {

	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();


	$scope.years = ['2010', '2011', '2012', '2013', '2014', '2015', '2016', '2017', '2018', '2019', '2020', '2021', '2022', '2023', '2024', '2025', '2026', '2027', '2028', '2029', '2030'];
	$scope.months = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'];

	$scope.goToByDate = function () {
		var year = $scope.selectYear;
		var month = $scope.selectMonth;
		var date = year + ',' + month;
		var goToDate = $.fullCalendar.moment(date);
		uiCalendarConfig.calendars.myCalendar.fullCalendar('gotoDate', goToDate);
	}

	/* config object */
	$scope.uiConfig = {
    calendar: {
      height: "auto",
      // 是否可编辑，即进行可拖动和缩放操作,默认false
      editable: false,
      // 设置一周中显示的第一天是哪天，周日是0
      firstDay: 1,
      // 设置周数不固定
      weekMode: "liquid",
      //在agenda视图模式下，是否在日历上方显示all-day(全天)
      allDaySlot: false,
      // 强调日历中的某些时间段，
      businessHours: {
        dow: [1, 2, 3, 4, 5], // 周一 - 周四

        start: "9:00", // 上午9点开始
        end: "17:00", // 下午17点结束
      },
      // 自定义按钮
      customButtons: {
        buttonCreate: {
          text: "新建",
          click: function () {
            $(".datepicker").datepicker({
              language: "zh-CN",
              format: "yyyy-mm-dd",
              todayHighlight: true,
              autoclose: true,
              weekStart: 0,
            });
            $(".timepicki").wickedpicker({
              title: "",
              showSeconds: true,
              twentyFour: true,
            });
            $("#isallday").click(function () {
              if ($("#isallday").prop("checked") == true) {
                $("#isallday").val("1");
                $("#starttime,#endtime").hide();
              } else {
                $("#isallday").val("0");
                $("#starttime,#endtime").show();
              }
            });
            $("#end").click(function () {
              if ($("#end").prop("checked") == true) {
                $("#enddate").show();
              } else {
                $("#enddate").hide();
              }
            });
            $("#repeat").click(function () {
              if ($("#repeat").prop("checked") == true) {
                $("#repeattype,#repeattime").show();
              } else {
                $("#repeattype,#repeattime").hide();
              }
            });
            $("#repeatselect").change(function () {
              switch ($("#repeatselect").val()) {
                case "1":
                  $("#repeatclock").show();
                  $("#repeatmonth,#repeatweek,#repeatday").hide();
                  break;
                case "2":
                  $("#repeatmonth,#repeatday").hide();
                  $("#repeatweek,#repeatclock").show();
                  break;
                case "3":
                  $("#repeatmonth,#repeatweek").hide();
                  $("#repeatday,#repeatclock").show();
                  break;
                case "4":
                  $("#repeatweek").hide();
                  $("#repeatmonth,#repeatday,#repeatclock").show();
                  break;
                case "5":
                  $("#repeatclock").show();
                  $("#repeatmonth,#repeatweek,#repeatday").hide();
                  break;
              }
            });
            dialog({
              title: "新建日程",
              content: $("#dialog-form"),
              okValue: "确定",
              ok: function () {
                var titledetail = $("#titledetail").val();
                var startdate = $("#startdate").val();
                var starttime = $("#starttime").val().split(" ").join("");
                var enddate = $("#stopdate").val();
                var endtime = $("#endtime").val().split(" ").join("");
                var allDay = $("#isallday").val();
                if (titledetail) {
                  $.ajax({
                    url: "http://localhost/fullcalendar/detail.php",
                    data: {
                      title: titledetail,
                      sdate: startdate,
                      stime: starttime,
                      edate: enddate,
                      etime: endtime,
                      allDay: allDay,
                    },
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                      $("#calendar").fullCalendar("renderEvent", data, true);
                    },
                    error: function () {
                      alert("Failed");
                    },
                  });
                }
              },
              cancelValue: "关闭",
              cancel: function () {
                //$("#ui-datepicker-div").remove();
              },
            }).showModal();
          },
        },
        buttonQuery: {
          text: "查询",
          click: function () {
            $(".datepicker").datepicker({
              language: "zh-CN",
              format: "yyyy-mm-dd",
              todayHighlight: true,
              autoclose: true,
              weekStart: 0,
            });
            dialog({
              title: "查询",
              content: $("#search"),
              okValue: "查询",
              ok: function () {
                $("#ui-datepicker-div").remove();
              },
              button: [
                {
                  value: "打印",
                },
              ],
              cancelValue: "返回",
              cancel: function () {
                $("#ui-datepicker-div").remove();
              },
            }).showModal();
          },
        },
      },
      header: {
        left: "prev,next today",
        center: "title",
        right: "buttonCreate buttonQuery month,agendaWeek,agendaDay",
      },
      buttonText: {
        today: "今天",
        month: "月",
        agendaWeek: "周",
        agendaDay: "天",
      },

      /********dayClick,eventClick 作为弹出框显示暂时不用*******/

      /* dayClick: function(date,allDay,jsEvent,view){
				// var selDate = $.fullCalendar.formatDate(date,"YYYY-MM-DD");
				var d = dialog({
					title:"新建事件",
					content:"<textarea rows=5 class='taxt' placeholder='内容' id='eventall'></textarea>",
					width:460,
					button:[{
						value:"完整编辑",
						callback:function(){
							$(".datepicker").datepicker({
								language:"zh-CN",
								format:"yyyy-mm-dd",
								todayHighlight:true,
								autoclose:true,
								weekStart:0
							});
							$(".timepicki").wickedpicker({
								title:'',
								showSeconds:true,
								twentyFour:true
							});
							$("#isallday").click(function(){
								if($("#isallday").prop("checked") == true){
									$("#isallday").val("1");
									$("#starttime,#endtime").hide();
								}else{
									$("#isallday").val("0");
									$("#starttime,#endtime").show();
								};	
							});
							$("#end").click(function(){
								if($("#end").prop("checked") == true){
									$("#enddate").show();
								}else{
									$("#enddate").hide();
								};
							});
							$("#repeat").click(function(){
								if($("#repeat").prop("checked") == true){
									$("#repeattype,#repeattime").show();
								}else{
									$("#repeattype,#repeattime").hide();
								};
							});
							$("#repeatselect").change(function(){
								switch($("#repeatselect").val()){
									case "1":
										$("#repeatclock").show();
										$("#repeatmonth,#repeatweek,#repeatday").hide();
										break;
									case "2":
										$("#repeatmonth,#repeatday").hide();
										$("#repeatweek,#repeatclock").show();
										break;
									case "3":
										$("#repeatmonth,#repeatweek").hide();
										$("#repeatday,#repeatclock").show();
										break;
									case "4":
										$("#repeatweek").hide();
										$("#repeatmonth,#repeatday,#repeatclock").show();
										break;
									case "5":
										$("#repeatclock").show();
										$("#repeatmonth,#repeatweek,#repeatday").hide();
										break;
								}
							});
							dialog({
								title:"新建日程",
								content:$("#dialog-form"),
								okValue:"确定",
								ok:function(){
									var titledetail = $("#titledetail").val();
									var startdate = $("#startdate").val();
									var starttime = $("#starttime").val().split(" ").join("");
									var enddate = $("#stopdate").val();
									var endtime = $("#endtime").val().split(" ").join("");
									var allDay = $("#isallday").val();
									if(titledetail){
										$.ajax({
											url:'http://localhost/fullcalendar/detail.php',
					   						data:{title:titledetail,sdate:startdate,stime:starttime,edate:enddate,etime:endtime,allDay:allDay},
					   						type:'POST',
					   						dataType:'json',
					  						success:function(data){
					  							$("#calendar").fullCalendar("renderEvent",data,true);
					  						},
					  						error:function(){
					  							alert("Failed");
					  						}
					   						
										});
									};
								},
								cancelValue:"关闭",
								cancel:function(){}
							}).showModal();
						},	
					}],
					okValue:"确定",
					ok:function(){
						var titleall = $("#eventall").val();
						if(titleall){
							$.ajax({
								url:'http://localhost/fullcalendar/events.php',
		   						data:{title:titleall, start:selDate},
		   						type:'POST',
		   						dataType:'json',
		  						success:function(data){
		  							$("#calendar").fullCalendar("renderEvent",data,true);
		  						},
		  						error:function(){
		  							alert("Failed");
		  						}
		   						
							});
						};
					},
					cancelValue:"取消",
					cancel:function(){}
				});
				d.showModal();
			}, */
      /* eventClick: function (event, jsEvent, view) {
        	// var editstarttime = $.fullCalendar.formatDate(event.start,"YYYY-MM-DD HH:mm:ss");
				$("#edittitle").html(event.title);
				var eventtitle = event.title;
				// if(event.end){
				// 	var editendtime = $.fullCalendar.formatDate(event.end,"YYYY-MM-DD HH:mm:ss");
				// 	$("#edittime").html(editstarttime+"  至  "+editendtime);
				// }else{
				// 	$("#edittime").html(editstarttime);
				// }; 
				// var time = '19:00:00';
				dialog({
					title:"编辑日程",
					content:$("#edit"),
					okValue:"编辑",
					ok:function(){
						$(".datepicker").datepicker({
							language:"zh-CN",
							format:"yyyy-mm-dd",
							todayHighlight:true,
							autoclose:true,
							weekStart:0
						});
						$(".timepicki").wickedpicker({
							// now: time,
							title:'',
							showSeconds:true,
							twentyFour:true
						});
						$("#isallday").click(function(){
							if($("#isallday").prop("checked") == true){
								$("#isallday").val("1");
								$("#starttime,#endtime").hide();
							}else{
								$("#isallday").val("0");
								$("#starttime,#endtime").show();
							};	
						});
						$("#end").click(function(){
							if($("#end").prop("checked") == true){
								$("#enddate").show();
							}else{
								$("#enddate").hide();
							};
						});
						$("#repeat").click(function(){
							if($("#repeat").prop("checked") == true){
								$("#repeattype,#repeattime").show();
							}else{
								$("#repeattype,#repeattime").hide();
							};
						});
						$("#repeatselect").change(function(){
							switch($("#repeatselect").val()){
								case "1":
									$("#repeatclock").show();
									$("#repeatmonth,#repeatweek,#repeatday").hide();
									break;
								case "2":
									$("#repeatmonth,#repeatday").hide();
									$("#repeatweek,#repeatclock").show();
									break;
								case "3":
									$("#repeatmonth,#repeatweek").hide();
									$("#repeatday,#repeatclock").show();
									break;
								case "4":
									$("#repeatweek").hide();
									$("#repeatmonth,#repeatday,#repeatclock").show();
									break;
								case "5":
									$("#repeatclock").show();
									$("#repeatmonth,#repeatweek,#repeatday").hide();
									break;
							}
						});
						dialog({
							title:"新建日程",
							content:$("#dialog-form"),
							okValue:"确定",
							ok:function(){
								var titledetail = $("#titledetail").val();
								var startdate = $("#startdate").val();
								var starttime = $("#starttime").val().split(" ").join("");
								var enddate = $("#stopdate").val();
								var endtime = $("#endtime").val().split(" ").join("");
								var allDay = $("#isallday").val();
								if(titledetail){
									$.ajax({
										url:'http://localhost/fullcalendar/detail.php',
				   						data:{title:titledetail,sdate:startdate,stime:starttime,edate:enddate,etime:endtime,allDay:allDay},
				   						type:'POST',
				   						dataType:'json',
				  						success:function(data){
				  							$("#calendar").fullCalendar("renderEvent",data,true);
				  						},
				  						error:function(){
				  							alert("Failed");
				  						}
				   						
									});
								};
							},
							cancelValue:"关闭",
							cancel:function(){
								//$("#ui-datepicker-div").remove();
							}
						}).showModal();
						$("#calendar").fullCalendar("removeEvents",function(event){
									if(event.title==eventtitle){
										return true;
									}else{
										return false;
									}
						});
					},
					button:[{
						value:"删除",
						callback:function(){
							$("#calendar").fullCalendar("removeEvents",function(event){
									if(event.title==eventtitle){
										return true;
									}else{
										return false;
									}
							});
						}
					}],
					cancelValue:"取消",
					cancel:function(){}
				}).showModal();
			} */
      /*******************************************************/
    },
  };

	/* event source that contains custom events on the scope */
	$scope.events = [
		{ title: 'All Day Event', start: new Date(y, m, 1) },
		{ title: 'Long Event', start: new Date(y, m, d - 5), end: new Date(y, m, d - 2), color: 'rgba(109,109,192,0.2)' },
		{ id: 999, title: 'Repeating Event', start: new Date(y, m, d - 3, 16, 0), allDay: false },
		{ id: 999, title: 'Repeating Event', start: new Date(y, m, d + 4, 16, 0), allDay: false },
		{ title: 'Birthday Party', start: new Date(y, m, d + 1, 19, 0), end: new Date(y, m, d + 1, 22, 30), allDay: false },
		{ title: 'Click for Google', start: new Date(y, m, 28), end: new Date(y, m, 29), url: 'http://google.com/' },
		{ title: 'zql', start: new Date(y, m, 3) }
	];

	/* event sources array*/
	$scope.eventSources = [$scope.events];
}]);