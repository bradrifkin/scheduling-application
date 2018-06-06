package rifkinc195final.util;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class ActivityLog {
 
    private static FileHandler handler = null;
    private final static Logger LOGGER = Logger.getLogger(ActivityLog.class.getName());
 
    public static void startLog(){
        try {
            handler = new FileHandler("Activity Log.%u.%g.txt", 960 * 960, 25, true);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        
        Logger logger = Logger.getLogger("");
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
        logger.setLevel(Level.INFO);
    }
    
}
