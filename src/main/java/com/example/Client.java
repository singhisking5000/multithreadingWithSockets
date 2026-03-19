
package com.example;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

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
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
        ObjectOutputStream   out = new ObjectOutputStream(socket.getOutputStream()); // our message to be sent
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); // what we recieve
        // keep reading the input stream
        // when we hit enter, we want to output our text

        JFrame f = new JFrame("Client");
        JPanel contentPanel = new JPanel();
        JTextField inputField = new JTextField();
        contentPanel.setLayout(new GridLayout(1, 2));
        JTextArea messageArea = new JTextArea();
        contentPanel.add(messageArea);
        contentPanel.add(inputField);
        f.setLayout(new GridLayout());
        f.add(contentPanel);
        

        //make a guid here
        //put the writing stuff (below) to attach to an action listener or a text box


    
        Scanner input = new Scanner(System.in);
        String line ="";
        while(!(line = input.nextLine()).equals("disconnect")){
            out.writeObject(line);
            out.flush();
        }

        input.close();
        socket.shutdownOutput();
        System.out.println("connection closed!");

        // while(true)
        // {
        //     String incomingMessage = (String)in.readObject();
        //     System.out.println(incomingMessage);
        // }


        
        // 2 threads for reading and 
    }
    private void enterPressed()
    {

    }
}