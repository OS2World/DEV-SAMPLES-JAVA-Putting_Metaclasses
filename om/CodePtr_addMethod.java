/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//  void addMethod ( ClassReference introducingClass, String methodName, CodePtr methodImpl )
//
class CodePtr_addMethod       extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2, Object param3 ) {
	try {
	    Environment.resolve( (ClassReference)target, (ClassReference)param1, (String)param2 );
	    System.out.println( "***** FAILURE ***** addMethod: " + param2 + " already defined");
            throw new OMRuntimeException();
	}  catch ( OMResolutionFailure e ) {
            // Do not do anything -- resolve (above) is supposed to fail.
	}

	ClassIVS thisClassIVS = Environment.getClassData( (ClassReference)target );
	RDictionary segment = new RDictionary( param2, param3 );
	RDictionary newMtab = new RDictionary( param1, segment );
	thisClassIVS.mdefs = newMtab.recursive_merge(thisClassIVS.mdefs);
	thisClassIVS.mtab =  newMtab.recursive_merge(thisClassIVS.mtab);
	return null;
    }
    public String toString() { return "Class_addMethod";} 
}
