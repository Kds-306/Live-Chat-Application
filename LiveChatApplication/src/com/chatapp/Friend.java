package com.chatapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Friend {
    
    private JFrame frame;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket; // Declare the socket here
    private JTextField textField; // Text field for user input
    private JButton sendButton; // Button to send messages
    private JTextPane textPane; // Text pane to display messages

    public Friend() {
        // Create the frame
        frame = new JFrame("Chat Application - User 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(433, 405);
        frame.getContentPane().setLayout(new BorderLayout());

        // Create the text pane for displaying messages
        textPane = new JTextPane();
        textPane.setEditable(false); // Make the text pane non-editable
        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create the text field for user input
        textField = new JTextField();
        frame.getContentPane().add(textField, BorderLayout.SOUTH);

        // Create the send button
        sendButton = new JButton("Send");
        frame.getContentPane().add(sendButton, BorderLayout.EAST);

        // Add action listener to the send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Add action listener to the text field for Enter key
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Connect to the server
        connectToServer();

        // Make the frame visible
        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345); // Initialize the socket here
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Start a thread to listen for incoming messages
            new Thread(new IncomingMessageHandler()).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error connecting to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit the application if unable to connect
        }
    }

    private void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) {
            out.println(message); // Send message to the server
            textField.setText(""); // Clear the text field
            appendMessage("You: " + message); // Display sent message
        }
    }

    private void appendMessage(String message) {
        // Append message to the JText Pane
        textPane.setText(textPane.getText() + message + "\n");
        textPane.setCaretPosition(textPane.getDocument().getLength()); // Scroll to the bottom
    }

    private class IncomingMessageHandler implements Runnable {
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    appendMessage("Friend: " + message); // Use appendMessage method to display incoming messages
                }
            } catch (IOException e) {
                System.err.println("Error reading from server: " + e.getMessage());
            } finally {
                // Close resources in the finally block
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close(); // Close the socket here
                } catch (IOException e) {
                    System.err.println("Error closing streams: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Friend());
    }
}