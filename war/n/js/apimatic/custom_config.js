<!-- override the APIMATIC settings with the current host/deployed env -->
angular.module('CrudServiceLib').factory('Configuration', function() {
    return {
        BASEURI : location.origin
    };
});