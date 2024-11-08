package eu.example.src.controller;

import eu.example.src.domain.Friendship;
import eu.example.src.domain.Utilizator;
import eu.example.src.services.FriendshipService;
import eu.example.src.services.UtilizatorService;

import eu.example.src.ui.ErrorPopup;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


import java.util.Iterator;
import java.util.Optional;

public class FriendsController {

    public Label Prietenii;
    private FriendshipService friendshipService;
    private UtilizatorService utilizatorService;
    private Long id;

    @FXML
    private Button backButton;

    @FXML
    private ListView<String> friendsList;

    public void setupController(){
        initialize();
    }

    @FXML
    public void initialize(){



    }

    public void setUtilizatorService(UtilizatorService UtilizatorService) {
        this.utilizatorService = UtilizatorService;
    }

    public void setFriendshipService(FriendshipService friends) {
        this.friendshipService = friends;
        friendshipService.addChangeListener(this::initModel);
        System.out.println("Am ajuns aici");
        initModel();
    }

    private void selectFriendsRequestSent(String stat){
        friendsList.getItems().clear();
        Iterable<Friendship> friendss = this.friendshipService.findAll();
        System.out.println(id);
        Iterator<Friendship> it = friendss.iterator();
        System.out.println("Am ajuns aici");
        while (it.hasNext()) {
            System.out.println("Am ajuns aici");
            Friendship f = it.next();
            if (f.getSender().equals(id) && f.getStatus().equals(stat)) {
                Optional<Utilizator> u = utilizatorService.findOne(f.getReceiver());
                u.ifPresent(value ->{
                    friendsList.getItems().add(value.getUsername().toString() + " " + value.getFirstName().toString() + " " + value.getLastName().toString());
                });
            }
        }

        friendsList.refresh();
    }


    private void selectFriendsRequestReceived(String stat){
        friendsList.getItems().clear();
        Iterable<Friendship> friendss = this.friendshipService.findAll();
        System.out.println(id);
        Iterator<Friendship> it = friendss.iterator();
        System.out.println("Am ajuns aici");
        while (it.hasNext()) {
            System.out.println("Am ajuns aici");
            Friendship f = it.next();
            System.out.println(f.getId());
            if (f.getReceiver().equals(id) && f.getStatus().equals(stat)) {
                System.out.println(f.getId().getFirst());
                Optional<Utilizator> u = utilizatorService.findOne(f.getSender());
                u.ifPresent(value ->{
                    friendsList.getItems().add(value.getUsername().toString() + " " + value.getFirstName().toString() + " " + value.getLastName().toString());
                });
            }
        }

        friendsList.refresh();
    }

    @FXML
    private void initModel(){
        friendsList.getItems().clear();
        Iterable<Friendship> friendss = this.friendshipService.findAll();
        System.out.println(id);
        Iterator<Friendship> it = friendss.iterator();
        System.out.println("Am ajuns aici");
        while (it.hasNext()) {
            System.out.println("Am ajuns aici");
            Friendship f = it.next();
            if (f.getId().getFirst().equals(id) && f.getStatus()=="accepted") {
                System.out.println(f.getId().getSecond());
                Optional<Utilizator> u = utilizatorService.findOne(f.getId().getSecond());
                u.ifPresent(value ->{
                    friendsList.getItems().add(value.getUsername().toString() + " " + value.getFirstName().toString() + " " + value.getLastName().toString());
                });
            } else if (f.getId().getSecond().equals(id) && f.getStatus()=="accepted") {
                System.out.println(f.getId().getFirst());
                Optional<Utilizator> u = utilizatorService.findOne(f.getId().getFirst());
                u.ifPresent(value ->{
                    friendsList.getItems().add(value.getUsername().toString() + " " + value.getFirstName().toString() + " " + value.getLastName().toString());
                });
            }

        }

        friendsList.refresh();
    }

    @FXML
    public void handleBack(){
        //user page controller
        System.out.println("Back");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/example/fxml/userHomePage.fxml"));
            System.out.println("Back");
            Parent root = loader.load();
            UserHomePageController controller = loader.getController();
            controller.setUtilizatorService(utilizatorService);
            controller.setFriendshipService(friendshipService);
            controller.setUtilizator(utilizatorService.findOne(id).get());

            System.out.println("Back");
            Stage stage = (Stage) friendsList.getScene().getWindow();
            System.out.println("Back");
            stage.setScene(new Scene(root, 1000, 1000));
            System.out.println("Back");
        }catch (Exception e){
            ErrorPopup.showError("Eroare", "Nu s-a putut încărca pagina de login: " + e.getMessage());
        }
    }

    @FXML
    public void handleFriendshipRequests(){
        selectFriendsRequestReceived("pending");
    }

    @FXML
    public void handleRequestsSent(){
        selectFriendsRequestSent("pending");
    }

    public void setId(Long id) {
        this.id = id;
    }
}
