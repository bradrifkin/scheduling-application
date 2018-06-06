package rifkinc195final.model;

public class City {
    
    private int cityID;
    private String city;
    private int countryID;
    
    // CONSTRUCTORS
    public City() {}
    
    public City(int cityID, String city, int countryID) {
        this.cityID = cityID;
        this.city = city;
        this.countryID = countryID;
    }
    
    public City(int cityID, String city) {
        this.cityID = cityID;
        this.city = city;
    }
    
    // GETTERS FOR PROPERTY VALUES
    public int getCityID() {
        return cityID;
    }
    
    public String getCity() {
        return city;
    }
    
    public int getCountryID() {
        return countryID;
    }
    
    // SETTERS FOR PROPERTY VALUES
    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
    
    @Override
    public String toString() {
        return city;
    }
       
    
}
