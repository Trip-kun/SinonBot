package wtf.triplapeeck.sinon;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.reflections.Reflections;
import wtf.triplapeeck.sinon.command.Command;
import wtf.triplapeeck.sinon.command.CommandHandler;
import wtf.triplapeeck.sinon.command.EventConsumer;
import wtf.triplapeeck.sinon.command.consumer.RakUpdateConsumer;
import wtf.triplapeeck.sinon.command.essential.Ping;
import wtf.triplapeeck.sinon.command.essential.Test;
import wtf.triplapeeck.sinon.database.ORMLiteDatabaseUtil;
import wtf.triplapeeck.sinon.entity.ormlite.*;
import wtf.triplapeeck.sinon.manager.DataManager;
import wtf.triplapeeck.sinon.manager.ORMLiteManager;
import wtf.triplapeeck.sinon.manager.ThreadManager;
import wtf.triplapeeck.sinon.runnable.Heartbeat;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Logger.setLogLevel(Logger.Level.WARNING);
        ORMLiteDatabaseUtil.init();
        DataManager.channelDataManager = new ORMLiteManager<>(ORMLiteChannelData.class);
        DataManager.channelMemberDataManager = new ORMLiteManager<>(ORMLiteChannelMemberData.class);
        DataManager.customResponseDataManager = new ORMLiteManager<>(ORMLiteCustomResponseData.class);
        DataManager.deckCardDataManager = new ORMLiteManager<>(ORMLiteDeckCardData.class);
        DataManager.guildDataManager = new ORMLiteManager<>(ORMLiteGuildData.class);
        DataManager.memberDataManager = new ORMLiteManager<>(ORMLiteMemberData.class);
        DataManager.playerCardDataManager = new ORMLiteManager<>(ORMLitePlayerCardData.class);
        DataManager.playerSpotDataManager = new ORMLiteManager<>(ORMLitePlayerSpotData.class);
        DataManager.reminderDataManager = new ORMLiteManager<>(ORMLiteReminderData.class);
        DataManager.starboardEntryDataManager = new ORMLiteManager<>(ORMLiteStarboardEntryData.class);
        DataManager.userDataManager = new ORMLiteManager<>(ORMLiteUserData.class);
        ThreadManager.getInstance().addThread("ChannelDataManager", new Thread(DataManager.channelDataManager));
        ThreadManager.getInstance().addThread("ChannelMemberDataManager", new Thread(DataManager.channelMemberDataManager));
        ThreadManager.getInstance().addThread("CustomResponseDataManager", new Thread(DataManager.customResponseDataManager));
        ThreadManager.getInstance().addThread("DeckCardDataManager", new Thread(DataManager.deckCardDataManager));
        ThreadManager.getInstance().addThread("GuildDataManager", new Thread(DataManager.guildDataManager));
        ThreadManager.getInstance().addThread("MemberDataManager", new Thread(DataManager.memberDataManager));
        ThreadManager.getInstance().addThread("PlayerCardDataManager", new Thread(DataManager.playerCardDataManager));
        ThreadManager.getInstance().addThread("PlayerSpotDataManager", new Thread(DataManager.playerSpotDataManager));
        ThreadManager.getInstance().addThread("ReminderDataManager", new Thread(DataManager.reminderDataManager));
        ThreadManager.getInstance().addThread("StarboardEntryDataManager", new Thread(DataManager.starboardEntryDataManager));
        ThreadManager.getInstance().addThread("UserDataManager", new Thread(DataManager.userDataManager));
        JDA api;
        CommandHandler commandHandler = new CommandHandler(Config.getConfig().prefix);
        SinonListener sinonListener = new SinonListener(commandHandler);
        api = JDABuilder.createLight(Config.getConfig().token)
                .disableCache(new ArrayList<>(Arrays.asList(CacheFlag.values())))
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .addEventListeners(sinonListener)
                .build();
        Heartbeat.setJDA(api);
        ThreadManager.getInstance().addThread("Heartbeat", new Thread(Heartbeat.getInstance()));
        Reflections reflections = new Reflections("wtf.triplapeeck.sinon");
        Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> command : commands) {
            try {
                commandHandler.registerCommand(command.getDeclaredConstructor(JDA.class).newInstance(api));
            } catch (InvocationTargetException e2) {
                Throwable e = e2.getTargetException();
                Logger.log(Logger.Level.ERROR, "Failed to register command: " + command.getName());
                Logger.log(Logger.Level.ERROR, e.getMessage());
                Logger.log(Logger.Level.ERROR, e.getClass().getName());
                for (StackTraceElement element : e.getStackTrace()) {
                    Logger.log(Logger.Level.ERROR, element.toString());
                }
            } catch (Exception e) {
                Logger.log(Logger.Level.ERROR, "Failed to register command: " + command.getName());
                Logger.log(Logger.Level.ERROR, e.getMessage());
                Logger.log(Logger.Level.ERROR, e.getClass().getName());
                for (StackTraceElement element : e.getStackTrace()) {
                    Logger.log(Logger.Level.ERROR, element.toString());
                }
            }
        }
        Set<Class<? extends EventConsumer>> consumers = reflections.getSubTypesOf(EventConsumer.class);
        for (Class<? extends EventConsumer> consumer : consumers) {
            try {
                commandHandler.registerConsumer(consumer.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                Logger.log(Logger.Level.ERROR, "Failed to register consumer: " + consumer.getName());
                Logger.log(Logger.Level.ERROR, e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    Logger.log(Logger.Level.ERROR, element.toString());
                }
            }
        }
        try {
            api.awaitReady();
        } catch (InterruptedException ignored) {
        }
        Logger.log(Logger.Level.INFO, "Bot is ready");
    }
}