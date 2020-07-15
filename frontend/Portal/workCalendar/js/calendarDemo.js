/**
 * calendarDemoApp - 0.9.0
 */
var WorkCalendarApp = angular.module('WorkCalendarApp', ['ui.calendar', 'ui.bootstrap']);

WorkCalendarApp.controller('workCalendarCtrl', ['$scope', '$compile', '$timeout','uiCalendarConfig',function ($scope, $compile, $timeout, uiCalendarConfig) {

/*     $(document).ready(function () {
    // 加入根据年月跳转
    // $('#directives-calendar .fc-right').prepend('');

    $("#monthbutton").click(function () {
      var monthtext = $("#monthpicker").val();
      console.log(monthtext);

      var date = $.fullCalendar.moment(monthtext);
      console.log(date);

      uiCalendarConfig.calendars.myCalendar1.fullCalendar('gotoDate', date);
    })
  }); */

  var date = new Date();
  var d = date.getDate();
  var m = date.getMonth();
  var y = date.getFullYear();

  $scope.years = ['2010', '2011', '2012', '2013', '2014', '2015', '2016', '2017', '2018', '2019','2020','2021','2022','2023','2024','2025','2026','2027','2028','2029','2030'];
  $scope.months = ['1','2','3','4','5','6','7','8','9','10','11','12'];

  $scope.goToByDate=function(){
    // alert($scope.selectYear);
    // alert($scope.selectMonth);
    var year = $scope.selectYear;
    var month = $scope.selectMonth;
    var date = year + ','+ month;
    var goToDate = $.fullCalendar.moment(date);
    uiCalendarConfig.calendars.myCalendar1.fullCalendar('gotoDate', goToDate);
  }




  /* event source that contains custom events on the scope */
  $scope.events = [
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'All Day Event', start: new Date(y, m, 1) },
    { title: 'Long Event', start: new Date(y, m, d - 5), end: new Date(y, m, d - 2) },
    { id: 999, title: 'Repeating Event', start: new Date(y, m, d - 3, 16, 0), allDay: false },
    { id: 999, title: 'Repeating Event', start: new Date(y, m, d + 4, 16, 0), allDay: false },
    { title: 'Birthday Party', start: new Date(y, m, d + 1, 19, 0), end: new Date(y, m, d + 1, 22, 30), allDay: false },
    { title: 'Click for Google', start: new Date(y, m, 28), end: new Date(y, m, 29), url: 'http://google.com/' },
    { title: 'zql', start: new Date(y, m, 3) }
  ];

  /* config object */
  $scope.uiConfig = {
    calendar: {
      height: 450,
      editable: true,
      header: {
        left: 'prev,next today',
        center: 'title',
        right: 'month,agendaWeek,agendaDay'
      },
      weekMode: 'liquid'
    }
  };

  /* event sources array*/
  $scope.eventSources = [$scope.events];
 



}]
   );
/* EOF */
