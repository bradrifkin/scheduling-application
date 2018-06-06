package rifkinc195final.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private final IntegerProperty userID = new SimpleIntegerProperty(0);
    private final StringProperty userName = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    
    // CONSTRUCTORS
    public User() {
        this(0, "", "");
    }
    
    public User(int userID, String userName, String password) {
        setUserID(userID);
        setUserName(userName);
        setPassword(password);
    }
    
    public User(int userID, String userName) {
        setUserID(userID);
        setUserName(userName);
    }
    
    // GETTERS FOR PROPERTY VALUES
    public int getUserID() {
        return userID.get();
    }
    
    public String getUserName() {
        return userName.get();
    }
    
    public String getPassword() {
        return password.get();
    }
    
    // SETTERS FOR PROPERTY VALUES
    public void setUserID(int userID) {
        this.userID.set(userID);
    }
    
    public void setUserName(String userName) {
        this.userName.set(userName);
    }
    
    public void setPassword(String password) {
        this.password.set(password);
    }
    
    // GETTERS FOR PROPERTY ITSELF
    public IntegerProperty userIDProperty() {
        return userID;
    }
    
    public StringProperty userNameProperty() {
        return userName;
    }
    
    public StringProperty passwordProperty() {
        return password;
    }
    
}
