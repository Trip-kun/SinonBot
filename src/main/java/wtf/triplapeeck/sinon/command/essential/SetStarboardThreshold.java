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

public class SetStarboardThreshold extends Command {
    public SetStarboardThreshold(JDA jda) {
        addArgument(new Argument("setstarboardthreshold", "o", true, Argument.Type.COMMAND, null));
        addArgument(new Argument("threshold", "The threshold for the starboard", true, Argument.Type.UINT_OVER_ZERO, null));
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
        event.getChannel().sendMessage(s).queue();
    }
    private String commonWork(JDA jda, Guild guild, Member author, Long threshold) {
        Optional<String> isAdmin = ensureAdministrator(author);
        if (isAdmin.isPresent()) {
            return isAdmin.get();
        }
        try (ClosableEntity<? extends GuildData> gData = DataManager.guildDataManager.getData(guild.getId())) {
            GuildData data = gData.getData();
            try {
                data.setStarboardThreshold(Math.toIntExact(threshold));
            } catch (ArithmeticException e) {
                return "Threshold too large";
            }
            return "Threshold set to " + threshold;
        }

    }


    @Override
    public String getDescription() {
        return "Set the threshold for the starboard";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.ESSENTIAL;
    }
}
