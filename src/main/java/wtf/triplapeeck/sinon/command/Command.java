package wtf.triplapeeck.sinon.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.Nullable;
import wtf.triplapeeck.sinon.Config;
import wtf.triplapeeck.sinon.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Command {
    public void init(JDA jda) {
        SlashCommandData data = Commands.slash(getFirstArgument().name, getDescription());
        for (Argument argument : arguments) {
            switch (argument.type) {
                case INTEGER, UINT, UINT_OVER_ZERO -> {
                    data.addOption(OptionType.INTEGER, argument.name, argument.description);
                }
                case WORD, TEXT -> {
                    data.addOption(OptionType.STRING, argument.name, argument.description);
                }
                case BOOLEAN -> {
                    data.addOption(OptionType.BOOLEAN, argument.name, argument.description);
                }
                case DECIMAL, UDECIMAL, UDECIMAL_OVER_ZERO -> {
                    data.addOption(OptionType.NUMBER, argument.name, argument.description);
                }
                case USER -> {
                    data.addOption(OptionType.USER, argument.name, argument.description);
                }
                case CHANNEL -> {
                    data.addOption(OptionType.CHANNEL, argument.name, argument.description);
                }
                case ROLE -> {
                    data.addOption(OptionType.ROLE, argument.name, argument.description);
                }
                case ATTACHMENT -> {
                    data.addOption(OptionType.ATTACHMENT, argument.name, argument.description);
                }
                case SUBCOMMAND -> {
                    OptionData optionData = new OptionData(OptionType.STRING, argument.name, argument.description, argument.required, false);
                    for (String choice : argument.choices) {
                        optionData.addChoice(choice, choice);
                    }
                    data.addOptions(optionData);
                }
                default -> {
                }
            }
        }
        jda.upsertCommand(data).complete();

    }
    public enum CommandCategory {
        ESSENTIAL("Essential", 1, "Essential commands for the bot"),
        MISC("Miscellaneous", 2, "Miscellaneous commands for the bot"),
        CARD_GAMES("Card Games", 3, "Card games for the bot"),
        CURRENCY("Currency", 4, "Currency commands for the bot"),
        CUSTOM_COMMAND("Custom Commands", 5, "Custom commands for the bot"),
        SINON_ADMIN("Sinon Admin", 10, "Sinon Admin commands for the bot"),
        SINON_OWNER("Sinon Owner", 11, "Sinon Owner commands for the bot");

        private final String name;
        private final int id;
        private final String description;

        CommandCategory(String name, int id, String description) {
            this.name = name;
            this.id = id;
            this.description = description;
        }

        @Nullable
        public static CommandCategory fromInt(int id) {
            switch (id) {
                case 1 -> {
                    return ESSENTIAL;
                }
                case 2 -> {
                    return MISC;
                }
                case 3 -> {
                    return CARD_GAMES;
                }
                case 4 -> {
                    return CURRENCY;
                }
                case 5 -> {
                    return CUSTOM_COMMAND;
                }
                case 10 -> {
                    return SINON_ADMIN;
                }
                case 11 -> {
                    return SINON_OWNER;
                }
                default -> {
                    return null;
                }
            }
        }

        @Nullable
        public static CommandCategory fromString(String value) {
            switch (value.toLowerCase(Locale.US)) {
                case "essential" -> {
                    return ESSENTIAL;
                }
                case "misc", "miscellaneous" -> {
                    return MISC;
                }
                case "card games", "card" -> {
                    return CARD_GAMES;
                }
                case "currency" -> {
                    return CURRENCY;
                }
                case "custom commands", "custom command" -> {
                    return CUSTOM_COMMAND;
                }
                case "sinon admin", "admin" -> {
                    return SINON_ADMIN;
                }
                case "sinon owner", "owner" -> {
                    return SINON_OWNER;
                }
                default -> {
                    return null;
                }
            }
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
    private ArrayList<Argument> arguments = new ArrayList<>();
    protected void addArgument(Argument argument) throws IllegalArgumentException {
        if (arguments.isEmpty() && argument.type != Argument.Type.COMMAND) {
            throw new IllegalArgumentException("First argument must be a command");
        }
        if (arguments.stream().anyMatch(arg -> arg.name.equals(argument.name))) {
            throw new IllegalArgumentException("Argument with name " + argument.name + " already exists");
        }
        if (arguments.stream().anyMatch(arg -> arg.type== Argument.Type.TEXT)) {
            throw new IllegalArgumentException("Text argument must be the last argument");
        }
        if (!arguments.isEmpty() && argument.type == Argument.Type.COMMAND) {
            throw new IllegalArgumentException("Command argument must be first");
        }
        if (arguments.size()>=26) { // +1 for the command argument
            throw new IllegalArgumentException("Command cannot have more than 25 arguments");
        }
        if (arguments.stream().anyMatch(arg -> arg.required==false) && argument.required) {
            throw new IllegalArgumentException("Required arguments must be first");
        }
        if (argument.type == Argument.Type.SUBCOMMAND && arguments.stream().anyMatch(arg -> arg.type != Argument.Type.COMMAND && arg.type != Argument.Type.SUBCOMMAND )) {
            throw new IllegalArgumentException("Subcommand arguments must be first");
        }
        arguments.add(argument);
    }
    public ArrayList<Argument> getArguments() {
        return arguments;
    }
    protected ArrayList<ParsedArgument> parseArguments(String messageContent, Collection<Message.Attachment> attachments, Collection<Long> mentionedRoles, Collection<Long> mentionedUsers, Collection<Long> mentionedChannels) throws IllegalArgumentException {
        ArrayList<ParsedArgument> parsedArguments = new ArrayList<>();
        String[] messageParts = messageContent.split("\\s+");
        List<Message.Attachment> attachmentsArray = new ArrayList<>(attachments);
        List<Long> mentionedRolesArray = new ArrayList<>(mentionedRoles);
        List<Long> mentionedUsersArray = new ArrayList<>(mentionedUsers);
        List<Long> mentionedChannelsArray = new ArrayList<>(mentionedChannels);
        int argIndex = 0;
        int numAttachments=0;
        int numMentionedRoles=0;
        int numMentionedUsers=0;
        int numMentionedChannels=0;
        for (Argument argument : arguments) {
            try {
                switch (argument.type) {
                    case TEXT -> {
                        StringBuilder text = new StringBuilder();
                        if (messageParts.length <= argIndex) {
                            if (argument.required) {
                                throw new IllegalArgumentException("Text argument " + argument.name + " is required");
                            }
                            parsedArguments.add(new ParsedArgument(argument, "", null));
                        }
                        for (int i = argIndex; i < messageParts.length; i++) {
                            text.append(messageParts[i]).append(" ");
                        }
                        parsedArguments.add(new ParsedArgument(argument, text.toString().trim(), null));
                        return parsedArguments;
                    }
                    case ATTACHMENT -> {
                        if (attachments.size() < ++numAttachments) {
                            throw new IllegalArgumentException("Attachment argument " + argument.name + " is required");
                        }
                        parsedArguments.add(new ParsedArgument(argument, null, attachmentsArray.get(numAttachments - 1)));
                    }
                    case ROLE -> {
                        if (mentionedRolesArray.size() < ++numMentionedRoles) {
                            throw new IllegalArgumentException("Role argument " + argument.name + " is required");
                        }
                        parsedArguments.add(new ParsedArgument(argument, String.valueOf(mentionedRolesArray.get(numMentionedRoles - 1)), null));
                        argIndex++;
                    }
                    case USER -> {
                        if (mentionedUsersArray.size() < ++numMentionedUsers) {
                            throw new IllegalArgumentException("User argument " + argument.name + " is required");
                        }
                        parsedArguments.add(new ParsedArgument(argument, String.valueOf(mentionedUsersArray.get(numMentionedUsers-1)), null));
                        argIndex++;
                    }
                    case CHANNEL -> {
                        if (mentionedChannelsArray.size() < ++numMentionedChannels) {
                            throw new IllegalArgumentException("Channel argument " + argument.name + " is required");
                        }
                        parsedArguments.add(new ParsedArgument(argument, String.valueOf(mentionedChannelsArray.get(numMentionedChannels-1)), null));
                        argIndex++;
                    }
                    case INTEGER -> {
                        if (messageParts.length <= argIndex) {
                            if (argument.required) {
                                throw new IllegalArgumentException("Integer argument " + argument.name + " is required");
                            }
                            parsedArguments.add(new ParsedArgument(argument, "0", null));
                        } else {
                            parsedArguments.add(new ParsedArgument(argument, messageParts[argIndex], null));
                        }
                        argIndex++;
                    }
                    case UINT -> {
                        if (messageParts.length <= argIndex) {
                            if (argument.required) {
                                throw new IllegalArgumentException("Unsigned integer argument " + argument.name + " is required");
                            }
                            parsedArguments.add(new ParsedArgument(argument, "0", null));
                        } else {
                            if (Long.parseLong(messageParts[argIndex]) < 0) {
                                throw new IllegalArgumentException("Unsigned integer argument " + argument.name + " must be positive");
                            }
                            parsedArguments.add(new ParsedArgument(argument, messageParts[argIndex], null));
                        }
                        argIndex++;
                    }
                    case UINT_OVER_ZERO -> {
                        if (messageParts.length <= argIndex) {
                            if (argument.required) {
                                throw new IllegalArgumentException("Unsigned integer argument " + argument.name + " is required");
                            }
                            parsedArguments.add(new ParsedArgument(argument, "0", null));
                        } else {
                            if (Long.parseLong(messageParts[argIndex]) <= 0) {
                                throw new IllegalArgumentException("Unsigned integer argument " + argument.name + " must be positive and non-zero");
                            }
                            parsedArguments.add(new ParsedArgument(argument, messageParts[argIndex], null));
                        }
                        argIndex++;
                    }
                    case DECIMAL -> {
                        if (messageParts.length <= argIndex) {
                            if (argument.required) {
                                throw new IllegalArgumentException("Decimal argument " + argument.name + " is required");
                            }
                            parsedArguments.add(new ParsedArgument(argument, "0", null));
                        } else {
                            parsedArguments.add(new ParsedArgument(argument, messageParts[argIndex], null));
                        }
                        argIndex++;
                    }
                    case UDECIMAL -> {
                        if (messageParts.length <= argIndex) {
                            if (argument.required) {
                                throw new IllegalArgumentException("Unsigned decimal argument " + argument.name + " is required");
                            }
                            parsedArguments.add(new ParsedArgument(argument, "0", null));
                        } else {
                            if (Double.parseDouble(messageParts[argIndex]) < 0) {
                                throw new IllegalArgumentException("Unsigned decimal argument " + argument.name + " must be positive");
                            }
                            parsedArguments.add(new ParsedArgument(argument, messageParts[argIndex], null));
                        }
                        argIndex++;
                    }
                    case UDECIMAL_OVER_ZERO -> {
                        if (messageParts.length <= argIndex) {
                            if (argument.required) {
                                throw new IllegalArgumentException("Unsigned decimal argument " + argument.name + " is required");
                            }
                            parsedArguments.add(new ParsedArgument(argument, "0", null));
                        } else {
                            if (Double.parseDouble(messageParts[argIndex]) <= 0) {
                                throw new IllegalArgumentException("Unsigned decimal argument " + argument.name + " must be positive and non-zero");
                            }
                            parsedArguments.add(new ParsedArgument(argument, messageParts[argIndex], null));
                        }
                        argIndex++;
                    }
                    case BOOLEAN -> {
                        if (messageParts.length <= argIndex) {
                            if (argument.required) {
                                throw new IllegalArgumentException("Boolean argument " + argument.name + " is required");
                            }
                            parsedArguments.add(new ParsedArgument(argument, "false", null));
                        } else {
                            if (!messageParts[argIndex].equalsIgnoreCase("true") && !messageParts[argIndex].equalsIgnoreCase("false")) {
                                throw new IllegalArgumentException("Boolean argument " + argument.name + " must be true or false");
                            }
                            parsedArguments.add(new ParsedArgument(argument, messageParts[argIndex], null));
                        }
                        argIndex++;
                    }
                    case WORD, SUBCOMMAND -> {
                        if (messageParts.length <= argIndex) {
                            if (argument.required) {
                                if (argument.type== Argument.Type.SUBCOMMAND) {
                                    throw new IllegalArgumentException("Subcommand argument " + argument.name + " is required");
                                }
                                throw new IllegalArgumentException("Word argument " + argument.name + " is required");
                            }
                            parsedArguments.add(new ParsedArgument(argument, "", null));
                        } else {
                            if (!argument.required && messageParts[argIndex].isEmpty()) { // Accept a null value for optional arguments
                                parsedArguments.add(new ParsedArgument(argument, "", null));
                                argIndex++;
                            } else if (!argument.required && argument.type== Argument.Type.SUBCOMMAND && !argument.choices.contains(messageParts[argIndex])) { // Argument is skipped by user (the current arg is NOT for this argument), pass null string and move on
                                parsedArguments.add(new ParsedArgument(argument, "", null));
                            } else if (argument.type== Argument.Type.SUBCOMMAND && !argument.choices.contains(messageParts[argIndex]) && argument.required) { // If required, check that it contains a valid subcommand
                                throw new IllegalArgumentException("Subcommand argument " + argument.name + " must be one of " + String.join(", ", argument.choices));
                            }  else { // Value successful taken, increment
                                parsedArguments.add(new ParsedArgument(argument, messageParts[argIndex], null));
                                argIndex++;
                            }
                        }
                    }
                    default -> {}
                    // Case COMMAND is not needed as it is handled in the CommandHandler
                }
            } catch (NumberFormatException e) {
                if (argument.type== Argument.Type.INTEGER || argument.type== Argument.Type.UINT || argument.type== Argument.Type.UINT_OVER_ZERO) {
                    throw new IllegalArgumentException("Argument " + argument.name + " must be an integer");
                }
                if (argument.type== Argument.Type.DECIMAL || argument.type== Argument.Type.UDECIMAL || argument.type== Argument.Type.UDECIMAL_OVER_ZERO) {
                    throw new IllegalArgumentException("Argument " + argument.name + " must be a decimal number");
                }
            }
        }
        return parsedArguments;
    }
    protected ArrayList<ParsedArgument> parseArguments(MessageReceivedEvent event) throws IllegalArgumentException {
        ArrayList<Long> roles = new ArrayList<>();
        ArrayList<Long> channels = new ArrayList<>();
        ArrayList<Long> users = new ArrayList<>();
        for (int i = 0; i < event.getMessage().getMentions().getRoles().size(); i++) {
            roles.add(event.getMessage().getMentions().getRoles().get(i).getIdLong());
        }
        for (int i = 0; i < event.getMessage().getMentions().getChannels().size(); i++) {
            channels.add(event.getMessage().getMentions().getChannels().get(i).getIdLong());
        }
        for (int i = 0; i < event.getMessage().getMentions().getUsers().size(); i++) {
            users.add(event.getMessage().getMentions().getUsers().get(i).getIdLong());
        }
        Integer index = Config.getConfig().prefix.length()+1+getFirstArgument().name.length();
        return parseArguments(event.getMessage().getContentRaw().substring(index >=event.getMessage().getContentRaw().length() ? event.getMessage().getContentRaw().length()-1 : index), event.getMessage().getAttachments(), roles, users, channels);
    }
    protected ArrayList<ParsedArgument> parseArguments(SlashCommandInteractionEvent event) {
        ArrayList<Long> roles = new ArrayList<>();
        ArrayList<Long> channels = new ArrayList<>();
        ArrayList<Long> users = new ArrayList<>();
        ArrayList<Message.Attachment> attachments = new ArrayList<>();
        String s = "";
        boolean first = false;
        for (OptionMapping option : event.getOptions()) {
            if (option.getType().equals(OptionType.ROLE)) {
                roles.add(option.getAsRole().getIdLong());
            } else if (option.getType().equals(OptionType.CHANNEL)) {
                channels.add(option.getAsChannel().getIdLong());
            } else if (option.getType().equals(OptionType.USER)) {
                users.add(option.getAsUser().getIdLong());
            } else if (option.getType().equals(OptionType.ATTACHMENT)) {
                attachments.add(option.getAsAttachment());
            }
            if (option.getType()!=OptionType.ATTACHMENT) {
                if (!first) {
                    first = true;
                    s = option.getAsString();
                } else {
                    s = s + " " + option.getAsString();

                }
            }
        }
        return parseArguments(s,attachments, roles, channels, users);
    }
    public abstract void handler(MessageReceivedEvent event, JDA jda);
    public abstract void handler(SlashCommandInteractionEvent event, JDA jda);
    public abstract String getDescription();
    public abstract CommandCategory getCommandCategory();
    public Argument getFirstArgument() {
        if (arguments.isEmpty()) {
            throw new IllegalArgumentException("Command must have at least one argument");
        }
        return arguments.getFirst();
    }
    public boolean isAdministrator(Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }
    public Optional<String> ensureAdministrator(Member member) {
        if (isAdministrator(member)) {
            return Optional.empty();
        } else {
            return Optional.of("You must be an administrator to use this command!");
        }
    }
}
