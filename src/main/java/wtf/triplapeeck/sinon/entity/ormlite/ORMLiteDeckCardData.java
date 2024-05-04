package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.cards.Face;
import wtf.triplapeeck.sinon.cards.Suit;
import wtf.triplapeeck.sinon.entity.ChannelData;
import wtf.triplapeeck.sinon.entity.DeckCardData;

@DatabaseTable(tableName = "oatmeal_deck_cards")
public class ORMLiteDeckCardData extends DeckCardData {
    @DatabaseField(generatedId = true)
    private @NotNull Integer id;
    @DatabaseField(canBeNull=false, dataType = DataType.ENUM_INTEGER)
    private @NotNull Face face;
    @DatabaseField(canBeNull=false, dataType = DataType.ENUM_INTEGER)
    private @NotNull Suit suit;
    @DatabaseField(canBeNull=false, foreign=true, foreignAutoRefresh=true)
    private @NotNull ORMLiteChannelData channel;
    @DatabaseField(canBeNull=false)
    private @NotNull Integer index;
    public ORMLiteDeckCardData() {}
    public ORMLiteDeckCardData(String ignored) {}
    @Override
    public @NotNull Face getFace() {
        return face;
    }
    @Override
    public @NotNull Suit getSuit() {
        return suit;
    }
    @Override
    public @NotNull ChannelData getChannel() {
        return channel;
    }
    @Override
    public @NotNull Integer getIndex() {
        return index;
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
    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public void load() {

    }

    @Override
    public @NotNull String getID() {
        return String.valueOf(id);
    }
}
