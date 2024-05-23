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

public class RemoveStarboard extends Command {
    public RemoveStarboard(JDA jda) {
        addArgument(new Argument("removestarboard", "o", true, Argument.Type.COMMAND, null));
        init(jda);
    }
    @Override
    public void handler(MessageReceivedEvent event, JDA jda) {
        ensureGuild(event);
        String s;
        if (event.isFromGuild()) {
            s = commonWork(jda, event.getGuild(), event.getMember());
        } else {
            s = "This command can only be used in a guild";
        }
        event.getChannel().sendMessage(s).queue();
    }

    @Override
    public void handler(SlashCommandInteractionEvent event, JDA jda) {
        ensureGuild(event);
        String s;
        if (event.isFromGuild()) {
            s = commonWork(jda, event.getGuild(), event.getMember());
        } else {
            s = "This command can only be used in a guild";
        }
        event.getHook().sendMessage(s).queue();
    }
    private String commonWork(JDA jda, Guild guild, Member author) {
        Optional<String> isAdmin = ensureAdministrator(author);
        if (isAdmin.isPresent()) {
            return isAdmin.get();
        }
        try (ClosableEntity<? extends GuildData> gData = DataManager.guildDataManager.getData(guild.getId())) {
            GuildData guildData = gData.getData();
            if (guildData.getStarboardChannelID() == null) {
                return "Starboard channel not set";
            }
            guildData.setStarboardChannelID(null);
            return "Starboard channel removed";
        }
    }
    @Override
    public String getDescription() {
        return "Remove the starboard channel";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.ESSENTIAL;
    }
}
