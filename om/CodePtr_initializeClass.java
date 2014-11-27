/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//  Boolean initializeClass ( List aParentList, RDictionary nameValuePairs )
//
class CodePtr_initializeClass     extends CodePtr { 
    public Object execute( ObjectReference target, Object param1, Object param2 ) {
	ClassReference myMetaclass =  Environment.getClass( target );
	ClassIVS thisClassIVS = Environment.getClassData( (ClassReference)target );
	for ( Enumeration e = thisClassIVS.parents.elements(); e.hasMoreElements(); ) {
	    ClassReference aParentMetaclass = Environment.getClass( (ObjectReference)e.nextElement() );
	    if ( !Environment.hasAncestor( myMetaclass, aParentMetaclass ) )
		return new Boolean( false );
	}
	thisClassIVS.parents = (List)param1;
        List theMRO = Environment.MRO( (ClassReference)target );
        if ( theMRO == null ) {
            return new Boolean( false );
            }
	RDictionary dataSegment = new RDictionary(target,param2);
	thisClassIVS.ivdefs = dataSegment.recursive_merge(thisClassIVS.ivdefs);
	for ( Enumeration e = theMRO.elements(); e.hasMoreElements(); ) { 
	    ObjectReference anAncestor = (ObjectReference)e.nextElement();
	    ClassIVS anAncestorIVS = (ClassIVS)anAncestor.get(Environment.Class);
	    thisClassIVS.ivs =  thisClassIVS.ivs .recursive_merge( anAncestorIVS.ivdefs );
	    thisClassIVS.mtab = thisClassIVS.mtab.recursive_merge( anAncestorIVS.mdefs );
	}
	return new Boolean( true );
    }
    public String toString() { return "Class_initializeClass";} 
}
