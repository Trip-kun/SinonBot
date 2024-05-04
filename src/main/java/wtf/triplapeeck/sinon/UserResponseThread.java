package wtf.triplapeeck.sinon;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class UserResponseThread extends Thread{
    private final TextChannel channel;
    public UserResponseThread(TextChannel channel) {
        this.channel=channel;
    }
    public TextChannel getChannel() {
        return channel;
    }
}
