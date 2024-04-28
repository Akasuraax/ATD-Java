package com.example.atd.application;
import com.example.atd.LoginController;
import javafx.fxml.FXMLLoader;
import com.example.atd.ApiRequester;
import com.example.atd.Login;
import com.example.atd.SessionManager;
import com.example.atd.adapter.MessageTypeAdapter;
import com.example.atd.adapter.TicketTypeAdapter;
import com.example.atd.exception.ApiRequestException;
import com.example.atd.model.Message;
import com.example.atd.model.Ticket;
import com.google.gson.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportTicket extends Application {

    private ListView<Ticket> ticketListView;
    private Label ticketDetailsLabel;
    private ObservableList<Ticket> tickets = FXCollections.observableArrayList();
    private TextField messageInputField;
    private Button sendMessageButton;
    private VBox messageContainer;
    private SplitPane splitPane;
    private int selectedTicketId;
    private ScrollPane messageScrollPane; // Declare messageScrollPane as a class member



    @Override
    public void start(Stage primaryStage) {
        initializeComponents();
        configureSplitPane();
        configureTicketListView();
        configureMessageInputFieldAndButton();

        BorderPane root = new BorderPane();
        root.setLeft(ticketListView);
        root.setCenter(splitPane);
        root.setRight(ticketDetailsLabel);

        // Créer le MenuBar et le MenuItem pour le bouton de déconnexion
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Fichier");
        MenuItem logoutMenuItem = new MenuItem("Déconnexion");
        menu.getItems().add(logoutMenuItem);
        menuBar.getMenus().add(menu);

        // Dans votre gestionnaire d'événements pour le bouton de déconnexion ou une autre action
        logoutMenuItem.setOnAction(event -> {
            // Fermez la scène principale ou changez la scène pour la page de connexion
            primaryStage.close();
            // Ouvrez la scène de connexion ici
            try {
                Login.changeToLoginScene();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Ajouter le MenuBar à la scène
        VBox topContainer = new VBox(menuBar);
        root.setTop(topContainer);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Gestion des Tickets");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeComponents() {
        ticketListView = new ListView<>();
        ticketDetailsLabel = new Label(); // Utilisé pour afficher les détails du ticket
        messageInputField = new TextField(); // Utilisé pour l'input du message
        sendMessageButton = new Button("Envoyer");
        messageContainer = new VBox();

        messageScrollPane = new ScrollPane();
        messageContainer = new VBox();
        messageScrollPane.setContent(messageContainer);
        messageScrollPane.setFitToWidth(true);
        messageScrollPane.setFitToHeight(true);
    }

    private void configureSplitPane() {
        splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);

        // Utilisez messageScrollPane pour envelopper messageContainer
        VBox messageContainerVBox = new VBox();
        messageContainerVBox.getChildren().add(messageScrollPane); // Utilisez messageScrollPane ici
        VBox.setVgrow(messageScrollPane, Priority.ALWAYS);

        // Assurez-vous que inputContainer est correctement initialisé
        HBox inputContainer = new HBox();
        inputContainer.getChildren().addAll(messageInputField, sendMessageButton);
        HBox.setHgrow(messageInputField, Priority.ALWAYS);

        // Ajoutez messageContainerVBox et inputContainer à splitPane
        splitPane.getItems().addAll(messageContainerVBox, inputContainer);

        // Définissez les positions des diviseurs
        splitPane.setDividerPositions(1.0, 0.0);
    }




    private void configureTicketListView() {
        ticketListView.setItems(tickets);
        ticketListView.setCellFactory(param -> new TicketCellSupport());
        int userId = SessionManager.getInstance().getUser().getUserDetails().getId();
        String endpoint = "user/" + userId + "/tickets";
        tickets.addAll(getTickets(endpoint));

        ticketDetailsLabel.setStyle("-fx-padding: 10px;");

        ticketListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTicketId = newSelection.getId();
                String ticketDetails = "Titre: " + newSelection.getTitle() + "\nDétails: " + newSelection.getDescription() + "\n";
                ticketDetailsLabel.setText(ticketDetails);

                Platform.runLater(() -> {
                    // Effacer les messages actuels avant d'ajouter les nouveaux
                    messageContainer.getChildren().clear();

                    ObservableList<Message> ticketMessages = getMessages(newSelection.getId());
                    if (!ticketMessages.isEmpty()) {
                        // Ajoutez chaque message à messageContainer
                        for (Message message : ticketMessages) {
                            // Vous pouvez ajuster cette logique pour déterminer le style et le texte en fonction du type de message
                            String messageText = message.getDescription(); // Assurez-vous que Message a une méthode getContent()
                            addMessageToContainer(messageText, message.getIdUser(),"#101010");
                        }
                    }
                });
            }
        });
    }

    private void configureMessageInputFieldAndButton() {
        sendMessageButton.setOnAction(event -> {
            String messageContent = messageInputField.getText();
            if (!messageContent.isEmpty()) {
                sendMessage(messageContent);
                addMessageToContainer(messageContent,SessionManager.getInstance().getUser().getUserDetails().getId(), "#101010");
                messageInputField.clear();
            }
        });
    }

    private void sendMessage(String message) {
        Map<String, String> data = new HashMap<>();
        data.put("description", message);
        try {
            HttpResponse<String> response = ApiRequester.postRequest("ticket/" + selectedTicketId, data);
        } catch(ApiRequestException e) {
            handleApiRequestException(e);
        }
    }

    private void addMessageToContainer(String messageText, int userId, String textColor) {
        // Créez un VBox pour contenir le message
        VBox messageBox = new VBox();

        // Déterminez l'alignement en fonction de l'ID de l'utilisateur
        Pos alignment = Pos.CENTER_LEFT; // Alignement par défaut à gauche
        String backgroundColor;
        if (userId == SessionManager.getInstance().getUser().getUserDetails().getId()) {
            alignment = Pos.CENTER_RIGHT; // Alignement à droite si l'ID de l'utilisateur correspond
            backgroundColor = "#548FEB";
        } else {
            backgroundColor = "#F0F1F1";
        }
        messageBox.setAlignment(alignment);

        // Définissez le style du VBox pour avoir un padding
        messageBox.setStyle("-fx-padding: 8px;");

        // Créez un Label pour le texte du message
        Label messageLabel = new Label(messageText);

        // Définissez le style du Label pour avoir un fond, un alignement adapté, et des coins arrondis
        messageLabel.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-padding: 8px; -fx-border-color: #969696; -fx-border-width: 1px;");

        // Ajoutez le Label au VBox
        messageBox.getChildren().add(messageLabel);
        Platform.runLater(() -> {
            messageScrollPane.setVvalue(1.0); // Scroll to the bottom
        });

        // Ajoutez le VBox à messageContainer
        messageContainer.getChildren().add(messageBox);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Ticket> getTickets(String endpoint) {
        try {
            HttpResponse<String> response = ApiRequester.getRequest(endpoint);
            List<Ticket> ticketList = parseTicketList(response.body());
            return FXCollections.observableArrayList(ticketList);
        } catch (ApiRequestException e) {
            handleApiRequestException(e);
        }
        return FXCollections.observableArrayList();
    }

    private List<Ticket> parseTicketList(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Ticket.class, new TicketTypeAdapter());
        Gson gson = gsonBuilder.create();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("tickets");

        List<Ticket> ticketList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            Ticket ticket = gson.fromJson(jsonElement, Ticket.class);
            ticketList.add(ticket);
        }

        return ticketList;
    }

    public ObservableList<Message> getMessages(int ticketId) {
        try {
            String endpoint = "ticket/" + ticketId + "/messages";
            HttpResponse<String> response = ApiRequester.getRequest(endpoint);

            List<Message> messageList = parseMessageList(response.body());
            return FXCollections.observableArrayList(messageList);
        } catch (ApiRequestException e) {
            handleApiRequestException(e);
        }
        return FXCollections.observableArrayList();
    }

    private List<Message> parseMessageList(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageTypeAdapter());
        Gson gson = gsonBuilder.create();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("messages");

        List<Message> messageList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            Message message = gson.fromJson(jsonElement, Message.class);
            messageList.add(message);
        }

        return messageList;
    }


    private void handleApiRequestException(ApiRequestException e) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de la récupération des données");
            alert.showAndWait();
        });
    }
}
