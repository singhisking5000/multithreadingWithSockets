package com.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Content extends JPanel implements KeyListener {
    // JFrame f = new JFrame("Client");
    // JPanel contentPanel = new JPanel();
    // inputField = new JTextField();
    // // InputMap inputMap = new InputMap();
    // contentPanel.setLayout(new GridLayout(1, 2));
    // JTextArea messageArea = new JTextArea();
    // contentPanel.add(messageArea);
    // contentPanel.add(inputField);

    public JTextField prompt = new JTextField();
    public JTextArea messages = new JTextArea();
    private ObjectOutputStream out;
    
    public Content(ObjectOutputStream o)
    {
        this.setLayout(new GridLayout(1,2));
        this.add(messages);
        this.add(prompt);
        this.out = o;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            String text = prompt.getText();
            try {
                out.writeObject(text);
            } catch (Exception a) {
                System.out.println("Error in enter key press: " + a);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    
}
