package wtf.triplapeeck.sinon;
/**
 * Logger class for logging messages.
 */
public class Logger {
    private static Level logLevel = Level.INFO;
    public enum Level {
        INFO, // Used for general information
        WARNING, // Used for lower level errors
        ERROR, // Used for higher level errors
        FATAL; // Used for critical errors
        public String toString() {
            return this.name();
        }
    }
    /* Set the log level
     * Any log message with a level lower than the set level will not be printed
     * @param level The log level to set
     * @see Level
     */
    public static void setLogLevel(Level level) {
        logLevel = level;
    }
    public static void log(Level level, String message) {
        if (level.ordinal() >= logLevel.ordinal()) {
            System.out.println("[" + level + "] " + message);
        }
    }
}
