var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
app.get('/',function(req,res){
      res.sendFile(__dirname+'/index.html');

  })
io.on('connection',function(socket){
    console.log('one user connected '+socket.id);
    socket.on('register',function(data){
        console.log('register requested ');
        socket.broadcast.emit('register', data.username);
    });
    socket.on('echo',function(data){
            socket.broadcast.emit("echo", data);
        });
    socket.on('disconnect',function(){
        console.log('one user disconnected '+socket.id);
    })
})



http.listen(3000,function(){
    console.log('server listening on port 3000');
})