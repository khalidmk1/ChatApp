package com.khalid.chatapp.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChatUI extends Application {

    private ChatClient client;
    private TextArea messageArea;
    private TextField inputField;
    private Button sendButton;

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    @Override
    public void start(Stage stage) {
        // Message display area
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setFont(Font.font("Consolas", 14));
        messageArea.setStyle("-fx-control-inner-background: #1e1e1e; -fx-text-fill: #d4d4d4;");

        // ScrollPane for better scrolling experience
        ScrollPane scrollPane = new ScrollPane(messageArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Input field for typing message
        inputField = new TextField();
        inputField.setPromptText("Entrez votre message...");
        inputField.setFont(Font.font(14));
        inputField.setPrefWidth(300);
        inputField.setOnAction(e -> sendMessage());

        // Send button
        sendButton = new Button("Envoyer");
        sendButton.setDefaultButton(true);
        sendButton.setFont(Font.font(14));
        sendButton.setOnAction(e -> sendMessage());
        sendButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white;");

        // HBox for input + button
        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);

        // Root VBox with padding and spacing
        VBox root = new VBox(10, messageArea, inputBox);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #2d2d30;");

        Scene scene = new Scene(root, 500, 400);

        stage.setTitle("Chat Client");
        stage.setScene(scene);
        stage.show();

        connectToServer();
    }

    private void connectToServer() {
        try {
            client = new ChatClient("localhost", 12345);
            client.receiveMessages(message -> {
                Platform.runLater(() -> {
                    messageArea.appendText(message + "\n");
                    // Auto-scroll to bottom
                    messageArea.setScrollTop(Double.MAX_VALUE);
                });
            });
        } catch (Exception e) {
            messageArea.setText("Erreur de connexion au serveur.");
        }
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            client.sendMessage(msg);
            inputField.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
