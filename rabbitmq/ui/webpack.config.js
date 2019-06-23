const path = require('path');

module.exports = {
    mode: 'development',
    entry: './app.js',
    output: {
        path: path.join(__dirname, '../src/main/resources/static/'),
        filename: 'app.js'
    },
    devServer: {
        contentBase: path.join(__dirname, '../src/main/resources/static/'),
    },
    module: {
        rules: [
            {
                test: /.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                query: {
                    presets: ['@babel/preset-env', '@babel/preset-react']
                }
            }
        ]
    },
    node: {
        net: 'empty'
    }
};