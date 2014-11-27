/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//  void deleteInstance ( ObjectReference objToBeDeleted )
//
class CodePtr_deleteInstance         extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
	((ObjectReference)param1).invoke( Environment.Object, "destroy" );
	return null;
    }
    public String toString() { return "Class_deleteInstance";} 
}
