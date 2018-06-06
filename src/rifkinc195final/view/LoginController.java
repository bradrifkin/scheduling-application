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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import rifkinc195final.RifkinC195Final;
import rifkinc195final.model.Appointment;
import rifkinc195final.model.Customer;
import rifkinc195final.model.User;
import rifkinc195final.util.Database;

public class LoginController {

    @FXML
    private Label errorMessage;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;

    private RifkinC195Final mainApp;
    User user = new User();
    private User currUser;
    ObservableList<Appointment> reminderList = FXCollections.observableArrayList();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId zid = ZoneId.systemDefault();
    ResourceBundle rb = ResourceBundle.getBundle("login", Locale.getDefault());
    private final static Logger LOGGER = Logger.getLogger(Logger.class.getName());

    
    @FXML
    private void signInButtonHandler(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText(); 
        
        if(username.length()==0 || password.length()==0) {
            errorMessage.setVisible(true);
            errorMessage.setText(rb.getString("empty"));    
        } else{
            User currUser = checkLoginInfo(username,password); 
            if (currUser == null) {
                errorMessage.setVisible(true);
                errorMessage.setText(rb.getString("incorrect"));
                return;
            }
            populateReminders();
            setReminder();
            mainApp.showSidebar(currUser);
            mainApp.showMyAppointmentsScreen(currUser);
            
            LOGGER.log(Level.INFO, "{0} logged in", currUser.getUserName());
        }
    }
    
    User checkLoginInfo(String username,String password) {
        try{           
            PreparedStatement pst = Database.getConnection().prepareStatement("SELECT * FROM user WHERE username=? AND password=?");
            pst.setString(1, username); 
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();                        
            if(rs.next()){
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setUserID(rs.getInt("userID"));
            } else {
                return null;    
            }            
                
        } catch(SQLException e){
            e.printStackTrace();
        }       
        return user;
    }
    
    public void setReminder() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15Min = now.plusMinutes(15);
        
        FilteredList<Appointment> filteredData = new FilteredList<>(reminderList);

        // UTILIZING LAMBDA EXPRESSION TO KEEP CODE SUCCINCT
        filteredData.setPredicate(row -> {
            LocalDateTime rowDate = LocalDateTime.parse(row.getStart(), dtf);
            return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(nowPlus15Min);
            }
        );
        if (filteredData.isEmpty()) {
            System.out.println("You have no appointments within 15 minutes.");
        } else {
            String type = filteredData.get(0).getDescription();
            String customer =  filteredData.get(0).getCustomer().getCustomerName();
            String start = filteredData.get(0).getStart();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointment Reminder");
            alert.setHeaderText("Reminder: You have the following appointment scheduled within the next 15 minutes.");
            alert.setContentText("Your upcoming " + type + " appointment with " + customer +
                " is currently set for " + start + ".");
            alert.showAndWait();
        }
    }
    
    private void populateReminders() {
        System.out.println(user.getUserName());
        try{           
            PreparedStatement pst = Database.getConnection().prepareStatement(
                "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.type, appointment.contact, appointment.description, appointment.url, "
                + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                + "FROM appointment, customer "
                + "WHERE appointment.customerId = customer.customerId AND appointment.contact = ? "
                + "ORDER BY `start`");
            pst.setString(1, user.getUserName());
            ResultSet rs = pst.executeQuery();
           
            while (rs.next()) {
                String tAppointmentId = rs.getString("appointment.appointmentId");
                
                Timestamp tsStart = rs.getTimestamp("appointment.start");
                ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(zid);

                Timestamp tsEnd = rs.getTimestamp("appointment.end");
                ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
        	ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(zid);

                String tTitle = rs.getString("appointment.title");
                String tType = rs.getString("appointment.description");
                String tContact = rs.getString("appointment.contact");
                String tDescription = rs.getString("appointment.description");
                String tUrl = rs.getString("appointment.url");
                Customer tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));
                String tUser = rs.getString("appointment.createdBy");
                      
                reminderList.add(new Appointment(tAppointmentId, newLocalStart.format(dtf), newLocalEnd.format(dtf), tTitle, tType, tContact, tDescription, tUrl, tCustomer, tUser));   
            }      
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setLogin(RifkinC195Final mainApp) {
        this.mainApp = mainApp;
    }  
    
}
