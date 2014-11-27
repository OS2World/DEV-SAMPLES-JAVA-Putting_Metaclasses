/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   List getMRO( )
//
class CodePtr_getMRO extends CodePtr { 
    public Object execute( ObjectReference target ) {
	return Environment.MRO( (ClassReference)target );
    }
    public String toString() { return "Class_getMRO";} 
}
