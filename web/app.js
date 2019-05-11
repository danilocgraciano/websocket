var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

http.listen(3000, () => console.log('Server running on port 3000...'));

app.get('/', (req, res) => res.send('Server is runnning...'));

io.on('connection', (client) => {

    client.on('send', function(obj){
        var data = JSON.parse(obj);
        io.to(data.destino).emit('message', obj);
    });
});