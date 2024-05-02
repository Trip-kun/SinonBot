package wtf.triplapeeck.sinon.command;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Argument {
    public enum Type {
        COMMAND,
        INTEGER,
        UINT,
        UINT_OVER_ZERO,
        WORD,
        TEXT,
        USER,
        CHANNEL,
        ROLE,
        BOOLEAN,
        DECIMAL,
        UDECIMAL,
        UDECIMAL_OVER_ZERO,
        ATTACHMENT,
        SUBCOMMAND,
    }
    public String name;
    public Type type;
    public String description;
    public ArrayList<String> choices = new ArrayList<>();
    public boolean required;

    public Argument(String name, String description, boolean required, Type type, @Nullable ArrayList<String> choices) {
        this.name = name;
        this.description = description;
        this.required = required;
        this.type=type;
        if (type==Type.SUBCOMMAND) {
            if (choices==null) {
                throw new IllegalArgumentException("Subcommand argument must have choices");
            }
        }
        if (choices!=null) {
            this.choices = choices;
        }
    }
}
