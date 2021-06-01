const { merge } = require('webpack-merge');
const webpack = require("webpack")
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new webpack.DefinePlugin({
      'WS_URL': JSON.stringify('wss://jam.ignission.tech')
    })
  ],
});
