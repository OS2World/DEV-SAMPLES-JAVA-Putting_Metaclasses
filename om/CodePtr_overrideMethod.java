/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//  void overrideMethod ( ClassReference introducingClass, String methodName, CodePtr methodImpl )
//
class CodePtr_overrideMethod  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) {
	try {
	    Environment.resolve( (ClassReference)target, (ClassReference)param1, (String)param2 );
	} catch ( OMResolutionFailure e ) {
	    System.out.println( "***** FAILURE ***** overrideMethod: " + param2 + " not defined");
            throw new OMRuntimeException();
	}
	RDictionary segment = new RDictionary( param2, param3 );
	RDictionary newMtab = new RDictionary( param1, segment );
	ClassIVS thisClassIVS = Environment.getClassData( (ClassReference)target );
	thisClassIVS.mdefs = newMtab.recursive_merge(thisClassIVS.mdefs);
	thisClassIVS.mtab =  newMtab.recursive_merge(thisClassIVS.mtab);
	return null;
    }
    public String toString() { return "Class_overrideMethod";} 
}

