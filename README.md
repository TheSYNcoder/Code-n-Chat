# Code-n-Chat


This repository contains the demonstration of TCP and UDP sockets.

### TCP Sockets

A multi-threaded chat application was made using TCP sockets using the ServerSocketChannel class 
in java.nio. 
* It supports group texts
* Personal chats can also be configured by adding a predicate in the brodcast method in [Server](./TCP/Server.java).
* Highly scalable due to non-blocking IO of java nio. ( Note `serverSocket.setBlocking(false)` )


### UDP Sockets

Just a demonstration of UDP sockets, showing client server connection. The client sends some messages 
which the server receives and are shown in the console.



