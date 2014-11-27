/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//  Object free()
//
class CodePtr_free           extends CodePtr { 
    public Object execute( ObjectReference target ) {
	Environment.getClass(target).invoke( Environment.Class, "deleteInstance", target );
	return new Boolean( true );
    }
    public String toString() { return "Object_free";} 
}
