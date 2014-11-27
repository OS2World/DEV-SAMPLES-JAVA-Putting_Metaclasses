/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class InvariantCheckedObject extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "InvariantCheckedObject class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement( InvariantChecked.newClass() );

            List aParentList = new List();
            aParentList.addElement( Environment.Object );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "InvariantCheckedObject";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "extent", new List() );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "addMethod", classObject, "computeInvariant", new InvariantCheckedObjectCodePtr_computeInvariant() );

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "InvariantCheckedObject class object: construction completed" );
        }
        return classObject;
    }
}



class InvariantCheckedObjectCodePtr_computeInvariant      extends CodePtr { 
    public Object execute( ObjectReference target ) {
        //
        //  boolean computeInvariant()
        //
        return new Boolean(true);
    }
    public String toString() { return "InvariantCheckedObjectCodePtr_computeInvariant";} 
}

