package rifkinc195final.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import rifkinc195final.RifkinC195Final;
import rifkinc195final.model.City;
import rifkinc195final.model.User;
import rifkinc195final.util.Database;

public class AddCustomerController {

    @FXML
    private TextField addCustomerNameTextField;
    @FXML
    private TextField addCustomerAddressTextField;
    @FXML
    private TextField addCustomerAddress2TextField;
    @FXML
    private TextField addCustomerCountryTextField;
    @FXML
    private ComboBox<City> addCustomerCityComboBox;
    @FXML
    private TextField addCustomerPostalCodeTextField;
    @FXML
    private TextField addCustomerPhoneTextField;
    @FXML
    private Button logOffBtn;
    @FXML
    private TextField addCustomerIDTextField;
    @FXML
    private Button addCustomerCancelBtn;
    @FXML
    private Button addCustomerSaveBtn;
    
    private RifkinC195Final mainApp;
    private User currUser; 

    @FXML
    private void logOffBtnHandler(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Click OK to log off, or cancel to stay here");
        alert.showAndWait()
                
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> mainApp.showLoginScreen());
        
    }

    @FXML
    private void addCustomerCancelBtnHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Please confirm cancelation");
        alert.setContentText("Are you sure you want to cancel the addition of the customer " + addCustomerNameTextField.getText() + "?");
        alert.showAndWait()
        
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> mainApp.showCustomersScreen(currUser));
    }

    @FXML
    private void addCustomerSaveBtnHandler(ActionEvent event) {
        if(isCustomerSavable() == true) {    
            try {

                PreparedStatement ps = Database.getConnection().prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)",Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, addCustomerAddressTextField.getText());
                ps.setString(2, addCustomerAddress2TextField.getText());
                ps.setInt(3, addCustomerCityComboBox.getValue().getCityID());
                ps.setString(4, addCustomerPostalCodeTextField.getText());
                ps.setString(5, addCustomerPhoneTextField.getText());
                ps.setString(6, currUser.getUserName());
                ps.setString(7, currUser.getUserName());
                boolean res = ps.execute();
                int newAddressId = -1;
                ResultSet rs = ps.getGeneratedKeys();

                if(rs.next()){
                    newAddressId = rs.getInt(1);
                    System.out.println("Generated AddressID: "+ newAddressId);
                }


                PreparedStatement psc = Database.getConnection().prepareStatement("INSERT INTO customer "
                + "(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");

                psc.setString(1, addCustomerNameTextField.getText());
                psc.setInt(2, newAddressId);
                psc.setInt(3, 1);
                psc.setString(4, currUser.getUserName());
                psc.setString(5, currUser.getUserName());

                int result = psc.executeUpdate();
                if (result == 1) {
                    System.out.println("New customer was saved!");
                } else {
                    System.out.println("New customer was not saved");
                }

            } catch (SQLException ex) {
            ex.printStackTrace();
            }

            mainApp.showCustomersScreen(currUser);
        }
    }
    
    private void fillCityComboBox() {
        ObservableList<City> cities = FXCollections.observableArrayList();
    
        try(
            PreparedStatement statement = Database.getConnection().prepareStatement("SELECT cityId, city FROM city LIMIT 100;");
            ResultSet rs = statement.executeQuery();){

            while (rs.next()) {
                cities.add(new City(rs.getInt("city.cityId"),rs.getString("city.city")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } 

        addCustomerCityComboBox.setItems(cities);
    }
    
    private void showCountry(String city) {
        switch (city) {
            case "Washington":
            case "New York":
            case "Los Angeles":
            case "Chicago":
            case "Houston":
            case "Phoenix":
                addCustomerCountryTextField.setText("United States");
                break;
            case "Tokyo":
            case "Toyohashi":
            case "Okazaki":
            case "Akita":
            case "Toyota":
                addCustomerCountryTextField.setText("Japan");
                break;
            case "Canberra":
            case "Sydney":
            case "Melbourne":
            case "Brisbane":
            case "Perth":
                addCustomerCountryTextField.setText("Australia");
                break;
            case "Moscow":
            case "Saint Petersburg":
            case "Yeketerinburg":
            case "Kazan":
            case "Novosibirsk":
                addCustomerCountryTextField.setText("Russia");
                break;
            case "London":
            case "Birmingham":
            case "Manchester":
            case "Sheffield":
            case "Leeds":
            case "Liverpool":
                addCustomerCountryTextField.setText("England");
                break;
            default:
                break;
        }
    }
    
    private boolean isCustomerSavable() {
        String name = addCustomerNameTextField.getText();
        String address = addCustomerAddressTextField.getText();
        City city = addCustomerCityComboBox.getValue();
        String postalCode = addCustomerPostalCodeTextField.getText();
        String phone = addCustomerPhoneTextField.getText();
        
        String errorMessage = "";

        if (name == null || name.length() == 0) {
            errorMessage += "Please enter a name.\n"; 
        }
        if (address == null || address.length() == 0) {
            errorMessage += "Please enter an address.\n";  
        } 
        if (city == null) {
            errorMessage += "Please select a city.\n"; 
        }         
        if (postalCode == null || postalCode.length() == 0) {
            errorMessage += "Please enter the postal code.\n"; 
        } else if (postalCode.length() > 10 || postalCode.length() < 5){
            errorMessage += "Please enter a valid postal code.\n";
        }
        if (phone == null || phone.length() == 0) {
            errorMessage += "Please enter a phone number, with area code.\n"; 
        } else if (phone.length() < 10 || phone.length() > 15 ){
            errorMessage += "Please enter a valid phone number, with area code.\n";
        }        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to Save Customer");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
    
    public void setAddCustomerScreen(RifkinC195Final mainApp, User currUser) {
        this.mainApp = mainApp;
        this.currUser = currUser;
        
        fillCityComboBox();
        addCustomerCityComboBox.setConverter(new StringConverter<City>() {

        @Override
        public String toString(City object) {
            return object.getCity();
        }     

        @Override
        public City fromString(String string) {
            return addCustomerCityComboBox.getItems().stream().filter(ap -> 
                ap.getCity().equals(string)).findFirst().orElse(null);
            }
        });
        
        addCustomerCityComboBox.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null)
                showCountry(newval.toString());
        });
    }
    
}
