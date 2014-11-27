/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   Boolean isAncestorOf( ClassReference anotherClass  )
//
class CodePtr_isAncestorOf  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        if ( Environment.hasAncestor( (ClassReference)param1, (ClassReference)target ) || target == param1 )
          return new Boolean(true);
        else
          return new Boolean(false);
    }
    public String toString() { return "Class_isAncestorOf ";} 
}
