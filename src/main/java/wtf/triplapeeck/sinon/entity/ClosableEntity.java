package wtf.triplapeeck.sinon.entity;

public class ClosableEntity<T extends AccessibleEntity> implements AutoCloseable {
    private final T data;
    public ClosableEntity(T data) {
        this.data = data;
        data.requestAccess();
    }
    public T getData() {
        return data;
    }
    @Override
    public void close() throws Exception {
        data.releaseAccess();
    }
}
