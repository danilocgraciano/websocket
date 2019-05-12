var express = require('express');
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);

app.use('/node_modules', express.static(__dirname + '/node_modules'));
app.use('/js', express.static(__dirname + '/js'));

http.listen(3000, () => console.log('Server running on port 3000...'));

app.get('/', (req, res) => res.sendFile(__dirname + "/index.html"));

app.get('/clients', (req, res) => {

    var html = "<h1>Clientes Conectados</h1>";

    var sockets = io.sockets.sockets;
    for (var socketId in sockets) {
        var socket = sockets[socketId];
        html += `<h2>${socket.id}</h2>`
    }
    res.send(html);
});

io.on('connection', (client) => {

    client.on('send', function (obj) {
        var data = JSON.parse(obj);
        io.to(data.destino).emit('message', obj);
    });
});