package automation.library.common.extendentassert;

import automation.library.common.TestContext;
import automation.library.common.listeners.AtomEventManager;
import automation.library.common.listeners.AtomEventTargetImpl;
import automation.library.common.listeners.CommonAtomEvents;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

public class SoftAssertWithNotifications extends SoftAssert {

    private static AtomEventManager getEventManager() {
        return TestContext.getInstance().getAtomEventManager();
    }

    public SoftAssertWithNotifications withText(String string){
        return this;
    }

    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        getEventManager().notify(CommonAtomEvents.ASSERT_ASSERTION_SUCCESS, new AtomEventTargetImpl<>(assertCommand));
    }

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        getEventManager().notify(CommonAtomEvents.ASSERT_ASSERTION_FAIL, new AtomEventTargetImpl<>(assertCommand));
        getEventManager().notify(CommonAtomEvents.ASSERT_ASSERTION_ERROR, new AtomEventTargetImpl<>(ex));
    }

}
