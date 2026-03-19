package com.example;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program should allow the client to send it messages. The messages should then 
 * become visible to all other clients.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example). 
 * 
 * This version of the program creates a new thread for
 * every connection request.
 */
public class Server {
    // OUR SERVERS PORT
    public static final int LISTENING_PORT = 9876;
    //All our connections(to our clients) will be stored in the ArrayList below
    //private static ArrayList<ConnectionHandler> handlers = new ArrayList<>();

    public static void main(String[] args) {

        ServerSocket listener;  // Listens for incoming connections.
        //Socket connection;

        try {
            // Create the servers socket
            listener = new ServerSocket(LISTENING_PORT);
            //connection = listener.accept();
            System.out.println("Listening on port " + LISTENING_PORT);

            while (true) {
                //Keep creating new ConnectionHandlers
                ConnectionHandler temp = new ConnectionHandler(listener.accept());
                System.out.println("Successsfully connected to (" + temp.socket.getInetAddress().toString() + ")!");
                temp.start();
                temp.out.writeObject("You have connected!");
            }
        } catch (Exception e) {
            System.out.println("Sorry, the server has shut down.");
            System.out.println("Error:  " + e);
            return;
        }
    }  // end main()


    /**
     *  Defines a thread that handles the connection with one
     *  client.
     */
    private static class ConnectionHandler extends Thread {
        //This handles our connection between the server socket and the 
        private static ArrayList<ConnectionHandler> handlers;
        Socket socket;
        ObjectInputStream in; // From client
        ObjectOutputStream out; // To client

        public ConnectionHandler(Socket s) {
            socket = s;
            if(handlers == null)
            {
                //Initialize handlers
                handlers = new ArrayList<>();
            }
            // Add us to the handlers
            handlers.add(this);
            //Attempt to fetch the input stream
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        public void run() {
            String clientAddress = socket.getInetAddress().toString();
            
            while(true) {
	            try {
                    String message = (String)in.readObject();
                    if(!message.equals("disconnect")) {
                        System.out.println("Attempting to send: " + message);
                        send(message);
                    } else {
                        System.out.println("disconnecting");
                        this.socket.close();
                        handlers.remove(this);
                        break;
                    }
	            } catch (Exception e){
	                System.out.println("Error on connection with: " + clientAddress + ": " + e);
                    break;
	            }
            }
        }

        public void send(String s) {
            String clientAddress = socket.getInetAddress().toString();
            synchronized(this) {
                // go throughout each other handler, and make their output stream the message
                for(ConnectionHandler handler : handlers)
                {
                    if (handler != this) 
                    {
                        try {
                            //For each OTHER handler
                            synchronized(handler)
                            {
                                //Isolate them, and change their output stream to the message and rush it out
                                handler.out.writeObject(s);
                                handler.out.flush();
                            }
                        } catch (Exception e) {
                            System.out.println("Error sending message from: " + clientAddress + ": " + e);
                        }
                    }
                }
            }
        }
    } // end of our ConnectionHandler thread
} // End of Server.java
