package wtf.triplapeeck.sinon.command.essential;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import wtf.triplapeeck.sinon.command.Argument;
import wtf.triplapeeck.sinon.command.Command;
import wtf.triplapeeck.sinon.command.ParsedArgument;
import wtf.triplapeeck.sinon.entity.ClosableEntity;
import wtf.triplapeeck.sinon.entity.GuildData;
import wtf.triplapeeck.sinon.manager.DataManager;

import java.util.ArrayList;
import java.util.Optional;

public class SetStarboard extends Command {
    public SetStarboard(JDA jda) {
        addArgument(new Argument("setstarboard", "o", true, Argument.Type.COMMAND, null));
        addArgument(new Argument("channel", "The channel to set as the starboard channel", true, Argument.Type.CHANNEL, null));
        init(jda);
    }
    @Override
    public void handler(MessageReceivedEvent event, JDA jda) {
        ensureGuild(event);
        ArrayList<ParsedArgument> parsedArguments = parseArguments(event);
        String s;
        if (event.isFromGuild()) {
            s = commonWork(jda, event.getGuild(), event.getMember(), parsedArguments.getFirst().getIntValue());
        } else {
            s = "This command can only be used in a guild";
        }
        event.getChannel().sendMessage(s).queue();
    }

    @Override
    public void handler(SlashCommandInteractionEvent event, JDA jda) {
        ensureGuild(event);
        ArrayList<ParsedArgument> parsedArguments = parseArguments(event);
        String s;
        if (event.isFromGuild()) {
            s = commonWork(jda, event.getGuild(), event.getMember(), parsedArguments.getFirst().getIntValue());
        } else {
            s = "This command can only be used in a guild";
        }
        event.getHook().sendMessage(s).queue();
    }
    private String commonWork(JDA jda, Guild guild, Member author, Long starboardChannelID) {
        Optional<String> isAdmin = ensureAdministrator(author);
        if (isAdmin.isPresent()) {
            return isAdmin.get();
        }
        if (starboardChannelID == null) {
            return "Please provide a channel ID";
        }
        if (jda.getGuildChannelById(starboardChannelID) == null) {
            return "Channel not found";
        }
        try (ClosableEntity<? extends GuildData> gData = DataManager.guildDataManager.getData(guild.getId())) {
            GuildData guildData = gData.getData();
            guildData.setStarboardChannelID(String.valueOf(starboardChannelID));
        }
        return "Starboard channel set";
    }

    @Override
    public String getDescription() {
        return "Set the starboard channel for the guild";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.ESSENTIAL;
    }
}
