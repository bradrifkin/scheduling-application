package rifkinc195final.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {

    private final StringProperty appointmentId = new SimpleStringProperty("");
    private final StringProperty customerID = new SimpleStringProperty("");
    private final IntegerProperty userID = new SimpleIntegerProperty(0);
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty location = new SimpleStringProperty("");
    private final StringProperty contact = new SimpleStringProperty("");
    private final StringProperty type = new SimpleStringProperty("");
    private final StringProperty url = new SimpleStringProperty("");
    private final StringProperty start = new SimpleStringProperty("");
    private final StringProperty end = new SimpleStringProperty("");
    private Customer customer;
    private String user;
    
    // CONSTRUCTORS
    public Appointment() {
        this("", "", 0, "", "", "", "", "", "", "", "");
    }
    
    public Appointment(String appointmentId, String customerID, int userID, String title, String description, String location, String contact, String type, String url, String start, String end) {
        setAppointmentId(appointmentId);
        setCustomerID(customerID);
        setUserID(userID);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContact(contact);
        setType(type);
        setUrl(url);
        setStart(start);
        setEnd(end);
    }
    
    public Appointment(String appointmentId, String start, String end, String title, String type, String contact, String description, String url, Customer customer, String user) {
        setAppointmentId(appointmentId);    
        setStart(start);
        setEnd(end);
        setTitle(title);
        setType(type);
        setContact(contact);
        setDescription(description);
        setUrl(url);
        setCustomer(customer);
        setUser(user);
    }
   
    // GETTERS FOR PROPERTY VALUES
    public String getAppointmentId() {
        return appointmentId.get();
    }
    
    public String getCustomerID() {
        return customerID.get();
    }
    
    public int getUserID() {
        return userID.get();
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public String getDescription() {
        return description.get();
    }
    
    public String getLocation() {
        return location.get();
    }
    
    public String getContact() {
        return contact.get();
    }
    
    public String getType() {
        return type.get();
    }
    
    public String getUrl() {
        return url.get();
    }
    
    public String getStart() {
        return start.get();
    }
    
    public String getEnd() {
        return end.get();
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public String getUser() {
        return user;
    }
    
    // SETTERS FOR PROPERTY VALUES
    public void setAppointmentId(String appointmentId) {
        this.appointmentId.set(appointmentId);
    }
    
    public void setCustomerID(String customerID) {
        this.customerID.set(customerID);
    }
    
    public void setUserID(int userID) {
        this.userID.set(userID);
    }
    
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public void setDescription(String description) {
        this.description.set(description);
    }
    
    public void setLocation(String location) {
        this.location.set(location);
    }
    
    public void setContact(String contact) {
        this.contact.set(contact);
    }
    
    public void setType(String type) {
        this.type.set(type);
    }
    
    public void setUrl(String url) {
        this.url.set(url);
    }
    
    public void setStart(String start) {
        this.start.set(start);
    }
    
    public void setEnd(String end) {
        this.end.set(end);
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    // GETTERS FOR PROPERTY ITSELF
    public StringProperty appointmentIdProperty() {
        return appointmentId;
    }
    
    public StringProperty customerIDProperty() {
        return customerID;
    }
    
    public IntegerProperty userIDProperty() {
        return userID;
    }
    
    public StringProperty titleProperty() {
        return title;
    }
    
    public StringProperty descriptionProperty() {
        return description;
    }
    
    public StringProperty locationProperty() {
        return location;
    }
    
    public StringProperty contactProperty() {
        return contact;
    }
    
    public StringProperty typeProperty() {
        return type;
    }
    
    public StringProperty urlProperty() {
        return url;
    }
    
    public StringProperty startProperty() {
        return start;
    }
    
    public StringProperty endProperty() {
        return end;
    }

}
