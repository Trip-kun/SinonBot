package wtf.triplapeeck.sinon.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public abstract class EventConsumer {
    public abstract void handleEvent(MessageReceivedEvent event, JDA jda);
    public abstract void handleEvent(SlashCommandInteractionEvent event, JDA jda);
    public abstract void handleEvent(MessageReactionAddEvent event, JDA jda);
    public abstract void handleEvent(MessageReactionRemoveEvent event, JDA jda);
    public abstract void handleEvent(MessageReactionRemoveAllEvent event, JDA jda);
    public abstract void handleEvent(MessageReactionRemoveEmojiEvent event, JDA jda);

}
