package wtf.triplapeeck.sinon.database;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import wtf.triplapeeck.sinon.Config;
import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.entity.AccessibleDataEntity;
import wtf.triplapeeck.sinon.entity.ormlite.ORMLiteChannelData;

public class ORMLiteDatabaseUtil {
    private static final int VERSION = 1;
    private static JdbcPooledConnectionSource connectionSource;
    private static ORMEntityDao<ORMLiteChannelData> channelDataDao;
    public static ORMEntityDao getDataDao(Class<? extends AccessibleDataEntity> c) {
        if (c==ORMLiteChannelData.class) {
            return channelDataDao;
        }
        return null;
    }
    public ORMLiteDatabaseUtil() {
        Config config = Config.getConfig();
        int tries = 0;
        boolean success = false;
        while (tries < config.maxRetries) {
            try {
                connectionSource = new JdbcPooledConnectionSource("jdbc:mariadb://" + config.dbUrl + ":" + config.dbPort + "/" + config.dbDatabase + "?user=" + config.dbUser + "&password=" + config.dbPass, config.dbType);
                success = true;
                break;
            } catch (Exception e) {
                tries++;
            }
        }
        if (!success) {
            Logger.log(Logger.Level.ERROR, "Failed to connect to database");
            throw new RuntimeException("Failed to connect to database");
        }
        connectionSource.setMaxConnectionsFree(config.maxConnections);
        channelDataDao = new ORMEntityDao<>(ORMLiteChannelData.class, connectionSource);
    }
    public static void upgrade() {
        Config config = Config.getConfig();
        if (config.version>VERSION) {
            Logger.log(Logger.Level.ERROR, "Database version is newer than the ORM version");
            throw new RuntimeException("Database version is newer than the ORM version");
        }
        switch (config.version) {
            case 1: // No break statement, fall through to next case to upgrade to the newest version
        }
    }
}
