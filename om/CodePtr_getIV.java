/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//  Object getIV( ClassReference introducingClass, String ivName )
//
class CodePtr_getIV          extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2  ) {
        RDictionary dataSegment = (RDictionary)target.get((ClassReference)param1);
        if ( dataSegment == null ) {
            System.out.println( "***** FAILURE ***** getIV(" 
                                + ((ClassReference)param1).name 
                                + "," 
                                + param2 
                                + "): could not find data segment where class of target is " + Environment.getClass(target).name  );
            throw new OMRuntimeException();
        }
        if ( !dataSegment.containsKey( (String)param2 ) ) {
            System.out.println( "***** FAILURE ***** getIV(" 
                                + ((ClassReference)param1).name 
                                + "," 
                                + param2 
                                + "): could not find instance variable where class of target is " + Environment.getClass(target).name );
            throw new OMRuntimeException();
        }
	return dataSegment.get( (String)param2 );        
    }
    public String toString() { return "Object_getIV";} 
}
