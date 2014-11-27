/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   Boolean supportsIV( ClassReference introducingClass, String IVName )
//
class CodePtr_supportsIV  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
	ClassIVS thisClassIVS = Environment.getClassData( (ClassReference)target );
        if ( ((Boolean)Environment.getClass(target).invoke( Environment.Class, "isDescendantOf", Environment.Class )).booleanValue() ) {
            if ( param1 == Environment.Class ) {
                 if (  ((String)param2).equals("parents")
                       || ((String)param2).equals("ivdefs")
                       || ((String)param2).equals("mdefs")
                       || ((String)param2).equals("ivs")
                       || ((String)param2).equals("mtab") ) {
                     return new Boolean( true );
                   }
                 else {
                     return new Boolean(false);
                 }
             }
        }
         if ( thisClassIVS.ivs.containsKey( param1 ) ) {
              RDictionary dataSegment = (RDictionary)thisClassIVS.ivs.get(param1);
            if ( dataSegment.containsKey( (String)param2 ) ) {
                return new Boolean(true);
            }
        }
        return new Boolean(false);
    }
    public String toString() { return "Class_supportsIV";} 
}
