******************************************************************
How to start:

To compile this program all you have to do is run compile.sh 

compile.sh contains:
javac TCPServer.java

To start TCPServer run the runServer.sh file

runServer.sh contains:
java -Djavax.net.ssl.keyStore=mySrvKeystore -Djavax.net.ssl.keyStorePassword=123456 TCPServer

I have attached a keyFile mySrvKeyStore in the zip


I created the keyFile using the following command
keytool -genkey -keystore mySrvKeystore -keyalg RSA

the password for this case was set to 123456

I have also included an example TCPClient that I used for testing purposes. 

inorder to compile execute the following code

javac TCPClient.java

To run it execute the runClient.sh file


******************************************************************

My thought process:

When creating this server class, it needed to do two basic functions. First it would need to establish connections to Clients so that messages can be sent. Second it would need a way to decode those messages in order to print the necessary information.  

When doing this, it’s normally pretty easier to establish a connection, but when I thought about it further I needed a way to set up a safer connection. For example look at the following code

ServerSocket serverSocket= new ServerSocket(1234)
Socket connectionSocket = serverSocket.accept();

This does the basisc, it sets up a connection between a server and client, then you can send messages back and forth. The downside to this is that this code could be subject to a middle man attack. Meaning that someone could read all incoming messages from the client and forward it to the server. All private information such as passwords or credit card numbers would be subject to an attack.

In order to fix this, I setup a SSH sockets. We need some basic level of encryption so that even if there was a middle man attack, the data he would be getting would be meaningless to him.

Another thing that I thought of was how would I handle multiple users trying to connect at once. One way could be to create a queue and have the client wait until it is their turn. This would would lead to the problem of having long wait times. It could be possible that one client is greedy and uses a lot of time, or might decide he doesn’t want to disconnect. 

The best approach would be to create a new thread for each client. This would allow for no wait times and the client wouldn’t be depended on the results of another client.

So that was my thought process of while I was coding this.

******************************************************************

Things I would add if more time:

The first thing I had in my mind was the issue of scalability. If 1000 or 1,000,000 clients are trying to connect to my server, how would it handle that. I could have a server farm with thousands of different servers available and have some type of load balancing. The load balancer will read all incoming clients and know which servers are free and direct clients to open servers. When a client and server are done, the server will have to notify the load balancer that it is free and ready for another client.

Another thought I had was after I print payload, what would I do with that. The payload could be anything ranging from a 6 digit password to a 1 gigabyte video file. From a business standpoint it would be important to know what could be possible so that the server would be able to handle it correctly and efficiently, otherwise it would like bad for the business. Once a connection is made, there will typically be a back and forth between the client and the server. The server will send information to the client and the client will have to respond to it. That would be the next possible step and making that efficient would give the user the best experience using the product. 


