package wtf.triplapeeck.sinon.runnable;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.entity.ClosableEntity;
import wtf.triplapeeck.sinon.entity.ReminderData;
import wtf.triplapeeck.sinon.manager.DataManager;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Heartbeat implements Runnable{
    private static final AtomicBoolean inject = new AtomicBoolean(false);
    private static final Heartbeat instance = new Heartbeat();
    private static final ArrayList<ClosableEntity<? extends ReminderData>> reminders = new ArrayList<>();
    private static ArrayList<ClosableEntity<? extends ReminderData>> temp = new ArrayList<>();
    private static JDA jda;
    private static boolean requestToEnd = false;
    @Override
    public void run() {
        AtomicLong loopCount = new AtomicLong(0);
        while(true) {
            if (inject.get()) {
                loopCount.set(298);
                inject.set(false);
            }
            if (loopCount.get()==299) {
                reminders.addAll(DataManager.reminderDataManager.queryLessThan("time", (int) (System.currentTimeMillis() / 1000 + 300)));
                loopCount.set(0);
            }

            for (ClosableEntity<? extends ReminderData> reminder : reminders) {
                String id = reminder.getData().getID();
                try (ClosableEntity<? extends ReminderData> reminderData = reminder) {
                    ReminderData rData = reminderData.getData();
                    if (Instant.now().compareTo(Instant.ofEpochSecond(reminderData.getData().getReminderTimestamp())) < 0) {
                        continue;
                    }
                    temp.add(reminder);
                    String s = "I am here to remind you of the following: " + rData.getReminderText();
                    int y = s.length();
                    PrivateChannel channel = jda.retrieveUserById(rData.getUser().getID()).complete().openPrivateChannel().complete();
                    do {
                        if (y<=2000) {
                            channel.sendMessage(s).queue();
                            y-=y;
                            s = "";
                        } else {
                            channel.sendMessage(s.substring(0, 1999)).queue();
                            s = s.substring(1999);
                        }
                        y-=2000;
                    } while (y>0);
                } catch (DateTimeException e) {
                    temp.add(reminder);
                }
            }
            if (!temp.isEmpty()) {
                for (ClosableEntity<? extends ReminderData> id : temp) {
                    reminders.remove(id);
                    DataManager.reminderDataManager.removeData(id.getData().getID());
                }
                temp.clear();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            loopCount.addAndGet(1);
            if (requestToEnd) {
                break;
            }
        }
    }
    public static Heartbeat getInstance() {
        return instance;
    }
    public static synchronized void injectReminder() {
        inject.set(true);
    }
    public static void setJDA(JDA jda) {
        Heartbeat.jda = jda;
    }
    public static void requestEnd() {
        requestToEnd = true;
    }
}
