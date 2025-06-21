package com.khalid.chatapp.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void receiveMessages(ChatUI.MessageListener listener) {
        new Thread(() -> {
            String msg;
            try {
                while ((msg = in.readLine()) != null) {
                    listener.onMessageReceived(msg);
                }
            } catch (IOException e) {
                listener.onMessageReceived("Connexion terminée.");
            }
        }).start();
    }
}
