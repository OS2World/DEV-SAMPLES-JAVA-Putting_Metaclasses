/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

public class ClassReference extends ObjectReference {

     static int toStringDepth = 0;

     // For debugging, it is useful to give classes names.

    public String name;

    public String toString() { 
        String result;
        if ( toStringDepth == 0 ) {
          toStringDepth = 1;
          result = name + "<" + Environment.getClassData( this ).parents.toString() + ">" + super.toString(); 
          toStringDepth = 0;
          return result;
        } 
        else {
          return name + "<" + Environment.getClassData( this ).parents.toString() + ">"; 
        } 
    }

}
