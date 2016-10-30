var webpack = require('webpack');
var path = require('path');
var CommonsChunkPlugin = require('./node_modules/webpack/lib/optimize/CommonsChunkPlugin');

module.exports = {
    entry: {
        main: './main.js',
        about: './dist/about',
        contact: './dist/contact'
        ,
        vendor: ['react', 'react-dom']
    },
    output: {
        path: path.join(__dirname, 'build'),
        filename: '[name].bundle.js'
    },
    devServer: {
        inline: true,
        contentBase: './build',
        port: 3000
    },
    module: {
        loaders: [
            {
                test: /\.js$/,
                exclude: ['/(node_modules)/', '/(bower_components)/', '../ng'],
                loader: 'babel',
                query: {
                  presets: ['es2015','react']
                }
            },
            {
                test: /\.(png|jpg)$/,
                loader: 'url'
            },
            {
                test: /\.css$/,
                loader: 'style-loader!css-loader'
            },
            {
                test: /\.scss$/,
                loader: 'style-loader!css-loader!sass-loader'
            }

        ],
    },
    plugins: [
        // new CommonsChunkPlugin('common', 'common.bundle.js')
        // ,
        new CommonsChunkPlugin('vendor', 'vendor.bundle.js')
    ]
};
