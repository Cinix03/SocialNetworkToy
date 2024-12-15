package eu.example.src.controller;


import eu.example.src.domain.Utilizator;
import eu.example.src.services.FriendshipService;
import eu.example.src.services.MessagesService;
import eu.example.src.services.UtilizatorService;
import eu.example.src.ui.ErrorPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import eu.example.src.controller.UserHomePageController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ProfileController {
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label numeUser;
    @FXML
    private Button logout;
    Utilizator utilizator;
    UtilizatorService utilizatorService;
    FriendshipService friendshipService;
    MessagesService messagesService;
    public ProfileController(){

    }
    public void setUtilizator(Utilizator Utilizator){
        this.utilizator = Utilizator;
        numeUser.setText(utilizator.getUsername());
        String filePath = "./untitled/src/main/resources/userPhotos/" + utilizator.getUsername() + ".jpg";
        System.out.println(filePath);
        File file = new File(filePath);
        if(file.exists() && file.isFile()){
            setProfileImage(filePath);
        }
    }
    public void setUtilizatorService(UtilizatorService UtilizatorService){
        this.utilizatorService = UtilizatorService;
    }

    public void setFriendshipService(FriendshipService FriendshipService){
        this.friendshipService = FriendshipService;
    }

    public void setMessagesService(MessagesService MessagesService){
        this.messagesService = MessagesService;
    }


    @FXML
    public void handleFriends(){
        try {
            URL fxmlLocation = getClass().getResource("/eu/example/fxml/friends.fxml");
            if(fxmlLocation == null)
            {
                System.out.println("Fișierul FXML nu a fost găsit! Verifică locația.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            FriendsController friendsController = loader.getController();
            friendsController.setUtilizatorService(utilizatorService);
            friendsController.setId(utilizator.getId());
            friendsController.setFriendshipService(friendshipService);
            friendsController.setMessagesService(messagesService);

            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 1000));

        } catch (Exception e) {
            ErrorPopup.showError("Eroare", e.getMessage());
        }
    }

    public void handleLogout() {
        try {
            URL fxmlLocation = getClass().getResource("/eu/example/fxml/login.fxml");
            if (fxmlLocation == null) {
                System.out.println("Fișierul FXML nu a fost găsit! Verifică locația.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            loginController.setUtilizatorService(utilizatorService);
            loginController.setFriendshipService(friendshipService);

            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 1000));

        } catch (Exception e) {
            ErrorPopup.showError("Eroare", e.getMessage());
        }
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

            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 1000));

        } catch (Exception e) {
            ErrorPopup.showError("Eroare", e.getMessage());
        }
    }
    private void setProfileImage(String imagePath) {
        File imageFile = new File(imagePath);
            Image profileImage = new Image(imageFile.toURI().toString());
            profileImageView.setImage(profileImage);
    }

    @FXML
    private void handleSelectProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());

        if (selectedFile != null) {
            String username = utilizator.getUsername();
            String newFileName = username + ".jpg";
            File directory = new File("./untitled/src/main/resources/userPhotos");
            if (!directory.exists()) {
                directory.mkdirs(); // Creează dosarul dacă nu există
            }
            File newFile = new File(directory, newFileName);

            try {
                // Copiază imaginea selectată în noul fișier
                Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Salvează calea fișierului în baza de date
                utilizatorService.addImagePath(utilizator, newFile.getAbsolutePath());
                setProfileImage(newFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                // Gestionează eroarea corespunzător
            }
        }
    }

}
