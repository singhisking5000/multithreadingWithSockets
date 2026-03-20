
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
    public static JTextField inputField;
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
        
        // keep reading the input stream
        // when we hit enter, we want to output our text

        JFrame f = new JFrame("Client");
        JPanel contentPanel = new JPanel();
        inputField = new JTextField();
        // InputMap inputMap = new InputMap();
        contentPanel.setLayout(new GridLayout(1, 2));
        JTextArea messageArea = new JTextArea();
        contentPanel.add(messageArea);
        contentPanel.add(inputField);

        // inputField.addKeyListener(KeyEvent k);

        f.setLayout(new GridLayout());
        f.add(contentPanel);

        //make a guid here
        //put the writing stuff (below) to attach to an action listener or a text box


    
        Scanner input = new Scanner(System.in);
        String line ="";
        

        input.close();
        socket.shutdownOutput();
        System.out.println("Connection closed!");
    }

    //
    // @Override
    // public void keyPressed(KeyEvent e) {
    //     if(e.getKeyCode() == KeyEvent.VK_ENTER) // If we hit the enter key
    //     {
    //         String text = inputField.getText
    //     }
    // }
    // @Override
    // public void keyReleased(KeyEvent e) {
    //     // Not necessary
    // }
    // @Override
    // public void keyTyped(KeyEvent e) {
    //     // not necessary
    // }

    private static class inputReader extends Thread
    {
        ObjectInputStream in;

        public inputReader(ObjectInputStream i)
        {
            in = i;
        }

        public void run()
        {
            while(true)
            {
                try
                {
                    String incomingMessage = (String)in.readObject();
                    System.out.println(incomingMessage);
                } catch (Exception e)
                {
                    System.err.println("Error at line 105: " + e);
                    break;
                }
                
            }
        }
    }
}