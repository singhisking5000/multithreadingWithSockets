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
    private static ArrayList<Socket> connections;

    public static void main(String[] args) {

        ServerSocket listener;  // Listens for incoming connections.

        /* Accept and process connections forever, or until some error occurs. */
        try {
            // Create the servers socket
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            while (true) {
                //Keep trying to accept new 
                connections.add(listener.accept());
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
        Socket client;
        ObjectInputStream in;
        ObjectOutputStream out;

        ConnectionHandler(Socket socket) {
            client = socket;
        }

        public void run() {
            String clientAddress = client.getInetAddress().toString();
            while(true) {
	            try {
	            	//your code to send messages goes here.
	            }
	            catch (Exception e){
	                System.out.println("Error on connection with: " 
	                        + clientAddress + ": " + e);
	            }
            }
        }

        public void send(String s){
            synchronized(this){
                //send message to just me
            }
        }
    } // end of our ConnectionHandler thread
} // End of Server.java
