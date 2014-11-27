/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//  void setIV( ClassReference introducingClass, String ivName, Object value )
//
class CodePtr_setIV          extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) {
        RDictionary dataSegment = (RDictionary)target.get((ClassReference)param1);
        if ( dataSegment == null ) {
            System.out.println( "***** FAILURE ***** setIV: could not find data segment");
            throw new OMRuntimeException();
        }
        if ( !dataSegment.containsKey( (String)param2 ) ) {
            System.out.println( "***** FAILURE ***** setIV: could not find instance variable " + param2);
            throw new OMRuntimeException();
        }
	dataSegment.put( (String)param2, param3 );        
        return null;
    }
    public String toString() { return "Object_setIV";} 
}
