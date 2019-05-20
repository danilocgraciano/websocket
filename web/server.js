const app = require('./config/express.js');

var http = require('http').Server(app);
var io = require('socket.io')(http);

http.listen(3000, () => console.log('Server running on port 3000...'));

app.get('/', (req, res) => res.sendFile(__dirname + "/index.html"));

app.get('/clients', (req, res) => {

    var html = "<h1>Clientes Conectados</h1>";

    var sockets = io.sockets.sockets;
    for (var socketId in sockets) {
        var socket = sockets[socketId];
        html += `<h2>${socket.token} - ${socket.id}</h2>`
    }
    res.send(html);
});

io.on('connection', (client) => {

    client.token = client.handshake.query.token;
    
    client.on('send', function (obj) {
        var data = JSON.parse(obj);

        // https://socket.io/docs/emit-cheatsheet/
        
        io.to(data.destino).emit('message', obj);
    });
});