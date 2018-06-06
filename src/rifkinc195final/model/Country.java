package rifkinc195final.model;

public class Country {

    private int countryID;
    private String country;
    
    // CONSTRUCTORS
    public Country() {}
    
    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }
    
    // GETTERS FOR PROPERTY VALUES
    public int getCountryID() {
        return countryID;
    }
    
    public String getCountry() {
        return country;
    }
    
    // SETTERS FOR PROPERTY VALUES
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
          
}
