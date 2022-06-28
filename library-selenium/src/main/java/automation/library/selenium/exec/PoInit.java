package automation.library.selenium.exec;

import automation.library.common.AtomException;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/*
 * Hi,
 * in StepDefinition class we can use "Lazy" PageObject instansiation.
 * With this approach instance of the Page object would be created only when any of it's method would be called for a first time in the StepDef class. It saves resources and time, and have few more benefits.
 * To use this approach define your PO variables as:
 *
 * PoInit<PageObjectClassName> pageObjectVarible = new PoInit<>(){};
 *
 * ex:
 *
 * PoInit<GooglePo> googlePo = new PoInit<>(){};
 *
 * it is important to keep all 3 empty breakers in the new instance of PoInit creation.
 * To call Page object methods use .get() method of PoInit class
 * ex:
 *
 * googlePo.get().sendToSearch("CLUB");
 *
 * @param <T> Any instance of the class with no argument constructor
 * @author Tim Pirogov
 */

public abstract class PoInit<T> extends LazyInitializer<T> {

    private T pageObjectInstance;

    @Override
    public T get() {
        //suppressing Exception to LazyInitializer
        try {
            return super.get();
        } catch (ConcurrentException e) {
            throw new AtomException(e);
        }
    }

    /**
     * based on
     * https://stackoverflow.com/questions/3437897/how-do-i-get-a-class-instance-of-generic-type-t
     *
     * @return
     */
    @Override
    protected T initialize() {
        try {
            Type sooper = this.getClass().getGenericSuperclass();
            Type t = ((ParameterizedType) sooper).getActualTypeArguments()[0];
            String typeName = t.getTypeName();
            pageObjectInstance = (T) (Class.forName(typeName).getDeclaredConstructor().newInstance());

        } catch (Exception e) {
            throw new AtomException(e);
        }
        return pageObjectInstance;
    }
}