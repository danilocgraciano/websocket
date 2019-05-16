const express = require('express');
const helmet = require('helmet');
const app = express();
app.use(helmet());

app.use('/node_modules', express.static('./node_modules'));
app.use('/js', express.static('./js'));

module.exports = app;