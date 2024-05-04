package wtf.triplapeeck.sinon.database;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import wtf.triplapeeck.sinon.Config;
import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.entity.AccessibleDataEntity;
import wtf.triplapeeck.sinon.entity.ChannelMemberData;
import wtf.triplapeeck.sinon.entity.ormlite.*;

import java.sql.SQLException;

public class ORMLiteDatabaseUtil {
    private static final int VERSION = 1;
    private static JdbcPooledConnectionSource connectionSource;
    private static ORMEntityDao<ORMLiteChannelData> channelDataDao;
    private static ORMEntityDao<ORMLiteChannelMemberData> channelMemberDao;
    private static ORMEntityDao<ORMLiteCustomCommandData> customCommandDao;
    private static ORMEntityDao<ORMLiteCustomResponseData> customResponseDao;
    private static ORMEntityDao<ORMLiteDeckCardData> deckCardDao;
    private static ORMEntityDao<ORMLiteGuildData> guildDao;
    private static ORMEntityDao<ORMLiteMemberData> memberDao;
    private static ORMEntityDao<ORMLitePlayerCardData> playerCardDao;
    private static ORMEntityDao<ORMLitePlayerSpotData> playerSpotDao;
    private static ORMEntityDao<ORMLiteReminderData> reminderDao;
    private static ORMEntityDao<ORMLiteStarboardEntryData> starboardEntityDao;
    private static ORMEntityDao<ORMLiteUserData> userDao;
    public static ORMEntityDao getDataDao(Class<? extends AccessibleDataEntity> c) {
        if (c==ORMLiteChannelData.class) {
            return channelDataDao;
        }
        if (c==ORMLiteChannelMemberData.class) {
            return channelMemberDao;
        }
        if (c==ORMLiteCustomCommandData.class) {
            return customCommandDao;
        }
        if (c==ORMLiteCustomResponseData.class) {
            return customResponseDao;
        }
        if (c==ORMLiteDeckCardData.class) {
            return deckCardDao;
        }
        if (c==ORMLiteGuildData.class) {
            return guildDao;
        }
        if (c==ORMLiteMemberData.class) {
            return memberDao;
        }
        if (c==ORMLitePlayerCardData.class) {
            return playerCardDao;
        }
        if (c==ORMLitePlayerSpotData.class) {
            return playerSpotDao;
        }
        if (c==ORMLiteReminderData.class) {
            return reminderDao;
        }
        if (c==ORMLiteStarboardEntryData.class) {
            return starboardEntityDao;
        }
        if (c==ORMLiteUserData.class) {
            return userDao;
        }
        return null;
    }
    public static void init() {
        Config config = Config.getConfig();
        int tries = 0;
        boolean success = false;
        while (tries < config.maxRetries) {
            try {
                connectionSource = new JdbcPooledConnectionSource("jdbc:mariadb://" + config.dbUrl + ":" + config.dbPort + "/" + config.dbDatabase + "?user=" + config.dbUser + "&password=" + config.dbPass, config.dbType);
                success = true;
                break;
            } catch (SQLException e) {
                tries++;
            }
        }
        if (!success) {
            Logger.log(Logger.Level.ERROR, "Failed to connect to database");
            throw new RuntimeException("Failed to connect to database");
        }
        connectionSource.setMaxConnectionsFree(config.maxConnections);
        channelDataDao = new ORMEntityDao<>(ORMLiteChannelData.class, connectionSource);
        channelMemberDao = new ORMEntityDao<>(ORMLiteChannelMemberData.class, connectionSource);
        customCommandDao = new ORMEntityDao<>(ORMLiteCustomCommandData.class, connectionSource);
        customResponseDao = new ORMEntityDao<>(ORMLiteCustomResponseData.class, connectionSource);
        deckCardDao = new ORMEntityDao<>(ORMLiteDeckCardData.class, connectionSource);
        guildDao = new ORMEntityDao<>(ORMLiteGuildData.class, connectionSource);
        memberDao = new ORMEntityDao<>(ORMLiteMemberData.class, connectionSource);
        playerCardDao = new ORMEntityDao<>(ORMLitePlayerCardData.class, connectionSource);
        playerSpotDao = new ORMEntityDao<>(ORMLitePlayerSpotData.class, connectionSource);
        reminderDao = new ORMEntityDao<>(ORMLiteReminderData.class, connectionSource);
        starboardEntityDao = new ORMEntityDao<>(ORMLiteStarboardEntryData.class, connectionSource);
        userDao = new ORMEntityDao<>(ORMLiteUserData.class, connectionSource);
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
