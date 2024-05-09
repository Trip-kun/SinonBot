package wtf.triplapeeck.sinon.command.essential;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import wtf.triplapeeck.sinon.Config;
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

public class NewResponse extends Command {
    private static final int MIN_TRIGGER_LENGTH=3;
    private static final int MAX_TRIGGER_LENGTH=10;
    public NewResponse(JDA jda) {
        addArgument(new Argument("newresponse", "o", true, Argument.Type.COMMAND, null));
        addArgument(new Argument("word", "The word to respond to", true, Argument.Type.WORD, null));
        addArgument(new Argument("response", "What to respond with.", true, Argument.Type.TEXT, null));
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
        String s = commonWork(guildID, parsedArguments.getFirst().getStringValue(), parsedArguments.get(1).getStringValue());
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
        String s = commonWork(guildID, parsedArguments.getFirst().getStringValue(), parsedArguments.get(1).getStringValue());
        event.getHook().sendMessage(s).queue();
    }
    private String commonWork(String guildID, String trigger, String response) {
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
                    data.setResponse(response);
                    return "Successfully updated trigger: " + trigger;
                }
            }
            if (list.size()>= Config.getConfig().maxResponses) {
                return "You can only have " + Config.getConfig().maxResponses + " custom responses! Search time isn't cheap, after all!";
            }
            CustomResponseData newData = DataManager.customResponseDataManager.getUncachedData(null);
            newData.setGuild(gData.getData());
            newData.setTrigger(trigger);
            newData.setResponse(response);
            gData.getData().addCustomResponse(newData);
            return "Successful added trigger: " + trigger;
        }
    }


    @Override
    public String getDescription() {
        return "Adds a word that will cause me to respond to any message that contains it with a custom response";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.ESSENTIAL;
    }
}
