import databaseHandler.SearchManager;
import databaseHandler.UserManager;
import elasticHandler.ElasticSearch;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Inzerat;
import model.Statistic;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {

    @FXML //  fx:id="searchButton"
    private Button searchButton; // Value injected by FXMLLoader
    @FXML
    private Button showInzeratButton;
    @FXML
    private Button searchAdvancedButton;
    @FXML
    private Button deleteAdButton;
    @FXML
    private Button removeFavoriteButton;
    @FXML
    private RadioButton buttonBuy;
    @FXML
    private RadioButton buttonSell;
    @FXML
    private CheckBox checkBoxBalcony;
    @FXML
    private CheckBox elasticCheckbox;
    @FXML
    private Hyperlink advancedSearch;
    @FXML
    private Hyperlink addAd;
    @FXML
    private Hyperlink loginLink;
    @FXML
    private Hyperlink registrateLink;
    @FXML
    private Hyperlink loadProfileLink;
    @FXML
    private Hyperlink addFavoritesLink;
    @FXML
    private Label menoLink;
    @FXML
    private Label priezviskoLink;
    @FXML
    private Label emailLink;
    @FXML
    private Label cisloLink;
    @FXML
    private DialogPane dialogLogin;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ComboBox typeBox;
    @FXML
    private ComboBox cityBox;
    @FXML
    private TextField searchField;
    @FXML
    private TextField minPrice;
    @FXML
    private TextField maxPrice;
    @FXML
    private TextField maxDistance;
    @FXML
    private TextField showField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField removeMyField;
    @FXML
    private TextField removeFavoriteField;
    @FXML
    private TextField keywordsField;
    @FXML
    private TextArea myAdsField;
    @FXML
    private TextArea infoField;
    @FXML
    private TextArea favoriteAdsField;
    @FXML
    private TextArea searchResultField;

    private Dialog dialog = new Dialog<>();


    public void showStatistics() throws SQLException {
        SearchManager sm = new SearchManager();
        Statistic statistics = sm.getStatistic().get(0);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(400, 200);
        alert.setTitle("Štatistika potálu");
        alert.setHeaderText(null);
        alert.setContentText(
                "\nPočet inzerátov na portáli: " + statistics.getTotal_ads() +
                        "\nPočet užívateľov na portáli: " + statistics.getTotal_users() +
                        "\nNajobľúbenejší inzerát: " + statistics.getFavorite_ad() +
                        "\n- Počet likov inzerátu: " + statistics.getAd_liked() +
                        "\nNajväčší inzerent: " + statistics.getBest_user() +
                        "\n- Počet inzerátov používateľa: " + statistics.getAds_created() +
                        "\n");
        alert.showAndWait();
    }

    public void validateAd() {
        SearchManager sm = new SearchManager();
        sm.updateAds(
                nameField.getText(), (String) typeBox.getSelectionModel().getSelectedItem().toString(),
                infoField.getText(), cityBox.getSelectionModel().getSelectedIndex(),
                Integer.parseInt(priceField.getText()), User.loggedUser.getId(), keywordsField.getText());
        //sm.updateAds(User.loggedUser.getId(),sm.searchMyAds(User.loggedUser.getId()).get);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pridanie inzerátu");
        alert.setHeaderText(null);
        alert.setContentText("Inzerát bol úspešne vložený do inzercie!");
        alert.showAndWait();
    }

    public void loadProfile() throws SQLException {
        addAd.setDisable(false);
        SearchManager sm = new SearchManager();
        menoLink.setText(menoLink.getText() + "  " + User.loggedUser.getName());
        priezviskoLink.setText(priezviskoLink.getText() + "  " + User.loggedUser.getSurname());
        emailLink.setText(emailLink.getText() + "  " + User.loggedUser.getEmail());
        cisloLink.setText(cisloLink.getText() + "  " + User.loggedUser.getPhone());
        myAdsField.clear();
        favoriteAdsField.clear();

        for (Inzerat inzerat : sm.searchMyAds(User.loggedUser.getId()))
            myAdsField.appendText(inzerat.getName() + " (ID:" + inzerat.getId() + ")\n" + "Adresa: " + inzerat.getContact() + "\n" + "Cena: " + inzerat.getPrice() + "€\n\n");

        for (Inzerat inzerat : sm.searchFavoriteAds(User.loggedUser.getId()))
            favoriteAdsField.appendText(inzerat.getName() + " (ID:" + inzerat.getId() + ")\n" + "Adresa: " + inzerat.getContact() + "\n" + "Cena: " + inzerat.getPrice() + "€\n\n");

    }


    public void showLogged() {
        System.out.print("logged");
        addAd.setDisable(false);
        addFavoritesLink.setDisable(false);
        loginLink.setText(User.loggedUser.getUsername());
        loginLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showProfile(e);
            }
        });
        registrateLink.setText("Odhlásiť");
        registrateLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                logout();
            }
        });
    }

    public void addFavorites() {
        searchResultField.setText("Inzerát pridaný medzi obľúbené položky");
        UserManager um = new UserManager();
        um.updateFavorites(Integer.parseInt(showField.getText()), User.loggedUser.getId());
    }

    public void logout() {
        User.loggedUser = null;
        addAd.setDisable(true);
        addFavoritesLink.setDisable(true);
        loginLink.setText("Prihlásiť sa");
        loginLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    showLogin(e, null);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        registrateLink.setText("Registrácia");
        registrateLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                showRegistration(e);
            }
        });
    }

    public void jumpShowLogged() {
        if (User.loggedUser != null)
            showLogged();
    }


    public void showFieldChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.matches("\\d*")) {
            int value = Integer.parseInt(newValue);
        } else {
            showField.setText(oldValue);
        }
    }


    public void removeFavoriteAd() throws SQLException {
        SearchManager sm = new SearchManager();
        UserManager um = new UserManager();
        Inzerat inzerat = sm.searchInzeratID(Integer.parseInt(removeFavoriteField.getText())).get(0);
        List<String> users = null;

        if (inzerat == null)
            favoriteAdsField.setText("Inzerát s týmto ID neexistuje!");
        else {
            users = um.searchFavoriteUser(inzerat.getId());
            for (String username : users) {
                if (username.trim().contains(User.loggedUser.getUsername())) {
                    //sm.dropFavoriteInzerat(inzerat.getId());
                    favoriteAdsField.setText("Inzerát bol odobraný zo zoznamu obľúbených položiek.");
                    return;
                }
            }
        }
        favoriteAdsField.setText("Inzerát s týmto ID sa nenachádza v zozname obľúbených položiek!");
    }

    public void editProfile(ActionEvent event) throws IOException {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Editácia profilu");
        dialog.setHeaderText("Zadajte nové meno a heslo");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Zmeniť", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Zrušiť", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Meno");
        PasswordField password = new PasswordField();
        password.setPromptText("Heslo");

        grid.add(new Label("Meno:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Heslo:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
                    UserManager um = new UserManager();
                    um.updateProfile(User.loggedUser.getUsername(), usernamePassword.getKey(), usernamePassword.getValue());
                    ;
                }
        );
    }

    public void deleteMyAd() {

    }

    public void showInzerat() throws SQLException {
        SearchManager sm = new SearchManager();
        searchResultField.setVisible(true);
        searchResultField.clear();
        Inzerat result = null;
        List<Inzerat> inzerat = null;

        inzerat = sm.searchInzeratID(Integer.parseInt(showField.getText()));
        if (inzerat.size() != 0) {
            result = inzerat.get(0);
            searchResultField.appendText(result.getName() + "\n\n" + result.getInfo() + "\n\n" + "Cena: "
                    + result.getPrice() + "€\nAdresa: " + result.getAdress() + "\nKontakt: " + result.getContact() + "\n\n");
        } else
            searchResultField.setText("ID inzerátu neexistuje!");

    }

    public void search() throws SQLException, UnknownHostException {

        SearchManager sm = new SearchManager();
        searchResultField.setVisible(true);
        searchResultField.clear();
        boolean result = false;
        String string = searchField.getText();
        ElasticSearch elastak = new ElasticSearch();
        Integer id = -1;

        //use elasticsearch
        if (elasticCheckbox.isSelected()) {
           if(searchField.getText().equals("")){
               try {
                   for (Inzerat inzerat : elastak.simpleSearch()){
                       result = true;
                       if(id != inzerat.getId())
                           searchResultField.appendText(inzerat.getName() + " (ID:" + inzerat.getId()
                                   + ")\n" + inzerat.getAdress() + "\n" + "Cena: " + inzerat.getCena() + "€\n\n");
                       id = inzerat.getId();

                   }
               } catch (IOException e) {
                   e.printStackTrace();
                   searchResultField.setText("Vyskytla sa chyba vyhľadávania!");
               }
           } else {
               try {
                   for (Inzerat inzerat : elastak.simpleSearch(searchField.getText())) {
                       result = true;
                       if(id != inzerat.getId())
                           searchResultField.appendText(inzerat.getName() + " (ID:" + inzerat.getId()
                               + ")\n" + inzerat.getAdress() + "\n" + "Cena: " + inzerat.getCena() + "€\n\n");
                       id = inzerat.getId();

                   }
               } catch (IOException e) {
                   e.printStackTrace();
                   searchResultField.setText("Vyskytla sa chyba vyhľadávania!");
               }
           }
        } else { //use postgresql
            for (Inzerat inzerat : sm.searchInzeratSimple(string)) {
                result = true;
                searchResultField.appendText(inzerat.getName() + " (ID:" + inzerat.getId()
                        + ")\n" + inzerat.getAdress() + "\n" + "Cena: " + inzerat.getPrice() + "€\n\n");
            }
        }
        if (!result)
            searchResultField.setText("Ľutujeme, kritériam vyhľadávania nevyhovuje žiaden inzerát");
    }



    public void searchAdvanced() throws SQLException, UnknownHostException {
        SearchManager sm = new SearchManager();
        searchResultField.clear();
        boolean result = false;
        ElasticSearch elastak = new ElasticSearch();

        Integer id = -1;
        Integer min = minPrice.getText().trim().isEmpty() ? 0 : Integer.parseInt(minPrice.getText());
        Integer max = maxPrice.getText().trim().isEmpty() ? 100000000 : Integer.parseInt(maxPrice.getText());
        Integer distance = maxDistance.getText().trim().isEmpty() ? 10000 : Integer.parseInt(maxDistance.getText());
        if (elasticCheckbox.isSelected()) {
            if (searchField.getText().equals("")) {
                try {
                    for (Inzerat inzerat : elastak.advancedSearch((String) typeBox.getSelectionModel().
                            getSelectedItem().toString(), (String) cityBox.getSelectionModel().getSelectedItem().toString(), min, max, distance)) {
                        result = true;
                        if(id != inzerat.getId())
                            searchResultField.appendText(inzerat.getName() + " (ID:" + inzerat.getId() + ")\n" + inzerat.getAdress() + "\n"
                                    + inzerat.getInfo() + "\n" + "Cena: " + inzerat.getCena() + "€\n\n");
                        id = inzerat.getId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    for (Inzerat inzerat : elastak.advancedSearch(searchField.getText(), (String) typeBox.getSelectionModel().
                            getSelectedItem().toString(), (String) cityBox.getSelectionModel().getSelectedItem().toString(), min, max, distance)) {
                        result = true;
                        searchResultField.appendText(inzerat.getName() + " (ID:" + inzerat.getId() + ")\n" + inzerat.getAdress() + "\n"
                                + inzerat.getInfo() + "\n" + "Cena: " + inzerat.getCena() + "€\n\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            try {
                for (Inzerat inzerat : sm.searchInzeratAdvanced(searchField.getText(), (String) typeBox.getSelectionModel().
                        getSelectedItem().toString(), (String) cityBox.getSelectionModel().getSelectedItem().toString(), min, max, distance)) {
                    result = true;
                    searchResultField.appendText(inzerat.getName() + " (ID:" + inzerat.getId() + ")\n"
                            + inzerat.getInfo() + "\n" + "Cena: " + inzerat.getPrice() + "€\n\n");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (!result)
            searchResultField.setText("Ľutujeme, kritériam vyhľadávania nevyhovuje žiaden inzerát");
    }

    public User verificate(String username, String password) throws SQLException {
        UserManager um = new UserManager();
        List<User> users = null;
        User user = null;

        users = um.verificateUser(username, password);
        if (users.size() == 1) {
            user = users.get(0);
            return user;
        }
        return null;

    }

    public void jumpLogin(ActionEvent event) throws IOException {
        try {
            showLogin(event, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLogin(ActionEvent event, String warning) throws IOException {

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Prihlásenie");
        dialog.setHeaderText("Zadajte meno a heslo");
        if (warning != null)
            dialog.setHeaderText(warning);

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Prihlásiť", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Zrušiť", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Meno");
        PasswordField password = new PasswordField();
        password.setPromptText("Heslo");

        grid.add(new Label("Meno:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Heslo:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            User user = null;
            try {
                user = verificate(usernamePassword.getKey(), usernamePassword.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (user != null) {
                System.out.print(user.getId() + user.getUsername() + user.getPassword());
                User.loggedUser = user;
                showLogged();
            } else {
                try {
                    showLogin(event, "Nesprávne meno alebo heslo!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showProfile(ActionEvent event) {
        try {
            //load up OTHER FXML document
            Parent root = FXMLLoader.load(getClass().getResource("userProfile.fxml"));
            //get reference to the button's stage
            Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showAdvancedSearch(ActionEvent event) {
        try {
            //get reference to the button's stage
            Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            //create a new scene with root and set the stage
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("advancedSearch.fxml")));
            window.setScene(scene);
            jumpShowLogged();
            window.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showCreateAd(ActionEvent event) {
        try {
            //load up OTHER FXML document
            Parent root = FXMLLoader.load(getClass().getResource("createAd.fxml"));
            //get reference to the button's stage
            Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void showRegistration(ActionEvent event) {
        try {
            //load up OTHER FXML document
            Parent root = FXMLLoader.load(getClass().getResource("registration.fxml"));
            //get reference to the button's stage
            Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void showStart(ActionEvent event) {
        try {
            Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(Main.class.getResource("mainView.fxml")));
            window.setScene(scene);
            jumpShowLogged();
            window.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
