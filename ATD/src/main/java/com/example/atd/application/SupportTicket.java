package com.example.atd.application;
import com.example.atd.model.UserDetails;
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
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
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

        ticketListView.setId("completedView");
        splitPane.setId("split");
        // Créer le MenuBar et le MenuItem pour le bouton de déconnexion
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menuBar");
        Menu menu = new Menu("Compte");
        menu.setId("menu");
        MenuItem logoutMenuItem = new MenuItem("Déconnexion");
        logoutMenuItem.setId("item");
        menu.getItems().add(logoutMenuItem);
        menuBar.getMenus().add(menu);

        // Dans votre gestionnaire d'événements pour le bouton de déconnexion ou une autre action
        logoutMenuItem.setOnAction(event -> {
            // Fermez la scène principale ou changez la scène pour la page de connexion
            primaryStage.close();
            try {
                Login.changeToLoginScene();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Ajouter le MenuBar à la scène
        VBox topContainer = new VBox(menuBar);
        root.setTop(topContainer);

        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add(getClass().getResource("/css/styleTicket.css").toExternalForm());

        primaryStage.setTitle("Gestion des Tickets");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeComponents() {
        ticketListView = new ListView<>();
        ticketDetailsLabel = new Label();
        messageInputField = new TextField();
        messageInputField.setId("input");
        sendMessageButton = new Button("Envoyer");
        sendMessageButton.setId("sendMsg");
        messageContainer = new VBox();

        messageScrollPane = new ScrollPane();
        messageContainer = new VBox();
        messageScrollPane.setContent(messageContainer);
        messageScrollPane.setFitToWidth(true);
        messageScrollPane.setFitToHeight(true);

        // Initialiser les éléments comme désactivés
        messageInputField.setDisable(true);
        sendMessageButton.setDisable(true);
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
        String endpoint = "user/" + userId + "/tickets/support";
        tickets.addAll(getTickets(endpoint));

        ticketDetailsLabel.setStyle("-fx-padding: 10px;");

        ticketListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection!= null) {
                selectedTicketId = newSelection.getId();
                String ticketDetails = "Titre: " + newSelection.getTitle() + "\nDétails: " + newSelection.getDescription() + "\n";

                // Créer un VBox pour contenir le texte des détails et le bouton d'archivage
                VBox ticketDetailsContainer = new VBox();
                ticketDetailsContainer.setId("detailsContainer");
                ticketDetailsContainer.setSpacing(10); // Ajouter un espace entre le texte et le bouton
                ticketDetailsContainer.setPadding(new Insets(10));

                // Utiliser un Text pour le texte des détails et le définir comme défilable
                Text detailsText = new Text(ticketDetails);
                detailsText.setWrappingWidth(280);
                detailsText.setStyle("-fx-background-color: #ffffff; -fx-border-color: none");

                // Créer un ScrollPane pour le Text des détails
                ScrollPane detailsScrollPane = new ScrollPane(detailsText);
                detailsScrollPane.setFitToWidth(true);
                detailsScrollPane.setFitToHeight(true);
                detailsScrollPane.setMaxWidth(300);
                // Ajouter le ScrollPane au VBox
                ticketDetailsContainer.getChildren().add(detailsScrollPane);

                // Créer et ajouter le bouton d'archivage au VBox
                Button archiveButton = new Button("Archiver");
                archiveButton.setId("archiveBtn");
                archiveButton.setOnAction(event -> archiveTicket(newSelection));
                ticketDetailsContainer.getChildren().add(archiveButton);

                // Définir le VBox comme le graphique de ticketDetailsLabel
                ticketDetailsLabel.setGraphic(ticketDetailsContainer);

                // Charger les messages pour le ticket sélectionné
                loadMessagesForSelectedTicket(newSelection.getId());

                // Activer le champ d'entrée de message et le bouton d'envoi
                messageInputField.setDisable(false);
                sendMessageButton.setDisable(false);
            } else {
                // Désactiver le champ d'entrée de message et le bouton d'envoi
                messageInputField.setDisable(true);
                sendMessageButton.setDisable(true);
            }
        });
    }



    private void loadMessagesForSelectedTicket(int ticketId) {
        Platform.runLater(() -> {
            // Effacer les messages actuels avant d'ajouter les nouveaux
            messageContainer.getChildren().clear();

            ObservableList<Message> ticketMessages = getMessages(ticketId);
            if (!ticketMessages.isEmpty()) {
                for (Message message : ticketMessages) {
                    String messageText = message.getDescription();
                    addMessageToContainer(messageText, message.getIdUser(), message.getUserWhoSendTheMessage(), "#101010");
                }
            }
        });
    }

    private void configureMessageInputFieldAndButton() {
        sendMessageButton.setOnAction(event -> {
            String messageContent = messageInputField.getText();
            if (!messageContent.isEmpty()) {
                sendMessage(messageContent);
                addMessageToContainer(messageContent,SessionManager.getInstance().getUser().getUserDetails().getId(),SessionManager.getInstance().getUser().getUserDetails(), "#101010");
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

    private void addMessageToContainer(String messageText, int userId, UserDetails user, String textColor) {
        VBox messageBox = new VBox();

        Pos alignment = Pos.CENTER_LEFT;
        String backgroundColor;
        if (userId == SessionManager.getInstance().getUser().getUserDetails().getId()) {
            alignment = Pos.CENTER_RIGHT;
            backgroundColor = "#c3ddfd";
        } else {
            backgroundColor = "#80b1ed";
        }
        messageBox.setAlignment(alignment);

        messageBox.setStyle("-fx-padding: 8px;");

        VBox messageContent = new VBox();

        if (userId != SessionManager.getInstance().getUser().getUserDetails().getId()) {
            Label userNameLabel = new Label(user.getName());
            userNameLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px 0px 5px 0px;");
            messageContent.getChildren().add(userNameLabel);
        }else{
            Label userNameLabel = new Label("Vous");
            userNameLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px 0px 5px 0px;");
            messageContent.getChildren().add(userNameLabel);
        }

        // Créez un Label pour le texte du message
        Label messageLabel = new Label(messageText);

        // Ajoutez le Label du message au VBox
        messageContent.getChildren().add(messageLabel);

        // Créez un VBox supplémentaire pour contenir le nom de l'utilisateur et le texte du message
        VBox userAndMessageBox = new VBox();
        userAndMessageBox.getChildren().addAll(messageContent);
        userAndMessageBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-padding: 8px; -fx-border-width: 1px; -fx-border-radius: 25px; -fx-font-size: 20px"); // Ajoutez une bordure et un padding au VBox

        // Ajoutez le VBox supplémentaire au VBox principal
        messageBox.getChildren().add(userAndMessageBox);
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

    private void archiveTicket(Ticket ticket) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            String url = "ticket/" + ticket.getId();
            Map<String, String> data = getStringStringMap(ticket);

            HttpResponse<String> response = ApiRequester.patchRequest(url, data);

            // After successfully archiving the ticket, remove it from the list
            tickets.remove(ticket);

            // Check if the archived ticket is the last selected ticket
            if (ticket.getId() == selectedTicketId) {
                // Clear the ticket details and the discussion
                clearTicketDetailsAndDiscussion();
            }
        } catch (ApiRequestException e) {
            // Afficher un message d'erreur à l'utilisateur
            System.err.println("Erreur lors de la requête API : " + e.getMessage());
            // Pour une application JavaFX, vous pouvez utiliser un dialogue pour afficher l'erreur
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de connexion");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
        }
    }

    private void clearTicketDetailsAndDiscussion() {
        // Clear the ticket details
        ticketDetailsLabel.setGraphic(null);
        ticketDetailsLabel.setText(null);

        // Clear the discussion
        messageContainer.getChildren().clear();

        // Disable the message input field and the send message button
        messageInputField.setDisable(true);
        sendMessageButton.setDisable(true);
    }

    private static Map<String, String> getStringStringMap(Ticket ticket) {
        Map<String, String> data = new HashMap<>();

        data.put("id", String.valueOf(ticket.getId()));
        data.put("title", ticket.getTitle());
        data.put("description", ticket.getDescription());
        data.put("status", "2");
        data.put("severity", String.valueOf(ticket.getSeverity()));
        data.put("archive", String.valueOf(ticket.isArchive()));
        data.put("problem", ticket.getProblem());
        return data;
    }
}
