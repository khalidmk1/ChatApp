package com.khalid.chatapp.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final int PORT = 12345;
    static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientThread = new ClientHandler(socket);
                clients.add(clientThread);
                clientThread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour diffuser un message à tous les clients
    public static void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}
