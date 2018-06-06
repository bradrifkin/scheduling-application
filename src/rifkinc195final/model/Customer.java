package rifkinc195final.model;

public class Customer {
    
    private String customerID;
    private String customerName;
    private String address;
    private String address2;
    private City city;
    private String country;
    private String postalCode;
    private String phone;
    private int addressID;
    private Address customerAddress;
    
    // CONSTRUCTORS
    public Customer(){}
    
    public Customer(String customerID, String customerName, String address, String address2, City city, String country, String postalCode, String phone, int addressID, Address customerAddress) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.phone = phone;
        this.addressID = addressID;
        this.customerAddress = customerAddress;
    }
    
    public Customer(String customerID, String customerName, String address, String address2, City city, String country, String postalCode, String phone) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.phone = phone;
    }
    
    public Customer (String customerID, String customerName){
        this.customerID = customerID;
        this.customerName = customerName;
    }

    // GETTERS FOR PROPERTY VALUES
    public String getCustomerID() {
        return customerID;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public String getAddress() {
        return address;
    }

    public String getAddress2() {
        return address2;
    }

    public City getCity() {
        return city;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public int getAddressID() {
        return addressID;
    }
    
    public Address getCustomerAddress() {
        return customerAddress;
    }
    
    public int getCityID(City object) {
        return object.getCityID();
    }
    
    // SETTERS FOR PROPERTY VALUES
    public void setCustomerId(String customerID) {
        this.customerID = customerID;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    public void setCity(City city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }
    
    public void setCustomerAddress(Address customerAddress) {
        this.customerAddress = customerAddress;
    }
          

    @Override
    public String toString() {
        return customerName;
    }
    
}