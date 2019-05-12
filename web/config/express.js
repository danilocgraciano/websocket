const express = require('express');
const app = express();

app.use('/node_modules', express.static('./node_modules'));
app.use('/js', express.static('./js'));

module.exports = app;