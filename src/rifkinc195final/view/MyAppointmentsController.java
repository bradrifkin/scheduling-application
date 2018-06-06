package rifkinc195final.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import rifkinc195final.RifkinC195Final;
import rifkinc195final.model.Appointment;
import rifkinc195final.model.User;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import rifkinc195final.model.Customer;
import rifkinc195final.util.Database;

public class MyAppointmentsController {

    @FXML
    private TableView<Appointment> myAppointmentsTable;
    @FXML
    private TableColumn<Appointment, Customer> myAppointmentsCustomerColumn;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> myAppointmentsStartColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> myAppointmentsEndColumn;
    @FXML
    private TableColumn<Appointment, String> myAppointmentsTypeColumn;
    @FXML
    private TableColumn<Appointment, String> myAppointmentsTitleColumn;
    @FXML
    private Button myAppointmentsAddBtn;
    @FXML
    private Button myAppointmentsUpdateBtn;
    @FXML
    private Button myAppointmentsDeleteBtn;
    @FXML
    private Button logOffBtn;
    @FXML
    private RadioButton viewUpcomingWeekRadioBtn;
    @FXML
    private ToggleGroup viewUpcoming;
    @FXML
    private RadioButton viewUpcomingMonthRadioBtn;
    @FXML
    private Button myAppointmentsResetButton;
    
    private RifkinC195Final mainApp; 
    private User currUser;
    public static ObservableList<Appointment> allAppointments;
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
    private void myAppointmentsAddBtnHandler(ActionEvent event) {
        mainApp.showAddAppointmentScreen(currUser);
    }

    @FXML
    private void myAppointmentsUpdateBtnHandler(ActionEvent event) {
        Appointment selectedAppointment = myAppointmentsTable.getSelectionModel().getSelectedItem();
        
        if (selectedAppointment != null) {
            mainApp.showUpdateAppointmentScreen(selectedAppointment, currUser);
            
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment from the table");
            alert.showAndWait();
        }
    }

    @FXML
    private void myAppointmentsDeleteBtnHandler(ActionEvent event) {
        Appointment selectedAppointment = myAppointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Deleting...");
            alert.setContentText("Are you sure you want to delete " + selectedAppointment.getTitle() + " with " + selectedAppointment.getCustomer() + "?");        
            alert.showAndWait()

                // UTILIZING LAMBDA EXPRESSION TO KEEP CODE SUCCINCT    
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    deleteAppointment(selectedAppointment); 
                    allAppointments.remove(selectedAppointment);
                });

            myAppointmentsTable.setItems(allAppointments);
        
        } else {
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment from the table.");

            alert.showAndWait();
        }
    }
    
    public void deleteAppointment(Appointment appointment) {
        try{           
            PreparedStatement pst = Database.getConnection().prepareStatement("DELETE appointment.* FROM appointment WHERE appointment.appointmentId = ?");
            pst.setString(1, appointment.getAppointmentId()); 
            pst.executeUpdate();  
        } catch(SQLException e){
            e.printStackTrace();
        }       
    }
    
    public static ObservableList<Appointment> getAppointmentData() {
        return allAppointments;
    }

    @FXML
    private void viewUpcomingWeekRadioBtnHandler(ActionEvent event) {
        LocalDate now = LocalDate.now();
        LocalDate nowPlusWeek = now.plusDays(7);
        
        FilteredList<Appointment> filteredData = new FilteredList<>(allAppointments);
        filteredData.setPredicate(row -> {
            LocalDate rowDate = LocalDate.parse(row.getStart(), dtf);
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlusWeek);
        });
        myAppointmentsTable.setItems(filteredData);
    }

    @FXML
    private void viewUpcomingMonthRadioBtnHandler(ActionEvent event) {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Month = now.plusMonths(1);
        
        FilteredList<Appointment> filteredData = new FilteredList<>(allAppointments);
        filteredData.setPredicate(row -> {
            LocalDate rowDate = LocalDate.parse(row.getStart(), dtf);
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Month);
        });
        myAppointmentsTable.setItems(filteredData);
    }

    @FXML
    private void myAppointmentsResetButtonHandler(ActionEvent event) {
        viewUpcomingWeekRadioBtn.setSelected(false);
        viewUpcomingMonthRadioBtn.setSelected(false);
        
        setMyAppointmentsScreen(mainApp, currUser);
        
    }
    
    public void setMyAppointmentsScreen(RifkinC195Final mainApp, User currUser) {
        this.mainApp = mainApp;
        this.currUser = currUser;
        
        viewUpcoming = new ToggleGroup();
        this.viewUpcomingMonthRadioBtn.setToggleGroup(viewUpcoming);
        this.viewUpcomingWeekRadioBtn.setToggleGroup(viewUpcoming);
        
        allAppointments = FXCollections.observableArrayList();
        
        myAppointmentsCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        myAppointmentsStartColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
        myAppointmentsEndColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
        myAppointmentsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        myAppointmentsTitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        
        myAppointmentsTable.setItems(allAppointments);
        
        // RUN SQL QUERY TO ADD DATA TO ALL APPOINTMENTS ARRAY
        try{              
            PreparedStatement statement = Database.getConnection().prepareStatement(
                "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, appointment.contact, appointment.description, appointment.url, "
                + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment, customer "
                + "WHERE appointment.customerId = customer.customerId "
                + "ORDER BY `start`");
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) { 
                String tAppointmentId = rs.getString("appointment.appointmentId");
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
                      
                allAppointments.add(new Appointment(tAppointmentId, newLocalStart.format(dtf), newLocalEnd.format(dtf), tTitle, tType, tContact, tDescription, tUrl, tCustomer, tUser));
            }   
        } catch (SQLException e) {
            e.printStackTrace();
        }         
    }
    
}
