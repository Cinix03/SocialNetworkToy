package eu.example.src.controller;

import eu.example.src.domain.Utilizator;
import eu.example.src.services.FriendshipService;
import eu.example.src.services.MessagesService;
import eu.example.src.services.Service;
import eu.example.src.services.UtilizatorService;
import eu.example.src.ui.ErrorPopup;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;

import static eu.example.src.Utils.PasswordUtils.checkPassword;
import static eu.example.src.Utils.PasswordUtils.hashPassword;


public class LoginController {

    private UtilizatorService utilizatorService;
    private FriendshipService friendshipService;
    private MessagesService messagesService;

    @FXML
    private Button showLogin;

    @FXML
    private Button showRegister;

    @FXML
    private Button login;

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private Button signUp;

    public LoginController() {

    }

    @FXML
    public void initialize() {
        username.setVisible(true);
        password.setVisible(true);
        name.setVisible(false);
        surname.setVisible(false);
        showLogin.setVisible(false);
        showRegister.setVisible(true);
        login.setVisible(true);
        signUp.setVisible(false);
    }

    public void setUtilizatorService(UtilizatorService UtilizatorService) {
        this.utilizatorService = UtilizatorService;
    }

    public void setFriendshipService(FriendshipService FriendshipService) {
        this.friendshipService = FriendshipService;
    }

    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }



    @FXML
    public void handleLogin() {
        try {
            Utilizator utilizator = utilizatorService.findByUsernameDB(username.getText()).get();
            if(checkPassword(password.getText(), utilizator.getPassword()) == false)
                throw new RuntimeException("Parola gresita");
            System.out.println("Logat cu succes");
            URL fxmlLocation = getClass().getResource("/eu/example/fxml/userHomePage.fxml");
            if (fxmlLocation == null) {
                System.out.println("Fișierul FXML nu a fost găsit! Verifică locația.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            UserHomePageController userHomePageController = loader.getController();
            userHomePageController.setUtilizatorService(utilizatorService);
            userHomePageController.setFriendshipService(friendshipService);
            userHomePageController.setMessagesService(messagesService);
            System.out.println("In login userul are: " + utilizator.getId());
            userHomePageController.setUtilizator(utilizator);

            Stage stage = (Stage) login.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 1000));

        } catch (Exception e) {
            // fa un pop up window cu eroarea
            ErrorPopup.showError("Eroare", e.getMessage());
            username.setText("");
            password.setText("");
        }
    }

    @FXML
    public void handleShowRegister() {
        name.setVisible(true);
        surname.setVisible(true);
        showLogin.setVisible(true);
        showRegister.setVisible(false);
        login.setVisible(false);
        signUp.setVisible(true);
    }

    @FXML
    public void handleShowLogin(){
        name.setVisible(false);
        surname.setVisible(false);
        showLogin.setVisible(false);
        showRegister.setVisible(true);
        login.setVisible(true);
        signUp.setVisible(false);
    }

    @FXML
    public void handleSignUp() {
        try {
            Utilizator utilizator = new Utilizator(name.getText(), surname.getText(), username.getText(), hashPassword(password.getText()));
            utilizatorService.add(utilizator);
            name.setText("");
            surname.setText("");
            username.setText("");
            password.setText("");
        }catch (Exception e){
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}
