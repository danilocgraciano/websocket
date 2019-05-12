$(document).ready(function () {

    $('#txaMensagem').val('WEB - Hello World!');

    var socket = io.connect('http://localhost:3000?token=WEB');
    socket.on('connect', () => {
        $('#txtOrigem').val(socket.id);
    });

    socket.on('message', function (obj) {
        var data = JSON.parse(obj);
        alert(`Eu recebi uma mensagem de ${data.origem} que diz ${data.mensagem}`);
    });

    $('#form').submit(function (e) {
        e.preventDefault();

        var obj = {
            'origem': $('#txtOrigem').val(),
            'destino': $('#txtDestino').val(),
            'mensagem': $('#txaMensagem').val()
        };

        socket.emit('send', JSON.stringify(obj))
    });

});