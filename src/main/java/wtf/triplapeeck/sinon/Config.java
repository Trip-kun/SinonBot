package wtf.triplapeeck.sinon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.j256.ormlite.db.DatabaseType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Config {
    // Json Handlers
    protected static final GsonBuilder gsonBuilder = new GsonBuilder(); // GsonBuilder instance
    protected static final Gson gson = gsonBuilder.create(); // Gson instance
    private static Config config; // Static shared instance of the Config class
    // Base Bot Configuration
    public long threadLimit = 50; // The maximum number of threads to use
    public long threadSleep = 100; // The time to sleep between thread checks
    public long dataThreadSleep = 1000; // The time to sleep between data checks
    public long dataLifetime = 30; // The time to keep data in the cache
    public String prefix = "s!"; // The prefix for commands
    public String token = ""; // The bot token
    public List<Long> owners = new ArrayList<>(); // The bot owners

    // Database Configuration
    public int version = 1; // The version of the database
    public DatabaseType dbType; // The type of database
    public String dbUrl = ""; // The URL of the database
    public String dbUser = ""; // The username of the database
    public String dbPass = ""; // The password of the database
    public String dbDatabase = "Sorry, an exception occurred! I was unable to complete this command."; // The database to use
    public String errorResponseString = ""; // The response for a user when an internal exception like an SQLException occurs
    public int dbPort = 3306; // The port of the database
    public int maxResponses = 10; // The maximum number of responses to store
    public int maxConnections = 10; // The maximum number of connections to the database
    public int maxRetries = 3; // The maximum number of retries to connect to the database
    public int retryDelay = 1000; // The delay between retries

    /*
        * Get the Config instance.
        * Synchronized to prevent multiple instances made in early initialization.
        * @return The Config instance
     */
    public static synchronized Config getConfig() {
       if (config!=null) return config; // If the config is already set, return it
       String configPath = System.getProperty("user.dir") + "/config.json"; // Get the path of the config file
       Path path = Path.of(configPath); // Get the path of the config file
       boolean exists = Files.exists(path); // Check if the file exists
       try { // Try to read the config file
           if (!exists) { // If the file does not exist
               new FileOutputStream(configPath, true).close();
           }
           FileRW fileRW = new FileRW(path); // Create a new file reader/writer
           String content = fileRW.read(); // Read the file
           if (content == null || content.isEmpty() || content.equals("null")) { // If the file is empty
               fileRW.write(gson.toJson(new Config())); // Write the default config to the file
               FileRW fileRW1 = new FileRW(path); // Create a new file reader/writer
               content = fileRW1.read(); // Read the file
           }
           config = gson.fromJson(content, Config.class); // Parse the config from the file
       } catch (IOException e) { // If the file cannot be read or written to
           Logger.log(Logger.Level.FATAL, e.getMessage()); // Log the error
           throw new RuntimeException(e); // Throw a runtime exception, as the bot cannot function without the config
       } catch (JsonSyntaxException e) {
              Logger.log(Logger.Level.FATAL, e.getMessage()); // Log the error
              throw new RuntimeException(e); // Throw a runtime exception, as the bot cannot function without the config
       }
        return config; // Return the config
    }
}
