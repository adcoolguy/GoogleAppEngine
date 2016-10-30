'use strict';
// You can use this with angular.
var ngAnnotatePlugin = require('ng-annotate-webpack-plugin');
var path = require('path');
// var $script = require("scriptjs");

module.exports = {
    devtool: 'inline-source-map', // in-line source maps instead of the default
    entry: "./entry.js",
    // entry: {
    //     app: "../ng/app.js",
    //     main: "./entry.js",
    //     jsx: ["./jsx/sr-start.jsx", "./jsx/sr-create.jsx", "./jsx/sr-update.jsx", "./jsx/mount-sr-create.jsx", "./jsx/mount-sr-update.jsx"],
    //     swaggerclient: "../js/swagger-client.js.SWG1"
    // },
    output: {
        path: __dirname,
        filename: "bundle.js"
    },
    module: {
        noParse: path.resolve("../js/swagger-client.js.SWG1"),
        loaders: [
            {
                test: /\.js$/,
                loader: 'babel',
                exclude: /node_modules/
            },
            { test: /[\/]angular\.js$/, loader: "exports?angular" },
            { test: /\.css$/, loader : 'style!css' },
            //{ test: /\.css$/, loader: "style-loader!css-loader" },
            {
                test: /.jsx?$/,
                loader: 'babel-loader',
                exclude: ['/node_modules/', '/bower_components/', '../ng'],
                query: {
                  presets: [
                    require.resolve('babel-preset-es2015'),
                    require.resolve('babel-preset-react')
                    //, require.resolve('babel-preset-stage-0')
                  ]
                }
            }
        ],
    },
    resolve: {
        root: __dirname,
        modulesDirectories: ['../js/bower_components', 'node_modules', 'js', 'css'],
        // extensions: ['', '.js']
        extensions: [ '', '.js', '.jsx' ],
        // ,
        // alias: {
        //     angular: __dirname + '/app/vendor/angular/angular',
        //     lodash: __dirname + '/app/vendor/lodash/lodash',
        //     angularRoute: __dirname + '/app/vendor/angular-ui-router/release/angular-ui-router',
        //     moment: __dirname + '/app/vendor/moment/min/moment-with-locales.min',
        //     'angular-moment': __dirname + '/app/vendor/angular-moment/angular-moment',
        // }
        fallback: [path.join(__dirname, "node_modules"), path.resolve(__dirname, '../ng')]
    },
    // resolve: {
    //     extensions: [ '', '.js', '.jsx' ],
    //     fallback: [path.join(__dirname, "node_modules"), path.resolve(__dirname, '../ng')]
    // },
    resolveLoader: {
        root: path.join(__dirname, "node_modules")
    },
    plugins: [
        new ngAnnotatePlugin({
            add: true
        })
    ]
};

// $script("//cdnjs.cloudflare.com/ajax/libs/moment.js/2.9.0/moment.min.js", function() {
//   console.log('loaded momentjs');
// });
// $script("//cdnjs.cloudflare.com/ajax/libs/angular-moment/0.9.0/angular-moment.min.js", function() {
//   console.log('loaded angular momentjs');
// });
