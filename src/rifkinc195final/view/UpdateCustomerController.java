package rifkinc195final.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import rifkinc195final.model.Customer;
import rifkinc195final.model.User;
import rifkinc195final.util.Database;

public class UpdateCustomerController {

    @FXML
    private TextField updateCustomerIDTextField;
    @FXML
    private TextField updateCustomerNameTextField;
    @FXML
    private TextField updateCustomerAddressTextField;
    @FXML
    private TextField updateCustomerAddress2TextField;
    @FXML
    private ComboBox<City> updateCustomerCityComboBox;
    @FXML
    private TextField updateCustomerPostalCodeTextField;
    @FXML
    private TextField updateCustomerCountryTextField;
    @FXML
    private TextField updateCustomerPhoneTextField;
    @FXML
    private Button updateCustomerCancelBtn;
    @FXML
    private Button updateCustomerSaveBtn;
    @FXML
    private Button logOffBtn;
    
    private RifkinC195Final mainApp;
    private User currUser;  
    private Customer selectedCustomer;

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
    private void updateCustomerCancelBtnHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Please confirm cancelation");
        alert.setContentText("Are you sure you want to cancel the modification of the customer " + updateCustomerNameTextField.getText() + "?");
        alert.showAndWait()
        
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> mainApp.showCustomersScreen(currUser));
    }

    @FXML
    private void updateCustomerSaveBtnHandler(ActionEvent event) {
        if(isCustomerSavable() == true) {
            try {
                PreparedStatement ps = Database.getConnection().prepareStatement("UPDATE address, customer, city, country "
                        + "SET address = ?, address2 = ?, address.cityId = ?, postalCode = ?, phone = ?, address.lastUpdate = CURRENT_TIMESTAMP, address.lastUpdateBy = ? "
                        + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");

                ps.setString(1, updateCustomerAddressTextField.getText());
                ps.setString(2, updateCustomerAddress2TextField.getText());
                ps.setInt(3, updateCustomerCityComboBox.getValue().getCityID());
                ps.setString(4, updateCustomerPostalCodeTextField.getText());
                ps.setString(5, updateCustomerPhoneTextField.getText());
                ps.setString(6, currUser.getUserName());
                ps.setString(7, updateCustomerIDTextField.getText());

                int result = ps.executeUpdate();
                if (result == 1) {
                    System.out.println("Address was updated!");
                } else {
                    System.out.println("Address was not updated");
                }

                PreparedStatement psc = Database.getConnection().prepareStatement("UPDATE customer, address, city "
                + "SET customerName = ?, customer.lastUpdate = CURRENT_TIMESTAMP, customer.lastUpdateBy = ? "
                + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId");

                psc.setString(1, updateCustomerNameTextField.getText());
                psc.setString(2, currUser.getUserName());
                psc.setString(3, updateCustomerIDTextField.getText());

                int result2 = psc.executeUpdate();
                if (result2 == 1) {
                    System.out.println("Customer was updated!");
                } else {
                    System.out.println("Customer was not updated");
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

        updateCustomerCityComboBox.setItems(cities);
    }
    
    private void showCountry(String city) {
            switch (city) {
            case "Washington":
            case "New York":
            case "Los Angeles":
            case "Chicago":
            case "Houston":
            case "Phoenix":
                updateCustomerCountryTextField.setText("United States");
                break;
            case "Tokyo":
            case "Toyohashi":
            case "Okazaki":
            case "Akita":
            case "Toyota":
                updateCustomerCountryTextField.setText("Japan");
                break;
            case "Canberra":
            case "Sydney":
            case "Melbourne":
            case "Brisbane":
            case "Perth":
                updateCustomerCountryTextField.setText("Australia");
                break;
            case "Moscow":
            case "Saint Petersburg":
            case "Yeketerinburg":
            case "Kazan":
            case "Novosibirsk":
                updateCustomerCountryTextField.setText("Russia");
                break;
            case "London":
            case "Birmingham":
            case "Manchester":
            case "Sheffield":
            case "Leeds":
            case "Liverpool":
                updateCustomerCountryTextField.setText("England");
                break;
            default:
                break;
        }
    }
    
    public void populateCustomerFields(Customer cust) {
        selectedCustomer = cust;
        
        updateCustomerIDTextField.setText(cust.getCustomerID());
        updateCustomerNameTextField.setText(cust.getCustomerName());
        updateCustomerAddressTextField.setText(cust.getAddress());
        updateCustomerAddress2TextField.setText(cust.getAddress2());
        updateCustomerCityComboBox.setValue(cust.getCity());
        updateCustomerPostalCodeTextField.setText(cust.getPostalCode());
        updateCustomerCountryTextField.setText(cust.getCountry());
        updateCustomerPhoneTextField.setText(cust.getPhone());              
    }
    
    private boolean isCustomerSavable() {
        String name = updateCustomerNameTextField.getText();
        String address = updateCustomerAddressTextField.getText();
        City city = updateCustomerCityComboBox.getValue();
        String postalCode = updateCustomerPostalCodeTextField.getText();
        String phone = updateCustomerPhoneTextField.getText();
        
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
            alert.setTitle("Unable to Update Customer");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
    
    public void setUpdateCustomerScreen(RifkinC195Final mainApp, User currUser) {
        this.mainApp = mainApp;
        this.currUser = currUser;
        
        fillCityComboBox();
        updateCustomerCityComboBox.setConverter(new StringConverter<City>() {

        @Override
        public String toString(City object) {
            return object.getCity();
        }     

        @Override
        public City fromString(String string) {
            return updateCustomerCityComboBox.getItems().stream().filter(ap -> 
                ap.getCity().equals(string)).findFirst().orElse(null);
            }
        });
        
        updateCustomerCityComboBox.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null)
                showCountry(newval.toString());
        });
    }

}
