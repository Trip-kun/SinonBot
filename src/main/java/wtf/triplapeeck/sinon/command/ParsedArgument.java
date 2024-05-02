package wtf.triplapeeck.sinon.command;

import net.dv8tion.jda.api.entities.Message;

public class ParsedArgument {
    private String stringValue;
    private Long intValue;
    private Double doubleValue;
    private Boolean boolValue;
    private Message.Attachment attachment;
    public ParsedArgument(Argument argument, String value, Message.Attachment attachment) {
        switch (argument.type) {
            case INTEGER:
                intValue = Long.parseLong(value);
                break;
            case UINT, UINT_OVER_ZERO, USER, CHANNEL, ROLE:
                intValue = Long.parseUnsignedLong(value);
                break;
            case WORD, SUBCOMMAND, TEXT:
                stringValue = value;
                break;
            case BOOLEAN:
                boolValue = Boolean.parseBoolean(value);
                break;
            case DECIMAL, UDECIMAL, UDECIMAL_OVER_ZERO:
                doubleValue = Double.parseDouble(value);
                break;
            case ATTACHMENT:
                this.attachment = attachment;
                break;
            default:
        }
    }
    public String getStringValue() {
        return stringValue;
    }
    public Long getIntValue() {
        return intValue;
    }
    public Double getDoubleValue() {
        return doubleValue;
    }
    public Boolean getBoolValue() {
        return boolValue;
    }

    public Message.Attachment getAttachmentValue() {
        return attachment;
    }
}