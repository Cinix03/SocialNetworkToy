package eu.example.src.controller;

import eu.example.src.domain.Utilizator;
import eu.example.src.services.FriendshipService;
import eu.example.src.services.MessagesService;
import eu.example.src.services.UtilizatorService;
import eu.example.src.ui.ErrorPopup;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class OtherProfileController {

    @FXML
    private Label friendsCountLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label numeUser;

    @FXML
    private Button logout;

    @FXML
    private Button findPeople;

     Utilizator utilizator;
     Utilizator utilizatorCurent;
     UtilizatorService utilizatorService;
     FriendshipService friendshipService;
     MessagesService messagesService;

     public OtherProfileController() {

     }

     public void initialize(){
         Circle c1 = new Circle(150, 150, 150);
         profileImageView.setClip(c1);
     }

     public void setUtilizatorCurent(Utilizator utilizator) {
         this.utilizatorCurent = utilizator;
         numeUser.setText(utilizator.getUsername());
         String filePath = "./untitled/src/main/resources/userPhotos/" + utilizatorCurent.getUsername() + ".jpg";
         System.out.println(filePath);
         File file = new File(filePath);
         if(file.exists() && file.isFile()){
             System.out.println(1);
             setProfileImage(filePath);
         }
     }

     public void setUtilizator(Utilizator utilizator) {
         this.utilizator = utilizator;
     }

     public void setUtilizatorService(UtilizatorService utilizatorService) {
         this.utilizatorService = utilizatorService;
         System.out.println(utilizatorCurent.getId());
         friendsCountLabel.setText("     " + friendshipService.numberOfFriends(utilizatorCurent.getId()) + "\nFriends");
     }

     public void setMessagesService(MessagesService messagesService) {
         this.messagesService = messagesService;
     }

     public void setFriendshipService(FriendshipService friendshipService) {
         this.friendshipService = friendshipService;
     }

     private void setProfileImage(String filePath) {
         File imageFile = new File(filePath);
         Image profileImage = new Image(imageFile.toURI().toString());
         profileImageView.setImage(profileImage);
     }

    @FXML
    public void handleFindPeople(){
        try {
            URL fxmlLocation = getClass().getResource("/eu/example/fxml/userHomePage.fxml");
            if(fxmlLocation == null)
            {
                System.out.println("Fișierul FXML nu a fost găsit! Verifică locația.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            UserHomePageController userHomePageController = loader.getController();
            userHomePageController.setUtilizatorService(utilizatorService);
            userHomePageController.setFriendshipService(friendshipService);
            userHomePageController.setUtilizator(utilizator);
            userHomePageController.setMessagesService(messagesService);

            Stage stage = (Stage) findPeople.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 1000));

        } catch (Exception e) {
            ErrorPopup.showError("Eroare", e.getMessage());
        }
    }

    @FXML
    private void openChatWindow() {
        try {
            if (messagesService == null) {
                throw new Exception("Service-ul de mesaje nu este disponibil.");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/example/fxml/chatWindow.fxml"));
            Parent root = loader.load();

            // Obține controllerul ferestrei de chat
            ChatController chatController = loader.getController();
            chatController.setMessagesService(messagesService);
            Long idNou = utilizatorService.findByUsername(utilizatorCurent.getUsername()).getId();
            chatController.setCurrentUserId(utilizator.getId());
            chatController.setChatParticipant(utilizatorCurent.getUsername(), idNou);

            Stage chatStage = new Stage();
            chatStage.setTitle("Chat cu " + utilizatorCurent.getUsername());
            chatStage.setScene(new Scene(root));
            chatStage.show();
        } catch (Exception e) {
            ErrorPopup.showError("Eroare", "Nu s-a putut deschide fereastra de chat: " + e.getMessage());
        }
    }
}
