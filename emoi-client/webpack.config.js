const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  mode: 'development',
  entry: `${__dirname}/src/index.tsx`,
  output: {
    path: path.resolve(__dirname, '../emoi-server/src/main/resources/dist'),
    filename: 'index.js',
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
      },
      {
        test: /\.tsx?$/,
        use: 'ts-loader',
      },
      {
        test: /\.css$/,
        use: [
          {
            loader: 'style-loader', // creates style nodes from JS strings
          },
          {
            loader: 'css-loader',
            options: { url: false }, // translates CSS into CommonJS
          },
        ],
      },
      {
        test: /\.html$/,
        loader: 'html-loader',
      },
      {
        test: /\.(jpg|png)$/,
        loaders: 'url-loader',
      },
    ],
  },
  resolve: {
    extensions: ['.ts', '.tsx', '.js', '.json'],
    modules: ['node_modules', path.resolve(__dirname, 'src')],
  },
  devtool: 'inline-source-map',
  devServer: {
    contentBase: `${__dirname}/public`,
    port: '3000',
    historyApiFallback: { disableDotRule: true },
    watchOptions: {
      poll: 1000,
    },
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: `${__dirname}/public/index.html`,
      minify: false,
    }),
  ],
};
