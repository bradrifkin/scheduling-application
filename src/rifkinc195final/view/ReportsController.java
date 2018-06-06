package rifkinc195final.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rifkinc195final.RifkinC195Final;
import rifkinc195final.model.Appointment;
import rifkinc195final.model.AppointmentforReports;
import rifkinc195final.model.Customer;
import rifkinc195final.model.User;
import rifkinc195final.util.Database;


public class ReportsController {

    @FXML
    private Tab ATBMtab;
    @FXML
    private TableView<AppointmentforReports> reportsATBMTable;
    @FXML
    private TableColumn<AppointmentforReports, String> reportsATBMMonthColumn;
    @FXML
    private TableColumn<AppointmentforReports, String> reportsATBMTypeColumn;
    @FXML
    private TableColumn<AppointmentforReports, Integer> reportsATBMCountColumn;
    @FXML
    private Tab ATBCtab;
    @FXML
    private TableView<AppointmentforReports> reportsATBCTable;
    @FXML
    private TableColumn<AppointmentforReports, String> reportsATBCCustomerColumn;
    @FXML
    private TableColumn<AppointmentforReports, String> reportsATBCTypeColumn;
    @FXML
    private TableColumn<AppointmentforReports, Integer> reportsATBCCountColumn;
    @FXML
    private Tab scheduleTab;
    @FXML
    private ComboBox<String> reportsComboBox;
    @FXML
    private TableView<Appointment> reportsScheduleTable;
    @FXML
    private TableColumn<Appointment, String> reportsScheduleApptColumn;
    @FXML
    private TableColumn<Appointment, Customer> reportsScheduleCustomerColumn;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> reportsScheduleStartColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> reportsScheduleEndColumn;
    @FXML
    private TableColumn<Appointment, String> reportsScheduleTypeColumn;
    @FXML
    private TableColumn<Appointment, String> reportsScheduleTitleColumn;
    @FXML
    private Button logOffBtn;
    
    private RifkinC195Final mainApp;  
    private User currUser;
    private ObservableList<AppointmentforReports> atbmAppointments;
    private ObservableList<AppointmentforReports> atbcAppointments;
    private ObservableList<Appointment> scheduleAppointments;
    private ObservableList<User> allUsers;
    private DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private ZoneId zid = ZoneId.systemDefault();
    

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
    private void reportsComboBoxHandler(ActionEvent event) {
        populateSchedule();
    }
    
    
    private void populateATBM() {
        atbmAppointments = FXCollections.observableArrayList();
        
        try{  
            PreparedStatement ps = Database.getConnection().prepareStatement(
                "SELECT MONTHNAME(`start`) as \"month\", type as \"type\", COUNT(*) as \"count\" "
                + "FROM appointment "
                + "GROUP BY MONTHNAME(`start`), type");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {               
                String month = rs.getString("month");
                String type = rs.getString("type");
                int count = rs.getInt("count");                     
                atbmAppointments.add(new AppointmentforReports(month, type, count));
            }  
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        reportsATBMTable.getItems().setAll(atbmAppointments);
    }
    
    private void populateATBC() {
        atbcAppointments = FXCollections.observableArrayList();
        
        try{  
            PreparedStatement statement = Database.getConnection().prepareStatement(
                "SELECT appointment.customerId, customer.customerId, customer.customerName, appointment.type as \"type\", COUNT(*) as \"count\" "
                + "FROM appointment, customer "
                + "WHERE appointment.customerId = customer.customerId "
                + "GROUP BY customer.customerId, description");
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {               
                Customer tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));
                String tType = rs.getString("type");
                int tCount = rs.getInt("count");                     
                atbcAppointments.add(new AppointmentforReports(tCustomer, tType, tCount));
            }  
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        reportsATBCTable.getItems().setAll(atbcAppointments);
    }
    
    private void populateSchedule() {
        
        scheduleAppointments = FXCollections.observableArrayList();
                
        reportsScheduleTable.setItems(scheduleAppointments);
        
        String chosenValue = reportsComboBox.getValue();
        System.out.println(chosenValue);
        
        if(chosenValue == "wflick") {
            try{              
                PreparedStatement statement = Database.getConnection().prepareStatement(
                    "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, appointment.contact, appointment.description, appointment.url, "
                    + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                    + "FROM appointment, customer "
                    + "WHERE appointment.customerId = customer.customerId AND appointment.contact = 'wflick' "
                    + "ORDER BY `start`");
                ResultSet rs = statement.executeQuery();

                while (rs.next()) { 
                    String tAppt = rs.getString("appointment.appointmentId");

                    Timestamp tsStart = rs.getTimestamp("appointment.start");
                    ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(zid);

                    Timestamp tsEnd = rs.getTimestamp("appointment.end");
                    ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(zid);

                    String tTitle = rs.getString("appointment.title");
                    String tType = rs.getString("appointment.type");
                    String tContact = rs.getString("appointment.contact");
                    String tDescription = rs.getString("appointment.description");
                    String tUrl = rs.getString("appointment.url");
                    Customer tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));
                    String tUser = rs.getString("appointment.createdBy");

                    scheduleAppointments.add(new Appointment(tAppt, newLocalStart.format(dtf), newLocalEnd.format(dtf), tTitle, tType, tContact, tDescription, tUrl, tCustomer, tUser));
                }   
            } catch (SQLException e) {
                e.printStackTrace();
            }    
        } 
        
        if(chosenValue == "user1") {
            try{              
                PreparedStatement statement = Database.getConnection().prepareStatement(
                    "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, appointment.contact, appointment.description, appointment.url, "
                    + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                    + "FROM appointment, customer "
                    + "WHERE appointment.customerId = customer.customerId AND appointment.contact = 'user1' "
                    + "ORDER BY `start`");
                ResultSet rs = statement.executeQuery();

                while (rs.next()) { 
                    String tAppt = rs.getString("appointment.appointmentId");

                    Timestamp tsStart = rs.getTimestamp("appointment.start");
                    ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(zid);

                    Timestamp tsEnd = rs.getTimestamp("appointment.end");
                    ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(zid);

                    String tTitle = rs.getString("appointment.title");
                    String tType = rs.getString("appointment.type");
                    String tContact = rs.getString("appointment.contact");
                    String tDescription = rs.getString("appointment.description");
                    String tUrl = rs.getString("appointment.url");
                    Customer tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));
                    String tUser = rs.getString("appointment.createdBy");

                    scheduleAppointments.add(new Appointment(tAppt, newLocalStart.format(dtf), newLocalEnd.format(dtf), tTitle, tType, tContact, tDescription, tUrl, tCustomer, tUser));
                }   
            } catch (SQLException e) {
                e.printStackTrace();
            }    
        } 
                
        if(chosenValue == "user2") {
            try{              
                PreparedStatement statement = Database.getConnection().prepareStatement(
                    "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, appointment.contact, appointment.description, appointment.url, "
                    + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                    + "FROM appointment, customer "
                    + "WHERE appointment.customerId = customer.customerId AND appointment.contact = 'user2' "
                    + "ORDER BY `start`");
                ResultSet rs = statement.executeQuery();

                while (rs.next()) { 
                    String tAppt = rs.getString("appointment.appointmentId");

                    Timestamp tsStart = rs.getTimestamp("appointment.start");
                    ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(zid);

                    Timestamp tsEnd = rs.getTimestamp("appointment.end");
                    ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(zid);

                    String tTitle = rs.getString("appointment.title");
                    String tType = rs.getString("appointment.type");
                    String tContact = rs.getString("appointment.contact");
                    String tDescription = rs.getString("appointment.description");
                    String tUrl = rs.getString("appointment.url");
                    Customer tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));
                    String tUser = rs.getString("appointment.createdBy");

                    scheduleAppointments.add(new Appointment(tAppt, newLocalStart.format(dtf), newLocalEnd.format(dtf), tTitle, tType, tContact, tDescription, tUrl, tCustomer, tUser));
                }   
            } catch (SQLException e) {
                e.printStackTrace();
            }    
        } 
                        
        if(chosenValue == "test") {
            try{              
                PreparedStatement statement = Database.getConnection().prepareStatement(
                    "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, appointment.contact, appointment.description, appointment.url, "
                    + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                    + "FROM appointment, customer "
                    + "WHERE appointment.customerId = customer.customerId AND appointment.contact = 'test' "
                    + "ORDER BY `start`");
                ResultSet rs = statement.executeQuery();

                while (rs.next()) { 
                    String tAppt = rs.getString("appointment.appointmentId");

                    Timestamp tsStart = rs.getTimestamp("appointment.start");
                    ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(zid);

                    Timestamp tsEnd = rs.getTimestamp("appointment.end");
                    ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                    ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(zid);

                    String tTitle = rs.getString("appointment.title");
                    String tType = rs.getString("appointment.type");
                    String tContact = rs.getString("appointment.contact");
                    String tDescription = rs.getString("appointment.description");
                    String tUrl = rs.getString("appointment.url");
                    Customer tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));
                    String tUser = rs.getString("appointment.createdBy");

                    scheduleAppointments.add(new Appointment(tAppt, newLocalStart.format(dtf), newLocalEnd.format(dtf), tTitle, tType, tContact, tDescription, tUrl, tCustomer, tUser));
                }   
            } catch (SQLException e) {
                e.printStackTrace();
            }    
        } 
    }
    
    private void populateReportsComboBox() {
        ObservableList<String> contactArray = FXCollections.observableArrayList();
        contactArray.addAll("wflick", "user1", "user2", "test");
        reportsComboBox.setItems(contactArray);
    }
    
    
    public void setReportsScreen(RifkinC195Final mainApp, User currUser) {
        this.mainApp = mainApp;
        this.currUser = currUser;
        
        populateATBM();
        populateATBC();
        populateReportsComboBox();
        
        reportsATBMMonthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        reportsATBMTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportsATBMCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        reportsATBCCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        reportsATBCTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportsATBCCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        reportsScheduleApptColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        reportsScheduleCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        reportsScheduleStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        reportsScheduleEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        reportsScheduleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportsScheduleTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    }
    
}
