const { merge } = require('webpack-merge');
const webpack = require("webpack")
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'inline-source-map',
  devServer: {
    contentBase: `${__dirname}/public`,
    port: '3000',
    historyApiFallback: { disableDotRule: true },
  },
  plugins: [
    new webpack.DefinePlugin({
      'WS_URL': JSON.stringify('ws://localhost:9000')
    })
  ],
});
