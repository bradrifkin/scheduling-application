package rifkinc195final.model;

public class AppointmentforReports {
    
    private String month;
    private String type;
    private int count;
    private Customer customer;
    
    // CONSTRUCTORS
    public AppointmentforReports() {}
    
    public AppointmentforReports(String month, String type, int count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }
    
    public AppointmentforReports(Customer customer, String type, int count) {
        this.customer = customer;
        this.type = type;
        this.count = count;
    }
    
    // GETTERS FOR PROPERTY VALUES
    public String getMonth() {
        return month;
    }
    
    public String getType() {
        return type;
    }
    
    public int getCount() {
        return count;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    // SETTERS FOR PROPERTY VALUES
    public void setMonth(String month) {
        this.month = month;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
}
