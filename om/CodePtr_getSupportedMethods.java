/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.om;

import java.util.*;

//
//   List getSupportedMethods( )
//
class CodePtr_getSupportedMethods  extends CodePtr { 
    public Object execute( ObjectReference target ) {
	RDictionary mtab = Environment.getClassData((ClassReference)target).mtab;
	List result = new List();
	for ( Enumeration e = mtab.keys(); e.hasMoreElements(); ) {
	    ClassReference ic = (ClassReference)e.nextElement();
            RDictionary msegment = (RDictionary)mtab.get(ic);
            for ( Enumeration e1 = msegment.keys(); e1.hasMoreElements(); ) {
                String nm = (String)e1.nextElement();
                Method m = new Method( ic, nm );
                result.addElement( m );
            }
	}
        return result;
    }
    public String toString() { return "Class_getSupportedMethods";} 
}
