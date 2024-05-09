package wtf.triplapeeck.sinon.command.essential;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import wtf.triplapeeck.sinon.command.Argument;
import wtf.triplapeeck.sinon.command.Command;
import wtf.triplapeeck.sinon.command.ParsedArgument;
import wtf.triplapeeck.sinon.entity.ClosableEntity;
import wtf.triplapeeck.sinon.entity.CustomResponseData;
import wtf.triplapeeck.sinon.entity.GuildData;
import wtf.triplapeeck.sinon.manager.DataManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class RemoveResponse extends Command {
    private static final int MIN_TRIGGER_LENGTH=3;
    private static final int MAX_TRIGGER_LENGTH=10;
    public RemoveResponse(JDA jda) {
        addArgument(new Argument("removeresponse", "o", true, Argument.Type.COMMAND, null));
        addArgument(new Argument("word", "The response trigger to remove", true, Argument.Type.WORD, null));
        init(jda);
    }
    @Override
    public void handler(MessageReceivedEvent event, JDA jda) {
        ArrayList<ParsedArgument> parsedArguments = parseArguments(event);
        String guildID;
        if (event.isFromGuild()) {
            Optional<String> admin = ensureAdministrator(event.getMember());
            if (admin.isPresent()) {
                event.getChannel().sendMessage(admin.get()).queue();
                return;
            }
            guildID = event.getGuild().getId();
        } else {
            guildID = event.getChannel().getId();
        }
        String s = commonWork(guildID, parsedArguments.getFirst().getStringValue());
        event.getChannel().sendMessage(s).queue();
    }

    @Override
    public void handler(SlashCommandInteractionEvent event, JDA jda) {
        ArrayList<ParsedArgument> parsedArguments = parseArguments(event);
        String guildID;
        if (event.isFromGuild()) {
            Optional<String> admin = ensureAdministrator(event.getMember());
            if (admin.isPresent()) {
                event.getHook().sendMessage(admin.get()).queue();
                return;
            }
            guildID = event.getGuild().getId();
        } else {
            guildID = event.getChannel().getId();
        }
        String s = commonWork(guildID, parsedArguments.getFirst().getStringValue());
        event.getHook().sendMessage(s).queue();
    }
    private String commonWork(String guildID, String trigger) {
        try(ClosableEntity<? extends GuildData> gData = DataManager.guildDataManager.getData(guildID)) {
            if (trigger.length()<MIN_TRIGGER_LENGTH) {
                return "The trigger must be at least " + MIN_TRIGGER_LENGTH + " characters";
            }
            if (trigger.length()>MAX_TRIGGER_LENGTH) {
                return "The trigger must be no more than " + MAX_TRIGGER_LENGTH + " characters";
            }
            Collection<? extends CustomResponseData> list = gData.getData().getCustomResponses();
            for (CustomResponseData data : list) {
                if (data.getTrigger().equals(trigger)) {
                    gData.getData().removeCustomResponse(data);
                    return "Successfully removed trigger: " + trigger;
                }
            }
            return "Trigger " + trigger + " not found!";
        }
    }

    @Override
    public String getDescription() {
        return "Removes a custom response trigger!";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.ESSENTIAL;
    }
}
