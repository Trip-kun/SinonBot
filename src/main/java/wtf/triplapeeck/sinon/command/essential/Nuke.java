package wtf.triplapeeck.sinon.command.essential;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import wtf.triplapeeck.sinon.command.Argument;
import wtf.triplapeeck.sinon.command.Command;
import wtf.triplapeeck.sinon.command.ParsedArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Nuke extends Command {
    public Nuke(JDA jda) {
        this.addArgument(new Argument("nuke", "c", true, Argument.Type.COMMAND, null));
        this.addArgument(new Argument("count", "c", false, Argument.Type.UINT_OVER_ZERO, null));
        this.init(jda);
    }

    @Override
    public void handler(MessageReceivedEvent event, JDA jda) {
        event.getChannel().sendMessage("Nuking...").queue();
        ArrayList<ParsedArgument> args = this.parseArguments(event);
        commonWork(event.getChannel(), args.getFirst().getIntValue());
    }

    @Override
    public void handler(SlashCommandInteractionEvent event, JDA jda) {
        event.getHook().sendMessage("Nuking...").queue();
        ArrayList<ParsedArgument> args = this.parseArguments(event);
        commonWork(event.getChannel(), args.getFirst().getIntValue());
    }
    private void commonWork(MessageChannelUnion channel, Long count) {
        TextChannel gChannel = channel.asTextChannel();
        if (gChannel==null && count==0L) {
            channel.sendMessage("The full-nuke can only be used in full text channels").queue();
            return;
        }
        if (count!=0L) {
            GuildMessageChannel uChannel = channel.asGuildMessageChannel();
            if (uChannel == null) {
                channel.sendMessage("The partial-nuke can only be used in guild text channels").queue();
                return;
            }
            List<Message> messageList = new ArrayList<>();
            uChannel.getIterableHistory().forEachAsync(message -> {
                messageList.add(message);
                if (messageList.size() >= count) {
                    return false;
                }
                return true;
            }).thenRun(() -> uChannel.purgeMessages(messageList));
        }
        if (count==0L) {
            gChannel.createCopy().queue();
            gChannel.delete().queueAfter(5000, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public String getDescription() {
        return "Nukes part or all of a channel's message history";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.ESSENTIAL;
    }
}
