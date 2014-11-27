/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//  Object dispatch ( ClassReference introducingClass, String methodName, List args, Object result )
//
class CodePtr_dispatch       extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3, Object param4 ) {
	return null;
    }
    public String toString() { return "Object_dispatch";} 
}

