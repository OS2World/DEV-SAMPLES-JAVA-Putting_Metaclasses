/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   void putRDStub( ClassReference introducingClass, String methodName )
//
class CodePtr_putRDStub  extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
        try {
	    Environment.resolve( (ClassReference)target, (ClassReference)param1, (String)param2 );
	} catch ( OMResolutionFailure e ) {
	    throw new OMRuntimeException();
	}
	RDictionary segment = new RDictionary( param2, new RedispatchStub( (ClassReference)param1, (String)param2 ) );
	RDictionary newMtab = new RDictionary( param1, segment );
	ClassIVS thisClassIVS = Environment.getClassData( (ClassReference)target );
	thisClassIVS.mtab =  newMtab.recursive_merge(thisClassIVS.mtab);
	return null;
    }
    public String toString() { return "Class_putRDStub";} 
}
