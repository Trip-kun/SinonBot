package wtf.triplapeeck.sinon.command.miscellaneous;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import wtf.triplapeeck.sinon.command.Argument;
import wtf.triplapeeck.sinon.command.Command;
import wtf.triplapeeck.sinon.command.ParsedArgument;
import wtf.triplapeeck.sinon.entity.ClosableEntity;
import wtf.triplapeeck.sinon.entity.ReminderData;
import wtf.triplapeeck.sinon.entity.UserData;
import wtf.triplapeeck.sinon.manager.DataManager;
import wtf.triplapeeck.sinon.runnable.Heartbeat;

import java.math.BigInteger;
import java.util.ArrayList;

public class Remind extends Command {
    private BigInteger count = BigInteger.ZERO;
    ArrayList<String> units = new ArrayList<>();
    public Remind(JDA jda) {
        this.addArgument(new Argument("reminder", "null", true, Argument.Type.COMMAND, null));
        units.add("seconds");
        units.add("minutes");
        units.add("hours");
        units.add("days");
        units.add("weeks");
        units.add("months");
        units.add("years");
        this.addArgument(new Argument("unit", "The unit of time", true, Argument.Type.SUBCOMMAND, units));
        this.addArgument(new Argument("time", "The amount of time until the reminder is sent", true, Argument.Type.UINT_OVER_ZERO, null));
        this.addArgument(new Argument("text", "The text to remind you of", true, Argument.Type.TEXT, null));
        init(jda);
    }

    @Override
    public void handler(MessageReceivedEvent event, JDA jda) {
        ArrayList<ParsedArgument> parsedArguments= parseArguments(event);
        User user = event.getAuthor();
        String reminder = parsedArguments.get(2).getStringValue();
        long time = parsedArguments.get(1).getIntValue();
        String unit = parsedArguments.get(0).getStringValue();
        commonWork(user, reminder, time, unit);
        event.getChannel().sendMessage("Reminder set!").queue();
    }

    @Override
    public void handler(SlashCommandInteractionEvent event, JDA jda) {
        ArrayList<ParsedArgument> parsedArguments= parseArguments(event);
        User user = event.getUser();
        String reminder = parsedArguments.get(2).getStringValue();
        long time = parsedArguments.get(1).getIntValue();
        String unit = parsedArguments.get(0).getStringValue();
        commonWork(user, reminder, time, unit);
        event.getHook().sendMessage("Reminder set!").queue();
    }
    private void commonWork(User user, String reminder, long time, String unit) {
        long unix = switch (unit) {
            case "seconds" -> time;
            case "minutes" -> time * 60;
            case "hours" -> time * 3600;
            case "days" -> time * 86400;
            case "weeks" -> time * 604800;
            case "months" -> time * 2628000L;
            case "years" -> time * 31540000000L;
            default -> 0;
        };
        long finalUnix = unix + System.currentTimeMillis() / 1000;
        try (ClosableEntity<? extends UserData> uData = DataManager.userDataManager.getData(user.getId())) {
            try (ClosableEntity<? extends ReminderData> rData = DataManager.reminderDataManager.getData(count.toString())) {
                ReminderData data = rData.getData();
                data.setReminderText(reminder);
                data.setReminderTimestamp(finalUnix);
                data.setUser(uData.getData());
                DataManager.userDataManager.saveData(user.getId(), false);
                DataManager.reminderDataManager.saveData(count.toString(), true);
                if (unix < 300) {
                    Heartbeat.injectReminder();
                }
            }
        }
        count = count.add(BigInteger.ONE);
    }

    @Override
    public String getDescription() {
        return "Set a reminder that's sent back to you a certain amount of time later";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.MISC;
    }
}
