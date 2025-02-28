package com.chatapp;

import javax.swing.*;
import java.awt.*;

public class MessageDisplay {
    
    private JFrame displayFrame;
    private JTextArea displayArea;

    public MessageDisplay() {
        // Create the display frame
        displayFrame = new JFrame("Message Display");
        displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayFrame.setSize(400, 400);
        displayFrame.setLayout(new BorderLayout());

        // Create the text area for displaying messages
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayFrame.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Make the frame visible
        displayFrame.setVisible(true);
    }

    public void appendMessage(String message) {
        displayArea.append(message + "\n");
    }
}