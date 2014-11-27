/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

public class ConstantObjectReference extends ObjectReference {

    public Object clone() {
        return this;
    }

}
