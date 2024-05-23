package wtf.triplapeeck.sinon;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.command.CommandHandler;

public class SinonListener extends ListenerAdapter {
    private final CommandHandler handler;
    public SinonListener(CommandHandler handler) {
        this.handler=handler;
    }
    private JDA jda;
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Logger.log(Logger.Level.INFO, "Sinon is ready");
        jda=event.getJDA();
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Logger.log(Logger.Level.INFO, "Message received");
        handler.handle(event, jda);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        handler.handle(event, jda);
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        handler.handle(event, jda);
    }
    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        handler.handle(event, jda);
    }
    @Override
    public void onMessageReactionRemoveAll(@NotNull MessageReactionRemoveAllEvent event) {
        handler.handle(event, jda);
    }
    @Override
    public void onMessageReactionRemoveEmoji(@NotNull MessageReactionRemoveEmojiEvent event) {
        handler.handle(event, jda);
    }

}
