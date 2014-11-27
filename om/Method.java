/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

public class Method {
    public ClassReference ic;
    public String         mn;

    public Method( ClassReference introducingClass, String methodName ) {
        ic = introducingClass;
        mn = methodName;
    }
}
