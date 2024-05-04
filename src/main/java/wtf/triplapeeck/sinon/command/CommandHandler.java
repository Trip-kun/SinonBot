package wtf.triplapeeck.sinon.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.Config;
import wtf.triplapeeck.sinon.database.DatabaseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class CommandHandler {
    private static HashMap<String, Command> commands = new HashMap<String, Command>();
    private static ArrayList<EventConsumer> consumers = new ArrayList<EventConsumer>();
    private final String prefix;
    public CommandHandler(@NotNull String prefix) {
        this.prefix = prefix;
    }
    public static void registerCommand(@NotNull Command command) {
        commands.put(command.getFirstArgument().name, command);
    }
    public static void registerConsumer(@NotNull EventConsumer consumer) {
        consumers.add(consumer);
    }
    public void handle(@NotNull MessageReceivedEvent event, @NotNull JDA jda) {
        if (event.getAuthor().getIdLong()==jda.getSelfUser().getIdLong()) {
            return;
        }
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            String[] args = event.getMessage().getContentRaw().substring(prefix.length()).split("\\s+");
            if (commands.containsKey(args[0])) {
                try {
                    commands.get(args[0]).handler(event, jda);
                } catch (IllegalArgumentException e)
                {
                    event.getChannel().sendMessage(e.getMessage()).queue();
                } catch (DatabaseException e) {
                    event.getChannel().sendMessage(Config.getConfig().errorResponseString + ". The following error occurred: " + e.getMessage()).queue();
                }
                return;
            }
        }
        for (EventConsumer consumer : consumers) {
            try {
                consumer.handleEvent(event, jda);
            } catch (DatabaseException e) {
                event.getChannel().sendMessage(Config.getConfig().errorResponseString + ". The following error occurred: " + e.getMessage()).queue();
            }
        }

    }
    public void handle(@NotNull SlashCommandInteractionEvent event, @NotNull JDA jda) {
        event.deferReply().queue();
        if (commands.containsKey(event.getName())) {
            try {
                commands.get(event.getName()).handler(event, jda);
            } catch (IllegalArgumentException e)
            {
                event.getHook().sendMessage(e.getMessage()).queue();
            } catch (DatabaseException e) {
                event.getHook().sendMessage(Config.getConfig().errorResponseString + ". The following error occurred: " + e.getMessage()).queue();
            }
        }
        for (EventConsumer consumer : consumers) {
            try {
                consumer.handleEvent(event, jda);
            } catch (RuntimeException e) {
                event.getHook().sendMessage(Config.getConfig().errorResponseString + ". The following error occurred: " + e.getMessage()).queue();
            }
        }
    }
    public static HashMap<String, Command> getCommands() {
        return commands;
    }
    public static Command getCommand(@NotNull String name) {
        return commands.get(name);
    }
}
