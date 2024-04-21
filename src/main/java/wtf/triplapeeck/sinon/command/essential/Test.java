package wtf.triplapeeck.sinon.command.essential;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import wtf.triplapeeck.sinon.Config;
import wtf.triplapeeck.sinon.command.Command;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Test extends Command {
    public Test(JDA jda) {
        this.addArgument(new Argument("test", "o", true, Argument.Type.COMMAND, null));
        ArrayList<String> choices = new ArrayList<String>();
        choices.add("test1");
        choices.add("test2");
        this.addArgument(new Argument("test2", "a", true, Argument.Type.SUBCOMMAND,choices));
        this.addArgument(new Argument("test3", "b", true, Argument.Type.WORD, null));
        this.addArgument(new Argument("test4", "c", true, Argument.Type.INTEGER, null));
        this.addArgument(new Argument("test5", "d", true, Argument.Type.UINT, null));
        this.addArgument(new Argument("test6", "e", true, Argument.Type.UINT_OVER_ZERO, null));
        this.addArgument(new Argument("test7", "f", true, Argument.Type.BOOLEAN, null));
        this.addArgument(new Argument("test8", "g", true, Argument.Type.USER, null));
        this.addArgument(new Argument("test9", "h", true, Argument.Type.CHANNEL, null));
        this.addArgument(new Argument("test10", "i", true, Argument.Type.ROLE, null));
        this.addArgument(new Argument("test11", "j", true, Argument.Type.DECIMAL, null));
        this.addArgument(new Argument("test12", "k", true, Argument.Type.UDECIMAL, null));
        this.addArgument(new Argument("test13", "l", true, Argument.Type.UDECIMAL_OVER_ZERO, null));
        this.addArgument(new Argument("test14", "m", true, Argument.Type.ATTACHMENT, null));
        this.addArgument(new Argument("test15", "n", true, Argument.Type.TEXT, null));

        LoadCommand(jda);
    }
    @Override
    public void handler(MessageReceivedEvent event, JDA jda) {
        ArrayList<Long> roles = new ArrayList<Long>();
        ArrayList<Long> channels = new ArrayList<Long>();
        ArrayList<Long> users = new ArrayList<Long>();
        for (int i = 0; i < event.getMessage().getMentions().getRoles().size(); i++) {
            roles.add(event.getMessage().getMentions().getRoles().get(i).getIdLong());
        }
        for (int i = 0; i < event.getMessage().getMentions().getChannels().size(); i++) {
            channels.add(event.getMessage().getMentions().getChannels().get(i).getIdLong());
        }
        for (int i = 0; i < event.getMessage().getMentions().getUsers().size(); i++) {
            users.add(event.getMessage().getMentions().getUsers().get(i).getIdLong());
        }
        ArrayList<ParsedArgument> args = parseArguments(event);
        StringBuilder s = new StringBuilder();
        for (ParsedArgument arg : args) {
            if (arg.getBoolValue()!=null) {
                s.append(arg.getBoolValue().toString()).append("\n");
            }
            if (arg.getIntValue()!=null) {
                s.append(arg.getIntValue().toString()).append("\n");
            }
            if (arg.getDoubleValue()!=null) {
                s.append(arg.getDoubleValue().toString()).append("\n");
            }
            if (arg.getStringValue()!=null) {
                s.append(arg.getStringValue()).append("\n");
            }
            if (arg.getAttachmentValue()!=null) {
                s.append("Attachment\n");
            }
        }
        event.getChannel().sendMessage(s.toString()).queue();
    }

    @Override
    public void handler(SlashCommandInteractionEvent event, JDA jda) {
         ArrayList<ParsedArgument> args = parseArguments(event);
         StringBuilder s = new StringBuilder();
         for (ParsedArgument arg : args) {
             if (arg.getBoolValue()!=null) {
                 s.append(arg.getBoolValue().toString()).append("\n");
             }
             if (arg.getIntValue()!=null) {
                 s.append(arg.getIntValue().toString()).append("\n");
             }
             if (arg.getDoubleValue()!=null) {
                 s.append(arg.getDoubleValue().toString()).append("\n");
             }
             if (arg.getStringValue()!=null) {
                 s.append(arg.getStringValue()).append("\n");
             }
             if (arg.getAttachmentValue()!=null) {
                s.append("Attachment\n");
            }
         }
         event.getHook().sendMessage(s.toString()).queue();
    }

    @Override
    public String getDescription() {
        return "NULL";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.ESSENTIAL;
    }
}
