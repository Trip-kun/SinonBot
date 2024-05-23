package wtf.triplapeeck.sinon.command.consumer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import wtf.triplapeeck.sinon.command.EventConsumer;
import wtf.triplapeeck.sinon.entity.ClosableEntity;
import wtf.triplapeeck.sinon.entity.CustomResponseData;
import wtf.triplapeeck.sinon.entity.GuildData;
import wtf.triplapeeck.sinon.manager.DataManager;

public class CustomResponseConsumer extends EventConsumer {
    public CustomResponseConsumer() {
    }
    @Override
    public void handleEvent(MessageReceivedEvent event, JDA jda) {
        String messageContentLower = event.getMessage().getContentRaw().toLowerCase();
        String guildId;
        try {
            guildId = event.getGuild().getId();
        } catch (IllegalStateException e) {
            guildId = event.getAuthor().getId();
        }
        try (ClosableEntity<? extends GuildData> gData = DataManager.guildDataManager.getData(guildId)) {
            GuildData guildData = gData.getData();
            for (CustomResponseData data : guildData.getCustomResponses()) {
                if (messageContentLower.contains(data.getTrigger())) {
                    event.getChannel().sendMessage(data.getResponse()).queue();
                }
            }
        }
    }
    // Not implemented
    @Override
    public void handleEvent(SlashCommandInteractionEvent event, JDA jda) {

    }

    @Override
    public void handleEvent(MessageReactionAddEvent event, JDA jda) {

    }

    @Override
    public void handleEvent(MessageReactionRemoveEvent event, JDA jda) {

    }

    @Override
    public void handleEvent(MessageReactionRemoveAllEvent event, JDA jda) {

    }

    @Override
    public void handleEvent(MessageReactionRemoveEmojiEvent event, JDA jda) {

    }
}
