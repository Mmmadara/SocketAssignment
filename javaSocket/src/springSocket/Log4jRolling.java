package springSocket;
import org.apache.log4j.Logger;
public class Log4jRolling {
    static Logger log = Logger.getLogger(Log4jRolling.class);

    public void logDebug(String message){
        log.debug(message);
    }

    public void logInfo(String message){
        log.info(message);
    }

    public void logError(String message){
        log.error(message);
    }
}