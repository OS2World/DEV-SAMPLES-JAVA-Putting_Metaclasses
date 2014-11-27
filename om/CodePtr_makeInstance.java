/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
// Object makeInstance ( ObjectReference obj )
//
// For this simulation, makeInstance has a parameter which is the space for the object.
// This because of the way we do implementation bindings, i.e., each object is
// a Java object of a class that is a descendant of ObjectReference. Such a class must override
// Execute.  To get the appropriate Execute invoked, the object must be of the appropriate
// Java class.  This must be done in the application.
//
class CodePtr_makeInstance             extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
	ClassIVS classIVS = Environment.getClassData( (ClassReference)target );
	ObjectReference obj = (ObjectReference)param1; 
	// The ivs cannot be cloned because that would produce an object of type RDictionary,
	// which cannot be cast down to ObjectReference.  So we iterate throught the slots of
	// the ivs adding each one to the new object.
	for ( Enumeration e = classIVS.ivs.keys(); e.hasMoreElements(); ) {
	    Object key = e.nextElement();
	    Value value = (Value)classIVS.ivs.get( key );
	    obj.put( key, value.clone() );
	}
	obj.put( Environment.myclass, target );
	obj.invoke( Environment.Object, "initialize" );
	return obj;
    }
    public String toString() { return "Class_makeInstance";} 
}
