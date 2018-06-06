package rifkinc195final.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    
    private static Connection conn;
  
    // CONSTRUCTOR
    public Database() {
        
    }
    
    public static void openConnection() {
        // Connection String
        // Server name: 52.206.157.109
        // Database name: U04YSW
        // Username: U04YSW
        // Password: 53688380342
        
        // JDBC driver name and database URL
        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://52.206.157.109/U04YSW";

        //  Database credentials
        final String DBUSER = "U04YSW";
        final String DBPASS = "53688380342";
      
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("The database connection is closed.");
        }
    }
    
    public static Connection getConnection(){
            return conn;
    }
    
}
