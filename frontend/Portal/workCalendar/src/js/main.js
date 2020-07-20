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
			height: 'auto',
			// 是否可编辑，即进行可拖动和缩放操作,默认false
			editable: false,
			// 设置一周中显示的第一天是哪天，周日是0
			firstDay: 1,
			// 设置周数不固定
			weekMode: 'liquid',
			//在agenda视图模式下，是否在日历上方显示all-day(全天)
			allDaySlot:false,
			// 强调日历中的某些时间段，
			businessHours: {
				dow: [1, 2, 3, 4, 5], // 周一 - 周四

				start: '9:00', // 上午9点开始
				end: '17:00', // 下午17点结束
			},
			// 自定义按钮
			customButtons: {
				buttonCreate: {
					text: '新建',
					click: function () {
						alert('新建日程')
					}
				},
				buttonQuery: {
					text: '查询',
					click: function () {
						alert('查询日程')
					}
				}
			},
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'buttonCreate buttonQuery month,agendaWeek,agendaDay'
			},
			buttonText: {
				today: "今天",
				month: "月",
				agendaWeek: "周",
				agendaDay: "天"
			},
			dayClick: function (date, allDay, jsEvent, view) {
				alert('1');
			},
			eventClick: function (event, jsEvent, view) {
				alert('2');
			}
		}
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