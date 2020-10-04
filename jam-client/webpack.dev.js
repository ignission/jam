const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'inline-source-map',
  devServer: {
    contentBase: `${__dirname}/public`,
    port: '3000',
    historyApiFallback: { disableDotRule: true },
    watchOptions: {
      poll: 1000,
    },
  },
});
