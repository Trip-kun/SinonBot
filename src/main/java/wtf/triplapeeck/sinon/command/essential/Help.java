package wtf.triplapeeck.sinon.command.essential;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.logging.Log;
import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.command.Argument;
import wtf.triplapeeck.sinon.command.Command;
import wtf.triplapeeck.sinon.command.CommandHandler;
import wtf.triplapeeck.sinon.command.ParsedArgument;
import wtf.triplapeeck.sinon.entity.ClosableEntity;
import wtf.triplapeeck.sinon.entity.UserData;
import wtf.triplapeeck.sinon.manager.DataManager;

import java.util.ArrayList;

public class Help extends Command {
    ArrayList<String> categories = new ArrayList<>();
    public Help(JDA jda) {
        this.addArgument(new Argument("help", "o", true, Argument.Type.COMMAND, null));

        for (Command.CommandCategory category : Command.CommandCategory.values()) {
            categories.add(category.name());
        }
        this.addArgument(new Argument("category", "The category of the command to ask for help for", false, Argument.Type.SUBCOMMAND, categories));
        this.addArgument(new Argument("command", "The command to ask for help for", false, Argument.Type.WORD, null));
        init(jda);
    }
    @Override
    public void handler(MessageReceivedEvent event, JDA jda) {
        ArrayList<ParsedArgument> parsedArguments = parseArguments(event);
        MessageEmbed embed = commonWork(event.getAuthor(), parsedArguments.get(1).getStringValue(), parsedArguments.get(0).getStringValue());
        event.getChannel().sendMessageEmbeds(embed).queue();
    }

    @Override
    public void handler(SlashCommandInteractionEvent event, JDA jda) {
        ArrayList<ParsedArgument> parsedArguments = parseArguments(event);
        MessageEmbed embed = commonWork(event.getUser(), parsedArguments.get(1).getStringValue(), parsedArguments.get(0).getStringValue());
        event.getHook().sendMessageEmbeds(embed).queue();
    }
    private MessageEmbed commonWork(User user, String command, String category) {
        EmbedBuilder builder = new EmbedBuilder();
        if (command.isEmpty() && category.isEmpty()) {
            builder.setTitle("Help");
            builder.setDescription("Sinonbot is a multipurpose bot with many features\nUse s!help <command> or /help to get information about a command\nUse s!help <category> or /help to get a list of commands in a category");
            StringBuilder categories = new StringBuilder();
            int count=1;
            for (Command.CommandCategory cat : Command.CommandCategory.values()) {
                if (cat==CommandCategory.SINON_ADMIN) {
                    try (ClosableEntity<? extends UserData> uData =DataManager.userDataManager.getData(user.getId())) {
                        UserData userData = uData.getData();
                        if (userData.isAdmin()) {
                            categories.append(count).append(".").append(cat.getName()).append("\n");
                        }
                    }
                } else if (cat==CommandCategory.SINON_OWNER) {
                    try (ClosableEntity<? extends UserData> uData = DataManager.userDataManager.getData(user.getId())) {
                        UserData userData = uData.getData();
                        if (userData.isOwner()) {
                            categories.append(count).append(".").append(cat.getName()).append("\n");
                        }
                    }
                } else {
                    categories.append(count).append(".").append(cat.getName()).append("\n");
                }
                count++;
            }
            builder.addField("Categories", categories.toString(), false);
        }
        if (!command.isEmpty() && !category.isEmpty()) {
            builder.setTitle("Error");
            builder.setDescription("You can only specify a command or a category, not both");
            builder.addField("Error", "You can only specify a command or a category, not both", false);
            builder.addField("CommonFix", "If you are using s!help, add an extra space before the categories", false);
        }
        if (!command.isEmpty() && category.isEmpty()) {
            builder.setTitle("Help for " + command);
            Command com = CommandHandler.getCommand(command);
            if (com==null) {
                builder.setDescription("The command " + command + " does not exist");
                builder.addField("Error", "The command " + command + " does not exist", false);
                builder.addField("CommonFix", "Check the spelling of the command", false);
            } else {
                builder.setDescription(com.getDescription());
                StringBuilder shortArgs = new StringBuilder(getFirstArgument().name + " ");
                StringBuilder args = new StringBuilder();
                for (Argument arg : com.getArguments()) {
                    if (arg.type != Argument.Type.COMMAND) {
                        switch (arg.type) {
                            case ROLE -> {
                                args.append("Mentioned Role ");
                                shortArgs.append("<role (").append(arg.name).append(" )>");
                            }
                            case SUBCOMMAND -> {
                                args.append("Subcommand ");
                                shortArgs.append("<").append(arg.name).append(">");
                            }
                            case USER -> {
                                args.append("Mentioned User ");
                                shortArgs.append("<user (").append(arg.name).append(")>");
                            }
                            case TEXT -> {
                                args.append("Text ");
                                shortArgs.append("<text (including whitespace) (").append(arg.name).append(")>");
                            }
                            case UINT -> {
                                args.append("Unsigned Integer ");
                                shortArgs.append("<uint (").append(arg.name).append(")>");
                            }
                            case UINT_OVER_ZERO -> {
                                args.append("Unsigned Integer Over Zero ");
                                shortArgs.append("<uint>0 (").append(arg.name).append(")>");
                            }
                            case WORD -> {
                                args.append("String ");
                                shortArgs.append("<String (no whitespace) (").append(arg.name).append(")>");
                            }
                            case BOOLEAN -> {
                                args.append("Boolean ");
                                shortArgs.append("<true/false (").append(arg.name).append(")>");
                            }
                            case DECIMAL -> {
                                args.append("Decimal ");
                                shortArgs.append("<decimal (").append(arg.name).append(")>");
                            }
                            case INTEGER -> {
                                args.append("Integer ");
                                shortArgs.append("<integer (").append(arg.name).append(")>");
                            }
                            case UDECIMAL -> {
                                args.append("Unsigned Decimal ");
                                shortArgs.append("<udecimal (").append(arg.name).append(")>");
                            }
                            case UDECIMAL_OVER_ZERO -> {
                                args.append("Unsigned Decimal Over Zero ");
                                shortArgs.append("<udecimal>0 (").append(arg.name).append(")>");
                            }
                            case ATTACHMENT -> {
                                args.append("Attachment ");
                                // Don't append short args
                            }
                            case CHANNEL -> {
                                args.append("Mentioned Channel ");
                                shortArgs.append("<channel (").append(arg.name).append(")>");
                            }
                            case COMMAND -> {
                            } // Silent internal value
                            default -> {
                                args.append("Unknown ");
                                shortArgs.append("<unknown (").append(arg.name).append(")>");
                            }
                        }
                        args.append(arg.name);
                        if (arg.required) {
                            args.append(" (Required)");
                        } else {
                            args.append(" (Optional)");
                        }
                        args.append(" - ").append(arg.description).append("\n");
                        if (arg.choices != null) {
                            args.append("Choices: ");
                            for (String choice : arg.choices) {
                                args.append(choice).append(", ");
                            }
                            args.delete(args.length() - 2, args.length());
                            args.append("\n");
                        }
                    }
                }
                builder.addField("Syntax", shortArgs.toString(), false);
                builder.addField("Arguments", args.toString(), false);
            }
        }
        if (command.isEmpty() && !category.isEmpty()) {
            builder.setTitle("Commands in " + category);
            Command.CommandCategory cat = Command.CommandCategory.valueOf(category);
            StringBuilder commands = new StringBuilder();
            for (Command com : CommandHandler.getCommands().values()) {
                if (com.getCommandCategory()==cat) {
                    commands.append(com.getFirstArgument().name).append("\n");
                }
            }
            builder.addField("Command", commands.toString(), false);

        }
        return builder.build();
    }

    @Override
    public String getDescription() {
        return "Allows you to view help for commands";
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.ESSENTIAL;
    }
}
