package wtf.triplapeeck.sinon.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class EventConsumer {
    public abstract void handleEvent(MessageReceivedEvent event, JDA jda);
    public abstract void handleEvent(SlashCommandInteractionEvent event, JDA jda);
}
