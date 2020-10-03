const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyFilePlugin = require('copy-webpack-plugin');
const WriteFilePlugin = require('write-file-webpack-plugin');

module.exports = {
  mode: 'development',
  entry: `${__dirname}/src/index.tsx`,
  output: {
    path: path.resolve(
      __dirname,
      '../jam-server/jam-server/src/main/resources/static'
    ),
    filename: 'index.js',
    chunkFilename: '[name].bundle.js',
  },
  module: {
    rules: [
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
  plugins: [
    new HtmlWebpackPlugin({
      template: `${__dirname}/public/index.html`,
      minify: false,
    }),
    new CopyFilePlugin({
      patterns: [
        { 
          context: 'public/images',
          from: '**/*',
          to: path.resolve(
            __dirname,
            '../jam-server/jam-server/src/main/resources/static/images'
          ),
        },
      ],
      options: {
        concurrency: 100,
      },
    }),
    new WriteFilePlugin(),
  ],
};
