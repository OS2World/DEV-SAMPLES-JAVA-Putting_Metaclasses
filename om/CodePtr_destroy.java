/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
// void destroy()
//
class CodePtr_destroy       extends CodePtr { 
    public Object execute( ObjectReference target ) {
	target.remove( Environment.myclass );
	// From the viewpoint of the simulation, removing the class of the object is effectively
	// destroying the object, because no further methods may be invoked on it.
	return null;
    }
    public String toString() { return "Object_destroy";} 
}
