/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   Boolean isDescendantOf( ClassReference anotherClass  )
//
class CodePtr_isDescendantOf  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        if ( Environment.hasAncestor( (ClassReference)target, (ClassReference)param1 ) || target == param1 )
          return new Boolean(true);
        else
          return new Boolean(false);
    }
    public String toString() { return "Class_isDescendantOf";} 
}
