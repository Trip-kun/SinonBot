package wtf.triplapeeck.sinon.manager;


import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.Config;
import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.Tuple2;
import wtf.triplapeeck.sinon.UserResponseThread;
import wtf.triplapeeck.sinon.database.DatabaseException;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * ThreadManager class for managing threads
 * This class is used to manage threads and ensure that the number of threads running at any given time does not exceed a certain limit
 * @see Runnable
 * @see Thread
 */
public class ThreadManager extends Thread {
    private ArrayList<Tuple2<String, Thread>> threads = new ArrayList<>();
    private LinkedBlockingQueue<Tuple2<String, Thread>> threadQueue = new LinkedBlockingQueue<>();
    private boolean requestToEnd = false;
    private ThreadManager() {
    }
    private static ThreadManager instance;
    /**
     * Get the instance of the ThreadManager
     * @return The instance of the ThreadManager
     */
    public synchronized static ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        instance.start();
        return instance;
    }
    @Override
    public void run() {
        UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                if (thread instanceof UserResponseThread) {
                    if (throwable instanceof DatabaseException)
                        ((UserResponseThread) thread).getChannel().sendMessage(Config.getConfig().errorResponseString + ". The following error occurred: " + throwable.getMessage()).queue();
                }
                Logger.log(Logger.Level.ERROR, "Uncaught exception in thread: " + thread.getName());
                Logger.log(Logger.Level.ERROR, throwable.getMessage());
                for (StackTraceElement element : throwable.getStackTrace()) {
                    Logger.log(Logger.Level.ERROR, element.toString());
                }
            }
        };

        Logger.log(Logger.Level.INFO, "ThreadManager started");
        boolean stillFinishing=false;
        ArrayList<Tuple2<String, Thread>> finishedThreads = new ArrayList<>();
        while (true) {
            for (Tuple2<String, Thread> thread : threads) {
                switch (thread.getB().getState()) {
                    case NEW:
                        Logger.log(Logger.Level.INFO, "Starting thread with name" + thread.getA());
                        stillFinishing = true;
                        thread.getB().start();
                        break;
                    case RUNNABLE, WAITING, TIMED_WAITING, BLOCKED:
                        stillFinishing = true;
                        break;
                    case TERMINATED:

                        finishedThreads.add(thread);
                        break;
                }
            }
            for (Tuple2<String, Thread> thread : finishedThreads) {
                threads.remove(thread);
            }
            if (requestToEnd && !stillFinishing) {
                Logger.log(Logger.Level.INFO, "ThreadManager ended");
                return;
            }
            while (threads.size()<=Config.getConfig().threadLimit && !threadQueue.isEmpty()) {
                Tuple2<String, Thread> thread = threadQueue.poll();
                threads.add(thread);
            }
            try {
                sleep(Config.getConfig().threadSleep);
            } catch (InterruptedException ignored) {
            }
        }
    }
    /**
     * Request the ThreadManager to end
     */
    public void requestToEnd() {
        requestToEnd = true;
        Logger.log(Logger.Level.INFO, "ThreadManager requested to end");
    }
    /**
     * Add a thread to the ThreadManager
     * @param name The name of the thread
     * @param thread The thread to add
     */
    public void addThread(@NotNull String name, @NotNull Thread thread) {
        threadQueue.add(new Tuple2<>(name, thread));
    }
}
