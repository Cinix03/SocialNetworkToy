package eu.example.src.controller;

import eu.example.src.domain.Messages;
import eu.example.src.services.MessagesService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.*;
import java.sql.Timestamp;

public class ChatController {

    @FXML
    private VBox chatContainer;  // Acum este un VBox în loc de TextArea

    private MessagesService messagesService;

    @FXML
    private TextField messageInput;

    private String chatParticipant; // Numele utilizatorului cu care se deschide chatul
    private Long currentUserId;     // ID-ul utilizatorului curent
    private Long chatParticipantId; // ID-ul utilizatorului cu care se comunică

    public void setChatParticipant(String username, Long chatParticipantId) {
        this.chatParticipant = username;
        this.chatParticipantId = chatParticipantId;

        loadChatHistory();
    }

    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
        messagesService.addChangeListener(this::reloadChat);
    }

    public void reloadChat(Long id1, Long id2) {
        if (currentUserId == id1 && chatParticipantId == id2 || currentUserId == id2 && chatParticipantId == id1) {
            chatContainer.getChildren().clear(); // Curăță vechile mesaje
            loadChatHistory();
        }
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    private void loadChatHistory() {
        // Preia mesajele dintre utilizatorul curent și participant
        Iterable<Messages> messages = messagesService.getMessagesBetween(currentUserId, chatParticipantId);

        // Adaugă mesajele în `chatContainer`
        for (Messages message : messages) {
            HBox messageBox = new HBox(10);
            Text messageText = new Text();
            String sender = message.getFrom().equals(currentUserId) ? "Tu: " : chatParticipant + ": ";
            messageText.setText(sender + message.getMessage());
            messageBox.getChildren().add(messageText);

            // Adăugăm stiluri pentru a plasa mesajele la stânga/dreapta
            if (message.getFrom().equals(currentUserId)) {
                messageBox.setAlignment(Pos.CENTER_RIGHT);
                messageBox.setStyle("-fx-background-color: #A8E6A3; -fx-padding: 10px; -fx-background-radius: 10px;");
            } else {
                messageBox.setAlignment(Pos.CENTER_LEFT);
                messageBox.setStyle("-fx-background-color: #A3C4F4; -fx-padding: 10px; -fx-background-radius: 10px;"); // Albastru pentru mesajul primit
            }

            chatContainer.getChildren().add(messageBox);  // Adaugă mesajul în VBox
        }
    }

    @FXML
    public void handleSendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            // Crează un HBox pentru mesajul trimis
            HBox messageBox = new HBox(10);
            Text messageText = new Text("Tu: " + message);
            messageBox.getChildren().add(messageText);
            messageBox.setAlignment(Pos.CENTER_RIGHT);  // Mesajele trimise vor fi aliniate la dreapta
            messageBox.setStyle("-fx-background-color: #A8E6A3; -fx-padding: 10px; -fx-background-radius: 10px;");

            // Adaugă mesajul în chatContainer
            chatContainer.getChildren().add(messageBox);

            // Golește TextField-ul pentru mesajul următor
            messageInput.clear();

            try {
                // Trimite mesajul
                Messages newMessage = new Messages(currentUserId, chatParticipantId, message, new Timestamp(System.currentTimeMillis()));
                messagesService.add(newMessage);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
