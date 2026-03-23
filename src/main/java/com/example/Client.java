
package com.example;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
    static JPanel contentPanel=new JPanel();
    static JTextField inputField=new JTextField();
    static JTextArea messageArea = new JTextArea();
    static String KEY_WORD = "disconnect";

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        System.out.println("Running main in client!");
        InetAddress host = InetAddress.getLocalHost();

 
        Socket socket = new Socket(host.getHostName(), 9876);

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        

        inputReader incoming = new inputReader(in);
        incoming.start();
        System.out.println("going to call createGUI");
        createGUI(out, socket, in, incoming);
    }

    private static void createGUI(ObjectOutputStream o, Socket s, ObjectInputStream i, inputReader reader) // creates the gui
    {
        System.out.println("Called createGUI");
        f = new JFrame("Client");
        f.setPreferredSize(new Dimension(600, 600));
        f.setSize(new Dimension(600, 600));
        contentPanel.setSize(new Dimension(600,600));
        contentPanel.setPreferredSize(new Dimension(600, 600));
        JTextArea messageHeader = new JTextArea("Chat: ");
        JTextArea promptHeader = new JTextArea("Insert a message: ");
        messageHeader.setEditable(false);
        promptHeader.setEditable(false);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;

        Dimension headers = new Dimension(600, 100);
        Dimension fields = new Dimension(600, 200);

        contentPanel.setLayout(layout);

        messageHeader.setPreferredSize(headers);
        messageHeader.setSize(headers);
        contentPanel.add(messageHeader, constraints);

        // (0,1) 1x2
        constraints.gridy = 1;
        constraints.gridheight = 2;
        // messageArea.setPreferredSize(fields);
        // messageArea.setSize(fields);
        constraints.weighty = 0.4;
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        contentPanel.add(scrollPane, constraints);

        // (0,3) 1x1
        constraints.gridy = 3;
        constraints.gridheight = 1;
        promptHeader.setPreferredSize(headers);
        promptHeader.setSize(headers);
        constraints.weighty = 0;
        contentPanel.add(promptHeader, constraints);

        constraints.gridy = 4;
        constraints.gridheight = 2;
        inputField.setPreferredSize(fields);
        inputField.setSize(fields);
        constraints.weighty = 0.4;
        contentPanel.add(inputField, constraints);
        messageArea.setEditable(false);

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
                            o.flush();
                            reader.interrupt();
                            i.close();
                            o.close();
                            s.close();
                            f.dispose();
                            System.out.println("Connection closed!");
                            System.exit(0);
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


        f.addWindowListener(new WindowListener() {
            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    o.writeObject("disconnect");
                    o.flush();
                    reader.interrupt();
                    i.close();
                    o.close();
                    s.close();
                    f.dispose();
                    System.out.println("Connection closed!");
                    System.exit(0);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowOpened(WindowEvent e) {
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;
        f.setLayout(layout);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(contentPanel, constraints);
        f.setVisible(true);
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
                    SwingUtilities.invokeLater(() -> {
                        System.out.println(incomingMessage);
                        messageArea.setText(messageArea.getText() + "\n" + incomingMessage);
                        messageArea.setCaretPosition(messageArea.getDocument().getLength());
                    });   
                } catch (Exception e)
                {
                    System.err.println("Error at line 105: " + e);
                    break;
                }
            }
        }
    }
}