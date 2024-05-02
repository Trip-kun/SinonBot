package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.entity.MemberData;
import wtf.triplapeeck.sinon.entity.PlayerSpotData;

import java.math.BigInteger;
import java.util.Collection;

@DatabaseTable(tableName = "oatmeal_members")
public class ORMLiteMemberData extends MemberData {
    @DatabaseField(id = true)
    private @NotNull String id;
    @DatabaseField(canBeNull = false)
    private @NotNull BigInteger rak;
    @DatabaseField(canBeNull = false)
    private @NotNull Integer messageCount;
    @ForeignCollectionField(eager = true)
    private @NotNull Collection<ORMLitePlayerSpotData> playerSpots;
    public ORMLiteMemberData(String id) {
        this.id = id;
        this.rak= BigInteger.ZERO;
        this.messageCount = 0;
    }
    public ORMLiteMemberData() {}
    @Override
    public @NotNull String getID() {
        return id;
    }
    @Override
    public @NotNull BigInteger getRak() {
        return rak;
    }
    @Override
    public @NotNull Collection<? extends PlayerSpotData> getSpots() {
        return playerSpots;
    }

    @Override
    public void setRak(@NotNull BigInteger rak) {
        this.rak = rak;
    }
    @Override
    public @NotNull Integer getMessageCount() {
        return messageCount;
    }

    @Override
    public void setMessageCount(@NotNull Integer messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public void load() {

    }
}
