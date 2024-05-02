package wtf.triplapeeck.sinon.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class CommandHandler {
    private HashMap<String, Command> commands = new HashMap<String, Command>();
    private ArrayList<EventConsumer> consumers = new ArrayList<EventConsumer>();
    private final String prefix;
    public CommandHandler(@NotNull String prefix) {
        this.prefix = prefix;
    }
    public void registerCommand(@NotNull Command command) {
        commands.put(command.getFirstArgument().name, command);
    }
    public void registerConsumer(@NotNull EventConsumer consumer) {
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
                }
                return;
            }
        }
        for (EventConsumer consumer : consumers) {
            consumer.handleEvent(event, jda);
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
            }
        }
        for (EventConsumer consumer : consumers) {
            consumer.handleEvent(event, jda);
        }
    }
    public Collection<String> getCommands() {
        return commands.keySet();
    }
    public Command getCommand(@NotNull String name) {
        return commands.get(name);
    }
}
