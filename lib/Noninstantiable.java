/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import java.util.*;
import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class Noninstantiable extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "Noninstantiable class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement( Environment.Class );

            List aParentList = new List();
            aParentList.addElement( Environment.Class );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "Noninstantiable";

            RDictionary anIVDefs = new RDictionary();

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "overrideMethod", Environment.Class, "makeInstance", new NoninstantiableCodePtr_makeInstance() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "Noninstantiable class object: construction completed" );
        }
        return classObject;
    }
}

class NoninstantiableCodePtr_makeInstance      extends CodePtr { 
    public Object execute( ObjectReference target, Object param1 ) {
        //
        //  Object makeInstance ( Object obj )
        //
        List theMRO = (List)target.invoke( Environment.Class, "getMRO" );
        if ( theMRO == null ) {
            throw new RuntimeException();
        }
        for ( Enumeration e = theMRO.elements(); e.hasMoreElements(); ) { 
            ObjectReference anAncestor = (ObjectReference)e.nextElement();
            if ( anAncestor != Environment.Class && anAncestor != Environment.Object ) {
                if ( !(((Boolean)Environment.getClass( anAncestor ).invoke( Environment.Class, 
                                                                            "isDescendantOf", 
                                                                            Noninstantiable.classObject )).booleanValue()) ) { 
                    return target.parentInvoke( Noninstantiable.classObject, Environment.Class, "makeInstance", param1 );
                }
            }
        }
        throw new RuntimeException();
    }
    public String toString() { return "NoninstantiableCodePtr_makeInstance";} 
}

