package rifkinc195final;

import java.io.IOException;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rifkinc195final.model.Appointment;
import rifkinc195final.model.Customer;
import rifkinc195final.model.User;
import rifkinc195final.util.Database;
import rifkinc195final.util.ActivityLog;
import rifkinc195final.view.AddAppointmentController;
import rifkinc195final.view.AddCustomerController;
import rifkinc195final.view.CustomersController;
import rifkinc195final.view.LoginController;
import rifkinc195final.view.MyAppointmentsController;
import rifkinc195final.view.ReportsController;
import rifkinc195final.view.SidebarController;
import rifkinc195final.view.UpdateAppointmentController;
import rifkinc195final.view.UpdateCustomerController;

public class RifkinC195Final extends Application {

    private Stage primaryStage;
    private AnchorPane login;
    private BorderPane sidebar;
    Locale locale = Locale.getDefault();
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("PLNR - Your Scheduling Application");
        
//        Locale.setDefault(new Locale("es", "ES"));
       
        showLoginScreen();        
    }
    
    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RifkinC195Final.class.getResource("view/Login.fxml"));
            login = (AnchorPane) loader.load();

            Scene scene = new Scene(login);
            String css = RifkinC195Final.class.getResource("styles/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            primaryStage.setScene(scene);
            
            LoginController controller = loader.getController();
            controller.setLogin(this);
            
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showSidebar(User currUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RifkinC195Final.class.getResource("view/Sidebar.fxml"));
            sidebar = (BorderPane) loader.load();

            Scene scene = new Scene(sidebar);
            String css = RifkinC195Final.class.getResource("styles/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            primaryStage.setScene(scene);
            
            SidebarController controller = loader.getController();
            controller.setSidebar(this, currUser);
            
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
    
    public void showMyAppointmentsScreen(User currUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RifkinC195Final.class.getResource("view/MyAppointments.fxml"));
            AnchorPane myAppointments = (AnchorPane) loader.load();
            sidebar.setCenter(myAppointments);
            MyAppointmentsController controller = loader.getController();
            controller.setMyAppointmentsScreen(this, currUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    public void showCustomersScreen(User currUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RifkinC195Final.class.getResource("view/Customers.fxml"));
            AnchorPane customers = (AnchorPane) loader.load();
            sidebar.setCenter(customers);
            CustomersController controller = loader.getController();
            controller.setCustomersScreen(this, currUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showAddAppointmentScreen(User currUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RifkinC195Final.class.getResource("view/AddAppointment.fxml"));
            AnchorPane addAppointment = (AnchorPane) loader.load();
            sidebar.setCenter(addAppointment);
            AddAppointmentController controller = loader.getController();
            controller.setAddAppointmentScreen(this, currUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }     
    
    public void showAddCustomerScreen(User currUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RifkinC195Final.class.getResource("view/AddCustomer.fxml"));
            AnchorPane addCustomer = (AnchorPane) loader.load();
            sidebar.setCenter(addCustomer);
            AddCustomerController controller = loader.getController();
            controller.setAddCustomerScreen(this, currUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showUpdateAppointmentScreen(Appointment appt, User currUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RifkinC195Final.class.getResource("view/UpdateAppointment.fxml"));
            AnchorPane updateAppointment = (AnchorPane) loader.load();
            sidebar.setCenter(updateAppointment);
            UpdateAppointmentController controller = loader.getController();
            controller.setUpdateAppointmentScreen(this, currUser);
            controller.populateAppointmentFields(appt);

        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    
    public void showUpdateCustomerScreen(Customer cust, User currUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RifkinC195Final.class.getResource("view/UpdateCustomer.fxml"));
            AnchorPane updateCustomer = (AnchorPane) loader.load();
            sidebar.setCenter(updateCustomer);
            UpdateCustomerController controller = loader.getController();
            controller.setUpdateCustomerScreen(this, currUser);
            controller.populateCustomerFields(cust);

        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    
    public void showReportsScreen(User currUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RifkinC195Final.class.getResource("view/Reports.fxml"));
            AnchorPane reports = (AnchorPane) loader.load();
            sidebar.setCenter(reports);
            ReportsController controller = loader.getController();
            controller.setReportsScreen(this, currUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    

    public static void main(String[] args) {
        Database.openConnection();
        ActivityLog.startLog();
        launch(args);
        Database.closeConnection();
    }
    
}
