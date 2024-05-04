package wtf.triplapeeck.sinon.command.consumer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import wtf.triplapeeck.sinon.command.EventConsumer;
import wtf.triplapeeck.sinon.entity.ClosableEntity;
import wtf.triplapeeck.sinon.entity.CustomResponseData;
import wtf.triplapeeck.sinon.entity.GuildData;
import wtf.triplapeeck.sinon.manager.DataManager;

public class CustomResponseConsumer extends EventConsumer {
    @Override
    public void handleEvent(MessageReceivedEvent event, JDA jda) {
        String messageContentLower = event.getMessage().getContentRaw().toLowerCase();
        try (ClosableEntity<? extends GuildData> gData = DataManager.guildDataManager.getData(event.getGuild().getId())) {
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
}
