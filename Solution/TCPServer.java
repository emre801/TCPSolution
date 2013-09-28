import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public
class TCPServer {
	
	private static class DecreyptMessage implements Runnable{
	
		SSLSocket sslsocket;
		Thread t;
		
		public DecreyptMessage(SSLSocket sslsocket)
		{
			this.sslsocket= sslsocket;
			t = new Thread(this, "TCPThread");
			t.start(); // Start the thread
		}
	
		public void run()
		{
			try
			{
					int minMessageLength=7;
					InputStream inputstream = sslsocket.getInputStream();
					InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
					BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

					//reads the message from the client and stores it as a String
					String message = null;
					message = bufferedreader.readLine();
					
					System.out.println("Received: " + message+"-");
					System.out.println("Message length"+message.length());
					
					//check to make sure the number of bytes is possible
					//for instance something along the lines 0000 will not be formattable, so print an error
					if(message.length()<=minMessageLength)
					{
						//This error could also be returned to the user.
						System.out.println("invalid message, lenght is too short ");
						return;
					}
					
					String version= message.substring(0,1);
					int messageType=0;
					int userID=0; 
					try
					{
						messageType=  Integer.parseInt(message.substring(1,3));
						userID=  Integer.parseInt(message.substring(3,7));
					}
					catch(NumberFormatException e)
					{
						//This means the input was not valid, either messageType or userID were Integers
						System.out.println("Invalid message");
						 return;
					}
					//The rest of the payload is stored here.
					String payload= message.substring(7,message.length());
					
					System.out.println("Version number: "+ version);
					System.out.println("Message type "+messageType);
					System.out.println("UserID: "+userID);
					System.out.println("Payload: "+payload);
			}
			catch (Exception exception) {
			//If for some reason there's an unexpected error we can see what might have caused it
				exception.printStackTrace();
			}
        
		
		
		}
	
	
	}



    public static void main(String[] arstring) {
	
		try {
			
			//Initialize the SSL socket with the random port of 9999 here.
            SSLServerSocketFactory sslserversocketfactory =(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket =(SSLServerSocket) sslserversocketfactory.createServerSocket(9999);
			while(true)
			{
				//This will wait until a successfull handshake is done and the client and server are now connected
				SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();
				//This will create a new thread so the server can wait for another client to connect right away.
				new DecreyptMessage(sslsocket);
				
			}
        } catch (Exception exception) {
		//If for some reason there's an unexpected error we can see what might have caused it
            exception.printStackTrace();
        }
        
    }
}
