jest.dontMock('../../../war/html/calendar');
jest.dontMock('../bower_components/jquery/dist/jquery.min');
jest.dontMock( "util" );

$ = require('../bower_components/jquery/dist/jquery.min');
var c = require('../../../war/html/calendar');

function log(msg) {
    //=== see and perform the removal as suggested by https://github.com/facebook/jest/issues/110
    //vim C:\Users\ag\AppData\Roaming\npm\node_modules\jest-cli\bin\jest.js
    console.log(msg);
}

describe('calendar', function() {
    //require('moment');
    require('datejs');

    it('create an event for a scheduled movie and not logged in', function() {
        //log('v1');
        var copiedEventObject;
        var tempDate1 = new Date(2014,11,22,11,30,30,0);
        var tempDate2 = new Date(2014,11,22,12,30,30,0);
        var allDay;
        var eventId = '1234567890'; //createEventId();
        //=== supports create event from Movie UI use case
        copiedEventObject = {
            id: eventId, allDay: allDay
        };
        copiedEventObject.title = $('input[name=title]').val();
        copiedEventObject.start = tempDate1.format("mm/dd/yyyy HH:MM");  //  - TypeError: Object Mon Dec 22 2014 11:30:30 GMT-0500 (Eastern Standard Time) has no method 'format'
        copiedEventObject.movieid = $('input[name=movieid]').val();
        copiedEventObject.movieUrl = $('input[name=movieUrl]').val();
        copiedEventObject.thumbUrl = $('input[name=thumbUrl]').val();
        var temp = c.saveNewEvent(copiedEventObject);
        //log('c.getSubTitle() = [' + temp + ']');
        expect(temp).toBe(false);
    });
});
