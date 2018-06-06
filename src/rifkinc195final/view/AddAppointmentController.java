package rifkinc195final.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import rifkinc195final.RifkinC195Final;
import rifkinc195final.model.City;
import rifkinc195final.model.Customer;
import rifkinc195final.model.User;
import rifkinc195final.util.Database;
import static rifkinc195final.view.CustomersController.allCustomers;

public class AddAppointmentController {

    @FXML
    private ComboBox<String> addAppointmentContactComboBox;
    @FXML
    private ComboBox<Customer> addAppointmentCustomerComboBox;
    @FXML
    private ComboBox<String> addAppointmentStartComboBox;
    @FXML
    private ComboBox<String> addAppointmentEndComboBox;
    @FXML
    private ComboBox<String> addAppointmentTypeComboBox;
    @FXML
    private TextField addAppointmentTitleTextField;
    @FXML
    private TextField addAppointmentDescriptionTextField;
    @FXML
    private TextField addAppointmentURLTextField;
    @FXML
    private Button logOffBtn;
    @FXML
    private Button addAppointmentSaveBtn;
    @FXML
    private DatePicker addAppointmentDatePicker;
    @FXML
    private TextField addAppointmentIDTextField;
    @FXML
    private Button addAppointmentCancelBtn;
    
    private RifkinC195Final mainApp;
    private User currUser;
    private ZoneId zid = ZoneId.systemDefault();
    private DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private ObservableList<String> startTimes = FXCollections.observableArrayList();
    private ObservableList<String> endTimes = FXCollections.observableArrayList();

    @FXML
    private void logOffBtnHandler(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Click OK to log off, or cancel to stay here");
        alert.showAndWait()
             
             // LAMBDA EXPRESSION USED TO SIMPLIFY CODE
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> mainApp.showLoginScreen());
        
    }

    @FXML
    private void addAppointmentCancelBtnHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Please confirm cancelation");
        alert.setContentText("Are you sure you want to cancel the addition of the appointment " + addAppointmentDescriptionTextField.getText() + "?");
        alert.showAndWait()
        
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> mainApp.showMyAppointmentsScreen(currUser));
    }

    @FXML
    private void addAppointmentSaveBtnHandler(ActionEvent event) {
        if(isAppointmentSavable() == true) {
            LocalDate localDate = addAppointmentDatePicker.getValue();
            LocalTime startTime = LocalTime.parse(addAppointmentStartComboBox.getSelectionModel().getSelectedItem(), timeDTF);
            LocalTime endTime = LocalTime.parse(addAppointmentEndComboBox.getSelectionModel().getSelectedItem(), timeDTF);

            LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
            LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

            ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));            

            Timestamp startsqlts = Timestamp.valueOf(startUTC.toLocalDateTime()); 
            Timestamp endsqlts = Timestamp.valueOf(endUTC.toLocalDateTime());       

            try {
                PreparedStatement pst = Database.getConnection().prepareStatement("INSERT INTO appointment "
                + "(customerID, contact, title, type, description, location, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");

                pst.setString(1, addAppointmentCustomerComboBox.getSelectionModel().getSelectedItem().getCustomerID());
                pst.setString(2, addAppointmentContactComboBox.getSelectionModel().getSelectedItem());
                pst.setString(3, addAppointmentTitleTextField.getText());
                pst.setString(4, addAppointmentTypeComboBox.getSelectionModel().getSelectedItem());
                pst.setString(5, addAppointmentDescriptionTextField.getText());
                pst.setString(6, "");
                pst.setString(7, addAppointmentURLTextField.getText());
                pst.setTimestamp(8, startsqlts);
                pst.setTimestamp(9, endsqlts);
                pst.setString(10, currUser.getUserName());
                pst.setString(11, currUser.getUserName());

                int result = pst.executeUpdate();
                if (result == 1) {
                    System.out.println("New appointment was saved!");
                } else {
                    System.out.println("New appointment was not saved");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            mainApp.showMyAppointmentsScreen(currUser);
        }
    }
    
    private void fillTypeComboBox() {
        ObservableList<String> typeArray = FXCollections.observableArrayList();
        typeArray.addAll("First Meeting", "First Consultation", "Follow Up", "Monthly Check In", "Ad Hoc");
        addAppointmentTypeComboBox.setItems(typeArray);
    }
    
    private void fillContactComboBox() {
        ObservableList<String> contactArray = FXCollections.observableArrayList();
        contactArray.addAll("wflick", "user1", "user2", "test");
        addAppointmentContactComboBox.setItems(contactArray);
    }
    
    private void fillCustomerComboBox() {
        allCustomers = FXCollections.observableArrayList();
        
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
                addAppointmentCustomerComboBox.setItems(allCustomers);
            }   
        } catch (SQLException e) {
            e.printStackTrace();
        }     
    }
    
    private boolean isAppointmentSavable() {
        String contact = addAppointmentContactComboBox.getValue();
        Customer customer = addAppointmentCustomerComboBox.getValue();
        String type = addAppointmentTypeComboBox.getValue();
        String title = addAppointmentTitleTextField.getText();
        String description = addAppointmentDescriptionTextField.getText();
        String url = addAppointmentURLTextField.getText();

        LocalDate localDate = addAppointmentDatePicker.getValue();
	LocalTime startTime = LocalTime.parse(addAppointmentStartComboBox.getSelectionModel().getSelectedItem(), timeDTF);
	LocalTime endTime = LocalTime.parse(addAppointmentEndComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        
        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

        ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));            
	        
        String errorMessage = "";

        if (contact == null) {
            errorMessage += "Please select a contact.\n";
        }
        if (customer == null) {
            errorMessage += "Please select a customer.\n"; 
        }
        if (type == null) {
            errorMessage += "Please select a type.\n";  
        }         
        if (title == null || title.length() == 0) {
            errorMessage += "Please enter a title.\n"; 
        }
        if (description == null || description.length() == 0) {
            errorMessage += "Please enter a description.\n";
        }
        if (url == null || url.length() == 0) {
            errorMessage += "Please enter a URL.\n";
        }
        if (startUTC == null) {
            errorMessage += "Please select a start time"; 
        }         
        if (endUTC == null) {
            errorMessage += "Please select an end time.\n"; 
            } else if (endUTC.equals(startUTC) || endUTC.isBefore(startUTC)){
                errorMessage += "The end time must be after the start time.\n";
            } else try {
                if (hasConflict(startUTC, endUTC)){
                    errorMessage += "Please select a new date and/or time, as the proposed date and/or time conflicts with an existing appointment of " + addAppointmentContactComboBox.getValue() + ".\n";
                }
        } catch (SQLException ex) {
            Logger.getLogger(AddAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to Save Appointment");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
    
    private boolean hasConflict(ZonedDateTime newStart, ZonedDateTime newEnd) throws SQLException {
        String apptID = "0";
        String contact = addAppointmentContactComboBox.getValue();
        
        try{
            
            PreparedStatement pst = Database.getConnection().prepareStatement(
            "SELECT * FROM appointment "
            + "WHERE (? BETWEEN start AND end OR ? BETWEEN start AND end OR ? < start AND ? > end) "
            + "AND (contact = ? AND appointmentID != ?)");
            pst.setTimestamp(1, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(2, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setTimestamp(3, Timestamp.valueOf(newStart.toLocalDateTime()));
            pst.setTimestamp(4, Timestamp.valueOf(newEnd.toLocalDateTime()));
            pst.setString(5, contact);
            pst.setString(6, apptID);
            ResultSet rs = pst.executeQuery();
           
            if(rs.next()) {
                return true;
            }
            
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
            e.printStackTrace();
        }
        return false;
    }
    
    public void setAddAppointmentScreen(RifkinC195Final mainApp, User currUser) {
        this.mainApp = mainApp;
        this.currUser = currUser;
        
        fillTypeComboBox();
        fillCustomerComboBox();
        fillContactComboBox();
        
        LocalTime time = LocalTime.of(8, 0);
        
	do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            time = time.plusMinutes(15);
	} while(!time.equals(LocalTime.of(17, 15)));
            startTimes.remove(startTimes.size() - 1);
            endTimes.remove(0);
        
        addAppointmentDatePicker.setValue(LocalDate.now());
        addAppointmentStartComboBox.setItems(startTimes);
	addAppointmentEndComboBox.setItems(endTimes);
	addAppointmentStartComboBox.getSelectionModel().select(LocalTime.of(8, 0).format(timeDTF));
	addAppointmentEndComboBox.getSelectionModel().select(LocalTime.of(8, 15).format(timeDTF));
    } 


}
