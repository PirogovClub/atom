package automation.library.common.listeners;

public class AtomEventImpl implements AtomEvent {
    public AtomEventType type=null;
    public AtomEventTarget target;
    public AtomEventTarget currentTarget;
    public short eventPhase;
    public boolean initialized=false, bubbles=true, cancelable=false;
    public boolean stopPropagation=false, preventDefault=false;

    protected long timeStamp = System.currentTimeMillis();

    /** The DOM doesn't deal with constructors, so instead we have an
     initializer call to set most of the read-only fields. The
     others are set, and reset, by the eventType subsystem during dispatch.
     <p>
     Note that init() -- and the subclass-specific initWhatever() calls --
     may be reinvoked. At least one initialization is required; repeated
     initializations overwrite the eventType with new values of their
     parameters.
     */
    @Override
    public void initEvent(AtomEventType eventType, boolean canBubbleArg,
                          boolean cancelableArg)
    {
        type=eventType;
        bubbles=canBubbleArg;
        cancelable=cancelableArg;

        initialized=true;
    }

    /** @return true iff this Event is of a class and type which supports
    bubbling. In the generic case, this is True.
     */
    @Override
    public boolean getBubbles()
    {
        return bubbles;
    }

    /** @return true iff this Event is of a class and type which (a) has a
    Default Behavior in this DOM, and (b)allows cancellation (blocking)
    of that behavior. In the generic case, this is False.
     */
    @Override
    public boolean getCancelable()
    {
        return cancelable;
    }

    /** @return the Node (EventTarget) whose EventListeners are currently
    being processed. During capture and bubble phases, this may not be
    the target node. */
    @Override
    public AtomEventTarget getCurrentTarget()
    {
        return currentTarget;
    }

    /** @return the current processing phase for this event --
    CAPTURING_PHASE, AT_TARGET, BUBBLING_PHASE. (There may be
    an internal DEFAULT_PHASE as well, but the users won't see it.) */
    @Override
    public short getEventPhase()
    {
        return eventPhase;
    }

    /** @return the EventTarget (Node) to which the event was originally
    dispatched.
     */
    @Override
    public AtomEventTarget getTarget()
    {
        return target;
    }

    /** @return event name as a string
     */

    @Override
    public AtomEventType getType()
    {
        return type;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

    /** Causes exit from in-progress event dispatch before the next
     currentTarget is selected. Replaces the preventBubble() and
     preventCapture() methods which were present in early drafts;
     they may be reintroduced in future levels of the DOM. */
    @Override
    public void stopPropagation()
    {
        stopPropagation=true;
    }

    /** Prevents any default processing built into the target node from
     occurring.
     */
    @Override
    public void preventDefault()
    {
        preventDefault=true;
    }
}
