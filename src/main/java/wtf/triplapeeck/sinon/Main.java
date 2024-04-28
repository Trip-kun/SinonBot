package wtf.triplapeeck.sinon;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.checkerframework.checker.units.qual.C;
import wtf.triplapeeck.sinon.command.CommandHandler;
import wtf.triplapeeck.sinon.command.essential.Ping;
import wtf.triplapeeck.sinon.command.essential.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Logger.setLogLevel(Logger.Level.WARNING);
        JDA api;
        CommandHandler commandHandler = new CommandHandler(Config.getConfig().prefix);
        SinonListener sinonListener = new SinonListener(commandHandler);
        api = JDABuilder.createLight(Config.getConfig().token)
                .disableCache(new ArrayList<>(Arrays.asList(CacheFlag.values())))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(sinonListener)
                .build();
        commandHandler.registerCommand(new Ping(api));
        commandHandler.registerCommand(new Test(api));
        try {
            api.awaitReady();
        } catch (InterruptedException ignored) {
        }
    }
}