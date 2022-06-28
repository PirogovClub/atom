package automation.library.common.listeners;


import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements Observer pattern for notifying listeners about events
 * happen in the framework
 * https://refactoring.guru/design-patterns/observer
 */

@Log4j2
public class AtomEventManager {
    private Map<String, List<AtomEventListener>> listenersOnString = new HashMap<>();
    private Map<AtomEventSimple, List<AtomEventListener>> listenersOnEnum = new HashMap<>();

    @Deprecated
    public AtomEventManager(List<String> operations) {
        for (String operation : operations) {
            this.listenersOnString.put(operation, new ArrayList<>());
        }
    }

    public AtomEventManager(AtomEventSimple[] operations) {
        this.addEvents(operations);
    }
    public AtomEventManager() {
    }

    public AtomEventManager addEvents(AtomEventSimple[] atomEvents) {
        for (AtomEventSimple atomEventSimple : atomEvents) {
            this.listenersOnEnum.put(atomEventSimple, new ArrayList<>());
        }
        return this;
    }

    @Deprecated
    public void subscribe(String eventType, AtomEventListener listener) {
        log.debug(listener + " subscribed to " + eventType);
        listenersOnString.get(eventType).add(listener);
    }

    public void subscribe(AtomEventSimple eventType, AtomEventListener listener) {
        log.debug(listener + " subscribed to " + eventType);
        listenersOnEnum.get(eventType).add(listener);
    }

    @Deprecated
    public void subscribe(List<String> eventTypes, AtomEventListener listener) {
        for (String eventType : eventTypes) {
            listenersOnString.get(eventType).add(listener);
            log.debug(listener + " subscribed to " + eventType);
        }
    }

    public void subscribe(AtomEventSimple[] eventTypes, AtomEventListener listener) {
        for (AtomEventSimple eventType : eventTypes) {
            listenersOnEnum.get(eventType).add(listener);
            log.debug(listener + " subscribed to " + eventType);
        }
    }

    @Deprecated
    public void unsubscribe(String eventType, AtomEventListener listener) {
        log.debug(listener + " unsubscribe from  " + eventType);
        listenersOnString.get(eventType).remove(listener);
    }

    public void unsubscribe(AtomEventSimple eventType, AtomEventListener listener) {
        log.debug(listener + " unsubscribe from  " + eventType);
        listenersOnEnum.get(eventType).remove(listener);
    }

    public void notify(AtomEventSimple eventType, AtomEventTarget source) {
        List<AtomEventListener> users = listenersOnEnum.get(eventType);
        log.debug(" notification sent on " + eventType + " happen with " + source);
        for (AtomEventListener listener : users) {
            listener.eventDispatched(eventType, source);
        }
    }

    @Deprecated
    public void notify(String eventType, AtomEventTarget source) {
        List<AtomEventListener> users = listenersOnString.get(eventType);
        log.debug(" notification sent on " + eventType + " happen with " + source);
        for (AtomEventListener listener : users) {
            listener.eventDispatched(eventType, source);
        }
    }
}
