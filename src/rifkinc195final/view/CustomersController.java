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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rifkinc195final.RifkinC195Final;
import rifkinc195final.model.City;
import rifkinc195final.model.Customer;
import rifkinc195final.model.User;
import rifkinc195final.util.Database;

public class CustomersController {

    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, String> customersNameColumn;
    @FXML
    private TableColumn<Customer, String> customersAddressColumn;
    @FXML
    private TableColumn<Customer, City> customersCityColumn;
    @FXML
    private TableColumn<Customer, String> customersPostalCodeColumn;
    @FXML
    private TableColumn<Customer, String> customersCountryColumn;
    @FXML
    private TableColumn<Customer, String> customersPhoneColumn;
    @FXML
    private Button customersAddBtn;
    @FXML
    private Button customersUpdateBtn;
    @FXML
    private Button customersDeleteBtn;
    @FXML
    private Button logOffBtn;
    
    private RifkinC195Final mainApp;
    private User currUser;  
    public static ObservableList<Customer> allCustomers;

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
    private void customersAddBtnHandler(ActionEvent event) {
        mainApp.showAddCustomerScreen(currUser);
    }

    @FXML
    private void customersUpdateBtnHandler(ActionEvent event) {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer != null) {
            mainApp.showUpdateCustomerScreen(selectedCustomer, currUser);
            
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer from the table");
            alert.showAndWait();
        }
    }

    @FXML
    private void customersDeleteBtnHandler(ActionEvent event) {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Deleting...");
            alert.setContentText("Are you sure you want to delete " + selectedCustomer.getCustomerName() + "?");        
            alert.showAndWait()

                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    deleteCustomer(selectedCustomer); 
                    allCustomers.remove(selectedCustomer);
                });

            customersTable.setItems(allCustomers);
        
        } else {
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer from the table.");

            alert.showAndWait();
        }
    }
    
    public void setCustomersScreen(RifkinC195Final mainApp, User currUser) {
        this.mainApp = mainApp;
        this.currUser = currUser;
        
        allCustomers = FXCollections.observableArrayList();
        
        customersNameColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        customersAddressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customersCityColumn.setCellValueFactory(new PropertyValueFactory<>("City"));
        customersPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        customersCountryColumn.setCellValueFactory(new PropertyValueFactory<>("Country"));
        customersPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        
        customersTable.setItems(allCustomers);
        
        // RUN SQL QUERY TO ADD DATA TO ALL CUSTOMERS ARRAY
        try{              
            PreparedStatement statement = Database.getConnection().prepareStatement(
                "SELECT customer.customerID, customer.customerName, address.address, address.address2, "
                + "address.postalCode, city.cityID, city.city, country.country, address.phone "
                + "FROM customer, address, city, country "
                + "WHERE customer.addressID = address.addressID AND address.cityID = city.cityID AND city.countryID = country.countryID "
                + "ORDER BY customer.customerName");
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) { 
                String tCustomerID = rs.getString("customer.customerId");
                String tCustomerName = rs.getString("customer.customerName");
                String tAddress = rs.getString("address.address");
                String tAddress2 = rs.getString("address.address2");
                City tCity = new City(rs.getInt("city.cityID"), rs.getString("city.city"));
                String tCountry = rs.getString("country.country");
                String tPostalCode = rs.getString("address.postalCode");
                String tPhone = rs.getString("address.phone");

                allCustomers.add(new Customer(tCustomerID, tCustomerName, tAddress, tAddress2, tCity, tCountry, tPostalCode, tPhone));
            }   
        } catch (SQLException e) {
            e.printStackTrace();
        }     
    }
    
    public void deleteCustomer(Customer customer) {
        try{           
            PreparedStatement pst = Database.getConnection().prepareStatement(
            "DELETE customer.*, address.* " 
            + "FROM customer, address "
            + "WHERE customer.customerID = ? AND customer.addressID = address.addressID");
            pst.setString(1, customer.getCustomerID()); 
            pst.executeUpdate();  
        } catch(SQLException e){
            e.printStackTrace();
        }       
    }
    
    public static ObservableList<Customer> getCustomerData() {
        return allCustomers;
    }
    
}
