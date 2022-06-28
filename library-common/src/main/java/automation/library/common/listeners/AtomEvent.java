package automation.library.common.listeners;

public interface AtomEvent {
    void initEvent(AtomEventType eventTypeArg, boolean canBubbleArg,
                   boolean cancelableArg);

    boolean getBubbles();

    boolean getCancelable();


    AtomEventTarget getCurrentTarget();

    short getEventPhase();

    AtomEventTarget getTarget();

    AtomEventType getType();

    long getTimeStamp();

    void stopPropagation();

    void preventDefault();
}
