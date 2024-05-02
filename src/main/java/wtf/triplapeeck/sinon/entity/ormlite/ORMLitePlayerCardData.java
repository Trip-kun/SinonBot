package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.cards.Face;
import wtf.triplapeeck.sinon.cards.Suit;
import wtf.triplapeeck.sinon.entity.*;
import wtf.triplapeeck.sinon.manager.DataManager;

@DatabaseTable(tableName = "oatmeal_player_cards")
public class ORMLitePlayerCardData extends PlayerCardData {
    @DatabaseField(generatedId = true)
    private @NotNull String id;
    @DatabaseField(canBeNull=false, dataType= DataType.ENUM_INTEGER)
    private @NotNull Face face;
    @DatabaseField(canBeNull=false, dataType= DataType.ENUM_INTEGER)
    private @NotNull Suit suit;
    @DatabaseField(canBeNull=false, foreign=true, foreignAutoRefresh=true)
    private @NotNull ORMLiteChannelData channel;
    @DatabaseField(canBeNull=false, foreign=true, foreignAutoRefresh=true)
    private @NotNull ORMLitePlayerSpotData player;
    public ORMLitePlayerCardData() {}
    public ORMLitePlayerCardData(String ignored) {}
    @Override
    public @NotNull Face getFace() {
        return face;
    }
    @Override
    public @NotNull Suit getSuit() {
        return suit;
    }
    @Override
    public @NotNull ORMLiteChannelData getChannel() {
        return channel;
    }
    public @NotNull ORMLitePlayerSpotData getPlayer() {
        return player;
    }
    @Override
    public void setFace(@NotNull Face face) {
        this.face = face;
    }
    @Override
    public void setSuit(@NotNull Suit suit) {
        this.suit = suit;
    }
    @Override
    public void setChannel(@NotNull ChannelData channel) {
        this.channel = (ORMLiteChannelData) channel;
    }
    @Override
    public void setPlayer(@NotNull PlayerSpotData player) {
        this.player = (ORMLitePlayerSpotData) player;
    }
    @Override
    public @NotNull String getID() {
        return id;
    }
    @Override
    public void load() {
    }
}
