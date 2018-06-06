package rifkinc195final.model;

public class Address {
    
    private Integer addressID;
    private String address;
    private String address2;
    private int cityID;
    private String postalCode;
    private String phone;
    
    // CONSTRUCTORS
    public Address() {}
    
    public Address(Integer addressID) {
        this.addressID = addressID;
    }
    
    public Address(Integer addressID, String address, String address2, int cityID, String postalCode, String phone) {
        this.addressID = addressID;
        this.address = address;
        this.address2 = address2;
        this.cityID = cityID;
        this.postalCode = postalCode;
        this.phone = phone;
    }
    
    // GETTERS FOR PROPERTY VALUES
    public int getAddressID() {
        return addressID;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getAddress2() {
        return address2;
    }
    
    public int getCityID() {
        return cityID;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public String getPhone() {
        return phone;
    }
    
    // SETTERS FOR PROPERTY VALUES
    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addressID != null ? addressID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.addressID == null && other.addressID != null) || (this.addressID != null && !this.addressID.equals(other.addressID))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "model.Address[ addressID=" + addressID + " ]";
    }
    
}
