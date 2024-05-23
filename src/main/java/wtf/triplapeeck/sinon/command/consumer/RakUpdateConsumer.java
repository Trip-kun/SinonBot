package wtf.triplapeeck.sinon.command.consumer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import wtf.triplapeeck.sinon.command.EventConsumer;
import wtf.triplapeeck.sinon.entity.ClosableEntity;
import wtf.triplapeeck.sinon.entity.GuildData;
import wtf.triplapeeck.sinon.entity.MemberData;
import wtf.triplapeeck.sinon.entity.UserData;
import wtf.triplapeeck.sinon.manager.DataManager;

import java.math.BigInteger;
import java.util.Random;

public class RakUpdateConsumer extends EventConsumer {
    public RakUpdateConsumer() {
    }
    private final Random random = new Random();
    @Override
    public void handleEvent(MessageReceivedEvent event, JDA jda) {
        String guildId;
        try {
            guildId = event.getGuild().getId();
        } catch (IllegalStateException e) {
            guildId = event.getAuthor().getId();
        }
        try (ClosableEntity<? extends GuildData> gData = DataManager.guildDataManager.getData(guildId)) {
            GuildData data = gData.getData();
            if (!data.isCurrencyEnabled()) {
                return;
            }
        }
        try (ClosableEntity<? extends MemberData> mData = DataManager.memberDataManager.getData(event.getAuthor().getId() + guildId)) {
            MemberData data = mData.getData();
            data.setMessageCount(data.getMessageCount() + 1);
            double lucky5int = random.nextInt(25)-12.5;
            boolean lucky5 = (lucky5int+data.getMessageCount()) > 50;
            int lucky5rak = random.nextInt(25)+25;
            if (!event.getAuthor().isBot() && lucky5) {
                data.setRak(data.getRak().add(BigInteger.valueOf(lucky5rak)));
                try (ClosableEntity<? extends UserData> uData = DataManager.userDataManager.getData(event.getAuthor().getId())) {
                    UserData userData = uData.getData();
                    if (userData.isCurrencyPreferenceOn()) {
                        event.getChannel().sendMessage("Here, have " + lucky5rak + " rak. Enjoy!").queue();
                    }
                }
                data.setMessageCount(0);
            }
        }
    }

    @Override // This method is not implemented
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
