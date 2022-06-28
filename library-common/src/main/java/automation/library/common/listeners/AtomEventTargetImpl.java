package automation.library.common.listeners;


import lombok.Getter;
import org.w3c.dom.events.EventListener;


public class AtomEventTargetImpl<T> implements AtomEventTarget<T>{

    public T getObjectToStore() {
        return objectToStore;
    }

    private T objectToStore;

    public AtomEventTargetImpl(T objectToStore) {
        this.objectToStore = objectToStore;
    }

    @Override
    public void addEventListener(String event, AtomEventListener listener, boolean useCapture) {

    }

    @Override
    public void removeEventListener(String event, EventListener listener, boolean useCapture) {

    }

    @Override
    public boolean dispatchEvent(String evt) throws AtomEventExeption {
        return false;
    }
}
