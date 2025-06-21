package com.khalid.chatapp.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public  class ChatUI extends Application {

    private ChatClient client;
    private TextArea messageArea;
    private TextField inputField;

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    @Override
    public void start(Stage stage) {
        messageArea = new TextArea();
        messageArea.setEditable(false);

        inputField = new TextField();
        inputField.setPromptText("Entrez votre message...");
        inputField.setOnAction(e -> sendMessage());

        VBox root = new VBox(10, messageArea, inputField);
        root.setPrefSize(400, 300);

        stage.setTitle("Client Chat");
        stage.setScene(new Scene(root));
        stage.show();

        connectToServer();
    }

    private void connectToServer() {
        try {
            client = new ChatClient("localhost", 12345);
            client.receiveMessages(message -> {
                Platform.runLater(() -> messageArea.appendText(message + "\n"));
            });
        } catch (Exception e) {
            messageArea.setText("Erreur de connexion au serveur.");
        }
    }

    private void sendMessage() {
        String msg = inputField.getText();
        if (!msg.isEmpty()) {
            client.sendMessage(msg);
            inputField.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
