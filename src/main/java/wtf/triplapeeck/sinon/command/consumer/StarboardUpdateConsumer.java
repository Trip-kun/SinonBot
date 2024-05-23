package wtf.triplapeeck.sinon.command.consumer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.*;
import net.dv8tion.jda.api.exceptions.ContextException;
import wtf.triplapeeck.sinon.command.EventConsumer;
import wtf.triplapeeck.sinon.entity.ClosableEntity;
import wtf.triplapeeck.sinon.entity.GuildData;
import wtf.triplapeeck.sinon.entity.StarboardEntryData;
import wtf.triplapeeck.sinon.manager.DataManager;
import wtf.triplapeeck.sinon.manager.ThreadManager;

import java.util.concurrent.atomic.AtomicInteger;

public class StarboardUpdateConsumer extends EventConsumer {
    public StarboardUpdateConsumer() {
    }
    private void updateMessage(Message message, GuildData data, MessageChannel channel, int threshold, int count) {
        try (ClosableEntity<? extends StarboardEntryData> entryData = DataManager.starboardEntryDataManager.getData(message.getId())) {
            StarboardEntryData entry = entryData.getData();
            channel.retrieveMessageById(entry.getStarboardMessageID()).queue(starboardMessage -> {
                if (count < threshold) {
                    starboardMessage.delete().queue();
                    DataManager.starboardEntryDataManager.removeData(entry.getID());
                    data.removeStarboardEntry(entry);
                } else {
                    starboardMessage.editMessage("Stars: " + count + " " + message.getChannel().getAsMention()).queue();
                }
            }, (error) -> {
                if (error instanceof ContextException) {
                    DataManager.starboardEntryDataManager.removeData(entry.getID());
                    data.removeStarboardEntry(entry);
                    createMessage(message, (TextChannel) channel, count);
                }
            });
        }
    }
    private Message createMessage(Message message, MessageChannel channel, int count) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(message.getAuthor().getEffectiveName(), null, message.getAuthor().getEffectiveAvatarUrl());
        builder.addField("Original Message", "[Jump!](" +message.getJumpUrl() +")", false);
        for (Message.Attachment attachment : message.getAttachments()) {
            builder.setImage(attachment.getUrl());
        }
        builder.setTimestamp(message.getTimeCreated());
        builder.setDescription(message.getContentRaw());
        builder.setFooter(message.getId());
        return channel.sendMessage("Stars: " + count + " " + message.getChannel().getAsMention()).addEmbeds(builder.build()).complete();
    }
    private static final Emoji starEmoji = Emoji.fromUnicode("U+2b50");
    @Override
    public void handleEvent(MessageReceivedEvent event, JDA jda) {

    }

    @Override
    public void handleEvent(SlashCommandInteractionEvent event, JDA jda) {

    }

    @Override
    public void handleEvent(MessageReactionAddEvent event, JDA jda) {
        if (!event.isFromGuild()) { return; }
        handleBasicReaction(event.isFromGuild(), event.getReaction(), event.getGuild(), event.getUserId(), event.getChannel(), event.getMessageId(), event);
    }

    @Override
    public void handleEvent(MessageReactionRemoveEvent event, JDA jda) {
        if (!event.isFromGuild()) { return; }
        handleBasicReaction(event.isFromGuild(), event.getReaction(), event.getGuild(), event.getUserId(), event.getChannel(), event.getMessageId(), event);
    }

    private void handleBasicReaction(boolean fromGuild, MessageReaction reaction, Guild guild, String userId, MessageChannelUnion channel, String messageId, GenericMessageReactionEvent event) {
        if (!fromGuild) {
            return;
        }
        if (!reaction.getEmoji().equals(starEmoji)) {
            return;
        }
        ThreadManager.getInstance().addThread("StarboardUpdateConsumer", new Thread(() -> {
            try (ClosableEntity<? extends GuildData> gData = DataManager.guildDataManager.getData(guild.getId())) {
                GuildData data = gData.getData();
                if (channel.getId().equals(data.getStarboardChannelID()) || data.getStarboardChannelID() == null) {
                    return;
                }
                Message message = channel.retrieveMessageById(messageId).complete();
                if (!message.getEmbeds().isEmpty()) {
                    return;
                }
                AtomicInteger count= new AtomicInteger();
                reaction.retrieveUsers().complete().forEach(user -> {
                    if (!user.getId().equals(message.getAuthor().getId())) {
                        count.getAndIncrement();
                    }
                });
                if (data.getStarboardEntries().stream().anyMatch(entry -> entry.getID().equals(messageId))) {
                    updateMessage(channel.retrieveMessageById(messageId).complete(), data, guild.getTextChannelById(Long.valueOf(data.getStarboardChannelID())), data.getStarboardThreshold(), count.get());
                    return;
                }
                if (count.get() < data.getStarboardThreshold()) {
                    return;
                }
                Message starboardEntry = createMessage(message, guild.getTextChannelById(Long.valueOf(data.getStarboardChannelID())), count.get());
                try (ClosableEntity<? extends StarboardEntryData> entryData = DataManager.starboardEntryDataManager.getData(messageId)) {
                    StarboardEntryData entry = entryData.getData();
                    entry.setStarboardMessageID(starboardEntry.getId());
                    entry.setOriginalMessageID(messageId);
                    data.addStarboardEntry(entry);
                }
            }
        }));
    }

    @Override
    public void handleEvent(MessageReactionRemoveAllEvent event, JDA jda) {
        if (!event.isFromGuild()) {
            return;
        }
        ThreadManager.getInstance().addThread("StarboardUpdateConsumer", new Thread(() -> {
            try (ClosableEntity<? extends GuildData> gData = DataManager.guildDataManager.getData(event.getGuild().getId())) {
                GuildData data = gData.getData();
                if (data.getStarboardEntries().stream().anyMatch(entry -> entry.getID().equals(event.getMessageId()))) {
                    updateMessage(event.getChannel().retrieveMessageById(event.getMessageId()).complete(), data, event.getChannel(), data.getStarboardThreshold(), 0);
                }
            }
        }));
    }

    @Override
    public void handleEvent(MessageReactionRemoveEmojiEvent event, JDA jda) {

    }
}
