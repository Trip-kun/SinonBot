package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.cards.BlackjackState;
import wtf.triplapeeck.sinon.entity.ChannelData;
import wtf.triplapeeck.sinon.entity.DeckCardData;
import wtf.triplapeeck.sinon.entity.PlayerCardData;
import wtf.triplapeeck.sinon.entity.PlayerSpotData;

import java.util.Collection;

@DatabaseTable(tableName = "oatmeal_channels")
public class ORMLiteChannelData extends ChannelData {
    @DatabaseField(id=true)
    private @NotNull String id;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean autoThread;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean blackjackInProgress;
    @DatabaseField(canBeNull=false, dataType = DataType.ENUM_INTEGER)
    private @NotNull BlackjackState blackjackState;
    @ForeignCollectionField(eager = true)
    private @NotNull Collection<ORMLitePlayerSpotData> playerSpots;
    @ForeignCollectionField(eager = true)
    private @NotNull Collection<ORMLiteDeckCardData> deckCards;
    @ForeignCollectionField(eager = true)
    private @NotNull Collection<ORMLitePlayerCardData> playerCards;
    public ORMLiteChannelData(String id) {
        this.id = id;
    }
    public ORMLiteChannelData() {} // For ORMLite
    public void setBlackjackInProgress(@NotNull Boolean blackjackInProgress) {
        this.blackjackInProgress = blackjackInProgress;
    }
    public void setBlackjackState(@NotNull BlackjackState blackjackState) {
        this.blackjackState = blackjackState;
    }
    public @NotNull Boolean getBlackjackInProgress() {
        return blackjackInProgress;
    }
    public @NotNull BlackjackState getBlackjackState() {
        return blackjackState;
    }
    public @NotNull Boolean getAutoThread() {
        return autoThread;
    }
    public void setAutoThread(@NotNull Boolean autoThread) {
        this.autoThread = autoThread;
    }
    public @NotNull Collection<? extends PlayerSpotData> getPlayerSpots() {
        return playerSpots;
    }
    public @NotNull Collection<? extends DeckCardData> getDeck() {
        return deckCards;
    }
    public @NotNull Collection<? extends PlayerCardData> getPlayerCards() {
        return playerCards;
    }

    @Override
    public @NotNull String getID() {
        return id;
    }

    @Override
    public void load() {

    }
}
