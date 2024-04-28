package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.entity.ChannelData;
@DatabaseTable(tableName = "oatmeal_channels")
public class ORMLiteChannelData extends ChannelData {
    @DatabaseField(id=true)
    public @NotNull String id;
    public ORMLiteChannelData(String id) {
        this.id = id;
    }
    public ORMLiteChannelData() {} // For ORMLite
    @Override
    public String getID() {
        return id;
    }
}
