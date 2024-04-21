package wtf.triplapeeck.sinon.command.essential;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.command.Command;

public class Ping extends Command {

    public Ping(JDA jda) {
        this.addArgument(new Argument("ping", "", true, Argument.Type.COMMAND, null));
        LoadCommand(jda);
    }

    @Override
    public void handler(MessageReceivedEvent event, JDA jda) {
        Logger.log(Logger.Level.INFO, "Ping command executed");
        event.getChannel().sendMessage("Pong!").queue();
    }

    @Override
    public void handler(SlashCommandInteractionEvent event, JDA jda) {
        Logger.log(Logger.Level.INFO, "Ping command executed");
        event.getHook().sendMessage("Pong!").queue();
    }

    @Override
    public String getDescription() {
        return "Simply replies with 'Pong!'";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.ESSENTIAL;
    }
}
