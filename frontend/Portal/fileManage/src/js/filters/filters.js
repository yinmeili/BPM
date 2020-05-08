(function(angular) {
    'use strict';
    // var app = angular.module('app');

    app.filter('strLimit', ['$filter', function($filter) {
        return function(input, limit) {
            if (input.length <= limit) {
                return input;
            }
            return $filter('limitTo')(input, limit) + '...';
        };
    }]);

    //var dateAsString = $filter('date')(item_date, "yyyy-MM-dd hh:mm:ss");

    app.filter('formatDate', ['$filter', function($filter) {
        return function(input) {
              return $filter('date')(input, "yyyy-MM-dd HH:mm:ss");
//            return input instanceof Date ?
//                input.toISOString().substring(0, 19).replace('T', ' ') :
//                (input.toLocaleString || input.toString).apply(input);
        };
    }]);
})(angular);
