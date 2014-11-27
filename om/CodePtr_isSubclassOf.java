/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   Goolean isSubclassOf( ClassReference anotherClass )
//
class CodePtr_isSubclassOf  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        if (Environment.getClassData((ClassReference)target).parents.contains( param1 ))
          return new Boolean(true);
        else
          return new Boolean(false);
    }
    public String toString() { return "Class_isSubclassOf";} 
}

