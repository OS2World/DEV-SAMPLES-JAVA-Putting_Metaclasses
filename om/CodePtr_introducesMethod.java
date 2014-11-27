/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   Boolean introducesMethod( ClassReference introducingClass, String methodName )
//
class CodePtr_introducesMethod  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        if ( Environment.defines( (ClassReference)target, (ClassReference)param1, (String)param2 ) ) {
            for ( Enumeration e = Environment.getClassData((ClassReference)target).parents.elements(); e.hasMoreElements(); ) {
                // If any parent supports the method, then the definition is an override.
                try {
                    ClassReference x = (ClassReference)e.nextElement();
                    Environment.resolve( x, (ClassReference)param1, (String)param2 );
                    return new Boolean(false);
                 } catch ( OMResolutionFailure ex ) {
                    // If the resolution fails, then the may still be the introducer of the method.
                 }
            }
            return new Boolean(true);
        }
        return new Boolean(false);
    }
    public String toString() { return "Class_introducesMethod";} 
}
