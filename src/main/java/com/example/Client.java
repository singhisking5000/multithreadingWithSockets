
package com.example;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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

    static JFrame f;
    static JPanel contentPanel;
    static JTextField inputField;
    static JTextArea messageArea;

    static String KEY_WORD = "disconnect";

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        System.out.println("Running main in client!");
        InetAddress host = InetAddress.getLocalHost();
        // Ignore the numbers, they are for testing the sequence
        System.out.println("1");
        Socket socket = new Socket(host.getHostName(), 9876);
        System.out.println("2");
        //write to socket using ObjectOutputStream
        // STOPS ON LINE 43 FOR SOME REASON?
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        System.out.println("3");
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("4");
        

        // keep reading the input stream
        // when we hit enter, we want to output our text
        //make a guid here
        //put the writing stuff (below) to attach to an action listener or a text box

        System.out.println("going to call createGUI");
        createGUI(out, socket);
        inputReader incoming = new inputReader(in);
        incoming.start();
    }

    private static void createGUI(ObjectOutputStream o, Socket s) // creates the gui
    {
        System.out.println("Called createGUI");
        f = new JFrame("Client");
        f.setPreferredSize(new Dimension(300,300));
        f.setSize(new Dimension(300,300));
        contentPanel = new JPanel();
        inputField = new JTextField();
        messageArea = new JTextArea();
        contentPanel.setLayout(new GridLayout(2, 1));
        contentPanel.add(messageArea);
        contentPanel.add(inputField);

        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("Enter was pressed!");
                    try {
                        String prompt = inputField.getText();
                        if(prompt.equals(KEY_WORD))
                        {
                            o.writeObject("disconnect");
                            s.shutdownOutput();
                            System.out.println("Connection closed!");
                        } else if (!prompt.equals("")) {
                            o.writeObject(inputField.getText());
                            o.flush();
                            inputField.setText("");
                        }
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
            }

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
            }
        });

        f.setLayout(new GridLayout());
        f.add(contentPanel);
        f.setVisible(true);;
    }

    private static class inputReader extends Thread
    {
        ObjectInputStream incomingMessageStream;

        public inputReader(ObjectInputStream i) {
            incomingMessageStream = i;
        }

        public void run() {
            while(true)
            {
                try
                {
                    String incomingMessage = (String) incomingMessageStream.readObject();
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