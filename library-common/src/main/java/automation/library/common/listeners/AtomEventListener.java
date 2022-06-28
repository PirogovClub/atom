package automation.library.common.listeners;


import java.util.EventListener;

public interface AtomEventListener extends EventListener {
    void eventDispatched(AtomEventSimple event, AtomEventTarget source);
    void eventDispatched(String event, AtomEventTarget source);
}

