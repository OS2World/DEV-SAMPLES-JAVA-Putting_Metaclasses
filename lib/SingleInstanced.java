/*
 *   @copyright  1997  Ira R. Forman and Scott H. Danforth
 */
package putting.lib;

import putting.om.*;

//=========================================================================
// Bindings
//=========================================================================
public class SingleInstanced extends ClassReference {
    // This is an implementation binding.

    // This is needed to make parent method calls.
    static public ClassReference classObject; 

    static public ObjectReference nullSingleInstance = new ConstantObjectReference();

    static public ClassReference newClass() {
        if ( classObject == null ) {

            if (Environment.traceLevel > 0) {
                System.out.println( "SingleInstanced class object: construction begins" );
            }

            List aMetaclassList = new List();
            aMetaclassList.addElement( MetaSingleInstanced.newClass() );

            List aParentList = new List();
            aParentList.addElement( Cooperative.newClass() );

            ClassReference metaclass;
            metaclass = Environment.solveMetaclassConstraints( aMetaclassList, aParentList );

            classObject = (ClassReference) metaclass.invoke( Environment.Class, "makeInstance", new ClassReference() );

            classObject.name = "SingleInstanced";

            RDictionary anIVDefs = new RDictionary();
            anIVDefs.put( "singleInstance", nullSingleInstance );
            anIVDefs.put( "referenceCount", new Long(0) );

            Boolean b = (Boolean)classObject.invoke( Environment.Class, "initializeClass", aParentList, anIVDefs );
            if ( !b.booleanValue() )
              throw new RuntimeException();

            classObject.invoke( Environment.Class, "readyClass" );

            if (Environment.traceLevel > 0)
              System.out.println( "SingleInstanced class object: construction completed" );
        }
        return classObject;
    }
}
