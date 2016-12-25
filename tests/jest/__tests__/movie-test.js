//__tests__/sum-test.js

function log(msg) {
    //=== see and perform the removal as suggested by https://github.com/facebook/jest/issues/110
    //vim C:\Users\ag\AppData\Roaming\npm\node_modules\jest-cli\bin\jest.js
    console.log(msg);
}

//courtesy of http://blog.thebrownmamba.com/2014/06/unit-testing-your-ionic-angular-app.html
//  angularUIRouter: '../bower_components/angular-ui-router',
var files = {
  // jquery: '../bower_components/jquery/dist/jquery',
  jquery: '../../../war/js/jquery-1.9.1',
  angular: '../bower_components/angular/angular',
  angularAnimate: '../bower_components/angular-animate/angular-animate',
  angularSanitize: '../bower_components/angular-sanitize/angular-sanitize',
  angularMocks: '../bower_components/angular-mocks/angular-mocks',
  lodash: '../bower_components/lodash/dist/lodash',
  angularBootstrap: '../bower_components/angular-bootstrap/ui-bootstrap',
  prefs: '../../../war/js/prefsmovie',
  app: '../../../war/mcrud/movie'
};

window.Event = {};

jest
  .dontMock(files.jquery)
  .dontMock(files.angular)
  .dontMock(files.angularAnimate)
  .dontMock(files.angularSanitize)
//  .dontMock(files.angularUIRouter)
  .dontMock(files.angularMocks)
  .dontMock(files.lodash)
  .dontMock(files.angularBootstrap)
  // .dontMock('../../../war/jquery/purl')
  .dontMock(files.prefs)
  .dontMock(files.app);

_ = require(files.lodash);
require(files.angular);
require(files.angularAnimate);
require(files.angularSanitize);
//require(files.angularUIRouter);
require(files.angularMocks);
require(files.angularBootstrap);
$ = require(files.jquery);

// var p = require('../../../war/jquery/purl');
var prefs = require(files.prefs);
var m = require(files.app);

describe('movie', function() {
   var scope, controller, p;

    beforeEach(angular.mock.module('myApp'));
	beforeEach(angular.mock.inject(function ($rootScope, $controller) {
		    scope = $rootScope.$new();
        scope.userid = 'user1';
        scope.logintype = 'type1';
        p = {};
        p.param = function(id) {
          if(id === 'username') return scope.userid;
          if(id === 'logintype') return scope.logintype;
        };
        $ = function() {
        };
        // scope.getUsername = function () {
        //     return scope.userid;
        // };
        showModernView = prefs.showModernView;
        showSlotView = prefs.showSlotView;
        $.url = function() {
            return p;
        };
        controller = $controller('MovieCtrl', {$scope: scope} );
	}));

  it('get a shuffled channel url for a movie playback (Play Now) from a text with a hastag', function() {
        //log('v5');
        var input = '#hash1';
       	//var temp = $scope.getHashtagUrl('#hash1', $scope.userid, $scope.logintype);
        var temp = m.getHashtagUrl(input, scope.userid, scope.logintype);
        // var temp = controller.getHashtagUrl(input, scope.userid, scope.logintype);
        var expected = '/html/channelshuffle.html?username=' + scope.userid + '&logintype=' + scope.logintype + '&filter=' + input.substr(1);
       	//log('getHashtagUrl() = [' + temp + ']');
       	expect(temp).toBe(expected);
    });

  it('get youtube video id from a youtube url', function() {
      var input = 'https://www.youtube.com/watch?v=g-x1QKriY90';
      var temp = m.getYoutubeVideoId(input);
      var expected = 'g-x1QKriY90';
    	expect(temp).toBe(expected);
  });
});
