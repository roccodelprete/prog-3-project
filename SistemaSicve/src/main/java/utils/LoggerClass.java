package utils;

/**
 * Class to represent the logger
 * @author Rocco Del Prete
 */
public class LoggerClass {
    /**
     * Enum to represent the log type
     */
    public enum LogType {
        INFO("INFO: "),
        ERROR("ERROR: ");

        private final String type;

        /**
         * LogType constructor
         * @param type The log type
         */
        LogType(String type) {
            this.type = type;
        }

        /**
         * Function to get the log type
         * @return The log type
         */
        public String getType() {
            return type;
        }
    }

    /**
     * Function to log a message
     * @param message The message to log
     * @param logType The log type
     */
    public static void log(String message, LogType logType) {
        System.out.println("[" + new java.util.Date() + "] " + logType + ": " + message);
    }
}
