package com.example;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
	
	
	/*
	 * Modify this example so that it opens a dialogue window using java swing, 
	 * takes in a user message and sends it
	 * to the server. The server should output the message back to all connected clients
	 * (you should see your own message pop up in your client as well when you send it!).
	 *  We will build on this project in the future to make a full fledged server based game,
	 *  so make sure you can read your code later! Use good programming practices.
	 *  ****HINT**** you may wish to have a thread be in charge of sending information 
	 *  and another thread in charge of receiving information.
	*/

    //object is to make a gui that sends and receives messages to/from the server


    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        for(int i=0; i<5;i++){
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 9876);

            //write to socket using ObjectOutputStream
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");

            if(i==4)out.writeObject("exit");
            else out.writeObject(""+i);
            
            //read the server response message
            in = new ObjectInputStream(socket.getInputStream());
            String message = (String) in.readObject();
            System.out.println("Message: " + message);
            //close resources
            in.close();
            out.close();
            Thread.sleep(100);
        }
    }
}
