/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   List getParents(  )
//
class CodePtr_getParents  extends CodePtr { 
    public Object execute( ObjectReference target ) {
        return Environment.getClassData( (ClassReference)target ).parents;
    }
    public String toString() { return "Class_getParents";} 
}
