package automation.library.common.listeners;

public class AtomEventExeption extends RuntimeException {
    private static final long serialVersionUID = 242753408332692061L;

    public AtomEventExeption(short code, String message) {
        super(message);
        this.code = code;
    }
    public short   code;
    // EventExceptionCode
    /**
     *  If the <code>Event</code>'s type was not specified by initializing the
     * event before the method was called. Specification of the Event's type
     * as <code>null</code> or an empty string will also trigger this
     * exception.
     */
    public static final short UNSPECIFIED_EVENT_TYPE_ERR = 0;

}
