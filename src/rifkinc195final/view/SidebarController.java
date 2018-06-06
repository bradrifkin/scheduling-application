package rifkinc195final.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import rifkinc195final.RifkinC195Final;
import rifkinc195final.model.User;

public class SidebarController {

    @FXML
    private Button sidebarAppointmentsBtn;
    @FXML
    private Button sidebarCustomersBtn;
    @FXML
    private Button sidebarReportsBtn;
    
    private RifkinC195Final mainApp;
    private User currUser;
    
    
    @FXML
    private void sidebarAppointmentsBtnHandler(ActionEvent event) {
        mainApp.showMyAppointmentsScreen(currUser);
    }

    @FXML
    private void sidebarCustomersBtnHandler(ActionEvent event) {
        mainApp.showCustomersScreen(currUser);
    }

    @FXML
    private void sidebarReportsBtnHandler(ActionEvent event) {
        mainApp.showReportsScreen(currUser);
    }
    
    public void setSidebar(RifkinC195Final mainApp, User currUser) {
	this.mainApp = mainApp;
        this.currUser = currUser;
    }
    
}
