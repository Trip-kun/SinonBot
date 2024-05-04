package wtf.triplapeeck.sinon.database;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String message) {
        super(message);
    }
    public DatabaseException(Throwable e) {
        super(e);
    }
}
