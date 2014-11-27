/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   Boolean definesMethod( ClassReference introducingClass, String methodName )
//
class CodePtr_definesMethod  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        return new Boolean(Environment.defines( (ClassReference)target, (ClassReference)param1, (String)param2 ));
    }
    public String toString() { return "Class_definesMethod";} 
}
