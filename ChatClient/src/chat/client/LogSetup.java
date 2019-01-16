package chat.client;

import java.io.IOException;
import java.util.logging.*;

public class LogSetup {

    public LogSetup(Logger logger){
        setupLogger(logger);
    }

    public static void setupLogger(Logger logger){
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.OFF);
        logger.addHandler(ch);

        try {
            FileHandler fh = new FileHandler("ClientLog.log");
            fh.setLevel(Level.FINE);
            logger.addHandler(fh);
        } catch (IOException ex){
            logger.log(Level.SEVERE, "File logger not working", ex);
        }
    }
}
