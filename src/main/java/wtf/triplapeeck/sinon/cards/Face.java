package wtf.triplapeeck.sinon.cards;

import com.google.gson.annotations.SerializedName;

public enum Face {
    ACE("Ace"),
    TWO("Two"),
    THREE("Three"),
    FOUR("Four"),
    FIVE("Five"),
    SIX("Six"),
    SEVEN("Seven"),
    EIGHT("Eight"),
    NINE("Nine"),
    TEN("Ten"),
    JACK("Jack"),
    QUEEN("Queen"),
    KING("King");
    private final String name;
    Face(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
