jest.dontMock('../../../war/html/channel');
jest.dontMock('../../../war/html/channel');
jest.dontMock('../bower_components/jquery/dist/jquery.min');

$ = require('../bower_components/jquery/dist/jquery.min');
var c = require('../../../war/html/channel');
var y = require('../../../war/jquery/YoutubeSubtitiles');

function log(msg) {
    //=== see and perform the removal as suggested by https://github.com/facebook/jest/issues/110
    //vim C:\Users\ag\AppData\Roaming\npm\node_modules\jest-cli\bin\jest.js
    console.log(msg);
}

describe('channel', function() {
    it('get sub from a text', function() {
        //log('v1');
        var temp = c.getSubTitle('#test_en.srt');
        //log('c.getSubTitle() = [' + temp + ']');
        expect(temp).toBe('test_en.srt');
    });

    /** somehow need to test the subtitle display on/off by peeking into <div class="srt"></div> */
    it('display a sub', function() {
        //log('v1');
        //TODO need to spawn a timer, check the div contents based on the period as specified in the .srt file!

        //var temp = c.loadMovieShuffle('tiffany');
        ////log('c.loadMovie() = [' + temp + ']');
        //expect(temp).toBe(false);
    });

    it('load random movies for a channel (My Channel) by username and not logged in', function() {
        //log('v1');
        var temp = c.loadMovieShuffle('tiffany');
        //log('c.loadMovie() = [' + temp + ']');
        expect(temp).toBe(false);
    });

    it('load all movies in sequence for a channel (My Channel) by username and not logged in', function() {
        //log('v1');
        var temp = c.loadMovieAll('tiffany');
        //log('c.loadMovie() = [' + temp + ']');
        expect(temp).toBe(false);
    });

    it('load movie for a channel (My Channel) by username and not logged in', function() {
        //log('v1');
        var temp = c.loadMovie('tiffany');
        //log('c.loadMovie() = [' + temp + ']');
        expect(temp).toBe(false);
    });


});
