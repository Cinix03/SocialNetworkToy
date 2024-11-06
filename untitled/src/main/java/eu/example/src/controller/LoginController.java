package eu.example.src.controller;

import eu.example.src.domain.Utilizator;
import eu.example.src.services.FriendshipService;
import eu.example.src.services.Service;
import eu.example.src.services.UtilizatorService;
import eu.example.src.ui.ErrorPopup;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController {

    private UtilizatorService utilizatorService;
    private FriendshipService friendshipService;

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

    public void setUtilizatorService(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @FXML
    public void handleLogin() {
        try {
            Utilizator utilizator = utilizatorService.findByUsername(username.getText());
            if(utilizator.getPassword() != password.getText())
                throw new RuntimeException("Parola gresita");
            System.out.println("Logat cu succes");
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
            Utilizator utilizator = new Utilizator(name.getText(), surname.getText(), username.getText(), password.getText());
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
