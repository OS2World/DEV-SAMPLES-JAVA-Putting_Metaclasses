/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   Boolean supportsMethod( ClassReference introducingClass, String methodName )
//
class CodePtr_supportsMethod  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        try {
            Environment.resolve( (ClassReference)target, (ClassReference)param1, (String)param2 );
        } catch ( OMResolutionFailure e ) {
            return new Boolean(false);
        }
        return new Boolean(true);
    }
    public String toString() { return "Class_supportsMethod";} 
}
