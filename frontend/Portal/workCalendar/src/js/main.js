app.controller('workCalendarCtrl', ['$scope', '$rootScope', '$http', '$compile', '$timeout', 'uiCalendarConfig' ,function ($scope, $rootScope, $http, $compile, $timeout, uiCalendarConfig) {

	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();

	$scope.userId = $rootScope.loginUser.User.ObjectID;
	$scope.url = "";
	// $('#newtag').attr('placeholder', $rootScope.loginUser.User.Name);
	$scope.years = ['2010', '2011', '2012', '2013', '2014', '2015', '2016', '2017', '2018', '2019', '2020', '2021', '2022', '2023', '2024', '2025', '2026', '2027', '2028', '2029', '2030'];
	$scope.months = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'];

	$scope.start = null;
	$scope.end = null;
	newyear = function () {

		var mm = date.getMonth() + 1;
		var yy = date.getFullYear();
		$scope.years.forEach((item, index, array) => {
			if (item == yy)
				$scope.selectYear = $scope.years[index];
		})
		$scope.months.forEach((item, index, array) => {
			if (item == mm)
				$scope.selectMonth = $scope.months[index];
		})
	},
		newyear();


	// $scope.renderTag = function (idName) {//input的元素的id名称
	// 	var obj = $("#" + idName);
	// 	var sltCategoryComBox;//拿到所有数据，然后再重新加载页面数据
	// 	var initId = "";
	// 	var initValue = "";
	// 	var tmpData = [];
	// 	//渲染数据的地方
	// 	sltCategoryComBox = obj.ligerComboBox({
	// 		initValue: initId,
	// 		initText: initValue,
	// 		data: tmpData,//返回的下拉框的数据
	// 		valueFieldID: 'category',
	// 		url: '/Portal/user/listSubordinate',//请求的后台地址
	// 		ajaxType: 'GET',
	// 		valueField: 'id',//返回的数据的键必须有id
	// 		textField: 'text',//返回的数据的键必须有test
	// 		autocomplete: true,
	// 		setTextBySource: true,
	// 		keySupport: true,
			
	// 	});

	// 	//设置想要的样式
	// 	obj.parent().removeClass();
	// 	var inputHeight = obj.outerHeight();
	// 	var inputWidth = obj.outerWidth();
	// 	obj.css({ "border": "1px solid #d9d9d9", "border-radius": "4px" });
	// 	//下拉框的内容样式设置,模态框样式有点
	// 	$("div.l-box-select-inner").parent().css({ "margin-top": inputHeight + 'px' });
	// 	$("div.l-box-select-inner").parent().css({ "margin-left": 5 + 'px' });
	// 	$("div.l-box-select").css({ "width": inputWidth - 2 + 'px' });
	// 	//删除下拉框的下拉图标
	// 	$("div.l-trigger-icon").hide();
	// 	$("div.l-trigger").hide();
	// 	//鼠标离开输入框，下拉框隐藏
	// 	obj.mouseleave(function (e) {
	// 		var x = e.pageX - obj.offset().left;
	// 		var y = e.pageY - obj.offset().top;
	// 		if (x < 0 || y < 0 || x - inputWidth > 0) {
	// 			$("div.l-box-select").hide();
	// 		}
	// 	});

	// 	//update by zhangj
	// 	obj.change(function () {//按回车键触发该函数
	// 		$scope.userId = $("#category").val();//获取的是数据中的，选中数据条的id


	// 	});
	// }

	// setTimeout(function () {
	// 	$scope.renderTag('newtag');
	// }, 500);


	//流程模板
	$scope.newWorkflowOptions = {//初始化流程模板
		Editable: true,
		Visiable: true,
		Mode: "WorkflowTemplate",//似乎不影响
		IsMultiple: true,
		OnChange: "",
		//PlaceHolder: $scope.LanJson.WorkFlow,//似乎不影响
		IsSearch: true //是否搜索目录，默认false
	}
	$scope.statusColorType = {
		0: 'rgb(164,0,0)',//red
		2: 'rgb(109,109,192,0.2)',//grey
		1: 'rgb(81,184,13)',
	}

	// 获取当前用户及年月日
	$scope.getday = function () {
		var myDate = new Date();
		var tYear = myDate.getFullYear();
		var my = new Date();
		var tMonth = my.getMonth() + 1;
		var ny = tYear + '0' + tMonth;//将获取的按钮数字结合为字符串
		if (ny != null && ny != '') {
			var nyYear = ny.slice(0, 4);
			var nyMonth = ny.slice(4, ny.length);
			var firstDay = new Date(nyYear, nyMonth - 1);
			var lastDay = new Date(new Date(nyYear, nyMonth).valueOf() - 60 * 60 * 1000 * 24);
			function datasFormat(d) {
				var datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate();
				return datetime;
			}
			$scope.y = datasFormat(firstDay);
			$scope.m = datasFormat(lastDay);
		}

	}
	$scope.getday();
	$scope.goToByDate = function () {
		var year = $scope.selectYear;
		var month = $scope.selectMonth;
		var date = year + ',' + month;
		var goToDate = $.fullCalendar.moment(date);
		uiCalendarConfig.calendars.myCalendar.fullCalendar('gotoDate', goToDate);
		// console.log(goToDate)
	},

		/* config object */
		$scope.uiConfig = {
			calendar: {
				height: "auto",
				editable: false,
				firstDay: 1,
				weekMode: "liquid",
				displayEventTime:false,

				//在agenda视图模式下，是否在日历上方显示all-day(全天)
				allDaySlot: false,
				// 强调日历中的某些时间段，
				businessHours: {
					dow: [1, 2, 3, 4, 5], // 周一 - 周四
					start: "9:00", // 上午9点开始
					end: "17:00", // 下午17点结束
				},
				header: {
					left: "prev,next today",
					center: "title",
					right: "",
				},
				buttonText: {
					today: "今天",
					month: "月",
					agendaWeek: "周",
					agendaDay: "天",
				},

				eventRender: function (event, element) {
					$(element).tooltip({ title: event.title,placement:"right"});	
				},
				eventClick: function (calEvent, jsEvent, view) {//日程区块，单击时触发
					window.open($scope.url = 'WorkItemSheets.html?WorkItemID=' + calEvent.id);
				},
				events: function (start, end, timezone, callback) {
					$scope.initStartAndEndDate();

					$rootScope.loading=true;
			    var userID = $rootScope.loginUser.User.ObjectID;
					var workflowCode = "";
					if (typeof ($("#sheet").SheetUIManager()) != 'undefined' && $("#sheet").SheetUIManager().GetValue() != "") {
						workflowCode = ($("#sheet").SheetUIManager().GetValue()).join();
					}
					var start = $scope.start;//获取上面分析的starttime
					var end = $scope.end;//获取上面分析的endtime
					$.ajax({
						dataType: 'json',
						type: 'GET',
						url: '/Portal/workCalendar/workitems/findAll',
					  // async:false,
						data: {
							userId: userID,
							startTime: start,
							endTime: end,
							workflowCode: workflowCode,
						},
						
						success:function (data) {//返回数据列表
							$.each(eval(data).data.workItemList, function (i, item) {
								var time = new Date(item.start);
                var year = time.getFullYear();
                var month = time.getMonth() + 1 
                var date = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
                var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
                var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
                var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
                date = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
								item.start = date;
								item.color = $scope.statusColorType[item.status];
								// item.url = 'WorkItemSheets.html?WorkItemID=' + item.id;
							})
							$(".newval4").html(data.data.unReadTotal);
							$(".newval").html(data.data.unfinishTotal);
							$(".newval1").html(data.data.finishTotal);
							$(".newval2").html(data.data.exceedTimeLimitTotal);
							$scope.events = data.data.workItemList;
							// 	console.log(data.data.workItemList)
							//  $scope.urlid=data.data.workItemList[].id;
							$('.calendar').fullCalendar('removeEvents', function () { return true; });

							$rootScope.loading = false;
							$scope.$apply();

							callback($scope.events);
							 
						},
						error: function () {
							alert("Failed");
						}

					})
				},
			

			

			}
			


		};

	//  $('#calendar').find('.button.fc-prev-button.fc-button.fc-state-default.fc-corner-left').click(function(){alert("niu")})

	// $('.fc-prev-button span').click(function(){
	// 			alert('prev is clicked, do something');
	// 	 });


	// 事件点击触发
	// eventClick: function(calEvent, jsEvent, view) {//日程区块，单击时触发
	// console.log(jsEvent.target)
	// alert(jsEvent.target.innerHTML)
	// console.log("↓↓↓eventClick↓↓↓");
	// console.log('Event: ' + calEvent.title);
	// console.log('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
	// console.log('Current view: ' + view.name);	},

	// 		return false;  //return false可以阻止点击后续事件发生（比如event中的url跳转事件）
	// 自定义按钮
	// customButtons: {
	//   buttonCreate: {
	//     text: "新建",
	//     click: function () {
	//       $(".datepicker").datepicker({
	//         language: "zh-CN",
	//         format: "yyyy-mm-dd",
	//         todayHighlight: true,
	//         autoclose: true,
	//         weekStart: 0,
	//       });
	//       $(".timepicki").wickedpicker({
	//         title: "",
	//         showSeconds: true,
	//         twentyFour: true,
	//       });
	//       $("#isallday").click(function () {
	//         if ($("#isallday").prop("checked") == true) {
	//           $("#isallday").val("1");
	//           $("#starttime,#endtime").hide();
	//         } else {
	//           $("#isallday").val("0");
	//           $("#starttime,#endtime").show();
	//         }
	//       });
	//       $("#end").click(function () {
	//         if ($("#end").prop("checked") == true) {
	//           $("#enddate").show();
	//         } else {
	//           $("#enddate").hide();
	//         }
	//       });
	//       $("#repeat").click(function () {
	//         if ($("#repeat").prop("checked") == true) {
	//           $("#repeattype,#repeattime").show();
	//         } else {
	//           $("#repeattype,#repeattime").hide();
	//         }
	//       });
	//       $("#repeatselect").change(function () {
	//         switch ($("#repeatselect").val()) {
	//           case "1":
	//             $("#repeatclock").show();
	//             $("#repeatmonth,#repeatweek,#repeatday").hide();
	//             break;
	//           case "2":
	//             $("#repeatmonth,#repeatday").hide();
	//             $("#repeatweek,#repeatclock").show();
	//             break;
	//           case "3":
	//             $("#repeatmonth,#repeatweek").hide();
	//             $("#repeatday,#repeatclock").show();
	//             break;
	//           case "4":
	//             $("#repeatweek").hide();
	//             $("#repeatmonth,#repeatday,#repeatclock").show();
	//             break;
	//           case "5":
	//             $("#repeatclock").show();
	//             $("#repeatmonth,#repeatweek,#repeatday").hide();
	//             break;
	//         }
	//       });
	//       dialog({
	//         title: "新建日程",
	//         content: $("#dialog-form"),
	//         okValue: "确定",
	//         ok: function () {
	//           var titledetail = $("#titledetail").val();
	//           var startdate = $("#startdate").val();
	//           var starttime = $("#starttime").val().split(" ").join("");
	//           var enddate = $("#stopdate").val();
	//           var endtime = $("#endtime").val().split(" ").join("");
	//           var allDay = $("#isallday").val();
	//           if (titledetail) {
	//             $.ajax({
	//               url: "http://localhost/fullcalendar/detail.php",
	//               data: {
	//                 title: titledetail,
	//                 sdate: startdate,
	//                 stime: starttime,
	//                 edate: enddate,
	//                 etime: endtime,
	//                 allDay: allDay,
	//               },
	//               type: "POST",
	//               dataType: "json",
	//               success: function (data) {
	//                 $("#calendar").fullCalendar("renderEvent", data, true);
	//               },
	//               error: function () {
	//                 alert("Failed");
	//               },
	//             });
	//           }
	//         },
	//         cancelValue: "关闭",
	//         cancel: function () {
	//           //$("#ui-datepicker-div").remove();
	//         },
	//       }).showModal();
	//     },
	//   },
	//   buttonQuery: {
	//     text: "查询",
	//     click: function () {
	//       $(".datepicker").datepicker({
	//         language: "zh-CN",
	//         format: "yyyy-mm-dd",
	//         todayHighlight: true,
	//         autoclose: true,
	//         weekStart: 0,
	//       });
	//       dialog({
	//         title: "查询",
	//         content: $("#search"),
	//         okValue: "查询",
	//         ok: function () {
	//           $("#ui-datepicker-div").remove();
	//         },
	//         button: [
	//           {
	//             value: "打印",
	//           },
	//         ],
	//         cancelValue: "返回",
	//         cancel: function () {
	//           $("#ui-datepicker-div").remove();
	//         },
	//       }).showModal();
	//     },
	//   },
	// },





	//   /********dayClick,eventClick 作为弹出框显示暂时不用*******/

	//   dayClick: function(date,allDay,jsEvent,view){
	// 			// var selDate = $.fullCalendar.formatDate(date,"YYYY-MM-DD");
	// 			var d = dialog({
	// 				title:"新建事件",
	// 				content:"<textarea rows=5 class='taxt' placeholder='内容' id='eventall'></textarea>",
	// 				width:460,
	// 				button:[{
	// 					value:"完整编辑",
	// 					callback:function(){
	// 						$(".datepicker").datepicker({
	// 							language:"zh-CN",
	// 							format:"yyyy-mm-dd",
	// 							todayHighlight:true,
	// 							autoclose:true,
	// 							weekStart:0
	// 						});
	// 						$(".timepicki").wickedpicker({
	// 							title:'',
	// 							showSeconds:true,
	// 							twentyFour:true
	// 						});
	// 						$("#isallday").click(function(){
	// 							if($("#isallday").prop("checked") == true){
	// 								$("#isallday").val("1");
	// 								$("#starttime,#endtime").hide();
	// 							}else{
	// 								$("#isallday").val("0");
	// 								$("#starttime,#endtime").show();
	// 							};	
	// 						});
	// 						$("#end").click(function(){
	// 							if($("#end").prop("checked") == true){
	// 								$("#enddate").show();
	// 							}else{
	// 								$("#enddate").hide();
	// 							};
	// 						});
	// 						$("#repeat").click(function(){
	// 							if($("#repeat").prop("checked") == true){
	// 								$("#repeattype,#repeattime").show();
	// 							}else{
	// 								$("#repeattype,#repeattime").hide();
	// 							};
	// 						});
	// 						$("#repeatselect").change(function(){
	// 							switch($("#repeatselect").val()){
	// 								case "1":
	// 									$("#repeatclock").show();
	// 									$("#repeatmonth,#repeatweek,#repeatday").hide();
	// 									break;
	// 								case "2":
	// 									$("#repeatmonth,#repeatday").hide();
	// 									$("#repeatweek,#repeatclock").show();
	// 									break;
	// 								case "3":
	// 									$("#repeatmonth,#repeatweek").hide();
	// 									$("#repeatday,#repeatclock").show();
	// 									break;
	// 								case "4":
	// 									$("#repeatweek").hide();
	// 									$("#repeatmonth,#repeatday,#repeatclock").show();
	// 									break;
	// 								case "5":
	// 									$("#repeatclock").show();
	// 									$("#repeatmonth,#repeatweek,#repeatday").hide();
	// 									break;
	// 							}
	// 						});
	// 						dialog({
	// 							title:"新建日程",
	// 							content:$("#dialog-form"),
	// 							okValue:"确定",
	// 							ok:function(){
	// 								var titledetail = $("#titledetail").val();
	// 								var startdate = $("#startdate").val();
	// 								var starttime = $("#starttime").val().split(" ").join("");
	// 								var enddate = $("#stopdate").val();
	// 								var endtime = $("#endtime").val().split(" ").join("");
	// 								var allDay = $("#isallday").val();
	// 								if(titledetail){
	// 									$.ajax({
	// 										url:'http://localhost/fullcalendar/detail.php',
	// 				   						data:{title:titledetail,sdate:startdate,stime:starttime,edate:enddate,etime:endtime,allDay:allDay},
	// 				   						type:'POST',
	// 				   						dataType:'json',
	// 				  						success:function(data){
	// 				  							$("#calendar").fullCalendar("renderEvent",data,true);
	// 				  						},
	// 				  						error:function(){
	// 				  							alert("Failed");
	// 				  						}

	// 									});
	// 								};
	// 							},
	// 							cancelValue:"关闭",
	// 							cancel:function(){}
	// 						}).showModal();
	// 					},	
	// 				}],
	// 				okValue:"确定",
	// 				ok:function(){
	// 					var titleall = $("#eventall").val();
	// 					if(titleall){
	// 						$.ajax({
	// 							url:'http://localhost/fullcalendar/events.php',
	// 	   						data:{title:titleall, start:selDate},
	// 	   						type:'POST',
	// 	   						dataType:'json',
	// 	  						success:function(data){
	// 	  							$("#calendar").fullCalendar("renderEvent",data,true);
	// 	  						},
	// 	  						error:function(){
	// 	  							alert("Failed");
	// 	  						}

	// 						});
	// 					};
	// 				},
	// 				cancelValue:"取消",
	// 				cancel:function(){}
	// 			});
	// 			d.showModal();
	// 		}, 
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
  

	//执行分析时间方法

	/*新增的ajax请求*/
	$scope.newway = function () {
		$scope.goToByDate();
		$scope.initStartAndEndDate();
		var userID = $("#newtag").val();
		if (userID === "") {
			var userID = $rootScope.loginUser.User.ObjectID;
		}

		var start = $scope.start;//获取上面分析的starttime
		var end = $scope.end;//获取上面分析的endtime
		$scope.sheet = $("#sheet").SheetUIManager().GetValue();

		var workflowCode = "";
		if ($scope.sheet != "") {
			workflowCode = ($scope.sheet).join();
		}
		$rootScope.loading=true;

		$.ajax({
			dataType: 'json',
			type: 'GET',
			url: '/Portal/workCalendar/workitems/findAll',
			data: {
				userId: userID,
				startTime: start,
				endTime: end,
				workflowCode: workflowCode,
			},
			success: function (data) {//返回数据列表
				$.each(eval(data).data.workItemList, function (i, item) {
					var time = new Date(item.start);
					var year = time.getFullYear();
					var month = time.getMonth() + 1 
					var date = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
					var hours = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
					var minutes = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
					var seconds = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
					date = year + '-' + (month) + '-' + (date) + ' ' + (hours) + ':' + (minutes) + ':' + (seconds);
					item.start = date;
					item.color = $scope.statusColorType[item.status];
					// item.url = 'WorkItemSheets.html?WorkItemID=' + item.id;
				})
				var events = data.data.workItemList;
				$(".newval4").html(data.data.unReadTotal);
				$(".newval").html(data.data.unfinishTotal);
				$(".newval1").html(data.data.finishTotal);
				$(".newval2").html(data.data.exceedTimeLimitTotal);
				$('.calendar').fullCalendar('removeEvents', function () { return true; });
				$('.calendar').fullCalendar('addEventSource', events, true);
				$rootScope.loading=false;
				$scope.$apply();
			},

			error: function () {
				alert("Failed");
			},
		})
	}
	$scope.initStartAndEndDate = function () {
		var e = new Date($('#calendarview').fullCalendar('getView').end);
		var tY = e.getFullYear();
		var tM = e.getMonth() + 1;
		var da = e.getDate() - 1;
		$scope.end = tY + '-' + tM + '-' + da;

		var s = new Date($('#calendarview').fullCalendar('getView').start);
		var tYear = s.getFullYear();
		var tMonth = s.getMonth() + 1;
		var my = s.getDate();
		$scope.start = tYear + '-' + tMonth + '-' + my;
	}





	$scope.announceshow = function () {
		$.ajax({
			dataType: 'json',
			type: 'GET',
			url: 'announcement/findShowAll',
			success: function (data) {//返回数据列表
				
				 $scope.announcement = data.data;
				 $scope.item = data.data;
				
				
				 
					 $scope.desc = function(index) {
					var item = $scope.item[index];
					window.open(item.link);
					};
			
				 
				
				

			},
			error: function () {
				alert("Failed");
			},
		});
	}
	$scope.announceshow ();


	//新人员下拉框js
	$scope.newtagRefresh=function(){	
		$('#newtag').on('changed.bs.select',
		function (e, clickedIndex, isSelected, previousValue) {
				//选中其他时，去除全部的选择状态
				if ($('#newtag').val() != null) {
						$scope.newtagData.forEach(el => {
								if ($('#newtag').val().indexOf(el.code) != "-1") {
									//   $('#newtagALL').removeAttr("selected");
									$('#newtagALL').attr("selected",false);
										$('#newtag').selectpicker('refresh');
										$('#newtag').selectpicker('render');
								}
						})
				}
				//选中全部时，只选全部，去除其他元素的选择状态
			 if(clickedIndex==0)
			 {
					$('#newtag').find("option:selected").attr("selected", false);
					$('#newtagALL').prop('selected',true);
					$('#newtag').selectpicker('refresh');
					$('#newtag').selectpicker('render');

			 }
			 //没有选中元素时，默认选择全部
			 if($('#newtag').val()==null){
					$('#newtagALL').prop('selected',true);
					$('#newtag').selectpicker('refresh');
					$('#newtag').selectpicker('render');
			 }
		 });
 
		$.ajax({
			"url": "/Portal/user/listSubordinate",
			"type": "Get",
			// "data": { categoryCode: "零售系统"},
			"dataType": "json",
			"success": function (data) {
					$scope.newtagData = data;
					var optionMulti = [];
					optionMulti = $scope.newtagData;
					//定义一个对象数组，用于储存所需选项
					for (var i = 0; i < optionMulti.length; i++) {
							$("#newtag").append($("<option value=\"" + optionMulti[i].id +"\">" + optionMulti[i].text + "</option>"));
					}
					$('#newtag').selectpicker({
						noneResultsText: '无搜索结果',
						noneSelectedText:'没有选中内容'
				  }); 
					//使用refresh方法更新UI以匹配新状态	
					$('#newtag').selectpicker('refresh');
					//render方法强制重新渲染引导程序 - 选择ui。
					$('#newtag').selectpicker('render');
				},
			"error": function () {
			}
		})
		$('#newtag').selectpicker('refresh');
		//render方法强制重新渲染引导程序 - 选择ui。
		$('#newtag').selectpicker('render');
	}
		$scope.newtagRefresh();
	
	

	// $scope.events = [
	// 	{ title: '你好', start: new Date(y, m, d), },
	// 	{ title: 'Long Event', start: new Date(y, m, d - 5), color: 'rgba(245,34,45)' },
	// 	{ id: 999, title: 'Repeating Event', start: new Date(y, m, d - 3, 16, 0), allDay: false },
	// 	{ id: 999, title: 'Repeating Event', start: new Date(y, m, d + 4, 16, 0), allDay: false },
	// 	{ title: 'Birthday Party', start: new Date(y, m, d + 1, 19, 0), end: new Date(y, m, d + 1, 22, 30), allDay: false },
	// 	{ title: 'Click for Google', start: new Date(y, m, 28), end: new Date(y, m, 29), url: 'http://google.com/' },
	// 	{ title: 'zql', start: new Date(y, m, 3) }
	// ];
	// $scope.eventSources = [$scope.events];

}]);