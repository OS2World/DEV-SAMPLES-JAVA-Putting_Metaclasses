/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   CodePtr resolveTerminal( ClassReference introducingClass, String methodName )
//
class CodePtr_resolveTerminal  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        if (Environment.traceLevel > 50)
          System.out.println( ((ClassReference)target).name + "->resolveTerminal(" + ((ClassReference)param1).name + "," + param2 + ")" ); 
        return Environment.resolve( (ClassReference)target, (ClassReference)param1, (String)param2 );
    }
    public String toString() { return "Class_resolveTerminal";} 
}
